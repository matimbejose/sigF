package br.com.villefortconsulting.sgfinancas.nfe.util;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnderEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEndereco;
import br.com.swconsultoria.nfe.schema_4.retEnviNFe.TLocal;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.servicos.NcmService;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributario;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validator {

    public static void removerMascara(NFe nfe) {
        TEnderEmi endEmi = nfe.getInfNFe().getEmit().getEnderEmit();
        if (endEmi.getCEP() != null) {
            endEmi.setCEP(endEmi.getCEP().replaceAll("\\D", ""));
        }
        if (endEmi.getFone() != null) {
            endEmi.setFone(endEmi.getFone().replaceAll("\\D", ""));
        }
        TEndereco end = nfe.getInfNFe().getDest().getEnderDest();
        if (end.getCEP() != null) {
            end.setCEP(end.getCEP().replaceAll("\\D", ""));
        }
        if (end.getFone() != null) {
            end.setFone(end.getFone().replaceAll("\\D", ""));
        }
    }

    public static Object removerCamposVazios(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof EntityId) {
            return obj;
        }
        boolean allNull = true;
        for (Method m : obj.getClass().getDeclaredMethods()) {
            try {
                if (m.getName().equals("get")) {
                    int size = (int) obj.getClass().getDeclaredMethod("size").invoke(obj);
                    Method setter = obj.getClass().getDeclaredMethod("set", int.class, m.getReturnType());
                    for (int i = 0; i < size; i++) {
                        setter.invoke(obj, i, removerCamposVazios(m.invoke(obj, i)));
                    }
                } else if (m.getName().startsWith("get")) {
                    Object val = m.invoke(obj);
                    if (val == null) {
                        continue;
                    }
                    allNull = false;
                    try {
                        Method setter = obj.getClass().getDeclaredMethod("set" + m.getName().substring(3), m.getReturnType());
                        switch (m.getReturnType().getSimpleName()) {
                            case "String":
                                String str = ((String) val).trim();
                                if (str.trim().isEmpty()) {
                                    str = null;
                                }
                                setter.invoke(obj, str);
                                break;
                            case "Double":
                                setter.invoke(obj, NumeroUtil.formatarCasasDecimais((Double) val, 2));
                                break;
                            case "Integer":
                            case "Imposto":
                                break;
                            default:
                                if (removerCamposVazios(val) == null && !m.getReturnType().getSimpleName().equals("List")) {
                                    setter.invoke(obj, (Object) null);
                                }
                                break;
                        }
                    } catch (NoSuchMethodException ex) {
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(Validator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return allNull ? null : obj;
    }

    public static List<String> validarImpostos(NFe nfe) {
        List<String> erros = new ArrayList<>();
        nfe.getInfNFe().getDet().forEach(det -> {
            if (det.getImposto().getICMS() == null) {
                erros.add("Informe o ICMS");
            }
//            else if (det.getImposto().getICMS().get) {TODO colocar validação se apenas um item está preenchido
//
//            }
            if (det.getImposto().getPIS() == null) {
                erros.add("Informe o PIS");
            }
            if (det.getImposto().getCOFINS() == null) {
                erros.add("Informe o COFINS");
            }
        });
        return erros;
    }

    public static List<String> validarNf(NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();
        //Colocar validações do objeto NF
        erros.addAll(validarObjTNFe(nf.getObjTNFe(), nf, empresa));
        return erros;
    }

    public static List<String> validarTEnderEmi(TEnderEmi enderEmit, String sufixo) {
        List<String> erros = new ArrayList<>();
        if (!sufixo.isEmpty()) {
            sufixo = " " + sufixo.trim();
        }
        if (enderEmit.getXLgr() == null) {
            erros.add("Informe o logradouro" + sufixo + ".");
        }
        if (enderEmit.getNro() == null) {
            erros.add("Informe o número" + sufixo + ".");
        }
        if (enderEmit.getXBairro() == null) {
            erros.add("Informe o nome do bairro" + sufixo + ".");
        }
        if (enderEmit.getCMun() == null) {
            erros.add("Informe o código do município" + sufixo + ".");
        }
        if (enderEmit.getXMun() == null) {
            erros.add("Informe o nome do município" + sufixo + ".");
        }
        if (enderEmit.getUF() == null) {
            erros.add("Informe a sigla ad UF" + sufixo + ".");
        }

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarObjTNFe(NFe objTNFe, final NF nf, final Empresa empresa) {
        List<String> erros = new ArrayList<>();
        erros.addAll(validarInfNFeSupl(objTNFe.getInfNFeSupl()));
        erros.addAll(validarInfNFe(objTNFe.getInfNFe(), nf, empresa));

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarInfNFeSupl(NFe.InfNFeSupl infNFeSupl) {
        List<String> erros = new ArrayList<>();
        if (infNFeSupl != null) {
            if (infNFeSupl.getQrCode() == null) {
                erros.add("Informe o QR code.");
            }
            if (infNFeSupl.getUrlChave() == null) {
                erros.add("Informe a URL da chave.");
            }
        }

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarInfNFe(NFe.InfNFe infNFe, final NF nf, final Empresa empresa) {
        List<String> erros = new ArrayList<>();

        if (infNFe.getIde() == null) {
            erros.add("Informe o Ide");
        } else {
            erros.addAll(validarIde(infNFe.getIde(), nf, empresa));
        }
        if (infNFe.getEmit() == null) {
            erros.add("Informe o Emit");
        } else {
            erros.addAll(validarEmit(infNFe.getEmit()));
        }
        if (infNFe.getAvulsa() != null) {
            erros.addAll(validarAvulsa(infNFe.getAvulsa(), nf, empresa));
        }
        if (infNFe.getDest() != null) {
            erros.addAll(validarDest(infNFe.getDest(), nf, empresa));
        }
        if (infNFe.getRetirada() != null) {
            erros.addAll(validarRetirada(infNFe.getRetirada(), nf, empresa));
        }
        if (infNFe.getEntrega() != null) {
            erros.addAll(validarEntrega(infNFe.getEntrega(), nf, empresa));
        }
        if (infNFe.getAutXML() != null) {
            erros.addAll(validarAutXML(infNFe.getAutXML()));
        }
        if (infNFe.getDet() == null) {
            erros.add("Informe o Det");
        } else {
            erros.addAll(validarDet(infNFe.getDet(), nf, empresa));
        }
        if (infNFe.getTotal() == null) {
            erros.add("Informe o Total");
        } else {
            erros.addAll(validarTotal(infNFe.getTotal(), nf, empresa));
        }
        if (infNFe.getTransp() == null) {
            erros.add("Informe o Transp");
        } else {
            erros.addAll(validarTransp(infNFe.getTransp(), nf, empresa));
        }
        if (infNFe.getPag() == null) {
            erros.add("Informe o Pag");
        } else {
            erros.addAll(validarPag(infNFe.getPag()));
        }
        if (infNFe.getInfAdic() != null) {
            erros.addAll(validarInfAdic(infNFe.getInfAdic(), nf, empresa));
        }
        if (infNFe.getExporta() != null) {
            erros.addAll(validarExporta(infNFe.getExporta(), nf, empresa));
        }
        if (infNFe.getCompra() != null) {
            erros.addAll(validarCompra(infNFe.getCompra(), nf, empresa));
        }
        if (infNFe.getCana() != null) {
            erros.addAll(validarCana(infNFe.getCana(), nf, empresa));
        }
        if (infNFe.getVersao() == null) {
            erros.add("Informe a versão");
        } else if (!infNFe.getVersao().equals("4.00")) {
            erros.add("Número de versão inválido.");
        }
        if (infNFe.getId() == null) {
            erros.add("Informe a chave da NFe.");
        } else if (!infNFe.getId().equals(Filler.gerarChaveNF(empresa, nf, infNFe.getIde().getTpEmis()))) {
            erros.add("Chave da NFe inválida.");
        }

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarIde(NFe.InfNFe.Ide ide, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(ide.getCUF()) && ide.getCUF().length() != 2) {
            erros.add("O tamanho do campo UF deverá conter apenas 2 caracteres.");
        }
        if (StringUtils.isNotEmpty(ide.getCNF()) && ide.getCNF().length() > 8) {
            erros.add("O tamanho do campo Código Numérico que compõe a Chave de Acesso deverá conter até 8 caracteres.");
        }

        if (StringUtils.isNotEmpty(ide.getNatOp()) && ide.getNatOp().length() == 0 || ide.getNatOp().length() > 60) {
            erros.add("O tamanho do campo  natureza da operação deverá conter de 1 até 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(ide.getMod()) && ide.getMod().length() != 2) {
            erros.add("O tamanho do campo Código do Modelo do Documento Fiscal deverá conter 2 dígitos, 55=NF-e emitida em substituição ao modelo 1 ou 1A; 65=NFC-e, utilizada nas operações de venda no varejo (a critério da UF aceitar este modelo de documento).");
        }

        if (StringUtils.isNotEmpty(ide.getSerie()) && ide.getSerie().length() == 0 || ide.getSerie().length() > 3) {
            erros.add("O tamanho do campo Série do Documento Fiscal deverá conter de 1 ate 3 dígitos, preencher com zeros na hipótese de a NF-e não possuir série. (v2.0) Série 890-899: uso exclusivo para emissão de NF-e avulsa, pelo contribuinte com seu certificado digital, através do site do Fisco (procEmi=2). (v2.0) Serie 900-999: uso exclusivo de NF-e emitidas no SCAN. (v2.0)");
        }

        if (ide.getNNF() != null && (ide.getNNF().length() == 0 || ide.getNNF().length() > 9)) {
            erros.add("O tamanho do campo Número do Documento Fiscal deverá conter de 1 a 9 dígitos.");
        }

        if (StringUtils.isNotEmpty(ide.getTpNF()) && ide.getTpNF().length() != 1) {
            erros.add("O tamanho do campo UF deverá conter apenas 1 dígito, 0=Entrada; 1=Saída.");
        }

        if (StringUtils.isNotEmpty(ide.getIdDest()) && ide.getIdDest().length() != 1) {
            erros.add("O tamanho do campo Identificador de local de destino da operaçã deverá conter apenas 1 dígito, 1=Operação interna; 2=Operação interestadual; 3=Operação com exterior.");
        }

        if (StringUtils.isNotEmpty(ide.getCMunFG()) && ide.getCMunFG().length() > 7) {
            erros.add("O tamanho do campo Código do Município de Ocorrência do Fato Gerador deverá conter até 8 caracteres, Informar o município de ocorrência do fato gerador do ICMS. Utilizar a Tabela do IBGE (Anexo IX - Tabela de UF, Município e País).");
        }

        if (StringUtils.isNotEmpty(ide.getTpImp()) && ide.getTpImp().length() != 1) {
            erros.add("O tamanho do campo Formato de Impressão do DANFE deverá conter apenas 1 dígito, 0=Sem geração de DANFE; 1=DANFE normal, Retrato; 2=DANFE normal, Paisagem; 3=DANFE Simplificado; 4=DANFE NFC-e; 5=DANFE NFC-e em mensagem eletrônica (o envio de mensagem eletrônica pode ser feita de forma simultânea com a impressão do DANFE; usar o tpImp=5 quando esta for a única forma de disponibilização do DANFE).");
        }

        if (StringUtils.isNotEmpty(ide.getTpImp()) && ide.getTpImp().length() != 1) {
            erros.add("O tamanho do campo Tipo de Emissão da NF-e  deverá conter apenas 1 dígito, 1=Emissão normal (não em contingência); 2=Contingência FS-IA, com impressão do DANFE em formulário de segurança; "
                    + "3=Contingência SCAN (Sistema de Contingência do Ambiente Nacional); 4=Contingência DPEC (Declaração Prévia da Emissão em Contingência); 5=Contingência FS-DA, com impressão do DANFE em formulário de segurança; 6=Contingência SVC-AN (SEFAZ Virtual de Contingência do AN); 7=Contingência SVC-RS (SEFAZ Virtual de Contingência do RS) 9=Contingência off-line da NFC-e (as demais opções de contingência são válidas também para a NFC-e). Para a NFC-e somente estão disponíveis e são válidas as opções de contingência 5 e 9; .");
        }

        if (StringUtils.isNotEmpty(ide.getCDV()) && ide.getCDV().length() != 1) {
            erros.add("O tamanho do campo Dígito Verificador da Chave de Acesso da NF-e deverá conter apenas 1 dígito, Informar o DV da Chave de Acesso da NF-e, o DV será calculado com a aplicação do algoritmo módulo 11 (base 2,9) da Chave de Acesso. (vide item 5 do Manual de Orientação).");
        }

        if (StringUtils.isNotEmpty(ide.getTpAmb()) && ide.getTpAmb().length() != 1) {
            erros.add("O tamanho do campo Identificação do Ambiente deverá conter apenas 1 dígito, 1=Produção/2=Homologação.");
        }

        if (StringUtils.isNotEmpty(ide.getFinNFe()) && ide.getFinNFe().length() != 1) {
            erros.add("O tamanho do campo Finalidade de emissão da NF-e deverá conter apenas 1 dígito, 1=NF-e normal; 2=NF-e complementar; 3=NF-e de ajuste; 4=Devolução de mercadoria.");
        }

        if (StringUtils.isNotEmpty(ide.getIndFinal()) && ide.getIndFinal().length() != 1) {
            erros.add("O tamanho do campo Indica operação com Consumidor final deverá conter apenas 1 dígito, 0=Normal; 1=Consumidor final.");
        }

        if (StringUtils.isNotEmpty(ide.getIndPres()) && ide.getIndPres().length() != 1) {
            erros.add("O tamanho do campo Indicador de presença do comprador no estabelecimento comercial no momento da operação deverá conter apenas 1 dígito, 0=Não se aplica (por exemplo, Nota Fiscal complementar ou de ajuste); 1=Operação presencial; 2=Operação não presencial, pela Internet; 3=Operação não presencial, Teleatendimento; 4=NFC-e em operação com entrega a domicílio; 9=Operação não presencial, outros.");
        }

        if (StringUtils.isNotEmpty(ide.getProcEmi()) && ide.getProcEmi().length() != 1) {
            erros.add("O tamanho do campo Processo de emissão da NF-e deverá conter apenas 1 dígito, 0=Emissão de NF-e com aplicativo do contribuinte; 1=Emissão de NF-e avulsa pelo Fisco; 2=Emissão de NF-e avulsa, pelo contribuinte com seu certificado digital, através do site do Fisco; 3=Emissão NF-e pelo contribuinte com aplicativo fornecido pelo Fisco.");
        }

        if ((StringUtils.isNotEmpty(ide.getVerProc())) && ide.getVerProc().length() == 0 || ide.getVerProc().length() > 20) {
            erros.add("O tamanho do campo Versão do Processo de emissão da NF-e deverá conter de 1 a 20 caracteres, Informar a versão do aplicativo emissor de NF-e.");
        }
        return erros;
    }

    public static List<String> validarEmit(NFe.InfNFe.Emit emit) {
        List<String> erros = new ArrayList<>();
        if (emit.getCNPJ() != null && !CpfCnpjUtil.validarCNPJ(emit.getCNPJ())) {
            erros.add("O CNPJ informado é inválido.");
        }
        if (emit.getCPF() != null && !CpfCnpjUtil.validarCPF(emit.getCPF())) {
            erros.add("O CPF informado é inválido.");
        }
        if (emit.getCNPJ() == null && emit.getCPF() == null) {
            erros.add("Informe o CPF ou o CNPJ do emitente.");
        }
        if (emit.getXNome() == null) {
            erros.add("Informe o nome do emissor.");
        }
        if (emit.getEnderEmit() == null) {
            erros.add("Informe o endereço do emissor.");
        } else {
            erros.addAll(validarTEnderEmi(emit.getEnderEmit(), "do emitente"));
        }
        if (emit.getIE() == null) {
            erros.add("Informe a IE.");
        }
        if (emit.getCRT() == null) {
            erros.add("Informe o código do regime tributário.");
        } else {
            // TODO adicionar validação do CRT
        }

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(emit.getCNAE()) && emit.getCNAE().length() > 7) {
            erros.add("O tamanho do campo CNAE fiscal deverá conter ate 7 caracteres, Campo Opcional. Pode ser informado quando a Inscrição Municipal (id:C19) for informada.");
        }
        String cpfCnpjSemMascara;
        if (StringUtils.isNotEmpty(emit.getCPF())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCpf(emit.getCPF());

            if (cpfCnpjSemMascara.length() != 11) {
                erros.add("O tamanho do campo CPF deverá conter 11 dígitos, Informar o CNPJ do emitente. Na emissão de NF-e avulsa pelo Fisco, as informações do remetente serão informadas neste grupo. O CNPJ ou CPF deverão ser informados com os zeros não significativos.");
            }
        }
        if (StringUtils.isNotEmpty(emit.getCNPJ())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCnpj(emit.getCNPJ());

            if (cpfCnpjSemMascara.length() != 14) {
                erros.add("O tamanho do campo CNPJ deverá conter 14 dígitos, Informar o CNPJ do emitente. Na emissão de NF-e avulsa pelo Fisco, as informações do remetente serão informadas neste grupo. O CNPJ ou CPF deverão ser informados com os zeros não significativos.");
            }
        }
        if (StringUtils.isNotEmpty(emit.getCRT()) && (emit.getCRT().length() != 1)) {
            erros.add("O tamanho do campo CNPJ deverá conter apenas 1 dígito, 1=Simples Nacional; 2=Simples Nacional, excesso sublimite de receita bruta; 3=Regime Normal. (v2.0)");
        }
        if (StringUtils.isNotEmpty(emit.getIE()) && (emit.getIE().length() == 1 || emit.getIE().length() > 14)) {
            erros.add("O tamanho do campo Inscrição Estadual do Emitente deverá conter de 1 ate 14 dígitos, Informar somente os algarismos, sem os caracteres de formatação (ponto, barra, hífen, etc.). Na emissão de NF-e Avulsa pode ser informado o literal “ISENTO�? para os contribuintes do ICMS isentos de inscrição no Cadastro de Contribuintes de ICMS.");
        }
        if (StringUtils.isNotEmpty(emit.getIEST()) && (emit.getIEST().length() == 1 || emit.getIEST().length() > 14)) {
            erros.add("O tamanho do campo IE do Substituto Tributário deverá conter de 1 ate 14 dígitos, IE do Substituto Tributário da UF de destino da mercadoria, quando houver a retenção do ICMS ST para a UF de destino.");
        }
        if (StringUtils.isNotEmpty(emit.getIM()) && (emit.getIM().length() == 0 || emit.getIM().length() > 15)) {
            erros.add("O tamanho do campo Inscrição Municipal do Prestador de Serviço deverá conter de 1 ate 14 dígitos, IE do Substituto Tributário da UF de destino da mercadoria, quando houver a retenção do ICMS ST para a UF de destino.");
        }
        if (StringUtils.isNotEmpty(emit.getXFant()) && (emit.getXFant().length() == 0 || emit.getXFant().length() > 60)) {
            erros.add("O tamanho do campo Nome fantasia deverá conter de 1 ate 14 caracteres.");
        }
        if (StringUtils.isNotEmpty(emit.getXNome()) && (emit.getXNome().length() == 1 || emit.getXNome().length() > 60)) {
            erros.add("O tamanho do campo Razão Social ou Nome do emitente deverá conter de 2 ate 60 caracteres.");
        }
        return erros;
    }

    public static List<String> validarAvulsa(NFe.InfNFe.Avulsa avulsa, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(avulsa.getFone()) && (avulsa.getFone().length() == 5 || avulsa.getFone().length() > 14)) {
            erros.add("O tamanho do campo Telefone deverá conter de 6 ate 14 caracteres.");
        }
        return erros;
    }

    public static List<String> validarDest(NFe.InfNFe.Dest dest, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        String cpfCnpjSemMascara;
        if (StringUtils.isNotEmpty(dest.getCPF())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCpf(dest.getCPF());

            if (cpfCnpjSemMascara.length() != 11) {
                erros.add("O tamanho do campo CPF deverá conter 11 dígitos, Informar o CNPJ do emitente. Na emissão de NF-e avulsa pelo Fisco, as informações do remetente serão informadas neste grupo. O CNPJ ou CPF deverão ser informados com os zeros não significativos.");
            } else if (!CpfCnpjUtil.validarCPF(dest.getCPF())) {
                erros.add("O CPF do cliente está inválido.");
            }
        }
        if (StringUtils.isNotEmpty(dest.getCNPJ())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCnpj(dest.getCNPJ());
            if (cpfCnpjSemMascara.length() != 14) {
                erros.add("O tamanho do campo CNPJ deverá conter 14 dígitos, Informar o CNPJ do emitente. Na emissão de NF-e avulsa pelo Fisco, as informações do remetente serão informadas neste grupo. O CNPJ ou CPF deverão ser informados com os zeros não significativos.");
            } else if (!CpfCnpjUtil.validarCNPJ(dest.getCNPJ())) {
                erros.add("O CNPJ do cliente está inválido.");
            }
        }

        if (StringUtils.isNotEmpty(dest.getEmail()) && (dest.getEmail().length() == 0 || dest.getEmail().length() > 60)) {
            erros.add("O tamanho do campo Email deverá conter de 1 ate 60 caracteres, Campo pode ser utilizado para informar o e-mail de recepção da NF-e indicada pelo destinatário (v2.0)");
        }

        if (StringUtils.isNotEmpty(dest.getIE()) && (dest.getIE().length() == 1 || dest.getIE().length() > 14)) {
            erros.add("O tamanho do campo IE deverá conter de 2 ate 14 caracteres, Campo opcional. Informar somente os algarismos, sem os caracteres de formatação (ponto, barra, hífen, etc.)");
        }

        if (StringUtils.isNotEmpty(dest.getIM()) && (dest.getIM().length() == 0 || dest.getIM().length() > 15)) {
            erros.add("O tamanho do campo Inscrição Municipal do Tomador do Serviço deverá conter de 2 ate 14 caracteres, Campo opcional, pode ser informado na NF-e conjugada, com itens de produtos sujeitos ao ICMS e itens de serviços sujeitos ao ISSQN.");
        }

        if (StringUtils.isNotEmpty(dest.getISUF()) && (dest.getISUF().length() == 7 || dest.getISUF().length() > 9)) {
            erros.add("O tamanho do campo Inscrição na SUFRAMA deverá conter de 8 ate 9 caracteres, Campo opcional. Obrigatório, nas operações que se beneficiam de incentivos fiscais existentes nas áreas sob controle da SUFRAMA. A omissão desta informação impede o processamento da operação pelo Sistema de Mercadoria Nacional da SUFRAMA e a liberação da Declaração de Ingresso, prejudicando a comprovação do ingresso / internamento da mercadoria nestas áreas. (v2.0)");
        }

        if (StringUtils.isNotEmpty(dest.getIndIEDest()) && (dest.getIndIEDest().length() != 1)) {
            erros.add("O tamanho do campo Indicador da IE do Destinatário deverá conter apenas 1 dígito, 1=Contribuinte ICMS (informar a IE do destinatário); 2=Contribuinte isento de Inscrição no cadastro de Contribuintes.");
        }

        if (StringUtils.isNotEmpty(dest.getXNome()) && (dest.getXNome().length() == 1 || dest.getXNome().length() > 60)) {
            erros.add("O tamanho do campo Razão Social ou nome do destinatário deverá conter de 2 ate 60 caracteres, Tag obrigatória para a NF-e (modelo 55) e opcional para a NFCe.");
        }
        return erros;
    }

    public static List<String> validarRetirada(TLocal retirada, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        String cpfCnpjSemMascara;
        if (StringUtils.isNotEmpty(retirada.getCPF())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCpf(retirada.getCPF());
            if (cpfCnpjSemMascara.length() != 11) {
                erros.add("O tamanho do campo CPF deverá conter 11 dígitos,O CPF em caso de pessoa física .");
            }
        }
        if (StringUtils.isNotEmpty(retirada.getCNPJ())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCnpj(retirada.getCNPJ());
            if (cpfCnpjSemMascara.length() != 14) {
                erros.add("O tamanho do campo CNPJ deverá conter 14 dígitos, CNPJ do contribuinte ");
            }
        }

        if (StringUtils.isNotEmpty(retirada.getCMun()) && (retirada.getCMun().length() > 7)) {
            erros.add("O tamanho do campo de Código do Município deverá conter de 1 ate 60 caracteres, Código do Município do Contribuinte, conforme Tabela do IBGE .");
        }

        if (StringUtils.isNotEmpty(retirada.getNro()) && retirada.getNro().length() == 0 || retirada.getNro().length() > 60) {
            erros.add("O tamanho do campo de Número deverá conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(retirada.getXBairro()) && (retirada.getXBairro().length() == 0 || retirada.getXBairro().length() > 60)) {
            erros.add("O tamanho do campo Nome do Bairro deverá conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(retirada.getXCpl()) && (retirada.getXCpl().length() == 0 || retirada.getXCpl().length() > 60)) {
            erros.add("O tamanho do campo Complemento deverá conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(retirada.getXLgr()) && (retirada.getXLgr().length() == 0 || retirada.getXLgr().length() > 255)) {
            erros.add("O tamanho do campo Logradouro deverá conter de 1 ate 255 caracteres.");
        }

        if (StringUtils.isNotEmpty(retirada.getXMun()) && (retirada.getXMun().length() == 0 || retirada.getXMun().length() > 60)) {
            erros.add("O tamanho do campo Nome do município deverá conter de 1 ate 60 caracteres.");
        }

        // transporte
        if (StringUtils.isNotEmpty(retirada.getCMun()) && (nf.getObjTNFe().getInfNFe().getTransp().getModFrete().length() != 1)) {
            erros.add("O tamanho do campo Modalidade do frete deverá conter apenas 1 dígito, 0=Por conta do emitente; 1=Por conta do destinatário/remetente; 2=Por conta de terceiros; 9=Sem frete. (V2.0).");
        }
        return erros;
    }

    public static List<String> validarEntrega(TLocal entrega, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(entrega.getCMun()) && (entrega.getCMun().length() > 7)) {
            erros.add("O tamanho do campo Código do município deverá conter de 7 caracteres, Utilizar a Tabela do IBGE (Anexo IX- Tabela de UF, Município e País).");
        }

        if (StringUtils.isNotEmpty(entrega.getNro()) && (entrega.getNro().length() == 1 || entrega.getNro().length() > 60)) {
            erros.add("O tamanho do campo Número deverá conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(entrega.getXBairro()) && (entrega.getXBairro().length() == 1 || entrega.getXBairro().length() > 60)) {
            erros.add("O tamanho do campo Bairro deverá conter de 2 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(entrega.getXCpl()) && (entrega.getXCpl().length() == 0 || entrega.getXCpl().length() > 60)) {
            erros.add("O tamanho do campo Complemento deverá conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(entrega.getXLgr()) && (entrega.getXLgr().length() == 1 || entrega.getXLgr().length() > 60)) {
            erros.add("O tamanho do campo Logradouro deverá conter de 2 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(entrega.getXMun()) && (entrega.getXMun().length() == 1 || entrega.getXMun().length() > 60)) {
            erros.add("O tamanho do campo Nome do município deverá conter de 2 ate 60 caracteres.");
        }
        return erros;
    }

    public static List<String> validarAutXML(List<NFe.InfNFe.AutXML> autXML) {
        List<String> erros = new ArrayList<>();
        autXML.forEach(aut -> {
            if (aut.getCNPJ() != null && !CpfCnpjUtil.validarCNPJ(aut.getCNPJ())) {
                erros.add("O CNPJ " + aut.getCNPJ() + " é inválido.");
            }
            if (aut.getCPF() != null && !CpfCnpjUtil.validarCPF(aut.getCPF())) {
                erros.add("O CPF " + aut.getCPF() + " é inválido.");
            }
        });

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarDet(List<NFe.InfNFe.Det> det, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();
        if (det.isEmpty()) {
            erros.add("Informe ao menos um produto na NFe.");
        }
        for (int i = 0; i < det.size(); i++) {
            NFe.InfNFe.Det item = det.get(i);
            Produto prod = item.getProd().getIdProduto().getValue();
            if (prod == null) {
                erros.add("O item " + (i + 1) + " da nota não está configurado corretamente.");
                continue;
            }
            String prodName = prod.getDescricao();
            if (item.getIdCst().getValue() == null && item.getIdCsosn().getValue() == null) {
                boolean usaCST = EnumRegimeTributario.retornaEnumSelecionado(empresa.getRegimeTributario()).isUsaCST();
                erros.add("Informe o " + (usaCST ? "CST" : "CSOSN") + " do produto " + prodName + ".");
            }
            Ncm ncm = item.getProd().getIdNcm().getValue();
            String cest = NcmService.buscarCest(item.getProd().getIdNcm().getValue());
            if (cest == null && (ncm == null || !ncm.getCodigo().equals("00"))) {
                erros.add("Informe o CEST do produto " + prodName + ". (No cadastro do produto)");
            }
            if (item.getProd().getIdCfop().getValue() == null) {
                erros.add("Informe o CFOP do produto " + prodName + ".");
            }
            if (ncm == null) {
                erros.add("Informe o NCM do produto " + prodName + ".");
            }
            if (prod.getCodigoBarra() == null || prod.getCodigoBarra().isEmpty()) {
                erros.add("Informe o código de barras do produto " + prodName + ". (No cadastro do produto; Informe \"SEM GTIN\" caso o produto não tenha um código)");
            }
            if (prod.getIdOrigemMercadoria() == null || prod.getIdOrigemMercadoria().getCodOrigemMercadoria() == null) {
                erros.add("Informe origem da mercadoria do produto " + prodName + ". (No cadastro do produto)");
            }
        }

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarTotal(NFe.InfNFe.Total total, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarTransp(NFe.InfNFe.Transp transp, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        if (transp != null && (StringUtils.isNotEmpty(transp.getModFrete()) && transp.getModFrete().length() != 1)) {
            erros.add("O tamanho do campo Modalidade do frete deverá contem apenas 1 dígito, 0=Por conta do emitente; 1=Por conta do destinatário/remetente; 2=Por conta de terceiros; 9=Sem frete. (V2.0).");
        }
        return erros;
    }

    public static List<String> validarPag(NFe.InfNFe.Pag pag) {
        List<String> erros = new ArrayList<>();
        if (pag.getDetPag() == null || pag.getDetPag().isEmpty()) {
            erros.add("Informe ao menos um meio de pagamento.");
        } else {
            pag.getDetPag().forEach(detPag -> {
                if (detPag.getIndPag() != null && (!detPag.getIndPag().equals("0") && !detPag.getIndPag().equals("1") && !detPag.getIndPag().equals("2"))) {
                    erros.add("O tipo de pagamento informado é inválido.");
                }
                if (detPag.getTPag() == null) {
                    erros.add("Informe o meio de pagamento.");
                }
                if (detPag.getVPag() == null) {
                    erros.add("Informe valor do pagamento.");
                }
                if (detPag.getCard() != null) {
                    if (!detPag.getTPag().equals(EnumMeioDePagamento.CARTAO_DE_CREDITO.getCodigo()) && !detPag.getTPag().equals(EnumMeioDePagamento.CARTAO_DE_DEBITO.getCodigo())) {
                        erros.add("Informado dados do cartão para um meio de pagamento diferente de \"Cartão de crédito\" ou \"Cartão de débito\".");
                    } else {
                        if (detPag.getCard().getTpIntegra() == null) {
                            erros.add("Informe o tipo de integração do pagamento via cartão.");
                        } else if (!detPag.getCard().getTpIntegra().equals(("1")) && !detPag.getCard().getTpIntegra().equals("2")) {
                            erros.add("O tipo de integração do pagamento via cartão informado é inválido.");
                        }
                        if (detPag.getCard().getTpIntegra().equals(("1"))) {
                            if (detPag.getCard().getCNPJ() == null) {
                                erros.add("Informe o CNPJ da credenciadora do cartão.");
                            } else if (!CpfCnpjUtil.validarCNPJ(detPag.getCard().getCNPJ())) {
                                erros.add("O CNPJ da credenciadora do cartão informado é inválido.");
                            }
                            if (detPag.getCard().getTBand() == null) {
                                erros.add("Informe a bandeira do cartão.");
                            }
                            if (detPag.getCard().getCAut() == null) {
                                erros.add("Informe o código de autorização da transação.");
                            }
                        }

                    }
                }
            });
        }
        if (pag.getVTroco() != null && pag.getVTroco() < 0) {
            erros.add(NumeroUtil.converterDecimalParaString(pag.getVTroco(), 2) + " não é um valor de troco válido.");
        }

        //Tamanho dos campos
        return erros;

    }

    public static List<String> validarInfAdic(NFe.InfNFe.InfAdic infAdic, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarExporta(NFe.InfNFe.Exporta exporta, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarCompra(NFe.InfNFe.Compra compra, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(compra.getXPed()) && (compra.getXPed().length() == 0 || compra.getXPed().length() > 60)) {
            erros.add("O tamanho do campo de Pedido deverá conter de 1 ate 60 caracteres.");
        }
        return erros;
    }

    public static List<String> validarCana(NFe.InfNFe.Cana cana, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        return erros;
    }

}
