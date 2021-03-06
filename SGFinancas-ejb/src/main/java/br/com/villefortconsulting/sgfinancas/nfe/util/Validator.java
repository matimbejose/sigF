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
//            else if (det.getImposto().getICMS().get) {TODO colocar valida????o se apenas um item est?? preenchido
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
        //Colocar valida????es do objeto NF
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
            erros.add("Informe o n??mero" + sufixo + ".");
        }
        if (enderEmit.getXBairro() == null) {
            erros.add("Informe o nome do bairro" + sufixo + ".");
        }
        if (enderEmit.getCMun() == null) {
            erros.add("Informe o c??digo do munic??pio" + sufixo + ".");
        }
        if (enderEmit.getXMun() == null) {
            erros.add("Informe o nome do munic??pio" + sufixo + ".");
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
            erros.add("Informe a vers??o");
        } else if (!infNFe.getVersao().equals("4.00")) {
            erros.add("N??mero de vers??o inv??lido.");
        }
        if (infNFe.getId() == null) {
            erros.add("Informe a chave da NFe.");
        } else if (!infNFe.getId().equals(Filler.gerarChaveNF(empresa, nf, infNFe.getIde().getTpEmis()))) {
            erros.add("Chave da NFe inv??lida.");
        }

        //Tamanho dos campos
        return erros;
    }

    public static List<String> validarIde(NFe.InfNFe.Ide ide, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(ide.getCUF()) && ide.getCUF().length() != 2) {
            erros.add("O tamanho do campo UF dever?? conter apenas 2 caracteres.");
        }
        if (StringUtils.isNotEmpty(ide.getCNF()) && ide.getCNF().length() > 8) {
            erros.add("O tamanho do campo C??digo Num??rico que comp??e a Chave de Acesso dever?? conter at?? 8 caracteres.");
        }

        if (StringUtils.isNotEmpty(ide.getNatOp()) && ide.getNatOp().length() == 0 || ide.getNatOp().length() > 60) {
            erros.add("O tamanho do campo  natureza da opera????o dever?? conter de 1 at?? 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(ide.getMod()) && ide.getMod().length() != 2) {
            erros.add("O tamanho do campo C??digo do Modelo do Documento Fiscal dever?? conter 2 d??gitos, 55=NF-e emitida em substitui????o ao modelo 1 ou 1A; 65=NFC-e, utilizada nas opera????es de venda no varejo (a crit??rio da UF aceitar este modelo de documento).");
        }

        if (StringUtils.isNotEmpty(ide.getSerie()) && ide.getSerie().length() == 0 || ide.getSerie().length() > 3) {
            erros.add("O tamanho do campo S??rie do Documento Fiscal dever?? conter de 1 ate 3 d??gitos, preencher com zeros na hip??tese de a NF-e n??o possuir s??rie. (v2.0) S??rie 890-899: uso exclusivo para emiss??o de NF-e avulsa, pelo contribuinte com seu certificado digital, atrav??s do site do Fisco (procEmi=2). (v2.0) Serie 900-999: uso exclusivo de NF-e emitidas no SCAN. (v2.0)");
        }

        if (ide.getNNF() != null && (ide.getNNF().length() == 0 || ide.getNNF().length() > 9)) {
            erros.add("O tamanho do campo N??mero do Documento Fiscal dever?? conter de 1 a 9 d??gitos.");
        }

        if (StringUtils.isNotEmpty(ide.getTpNF()) && ide.getTpNF().length() != 1) {
            erros.add("O tamanho do campo UF dever?? conter apenas 1 d??gito, 0=Entrada; 1=Sa??da.");
        }

        if (StringUtils.isNotEmpty(ide.getIdDest()) && ide.getIdDest().length() != 1) {
            erros.add("O tamanho do campo Identificador de local de destino da opera???? dever?? conter apenas 1 d??gito, 1=Opera????o interna; 2=Opera????o interestadual; 3=Opera????o com exterior.");
        }

        if (StringUtils.isNotEmpty(ide.getCMunFG()) && ide.getCMunFG().length() > 7) {
            erros.add("O tamanho do campo C??digo do Munic??pio de Ocorr??ncia do Fato Gerador dever?? conter at?? 8 caracteres, Informar o munic??pio de ocorr??ncia do fato gerador do ICMS. Utilizar a Tabela do IBGE (Anexo IX - Tabela de UF, Munic??pio e Pa??s).");
        }

        if (StringUtils.isNotEmpty(ide.getTpImp()) && ide.getTpImp().length() != 1) {
            erros.add("O tamanho do campo Formato de Impress??o do DANFE dever?? conter apenas 1 d??gito, 0=Sem gera????o de DANFE; 1=DANFE normal, Retrato; 2=DANFE normal, Paisagem; 3=DANFE Simplificado; 4=DANFE NFC-e; 5=DANFE NFC-e em mensagem eletr??nica (o envio de mensagem eletr??nica pode ser feita de forma simult??nea com a impress??o do DANFE; usar o tpImp=5 quando esta for a ??nica forma de disponibiliza????o do DANFE).");
        }

        if (StringUtils.isNotEmpty(ide.getTpImp()) && ide.getTpImp().length() != 1) {
            erros.add("O tamanho do campo Tipo de Emiss??o da NF-e  dever?? conter apenas 1 d??gito, 1=Emiss??o normal (n??o em conting??ncia); 2=Conting??ncia FS-IA, com impress??o do DANFE em formul??rio de seguran??a; "
                    + "3=Conting??ncia SCAN (Sistema de Conting??ncia do Ambiente Nacional); 4=Conting??ncia DPEC (Declara????o Pr??via da Emiss??o em Conting??ncia); 5=Conting??ncia FS-DA, com impress??o do DANFE em formul??rio de seguran??a; 6=Conting??ncia SVC-AN (SEFAZ Virtual de Conting??ncia do AN); 7=Conting??ncia SVC-RS (SEFAZ Virtual de Conting??ncia do RS) 9=Conting??ncia off-line da NFC-e (as demais op????es de conting??ncia s??o v??lidas tamb??m para a NFC-e). Para a NFC-e somente est??o dispon??veis e s??o v??lidas as op????es de conting??ncia 5 e 9; .");
        }

        if (StringUtils.isNotEmpty(ide.getCDV()) && ide.getCDV().length() != 1) {
            erros.add("O tamanho do campo D??gito Verificador da Chave de Acesso da NF-e dever?? conter apenas 1 d??gito, Informar o DV da Chave de Acesso da NF-e, o DV ser?? calculado com a aplica????o do algoritmo m??dulo 11 (base 2,9) da Chave de Acesso. (vide item 5 do Manual de Orienta????o).");
        }

        if (StringUtils.isNotEmpty(ide.getTpAmb()) && ide.getTpAmb().length() != 1) {
            erros.add("O tamanho do campo Identifica????o do Ambiente dever?? conter apenas 1 d??gito, 1=Produ????o/2=Homologa????o.");
        }

        if (StringUtils.isNotEmpty(ide.getFinNFe()) && ide.getFinNFe().length() != 1) {
            erros.add("O tamanho do campo Finalidade de emiss??o da NF-e dever?? conter apenas 1 d??gito, 1=NF-e normal; 2=NF-e complementar; 3=NF-e de ajuste; 4=Devolu????o de mercadoria.");
        }

        if (StringUtils.isNotEmpty(ide.getIndFinal()) && ide.getIndFinal().length() != 1) {
            erros.add("O tamanho do campo Indica opera????o com Consumidor final dever?? conter apenas 1 d??gito, 0=Normal; 1=Consumidor final.");
        }

        if (StringUtils.isNotEmpty(ide.getIndPres()) && ide.getIndPres().length() != 1) {
            erros.add("O tamanho do campo Indicador de presen??a do comprador no estabelecimento comercial no momento da opera????o dever?? conter apenas 1 d??gito, 0=N??o se aplica (por exemplo, Nota Fiscal complementar ou de ajuste); 1=Opera????o presencial; 2=Opera????o n??o presencial, pela Internet; 3=Opera????o n??o presencial, Teleatendimento; 4=NFC-e em opera????o com entrega a domic??lio; 9=Opera????o n??o presencial, outros.");
        }

        if (StringUtils.isNotEmpty(ide.getProcEmi()) && ide.getProcEmi().length() != 1) {
            erros.add("O tamanho do campo Processo de emiss??o da NF-e dever?? conter apenas 1 d??gito, 0=Emiss??o de NF-e com aplicativo do contribuinte; 1=Emiss??o de NF-e avulsa pelo Fisco; 2=Emiss??o de NF-e avulsa, pelo contribuinte com seu certificado digital, atrav??s do site do Fisco; 3=Emiss??o NF-e pelo contribuinte com aplicativo fornecido pelo Fisco.");
        }

        if ((StringUtils.isNotEmpty(ide.getVerProc())) && ide.getVerProc().length() == 0 || ide.getVerProc().length() > 20) {
            erros.add("O tamanho do campo Vers??o do Processo de emiss??o da NF-e dever?? conter de 1 a 20 caracteres, Informar a vers??o do aplicativo emissor de NF-e.");
        }
        return erros;
    }

    public static List<String> validarEmit(NFe.InfNFe.Emit emit) {
        List<String> erros = new ArrayList<>();
        if (emit.getCNPJ() != null && !CpfCnpjUtil.validarCNPJ(emit.getCNPJ())) {
            erros.add("O CNPJ informado ?? inv??lido.");
        }
        if (emit.getCPF() != null && !CpfCnpjUtil.validarCPF(emit.getCPF())) {
            erros.add("O CPF informado ?? inv??lido.");
        }
        if (emit.getCNPJ() == null && emit.getCPF() == null) {
            erros.add("Informe o CPF ou o CNPJ do emitente.");
        }
        if (emit.getXNome() == null) {
            erros.add("Informe o nome do emissor.");
        }
        if (emit.getEnderEmit() == null) {
            erros.add("Informe o endere??o do emissor.");
        } else {
            erros.addAll(validarTEnderEmi(emit.getEnderEmit(), "do emitente"));
        }
        if (emit.getIE() == null) {
            erros.add("Informe a IE.");
        }
        if (emit.getCRT() == null) {
            erros.add("Informe o c??digo do regime tribut??rio.");
        } else {
            // TODO adicionar valida????o do CRT
        }

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(emit.getCNAE()) && emit.getCNAE().length() > 7) {
            erros.add("O tamanho do campo CNAE fiscal dever?? conter ate 7 caracteres, Campo Opcional. Pode ser informado quando a Inscri????o Municipal (id:C19) for informada.");
        }
        String cpfCnpjSemMascara;
        if (StringUtils.isNotEmpty(emit.getCPF())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCpf(emit.getCPF());

            if (cpfCnpjSemMascara.length() != 11) {
                erros.add("O tamanho do campo CPF dever?? conter 11 d??gitos, Informar o CNPJ do emitente. Na emiss??o de NF-e avulsa pelo Fisco, as informa????es do remetente ser??o informadas neste grupo. O CNPJ ou CPF dever??o ser informados com os zeros n??o significativos.");
            }
        }
        if (StringUtils.isNotEmpty(emit.getCNPJ())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCnpj(emit.getCNPJ());

            if (cpfCnpjSemMascara.length() != 14) {
                erros.add("O tamanho do campo CNPJ dever?? conter 14 d??gitos, Informar o CNPJ do emitente. Na emiss??o de NF-e avulsa pelo Fisco, as informa????es do remetente ser??o informadas neste grupo. O CNPJ ou CPF dever??o ser informados com os zeros n??o significativos.");
            }
        }
        if (StringUtils.isNotEmpty(emit.getCRT()) && (emit.getCRT().length() != 1)) {
            erros.add("O tamanho do campo CNPJ dever?? conter apenas 1 d??gito, 1=Simples Nacional; 2=Simples Nacional, excesso sublimite de receita bruta; 3=Regime Normal. (v2.0)");
        }
        if (StringUtils.isNotEmpty(emit.getIE()) && (emit.getIE().length() == 1 || emit.getIE().length() > 14)) {
            erros.add("O tamanho do campo Inscri????o Estadual do Emitente dever?? conter de 1 ate 14 d??gitos, Informar somente os algarismos, sem os caracteres de formata????o (ponto, barra, h??fen, etc.). Na emiss??o de NF-e Avulsa pode ser informado o literal ???ISENTO???? para os contribuintes do ICMS isentos de inscri????o no Cadastro de Contribuintes de ICMS.");
        }
        if (StringUtils.isNotEmpty(emit.getIEST()) && (emit.getIEST().length() == 1 || emit.getIEST().length() > 14)) {
            erros.add("O tamanho do campo IE do Substituto Tribut??rio dever?? conter de 1 ate 14 d??gitos, IE do Substituto Tribut??rio da UF de destino da mercadoria, quando houver a reten????o do ICMS ST para a UF de destino.");
        }
        if (StringUtils.isNotEmpty(emit.getIM()) && (emit.getIM().length() == 0 || emit.getIM().length() > 15)) {
            erros.add("O tamanho do campo Inscri????o Municipal do Prestador de Servi??o dever?? conter de 1 ate 14 d??gitos, IE do Substituto Tribut??rio da UF de destino da mercadoria, quando houver a reten????o do ICMS ST para a UF de destino.");
        }
        if (StringUtils.isNotEmpty(emit.getXFant()) && (emit.getXFant().length() == 0 || emit.getXFant().length() > 60)) {
            erros.add("O tamanho do campo Nome fantasia dever?? conter de 1 ate 14 caracteres.");
        }
        if (StringUtils.isNotEmpty(emit.getXNome()) && (emit.getXNome().length() == 1 || emit.getXNome().length() > 60)) {
            erros.add("O tamanho do campo Raz??o Social ou Nome do emitente dever?? conter de 2 ate 60 caracteres.");
        }
        return erros;
    }

    public static List<String> validarAvulsa(NFe.InfNFe.Avulsa avulsa, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(avulsa.getFone()) && (avulsa.getFone().length() == 5 || avulsa.getFone().length() > 14)) {
            erros.add("O tamanho do campo Telefone dever?? conter de 6 ate 14 caracteres.");
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
                erros.add("O tamanho do campo CPF dever?? conter 11 d??gitos, Informar o CNPJ do emitente. Na emiss??o de NF-e avulsa pelo Fisco, as informa????es do remetente ser??o informadas neste grupo. O CNPJ ou CPF dever??o ser informados com os zeros n??o significativos.");
            } else if (!CpfCnpjUtil.validarCPF(dest.getCPF())) {
                erros.add("O CPF do cliente est?? inv??lido.");
            }
        }
        if (StringUtils.isNotEmpty(dest.getCNPJ())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCnpj(dest.getCNPJ());
            if (cpfCnpjSemMascara.length() != 14) {
                erros.add("O tamanho do campo CNPJ dever?? conter 14 d??gitos, Informar o CNPJ do emitente. Na emiss??o de NF-e avulsa pelo Fisco, as informa????es do remetente ser??o informadas neste grupo. O CNPJ ou CPF dever??o ser informados com os zeros n??o significativos.");
            } else if (!CpfCnpjUtil.validarCNPJ(dest.getCNPJ())) {
                erros.add("O CNPJ do cliente est?? inv??lido.");
            }
        }

        if (StringUtils.isNotEmpty(dest.getEmail()) && (dest.getEmail().length() == 0 || dest.getEmail().length() > 60)) {
            erros.add("O tamanho do campo Email dever?? conter de 1 ate 60 caracteres, Campo pode ser utilizado para informar o e-mail de recep????o da NF-e indicada pelo destinat??rio (v2.0)");
        }

        if (StringUtils.isNotEmpty(dest.getIE()) && (dest.getIE().length() == 1 || dest.getIE().length() > 14)) {
            erros.add("O tamanho do campo IE dever?? conter de 2 ate 14 caracteres, Campo opcional. Informar somente os algarismos, sem os caracteres de formata????o (ponto, barra, h??fen, etc.)");
        }

        if (StringUtils.isNotEmpty(dest.getIM()) && (dest.getIM().length() == 0 || dest.getIM().length() > 15)) {
            erros.add("O tamanho do campo Inscri????o Municipal do Tomador do Servi??o dever?? conter de 2 ate 14 caracteres, Campo opcional, pode ser informado na NF-e conjugada, com itens de produtos sujeitos ao ICMS e itens de servi??os sujeitos ao ISSQN.");
        }

        if (StringUtils.isNotEmpty(dest.getISUF()) && (dest.getISUF().length() == 7 || dest.getISUF().length() > 9)) {
            erros.add("O tamanho do campo Inscri????o na SUFRAMA dever?? conter de 8 ate 9 caracteres, Campo opcional. Obrigat??rio, nas opera????es que se beneficiam de incentivos fiscais existentes nas ??reas sob controle da SUFRAMA. A omiss??o desta informa????o impede o processamento da opera????o pelo Sistema de Mercadoria Nacional da SUFRAMA e a libera????o da Declara????o de Ingresso, prejudicando a comprova????o do ingresso / internamento da mercadoria nestas ??reas. (v2.0)");
        }

        if (StringUtils.isNotEmpty(dest.getIndIEDest()) && (dest.getIndIEDest().length() != 1)) {
            erros.add("O tamanho do campo Indicador da IE do Destinat??rio dever?? conter apenas 1 d??gito, 1=Contribuinte ICMS (informar a IE do destinat??rio); 2=Contribuinte isento de Inscri????o no cadastro de Contribuintes.");
        }

        if (StringUtils.isNotEmpty(dest.getXNome()) && (dest.getXNome().length() == 1 || dest.getXNome().length() > 60)) {
            erros.add("O tamanho do campo Raz??o Social ou nome do destinat??rio dever?? conter de 2 ate 60 caracteres, Tag obrigat??ria para a NF-e (modelo 55) e opcional para a NFCe.");
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
                erros.add("O tamanho do campo CPF dever?? conter 11 d??gitos,O CPF em caso de pessoa f??sica .");
            }
        }
        if (StringUtils.isNotEmpty(retirada.getCNPJ())) {
            cpfCnpjSemMascara = CpfCnpjUtil.removerMascaraCnpj(retirada.getCNPJ());
            if (cpfCnpjSemMascara.length() != 14) {
                erros.add("O tamanho do campo CNPJ dever?? conter 14 d??gitos, CNPJ do contribuinte ");
            }
        }

        if (StringUtils.isNotEmpty(retirada.getCMun()) && (retirada.getCMun().length() > 7)) {
            erros.add("O tamanho do campo de C??digo do Munic??pio dever?? conter de 1 ate 60 caracteres, C??digo do Munic??pio do Contribuinte, conforme Tabela do IBGE .");
        }

        if (StringUtils.isNotEmpty(retirada.getNro()) && retirada.getNro().length() == 0 || retirada.getNro().length() > 60) {
            erros.add("O tamanho do campo de N??mero dever?? conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(retirada.getXBairro()) && (retirada.getXBairro().length() == 0 || retirada.getXBairro().length() > 60)) {
            erros.add("O tamanho do campo Nome do Bairro dever?? conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(retirada.getXCpl()) && (retirada.getXCpl().length() == 0 || retirada.getXCpl().length() > 60)) {
            erros.add("O tamanho do campo Complemento dever?? conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(retirada.getXLgr()) && (retirada.getXLgr().length() == 0 || retirada.getXLgr().length() > 255)) {
            erros.add("O tamanho do campo Logradouro dever?? conter de 1 ate 255 caracteres.");
        }

        if (StringUtils.isNotEmpty(retirada.getXMun()) && (retirada.getXMun().length() == 0 || retirada.getXMun().length() > 60)) {
            erros.add("O tamanho do campo Nome do munic??pio dever?? conter de 1 ate 60 caracteres.");
        }

        // transporte
        if (StringUtils.isNotEmpty(retirada.getCMun()) && (nf.getObjTNFe().getInfNFe().getTransp().getModFrete().length() != 1)) {
            erros.add("O tamanho do campo Modalidade do frete dever?? conter apenas 1 d??gito, 0=Por conta do emitente; 1=Por conta do destinat??rio/remetente; 2=Por conta de terceiros; 9=Sem frete. (V2.0).");
        }
        return erros;
    }

    public static List<String> validarEntrega(TLocal entrega, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        if (StringUtils.isNotEmpty(entrega.getCMun()) && (entrega.getCMun().length() > 7)) {
            erros.add("O tamanho do campo C??digo do munic??pio dever?? conter de 7 caracteres, Utilizar a Tabela do IBGE (Anexo IX- Tabela de UF, Munic??pio e Pa??s).");
        }

        if (StringUtils.isNotEmpty(entrega.getNro()) && (entrega.getNro().length() == 1 || entrega.getNro().length() > 60)) {
            erros.add("O tamanho do campo N??mero dever?? conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(entrega.getXBairro()) && (entrega.getXBairro().length() == 1 || entrega.getXBairro().length() > 60)) {
            erros.add("O tamanho do campo Bairro dever?? conter de 2 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(entrega.getXCpl()) && (entrega.getXCpl().length() == 0 || entrega.getXCpl().length() > 60)) {
            erros.add("O tamanho do campo Complemento dever?? conter de 1 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(entrega.getXLgr()) && (entrega.getXLgr().length() == 1 || entrega.getXLgr().length() > 60)) {
            erros.add("O tamanho do campo Logradouro dever?? conter de 2 ate 60 caracteres.");
        }

        if (StringUtils.isNotEmpty(entrega.getXMun()) && (entrega.getXMun().length() == 1 || entrega.getXMun().length() > 60)) {
            erros.add("O tamanho do campo Nome do munic??pio dever?? conter de 2 ate 60 caracteres.");
        }
        return erros;
    }

    public static List<String> validarAutXML(List<NFe.InfNFe.AutXML> autXML) {
        List<String> erros = new ArrayList<>();
        autXML.forEach(aut -> {
            if (aut.getCNPJ() != null && !CpfCnpjUtil.validarCNPJ(aut.getCNPJ())) {
                erros.add("O CNPJ " + aut.getCNPJ() + " ?? inv??lido.");
            }
            if (aut.getCPF() != null && !CpfCnpjUtil.validarCPF(aut.getCPF())) {
                erros.add("O CPF " + aut.getCPF() + " ?? inv??lido.");
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
                erros.add("O item " + (i + 1) + " da nota n??o est?? configurado corretamente.");
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
                erros.add("Informe o c??digo de barras do produto " + prodName + ". (No cadastro do produto; Informe \"SEM GTIN\" caso o produto n??o tenha um c??digo)");
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
            erros.add("O tamanho do campo Modalidade do frete dever?? contem apenas 1 d??gito, 0=Por conta do emitente; 1=Por conta do destinat??rio/remetente; 2=Por conta de terceiros; 9=Sem frete. (V2.0).");
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
                    erros.add("O tipo de pagamento informado ?? inv??lido.");
                }
                if (detPag.getTPag() == null) {
                    erros.add("Informe o meio de pagamento.");
                }
                if (detPag.getVPag() == null) {
                    erros.add("Informe valor do pagamento.");
                }
                if (detPag.getCard() != null) {
                    if (!detPag.getTPag().equals(EnumMeioDePagamento.CARTAO_DE_CREDITO.getCodigo()) && !detPag.getTPag().equals(EnumMeioDePagamento.CARTAO_DE_DEBITO.getCodigo())) {
                        erros.add("Informado dados do cart??o para um meio de pagamento diferente de \"Cart??o de cr??dito\" ou \"Cart??o de d??bito\".");
                    } else {
                        if (detPag.getCard().getTpIntegra() == null) {
                            erros.add("Informe o tipo de integra????o do pagamento via cart??o.");
                        } else if (!detPag.getCard().getTpIntegra().equals(("1")) && !detPag.getCard().getTpIntegra().equals("2")) {
                            erros.add("O tipo de integra????o do pagamento via cart??o informado ?? inv??lido.");
                        }
                        if (detPag.getCard().getTpIntegra().equals(("1"))) {
                            if (detPag.getCard().getCNPJ() == null) {
                                erros.add("Informe o CNPJ da credenciadora do cart??o.");
                            } else if (!CpfCnpjUtil.validarCNPJ(detPag.getCard().getCNPJ())) {
                                erros.add("O CNPJ da credenciadora do cart??o informado ?? inv??lido.");
                            }
                            if (detPag.getCard().getTBand() == null) {
                                erros.add("Informe a bandeira do cart??o.");
                            }
                            if (detPag.getCard().getCAut() == null) {
                                erros.add("Informe o c??digo de autoriza????o da transa????o.");
                            }
                        }

                    }
                }
            });
        }
        if (pag.getVTroco() != null && pag.getVTroco() < 0) {
            erros.add(NumeroUtil.converterDecimalParaString(pag.getVTroco(), 2) + " n??o ?? um valor de troco v??lido.");
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
            erros.add("O tamanho do campo de Pedido dever?? conter de 1 ate 60 caracteres.");
        }
        return erros;
    }

    public static List<String> validarCana(NFe.InfNFe.Cana cana, NF nf, Empresa empresa) {
        List<String> erros = new ArrayList<>();

        //Tamanho dos campos
        return erros;
    }

}
