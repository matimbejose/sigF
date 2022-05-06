package br.com.villefortconsulting.sgfinancas.nfe.util;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnderEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEndereco;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TUf;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TUfEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Csosn;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import br.com.villefortconsulting.sgfinancas.nfe.ConfiguracoesIniciaisNfe;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumCST;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributario;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumAmbienteEmissaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumCodigoModeloFiscal;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumDestinoOperacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumFormatoImpressaoDanfe;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumOperacaoConsumidor;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumProcessoEmissao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoEmissaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoEntradaSaida;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumTipoFrete;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Cobr;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Dest;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS10;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS20;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS30;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS51;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS70;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Det.Prod;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Emit;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Ide;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.InfAdic;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Pag;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Pag.DetPag;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Total.ICMSTot;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Transp;
import br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Transp.RetTransp;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.EntityIdHolder;
import br.com.villefortconsulting.util.NumeroUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Filler {

    public static void preencherRestante(NF nf) {
        nf.getObjTNFe().getInfNFe().getDest().getEnderDest().setUF(TUf.valueOf(nf.getIdUFCliente().getDescricao()));
        nf.getObjTNFe().getInfNFe().getEmit().getEnderEmit().setUF(TUfEmi.valueOf(nf.getIdUfPrestacao().getDescricao()));
    }

    public static void preencherNFe(NF nf, Empresa empresa) {
        nf.getObjTNFe().getInfNFe().setVersao("4.00");
        nf.getObjTNFe().getInfNFe().setId(nf.getChave());
        nf.getObjTNFe().getInfNFe().getIde().setNNF(nf.getNumeroNotaFiscal() + "");
        nf.getObjTNFe().getInfNFe().getIde().setCDV(nf.getChave().substring(43));
        if (nf.getEnumModelo().isCalculaImpostos()) {
            preencherImposto(nf, empresa);
            preencherTotais(nf);
        }
        final int qteFormaPag = nf.getObjTNFe().getInfNFe().getPag().getDetPag().size();
        final Double valorNF = nf.getObjTNFe().getInfNFe().getTotal().getICMSTot().getVNF();
        nf.getObjTNFe().getInfNFe().getPag().getDetPag().forEach(pag -> {
            if (pag.getVPag() == null || pag.getVPag() == 0d) {
                pag.setVPag(valorNF / qteFormaPag);
            }
        });
        preencherEmit(nf, empresa);
        switch (nf.getEnumModelo()) {
            case DEVOLUCAO:// Devolução a partir de uma PJ
            case ENTRADA_DEVOLUCAO:// Devolução a partir de uma PF
                nf.getObjTNFe().getInfNFe().getIde().setNatOp("Devolução");
                nf.getObjTNFe().getInfNFe().getIde().setVerProc("VERSAO 4.00");
                break;
            case TRANSFERENCIA:
                nf.getObjTNFe().getInfNFe().getIde().setNatOp("Transferência de mercadoria");
                nf.getObjTNFe().getInfNFe().getIde().setVerProc("VERSAO 4.00");
                break;
            case VENDA:
                nf.getObjTNFe().getInfNFe().getIde().setNatOp("Venda");
                break;
            case ENTRADA_DA_COMPRA:
                nf.getObjTNFe().getInfNFe().getIde().setNatOp("Entrada de compra");
                break;
            case SERVICO:
                nf.getObjTNFe().getInfNFe().getIde().setNatOp("Prestação de serviço tributado pelo ISSQN");
                break;
            case COMPLEMENTAR:
            case OUTRA:
                break;
        }
    }

    public static void preencherTotais(NF nf) {
        ICMSTot icmsTot = nf.getObjTNFe().getInfNFe().getTotal().getICMSTot();
        icmsTot.reset();
        nf.getObjTNFe().getInfNFe().getDet().stream().forEach(det -> {
            Det.Imposto.ICMS icms = det.getImposto().getICMS();
            if (icms != null) {
                Det.TipoICMSBase icmsBase = icms.getImpostoPreenchido();
                if (icmsBase instanceof Det.TipoICMS) {
                    Det.TipoICMS icmsSelecionado = (Det.TipoICMS) det.getImposto().getICMS().getImpostoPreenchido();
                    if (icmsSelecionado != null) {
                        icmsTot.addVBC(icmsSelecionado.getVBC());
                        icmsTot.addVICMS(icmsSelecionado.getVICMS());
                        icmsTot.addVFCP(icmsSelecionado.getVFCP());
                    }
                }
            }

            icmsTot.addVICMSDeson(0d);
            icmsTot.addVFCPUFDest(0d);
            icmsTot.addVICMSUFDest(0d);
            icmsTot.addVICMSUFRemet(0d);
            icmsTot.addVBCST(0d);
            icmsTot.addVST(0d);
            icmsTot.addVFCPST(0d);
            icmsTot.addVFCPSTRet(0d);
            icmsTot.addVProd(det.getProd().getVProd());
            icmsTot.addVFrete(det.getProd().getVFrete());
            icmsTot.addVSeg(det.getProd().getVSeg());
            icmsTot.addVDesc(det.getProd().getVDesc());
            icmsTot.addVII(0d);
//            icmsTot.addVIPI(0d);
//            icmsTot.addVIPIDevol(0d);
            if (det.getImposto().getPIS() != null) {
                icmsTot.addVPIS(det.getImposto().getPIS().getTotal());
            }
            if (det.getImposto().getCOFINS() != null) {
                icmsTot.addVCOFINS(det.getImposto().getCOFINS().getTotal());
            }
            icmsTot.addVOutro(det.getProd().getVOutro());
            icmsTot.addVNF(det.getProd().getVProd());
        });
    }

    public static Ide preencherIde(NF nf, Empresa empresa) {
        Ide ide = nf.getObjTNFe().getInfNFe().getIde();
        return preencherIde(nf, empresa, ide.getTpEmis(), ide.getVerProc());
    }

    public static Ide preencherIde(NF nf, Empresa empresa, String naturezaOperacao, String verProc) {
        Ide ide = new Ide();
        if (nf.getObjTNFe() != null && nf.getObjTNFe().getInfNFe() != null && nf.getObjTNFe().getInfNFe().getIde() != null) {
            ide = nf.getObjTNFe().getInfNFe().getIde();
        }
        ide.setCUF(empresa.getEndereco().getIdCidade().getIdUF().getCuf());

        if (naturezaOperacao.equals(EnumTipoEmissaoNF.EMISSAO_NORMAL.getTipo())) {
            ide.setNatOp("VENDA");
        } else {
            ide.setNatOp("Devolução");
        }

        //Identificador de local de destino da operação
        // 1=Operação interna; 2=Operação interestadual; 3=Operação com exterior
        if (nf.getDestinoOperacao() != null) {
            ide.setIdDest(nf.getDestinoOperacao());
        } else {
            ide.setIdDest(EnumDestinoOperacao.OPERACAO_INTERNA.getTipo());
        }

        ide.setMod(EnumCodigoModeloFiscal.NF_E.getTipo());  // 55=NF-e emitida em substituição ao modelo 1 ou 1A; 65=NFC-e, utilizada nas operações de venda no varejo (a critério da UF aceitar este modelo de documento).
        ide.setSerie("1");
        ide.setDhEmi(DataUtil.retornaDataNFe(new Date()));
        ide.setDhSaiEnt(DataUtil.retornaDataNFe(new Date()));
        ide.setTpNF(EnumTipoEntradaSaida.SAIDA.getTipo()); // 0=Entrada; 1=Saída
        ide.setCMunFG(empresa.getEndereco().getIdCidade().getCodigoIBGE());
        ide.setTpImp(EnumFormatoImpressaoDanfe.NORMAL.getTipo());  //1=DANFE normal, Retrato
        ide.setTpEmis(naturezaOperacao);
        ide.setFinNFe("1");  // 1=NF-e normal
        ide.setIndFinal(EnumOperacaoConsumidor.CONSUMIDOR_FINAL.getTipo());  // 0=Normal; 1=Consumidor final
        ide.setProcEmi(EnumProcessoEmissao.COM_APLICATIVO_CONTRIBUINTE.getTipo());  // 0=Emissão de NF-e com aplicativo do contribuinte

        // Versão do Processo de emissão da NF-e
        if (verProc != null) {
            ide.setVerProc(verProc);  // colocar na tabela de parametro
        } else {
            ide.setVerProc("0.01");  // colocar na tabela de parametro
        }

        return ide;
    }

    public static Cobr preencherCobr() {
        return new Cobr();
    }

    public static Emit preencherEmit(NF nf, Empresa empresa) {
        Emit emit = nf.getObjTNFe().getInfNFe().getEmit();
        emit.setXNome(empresa.getNomeFantasia());
        emit.setCpfCnpj(empresa.getCnpj());
        emit.setEnderEmit(new TEnderEmi());
        emit.getEnderEmit().setCEP(empresa.getEndereco().getCep());
        emit.getEnderEmit().setCMun(empresa.getEndereco().getIdCidade().getCodigoIBGE());
        emit.getEnderEmit().setFone(empresa.getFone());
        emit.getEnderEmit().setNro(empresa.getEndereco().getNumero());
        emit.getEnderEmit().setUF(TUfEmi.valueOf(empresa.getEndereco().getIdCidade().getIdUF().getDescricao()));
        emit.getEnderEmit().setXBairro(empresa.getEndereco().getBairro());
        if (empresa.getEndereco().getComplemento() != null) {
            emit.getEnderEmit().setXCpl(empresa.getEndereco().getComplemento());
        }
        emit.getEnderEmit().setXLgr(empresa.getEndereco().getEndereco());
        emit.getEnderEmit().setXMun(empresa.getEndereco().getIdCidade().getDescricao());
        emit.setIE(empresa.getInscricaoEstadual());
        if (empresa.getRegimeTributario().equals(EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma())) {   // simples nacional
            emit.setCRT("1");// 1=Simples Nacional; 2=Simples Nacional, excesso sublimite de receita bruta; 3=Regime Normal. (v2.0).
        } else {
            emit.setCRT("3");
        }
        return emit;
    }

    public static Dest preencherDest(Cliente cliente, Fornecedor fornecedor, ConfiguracoesIniciaisNfe config) {
        boolean isHomolog = config != null && config.getAmbiente().equals(EnumAmbienteEmissaoNF.HOMOLOGACAO.getTipo());
        Dest dest = new Dest();
        Endereco endereco = null;

        if (cliente != null) {
            endereco = cliente.getEndereco();
            String cpfCnpj = CpfCnpjUtil.removerMascaraCnpj(cliente.getCpfCNPJ());
            if (cliente.getTipo().equals("PJ")) {
                dest.setCNPJ(cpfCnpj);
            } else {
                dest.setCPF(cpfCnpj);
            }

            if (!isHomolog) {
                dest.setXNome(cliente.getNome());
            }
            dest.setIndIEDest(cliente.getIndicadorInscricaoEstadual());
            if (!"9".equals(cliente.getIndicadorInscricaoEstadual()) && cliente.getInscricaoEstadual() != null) {
                dest.setIE(cliente.getInscricaoEstadual());
            }
            if (cliente.getInscricaoMunicipal() != null) {
                dest.setIM(cliente.getInscricaoMunicipal());
            }
            dest.setEmail(cliente.getEmail());
        } else if (fornecedor != null) {
            endereco = fornecedor.getEndereco();
            dest.setCNPJ(CpfCnpjUtil.removerMascaraCnpj(fornecedor.getCpfCnpj()));
            if (!isHomolog) {
                dest.setXNome(fornecedor.getRazaoSocial());
            } else {
                dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
            }
            if (fornecedor.getInscricaoEstadual() != null) {
                dest.setIE(fornecedor.getInscricaoEstadual());
            }
            if (fornecedor.getInscricaoMunicipal() != null) {
                dest.setIM(fornecedor.getInscricaoMunicipal());
            }
            dest.setEmail(fornecedor.getEmail());
        }

        TEndereco enderDest = new TEndereco();
        if (endereco != null) {
            enderDest.setXLgr(endereco.getEndereco());
            enderDest.setNro(endereco.getNumero());
            enderDest.setXBairro(endereco.getBairro());
            if (endereco.getIdCidade() != null) {
                enderDest.setCMun(endereco.getIdCidade().getCodigoIBGE());
                enderDest.setXMun(endereco.getIdCidade().getDescricao());
                enderDest.setUF(TUf.valueOf(endereco.getIdCidade().getIdUF().getDescricao()));
            }
            enderDest.setCEP(endereco.getCep().replace(".", "").replace("-", ""));
            enderDest.setCPais("1058");
            enderDest.setXPais("Brasil");
            String tel = "";
            if (cliente != null) {
                tel = cliente.getTelefoneComercial();
            } else if (fornecedor != null) {
                tel = fornecedor.getFoneComercial();
            }
            if (StringUtils.isNotEmpty(tel)) {
                enderDest.setFone(tel.replaceAll("\\D", ""));
            }
        }

        dest.setEnderDest(enderDest);

        return dest;
    }

    public static String gerarChaveNF(Empresa empresa, NF nf) {
        return gerarChaveNF(empresa, nf, nf.getObjTNFe().getInfNFe().getIde().getTpEmis());
    }

    public static String gerarChaveNF(Empresa empresa, NF nf, String tipo) {
        StringBuilder chave = new StringBuilder();

        // cUF - Código da UF do emitente do Documento Fiscal
        chave.append(empresa.getEndereco().getIdCidade().getIdUF().getCuf());

        // AAMM - Ano e Mês de emissão da NF-e
        chave.append(DataUtil.getAnoCorrenteCom2Digitos()).append(DataUtil.getMesCorrenteCom2Digitos());

        // CNPJ - CNPJ do emitente
        chave.append(CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()));

        // mod - Modelo do Documento Fiscal
        chave.append("55");

        // serie - Série do Documento Fiscal
        chave.append(String.format("%03d", Integer.parseInt(nf.getObjTNFe().getInfNFe().getIde().getSerie())));

        // nNF - Número do Documento Fiscal 9 caracteres
        chave.append(String.format("%09d", nf.getNumeroNotaFiscal()));

        // tpEmis – forma de emissão da NF-e
        chave.append(EnumTipoEmissaoNF.EMISSAO_NORMAL.getTipo());

        // cNF - Código Numérico que compõe a Chave de Acesso - 8 caracteres
        chave.append(String.format("%08d", nf.getCodigoNumerico()));

        // cDV - Dígito Verificador da Chave de Acesso
        chave.append(NumeroUtil.modulo11(chave.toString()));

        return chave.toString();
    }

    public static Transp preencherTranp(NF nf, Empresa empresa) {
        Transp transp = nf.getObjTNFe().getInfNFe().getTransp();
        if (transp == null) {
            transp = new br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Transp();
        }
        if ("S".equals(nf.getTemFrete())) {
            transp.setModFrete(nf.getResponsavelFrete());
            if ("S".equals(nf.getUsaTransportadora())) {
                br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Transp.Transporta transporta = new br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Transp.Transporta();
                if (EnumTipoFrete.POR_CONTA_EMITENTE.getTipo().equals(transp.getModFrete())) {
                    transporta.setCNPJ(empresa.getCnpj().replace(".", "").replace("/", "").replace("-", ""));
                    transporta.setXNome(empresa.getDescricao());
                    transporta.setIE(empresa.getInscricaoEstadual());
                    transporta.setXEnder(empresa.getEndereco().getEndereco());

                    if (empresa.getEndereco().getIdCidade() != null) {
                        transporta.setXMun(empresa.getEndereco().getIdCidade().getDescricao());
                        transporta.setUF(br.com.swconsultoria.nfe.schema_4.inutNFe.TUf.valueOf(empresa.getEndereco().getIdCidade().getIdUF().getDescricao()));
                    }
                } else if (EnumTipoFrete.POR_CONTA_REMETENTE.getTipo().equals(transp.getModFrete())) {
                    Dest dest = nf.getObjTNFe().getInfNFe().getDest();
                    transporta.setCpfCnpj(dest.getCpfCnpj().replace(".", "").replace("/", "").replace("-", ""));
                    transporta.setXNome(dest.getXNome());
                    transporta.setIE(dest.getIE());
                    transporta.setXEnder(dest.getEnderDest().getXLgr());
                    transporta.setXMun(dest.getEnderDest().getXMun());
                    transporta.setUF(br.com.swconsultoria.nfe.schema_4.inutNFe.TUf.valueOf(nf.getIdUFCliente().getDescricao()));
                }

                // Não está sendo usado.
                RetTransp retTransp = new RetTransp();
                retTransp.setVServ(0d);
                retTransp.setVBCRet(0d);
                retTransp.setPICMSRet(0d);
                retTransp.setVICMSRet(0d);
                retTransp.setCMunFG(0);
                retTransp.setCFOP("0");

                TVeiculo tVeiculo = new TVeiculo();
                tVeiculo.setPlaca(nf.getPlacaVeiculo().replace("-", "").toUpperCase());
                tVeiculo.setUF(TUf.fromValue(nf.getIdUfPlacaVeiculo().getDescricao()));
                tVeiculo.setRNTC(nf.getRntcVeiculo());

                br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Transp.Vol vol = new br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Transp.Vol();
                vol.setEsp(nf.getTipoVolumeTransportado());
                vol.setNVol(nf.getNumIdentificacaoVolume());
                vol.setQVol(nf.getQtdVolumeTransportado());
                vol.setPesoB(nf.getPesoBrutoTotal());
                vol.setPesoL(nf.getPesoLiquidoTotal());

                transp.getVol().add(vol);
                transp.setVeicTransp(tVeiculo);
                transp.setTransporta(transporta);
            }
        } else {
            transp.setModFrete(EnumTipoFrete.SEM_FRETE.getTipo());
        }

        return transp;
    }

    public static Pag preencherPagamento(NF nf, String tipo) {
        Pag pagamento = new Pag();
        DetPag detalheDoPagamento = new DetPag();
        Double valorPago;
        if (nf.getIdVenda() != null) {
            valorPago = nf.getIdVenda().getValorPago();
            detalheDoPagamento.setIndPag(nf.getIdVenda().getFormaPagamento().equals(EnumFormaPagamento.AVISTA.getForma()) ? "0" : "1");
            detalheDoPagamento.setTPag(nf.getIdVenda().getIdFormaPagamento().getCodigoNfe());
        } else if (nf.getIdCompra() != null) {
            valorPago = nf.getIdCompra().getValorTotal();
            detalheDoPagamento.setIndPag(nf.getIdCompra().getFormaPagamento().equals(EnumFormaPagamento.AVISTA.getForma()) ? "0" : "1");
        } else {
            valorPago = nf.getObjTNFe().getInfNFe().getDet().stream()
                    .map(Det::getProd)
                    .mapToDouble(Prod::getVProd)
                    .sum();
        }
        if (valorPago == 0d) {
            valorPago = null;
        }

        if (tipo.equals(EnumTipoEmissaoNF.EMISSAO_NORMAL.getTipo())) {
            detalheDoPagamento.setVPag(valorPago);
        } else {
            detalheDoPagamento.setVPag(0d);
        }
        detalheDoPagamento.setTPag(nf.getMeioDePagamento());

        if (nf.getMeioDePagamento() != null && (nf.getMeioDePagamento().equals(EnumMeioDePagamento.CARTAO_DE_CREDITO.getCodigo())
                || nf.getMeioDePagamento().equals(EnumMeioDePagamento.CARTAO_DE_DEBITO.getCodigo()))) {
            br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Pag.DetPag.Card cartao = new br.com.villefortconsulting.sgfinancas.util.nf.NFe.InfNFe.Pag.DetPag.Card();
            cartao.setTpIntegra("1");// Pagamento integrado com o sistema de automação da empresa
            detalheDoPagamento.setCard(cartao);
        }

        pagamento.getDetPag().add(detalheDoPagamento);

        return pagamento;
    }

    public static InfAdic preencherInfAdic(NF nf) {
        InfAdic infAdic = new InfAdic();
        if (StringUtils.isNotEmpty(nf.getInformacoesNota()) || StringUtils.isNotEmpty(nf.getInformacoesFisco())) {
            if (StringUtils.isNotEmpty(nf.getInformacoesNota())) {
                infAdic.setInfCpl(nf.getInformacoesNota());
            }

            if (StringUtils.isNotEmpty(nf.getInformacoesFisco())) {
                infAdic.setInfAdFisco(nf.getInformacoesFisco());
            }
        }
        return infAdic;
    }

    public static List<Det> processaDet(NF nf, Empresa empresa) {
        int nroProduto = 1;
        for (Det det : nf.getObjTNFe().getInfNFe().getDet()) {
            det.setNItem(nroProduto++);

            DadosProduto produto = DadosProduto.builder()
                    .desconto(NumeroUtil.somar(det.getProd().getVDesc()))
                    .idCfop(det.getProd().getIdCfop().getValue())
                    .idCsosn(det.getIdCsosn().getValue())
                    .idCst(det.getIdCst().getValue())
                    .idNcm(det.getProd().getIdNcm().getValue())
                    .idProduto(det.getProd().getIdProduto().getValue())
                    .quantidade(NumeroUtil.somar(det.getProd().getQCom()))
                    .valor(NumeroUtil.somar(det.getProd().getVUnTrib()))
                    .build();
            produto.setIdProduto(det.getProd().getIdProduto().getValue());
            det.getProd().setCProd(produto.getIdProduto().getCodigo());

            Double valorComDesconto = NumeroUtil.formatarCasasDecimais(produto.getValor(), 2);

            det.getProd().setVDesc(produto.getDesconto());
            det.getProd().setCEAN(produto.getIdProduto().getCodigoBarra());
            det.getProd().setXProd(produto.getIdProduto().getDescricao());
            if (produto.getIdCfop() != null) {
                det.getProd().setCFOP(produto.getIdCfop().getCodigo());
            }
            det.getProd().setUCom(produto.getIdProduto().getIdUnidadeMedida().getSigla());
            det.getProd().setQCom(produto.getQuantidade());
            det.getProd().setVUnCom(valorComDesconto);

            if (produto.getIdProduto().getCodigoBarra() != null) {
                det.getProd().setCEANTrib(produto.getIdProduto().getCodigoBarra());
            }

            det.getProd().setUTrib(produto.getQuantidade().toString());
            det.getProd().setQTrib(produto.getQuantidade().toString());
            det.getProd().setVUnTrib(valorComDesconto);

            det.getProd().setIndTot("1");  // 0=Valor do item (vProd) não compõe o valor total da NF-e 1=Valor do item (vProd) compõe o valor total da NF-e (vProd)
            if (det.getProd().getCEAN() == null) {
                det.getProd().setCEAN("");
            }
            if (det.getProd().getCEANTrib() == null) {
                det.getProd().setCEANTrib("");
            }

            preencherImposto(produto, det, empresa);
        }
        return nf.getObjTNFe().getInfNFe().getDet();
    }

    public static List<Det> preencherDet(NF nf, List<VendaProduto> produtosVenda, List<CompraProduto> produtosCompra, Empresa empresa) {
        List<DadosProduto> produtos = new ArrayList<>();
        produtos.addAll(produtosVenda.stream().map(VendaProduto::getDadosProduto).collect(Collectors.toList()));
        produtos.addAll(produtosCompra.stream().map(CompraProduto::getDadosProduto).collect(Collectors.toList()));
        return preencherDet(nf, produtos, empresa);
    }

    public static List<Det> preencherDet(NF nf, List<DadosProduto> produtos, Empresa empresa) {
        nf.getObjTNFe().getInfNFe().getDet().clear();
        nf.getObjTNFe().getInfNFe().getDet().addAll(produtos.stream().map(produto -> {
            Det det = new Det();
            Prod prod = new Prod();
            prod.setIdProduto(new EntityIdHolder<>());
            prod.getIdProduto().setValue(produto.getIdProduto());
            prod.setIdNcm(new EntityIdHolder<>());
            prod.getIdNcm().setValue(produto.getIdNcm());
            prod.setQCom(produto.getQuantidade());
            prod.setVUnTrib(produto.getValor());
            prod.setVDesc(produto.getDesconto());
            det.setProd(prod);
            return det;
        }).collect(Collectors.toList()));
        return processaDet(nf, empresa);

    }

    public static void preencherImposto(NF nf, Empresa empresa) {
        nf.getObjTNFe().getInfNFe().getDet().forEach(det -> preencherImposto(det, empresa));
    }

    public static void preencherImposto(Det det, Empresa empresa) {
        DadosProduto dp = new DadosProduto();
        dp.setDesconto(NumeroUtil.somar(det.getProd().getVDesc()));
        dp.setIdCsosn(new Csosn());
        if (det.getIdCst() != null) {
            dp.setIdCst(det.getIdCst().getValue());
        }
        if (det.getIdCsosn() != null) {
            dp.getIdCsosn().setCodigo(det.getIdCsosn().getValue().getCodigo());
        }
        dp.setIdProduto(det.getProd().getIdProduto().getValue());
        dp.setQuantidade(det.getProd().getQCom());
        dp.setValor(det.getProd().getVProd() - dp.getDesconto());
        preencherImposto(dp, det, empresa);
    }

    public static void preencherImposto(DadosProduto prod, Det det, Empresa empresa) {
        if (prod.getIdProduto().getIdOrigemMercadoria() == null) {
            throw new CadastroException("Preencha a origem da mercadoria para o produto " + prod.getIdProduto().getDescricao(), null);
        }
        if ((prod.getIdCst() == null || prod.getIdCst().getCodigo() == null) && (prod.getIdCsosn() == null || prod.getIdCsosn().getCodigo() == null)) {
            return;// Criação a partir da venda, não tem o CST ainda
        }
        Det.Imposto imposto = new Det.Imposto();
        Det.Imposto.ICMS icms = new Det.Imposto.ICMS();
        Det.Imposto.PIS pis = new Det.Imposto.PIS(det);
        Det.Imposto.COFINS cofins = new Det.Imposto.COFINS(det);
        String origem = "" + prod.getIdProduto().getIdOrigemMercadoria().getCodOrigemMercadoria();
        if (empresa.getRegimeTributario().equals(EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma())) {   // simples nacional
            // verifica se a tributação do produto está corretamente informada
            switch (prod.getIdCsosn().getCodigo()) {
                case "101":
                    // para o simples nacional regime de tributacao 101 --> para testar somente
                    Det.Imposto.ICMS.ICMSSN101 icmssn101 = new Det.Imposto.ICMS.ICMSSN101();
                    icmssn101.setOrig(origem);
                    icmssn101.setCSOSN(prod.getIdCsosn().getCodigo() + "");
                    icmssn101.setPCredSN(0d);
                    icmssn101.setVCredICMSSN(0d);
                    icms.setICMSSN101(icmssn101);
                    break;
                case "102":
                case "103":
                case "300":
                case "400":
                    Det.Imposto.ICMS.ICMSSN102 icmssn102 = new Det.Imposto.ICMS.ICMSSN102();
                    icmssn102.setOrig(origem);
                    icmssn102.setCSOSN(prod.getIdCsosn().getCodigo() + "");
                    icms.setICMSSN102(icmssn102);
                    break;
                case "201":
                    Det.Imposto.ICMS.ICMSSN201 icmssn201 = new Det.Imposto.ICMS.ICMSSN201();
                    icmssn201.setOrig(origem);
                    icmssn201.setCSOSN(prod.getIdCsosn().getCodigo() + "");
                    icmssn201.setModBCST("0");   // 0=Preço tabelado ou máximo sugerido; verificar tabela ou implementação
                    icmssn201.setPCredSN(0d);
                    icmssn201.setVCredICMSSN(0d);
                    icms.setICMSSN201(icmssn201);
                    break;
                case "202":
                case "203":
                    Det.Imposto.ICMS.ICMSSN202 icmssn202 = new Det.Imposto.ICMS.ICMSSN202();
                    icmssn202.setOrig(origem);
                    icmssn202.setCSOSN(prod.getIdCsosn().getCodigo() + "");
                    icmssn202.setModBCST("0");   // 0=Preço tabelado ou máximo sugerido; verificar tabela ou implementação
                    icms.setICMSSN202(icmssn202);
                    break;
                case "500":
                    Det.Imposto.ICMS.ICMSSN500 icmssn500 = new Det.Imposto.ICMS.ICMSSN500();
                    icmssn500.setOrig(origem);
                    icmssn500.setCSOSN(prod.getIdCsosn().getCodigo() + "");
                    icms.setICMSSN500(icmssn500);
                    break;
                case "900":
                    Det.Imposto.ICMS.ICMSSN900 icmssn900 = new Det.Imposto.ICMS.ICMSSN900();
                    icmssn900.setOrig(origem);
                    icmssn900.setCSOSN(prod.getIdCsosn().getCodigo() + "");
                    icms.setICMSSN900(icmssn900);
                    break;
                default:
                    break;
            }

            Det.Imposto.PIS.PISOutr pISOutr = new Det.Imposto.PIS.PISOutr(pis);
            pISOutr.setCST("99");
            pISOutr.setQBCProd("0.00");
            pISOutr.setVAliqProd(0d);
            pISOutr.setVPIS(0d);
            pis.setPISOutr(pISOutr);

            Det.Imposto.COFINS.COFINSOutr cOFINSOutr = new Det.Imposto.COFINS.COFINSOutr(cofins);
            cOFINSOutr.setCST("99");
            cOFINSOutr.setVBC(0d);
            cOFINSOutr.setPCOFINS(0d);
            cOFINSOutr.setVCOFINS(0d);
            cofins.setCOFINSOutr(cOFINSOutr);
        } else {
            // outro regime de tributacao
            switch (EnumCST.retornaEnumSelecionado(prod.getIdCst().getCodigo())) {
                case C00:
                    ICMS00 icms00 = new ICMS00();
                    icms00.setOrig(origem);
                    icms00.setCST("00");
                    icms00.setModBC("3");
                    icms00.setVBC(prod.getValorTotal());
                    icms00.setPFCP(prod.getIdProduto().getPorcentagemFcp());
                    icms00.setVFCP(prod.getValorTotal() * prod.getIdProduto().getPorcentagemFcp() / 100);
                    icms00.setPICMS(prod.getIdProduto().getPorcentagemIcmsInterno());
                    icms00.setVICMS(prod.getValorTotal() / prod.getIdProduto().getPorcentagemIcmsInterno() / 100);
                    icms.setICMS00(icms00);
                    break;
                case C10:
                    ICMS10 icms10 = new ICMS10();
                    icms10.setOrig(origem);
                    icms.setICMS10(icms10);
                    break;
                case C20:
                    ICMS20 icms20 = new ICMS20();
                    icms20.setOrig(origem);
                    icms.setICMS20(icms20);
                    break;
                case C30:
                    ICMS30 icms30 = new ICMS30();
                    icms30.setOrig(origem);
                    icms.setICMS30(icms30);
                    break;
                case C40:
                case C41:
                    ICMS40 icms40 = new ICMS40();
                    icms40.setOrig(origem);
                    icms.setICMS40(icms40);
                    break;
                case C50:
                case C51:
                    ICMS51 icms51 = new ICMS51();
                    icms51.setOrig(origem);
                    icms.setICMS51(icms51);
                    break;
                case C60:
                    ICMS60 icms60 = new ICMS60();
                    icms60.setOrig(origem);
                    icms60.setCST("60");
                    icms.setICMS60(icms60);
                    break;
                case C70:
                    ICMS70 icms70 = new ICMS70();
                    icms70.setOrig(origem);
                    icms.setICMS70(icms70);
                    break;
                case C90:
                    ICMS90 icms90 = new ICMS90();
                    icms90.setOrig(origem);
                    icms.setICMS90(icms90);
                    break;
                default:
                    break;
            }

            Double valor = prod.getValorTotal() * NumeroUtil.somar(prod.getIdProduto().getAliquotaPis()) / 100;
            Det.Imposto.PIS.PISAliq pisAliq = new Det.Imposto.PIS.PISAliq(pis);
            pisAliq.setCST("01");
            pisAliq.setVBC(prod.getValorTotal());
            pisAliq.setPPIS(prod.getIdProduto().getAliquotaPis());
            pisAliq.setVPIS(valor);
            pis.setPISAliq(pisAliq);

            valor = prod.getValorTotal() * NumeroUtil.somar(prod.getIdProduto().getAliquotaCofins()) / 100;
            Det.Imposto.COFINS.COFINSAliq cofinsAliq = new Det.Imposto.COFINS.COFINSAliq(cofins);
            cofinsAliq.setCST("01");
            cofinsAliq.setPCOFINS(prod.getIdProduto().getAliquotaCofins());
            cofinsAliq.setVCOFINS(valor);
            cofinsAliq.setVBC(prod.getValorTotal());
            cofins.setCOFINSAliq(cofinsAliq);
        }

//        if (nf.getTipoEmissao().equals(EnumTipoEmissaoNF.CONTIGENCIA_DPEC.getTipo())) {
//            Det.ImpostoDevol impostoDevol = new Det.ImpostoDevol();
//            impostoDevol.setPDevol(NumeroUtil.converterDecimalParaString(nf.getPorcentagemDevolucao(), 2));
//            Det.ImpostoDevol.IPI ipi = new Det.ImpostoDevol.IPI();
//            ipi.setVIPIDevol(NumeroUtil.converterDecimalParaString(nf.getValorIpiDevolucao(), 2));
//            impostoDevol.setIPI(ipi);
//            det.setImpostoDevol(impostoDevol);
//
//            // outro regime de tributacao
//            ICMS60 icms60 = new ICMS60();
//
//            icms60.setOrig("0");
//            icms60.setCST("60");
//            icms60.setVBCSTRet(NumeroUtil.converterDecimalParaString(nf.getIcms60ValorCstRetencao(), 2));
//            icms60.setPST(0d);
//            icms60.setVICMSSTRet(NumeroUtil.converterDecimalParaString(nf.getIcms60ValorIcmsStRetencao(), 2));
//            icms.setICMS60(icms60);
//
//            PIS pis = new PIS();
//            PISAliq pisAliq = new PISAliq();
//            pisAliq.setCST(nf.getPisCst());
//            pisAliq.setVBC(NumeroUtil.converterDecimalParaString(nf.getPisValorBc(), 2));
//            pisAliq.setPPIS(NumeroUtil.converterDecimalParaString(nf.getPisPercentualPis(), 2));
//            pisAliq.setVPIS(NumeroUtil.converterDecimalParaString(nf.getPisValorPis(), 2));
//            pis.setPISAliq(pisAliq);
//
//            COFINS cofins = new COFINS();
//            COFINSAliq cofinsAliq = new COFINSAliq();
//            cofinsAliq.setCST(nf.getCofinsCst());
//            cofinsAliq.setVBC(NumeroUtil.converterDecimalParaString(nf.getCofinsValorBC(), 2));
//            cofinsAliq.setPCOFINS(NumeroUtil.converterDecimalParaString(nf.getCofinsPercentualCofins(), 2));
//            cofinsAliq.setVCOFINS(NumeroUtil.converterDecimalParaString(nf.getCofinsValorCofins(), 2));
//            cofins.setCOFINSAliq(cofinsAliq);
//        }
        imposto.getContent().put("ICMS", icms);
        imposto.getContent().put("PIS", pis);
        imposto.getContent().put("COFINS", cofins);
        det.setImposto(imposto);
    }

}
