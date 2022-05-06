package br.com.villefortconsulting.sgfinancas.util.nf.cfe;

import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"infCFe", "any"})
@XmlRootElement(name = "CFe")
@Getter
@Setter
public class CFe {

    @XmlElement(required = true)
    protected CFe.InfCFe infCFe;

    @XmlAnyElement(lax = true)
    protected Object any;

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"ide", "emit", "dest", "entrega", "det", "total", "pgto", "infAdic"})
    @Getter
    @Setter
    public static class InfCFe {

        @XmlElement(required = true)
        protected CFe.InfCFe.Ide ide;

        @XmlElement(required = true)
        protected CFe.InfCFe.Emit emit;

        @XmlElement(required = true)
        protected CFe.InfCFe.Dest dest;

        protected CFe.InfCFe.Entrega entrega;

        @XmlElement(required = true)
        protected List<CFe.InfCFe.Det> det;

        @XmlElement(required = true)
        protected CFe.InfCFe.Total total;

        @XmlElement(required = true)
        protected CFe.InfCFe.Pgto pgto;

        protected CFe.InfCFe.InfAdic infAdic;

        @XmlAttribute(name = "versao", required = true)
        protected String versao;

        @XmlAttribute(name = "versaoDadosEnt", required = true)
        protected String versaoDadosEnt;

        @XmlAttribute(name = "versaoSB", required = true)
        protected String versaoSB;

        @XmlAttribute(name = "Id", required = true)
        protected String id;

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"cnpj", "cpf", "xNome"})
        @Getter
        @Setter
        public static class Dest {

            @XmlElement(name = "CNPJ")
            protected String cnpj;

            @XmlElement(name = "CPF")
            protected String cpf;

            protected String xNome;

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"prod", "imposto", "infAdProd"})
        @Getter
        @Setter
        public static class Det {

            @XmlElement(required = true)
            protected CFe.InfCFe.Det.Prod prod;

            @XmlElement(required = true)
            protected CFe.InfCFe.Det.Imposto imposto;

            protected String infAdProd;

            @XmlAttribute(name = "nItem", required = true)
            protected String nItem;

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"vItem12741", "icms", "issqn", "pis", "pisst", "cofins", "cofinsst"})
            @Getter
            @Setter
            public static class Imposto {

                protected String vItem12741;

                @XmlElement(name = "ICMS")
                protected CFe.InfCFe.Det.Imposto.ICMS icms;

                @XmlElement(name = "ISSQN")
                protected CFe.InfCFe.Det.Imposto.ISSQN issqn;

                @XmlElement(name = "PIS", required = true)
                protected CFe.InfCFe.Det.Imposto.PIS pis;

                @XmlElement(name = "PISST")
                protected CFe.InfCFe.Det.Imposto.PISST pisst;

                @XmlElement(name = "COFINS", required = true)
                protected CFe.InfCFe.Det.Imposto.COFINS cofins;

                @XmlElement(name = "COFINSST")
                protected CFe.InfCFe.Det.Imposto.COFINSST cofinsst;

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {"cofinsAliq", "cofinsQtde", "cofinsnt", "cofinssn", "cofinsOutr"})
                @Getter
                @Setter
                public static class COFINS {

                    @XmlElement(name = "COFINSAliq")
                    protected CFe.InfCFe.Det.Imposto.COFINS.COFINSAliq cofinsAliq;

                    @XmlElement(name = "COFINSQtde")
                    protected CFe.InfCFe.Det.Imposto.COFINS.COFINSQtde cofinsQtde;

                    @XmlElement(name = "COFINSNT")
                    protected CFe.InfCFe.Det.Imposto.COFINS.COFINSNT cofinsnt;

                    @XmlElement(name = "COFINSSN")
                    protected CFe.InfCFe.Det.Imposto.COFINS.COFINSSN cofinssn;

                    @XmlElement(name = "COFINSOutr")
                    protected CFe.InfCFe.Det.Imposto.COFINS.COFINSOutr cofinsOutr;

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst", "vbc", "pcofins", "vcofins"})
                    @Getter
                    @Setter
                    public static class COFINSAliq {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                        @XmlElement(name = "vBC", required = true)
                        protected String vbc;

                        @XmlElement(name = "pCOFINS", required = true)
                        protected String pcofins;

                        @XmlElement(name = "vCOFINS", required = true)
                        protected String vcofins;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst"})
                    @Getter
                    @Setter
                    public static class COFINSNT {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst", "vbcOrPCOFINS", "qbcProdOrVAliqProd", "vcofins"})
                    @Getter
                    @Setter
                    public static class COFINSOutr {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                        @XmlElementRefs({
                            @XmlElementRef(name = "vBC", type = JAXBElement.class, required = false),
                            @XmlElementRef(name = "pCOFINS", type = JAXBElement.class, required = false)
                        })
                        protected List<JAXBElement<String>> vbcOrPCOFINS;

                        @XmlElementRefs({
                            @XmlElementRef(name = "vAliqProd", type = JAXBElement.class, required = false),
                            @XmlElementRef(name = "qBCProd", type = JAXBElement.class, required = false)
                        })
                        protected List<JAXBElement<String>> qbcProdOrVAliqProd;

                        @XmlElement(name = "vCOFINS", required = true)
                        protected String vcofins;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst", "qbcProd", "vAliqProd", "vcofins"})
                    @Getter
                    @Setter
                    public static class COFINSQtde {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                        @XmlElement(name = "qBCProd", required = true)
                        protected String qbcProd;

                        @XmlElement(required = true)
                        protected String vAliqProd;

                        @XmlElement(name = "vCOFINS", required = true)
                        protected String vcofins;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst"})
                    @Getter
                    @Setter
                    public static class COFINSSN {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                    }

                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {"vbcOrPCOFINS", "qbcProdOrVAliqProd", "vcofins"})
                @Getter
                @Setter
                public static class COFINSST {

                    @XmlElementRefs({
                        @XmlElementRef(name = "vBC", type = JAXBElement.class, required = false),
                        @XmlElementRef(name = "pCOFINS", type = JAXBElement.class, required = false)
                    })
                    protected List<JAXBElement<String>> vbcOrPCOFINS;

                    @XmlElementRefs({
                        @XmlElementRef(name = "vAliqProd", type = JAXBElement.class, required = false),
                        @XmlElementRef(name = "qBCProd", type = JAXBElement.class, required = false)
                    })
                    protected List<JAXBElement<String>> qbcProdOrVAliqProd;

                    @XmlElement(name = "vCOFINS", required = true)
                    protected String vcofins;

                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {"icms00", "icms40", "icmssn102", "icmssn900"})
                @Getter
                @Setter
                public static class ICMS {

                    @XmlElement(name = "ICMS00")
                    protected CFe.InfCFe.Det.Imposto.ICMS.ICMS00 icms00;

                    @XmlElement(name = "ICMS40")
                    protected CFe.InfCFe.Det.Imposto.ICMS.ICMS40 icms40;

                    @XmlElement(name = "ICMSSN102")
                    protected CFe.InfCFe.Det.Imposto.ICMS.ICMSSN102 icmssn102;

                    @XmlElement(name = "ICMSSN900")
                    protected CFe.InfCFe.Det.Imposto.ICMS.ICMSSN900 icmssn900;

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"orig", "cst", "picms", "vicms"})
                    @Getter
                    @Setter
                    public static class ICMS00 {

                        @XmlElement(name = "Orig", required = true)
                        protected String orig;

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                        @XmlElement(name = "pICMS", required = true)
                        protected String picms;

                        @XmlElement(name = "vICMS", required = true)
                        protected String vicms;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"orig", "cst"})
                    @Getter
                    @Setter
                    public static class ICMS40 {

                        @XmlElement(name = "Orig", required = true)
                        protected String orig;

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"orig", "csosn"})
                    @Getter
                    @Setter
                    public static class ICMSSN102 {

                        @XmlElement(name = "Orig", required = true)
                        protected String orig;

                        @XmlElement(name = "CSOSN", required = true)
                        protected String csosn;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"orig", "csosn", "picms", "vicms"})
                    @Getter
                    @Setter
                    public static class ICMSSN900 {

                        @XmlElement(name = "Orig", required = true)
                        protected String orig;

                        @XmlElement(name = "CSOSN", required = true)
                        protected String csosn;

                        @XmlElement(name = "pICMS", required = true)
                        protected String picms;

                        @XmlElement(name = "vICMS", required = true)
                        protected String vicms;

                    }

                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {"vDeducISSQN", "vbc", "vAliq", "vissqn", "cMunFG", "cListServ", "cServTribMun", "cNatOp", "indIncFisc"})
                @Getter
                @Setter
                public static class ISSQN {

                    @XmlElement(required = true)
                    protected String vDeducISSQN;

                    @XmlElement(name = "vBC", required = true)
                    protected String vbc;

                    @XmlElement(required = true)
                    protected String vAliq;

                    @XmlElement(name = "vISSQN", required = true)
                    protected String vissqn;

                    protected String cMunFG;

                    protected String cListServ;

                    protected String cServTribMun;

                    @XmlElement(required = true)
                    protected String cNatOp;

                    @XmlElement(required = true)
                    protected String indIncFisc;

                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {"pisAliq", "pisQtde", "pisnt", "pissn", "pisOutr"})
                @Getter
                @Setter
                public static class PIS {

                    @XmlElement(name = "PISAliq")
                    protected CFe.InfCFe.Det.Imposto.PIS.PISAliq pisAliq;

                    @XmlElement(name = "PISQtde")
                    protected CFe.InfCFe.Det.Imposto.PIS.PISQtde pisQtde;

                    @XmlElement(name = "PISNT")
                    protected CFe.InfCFe.Det.Imposto.PIS.PISNT pisnt;

                    @XmlElement(name = "PISSN")
                    protected CFe.InfCFe.Det.Imposto.PIS.PISSN pissn;

                    @XmlElement(name = "PISOutr")
                    protected CFe.InfCFe.Det.Imposto.PIS.PISOutr pisOutr;

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst", "vbc", "ppis", "vpis"})
                    @Getter
                    @Setter
                    public static class PISAliq {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                        @XmlElement(name = "vBC", required = true)
                        protected String vbc;

                        @XmlElement(name = "pPIS", required = true)
                        protected String ppis;

                        @XmlElement(name = "vPIS", required = true)
                        protected String vpis;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst"})
                    @Getter
                    @Setter
                    public static class PISNT {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst", "vbcOrPPIS", "qbcProdOrVAliqProd", "vpis"})
                    @Getter
                    @Setter
                    public static class PISOutr {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                        @XmlElementRefs({
                            @XmlElementRef(name = "pPIS", type = JAXBElement.class, required = false),
                            @XmlElementRef(name = "vBC", type = JAXBElement.class, required = false)
                        })
                        protected List<JAXBElement<String>> vbcOrPPIS;

                        @XmlElementRefs({
                            @XmlElementRef(name = "qBCProd", type = JAXBElement.class, required = false),
                            @XmlElementRef(name = "vAliqProd", type = JAXBElement.class, required = false)
                        })
                        protected List<JAXBElement<String>> qbcProdOrVAliqProd;

                        @XmlElement(name = "vPIS", required = true)
                        protected String vpis;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst", "qbcProd", "vAliqProd", "vpis"})
                    @Getter
                    @Setter
                    public static class PISQtde {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                        @XmlElement(name = "qBCProd", required = true)
                        protected String qbcProd;

                        @XmlElement(required = true)
                        protected String vAliqProd;

                        @XmlElement(name = "vPIS", required = true)
                        protected String vpis;

                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {"cst"})
                    @Getter
                    @Setter
                    public static class PISSN {

                        @XmlElement(name = "CST", required = true)
                        protected String cst;

                    }

                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {"vbcOrPPIS", "qbcProdOrVAliqProd", "vpis"})
                @Getter
                @Setter
                public static class PISST {

                    @XmlElementRefs({
                        @XmlElementRef(name = "vBC", type = JAXBElement.class, required = false),
                        @XmlElementRef(name = "pPIS", type = JAXBElement.class, required = false)
                    })
                    protected List<JAXBElement<String>> vbcOrPPIS;

                    @XmlElementRefs({
                        @XmlElementRef(name = "vAliqProd", type = JAXBElement.class, required = false),
                        @XmlElementRef(name = "qBCProd", type = JAXBElement.class, required = false)
                    })
                    protected List<JAXBElement<String>> qbcProdOrVAliqProd;

                    @XmlElement(name = "vPIS", required = true)
                    protected String vpis;

                }

            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"cProd", "cean", "xProd", "ncm", "cfop", "uCom", "qCom", "vUnCom", "vProd", "indRegra", "vDesc", "vOutro", "vItem", "vRatDesc", "vRatAcr", "obsFiscoDet"})
            @Getter
            @Setter
            public static class Prod {

                @XmlElement(required = true)
                protected String cProd;

                @XmlElement(name = "cEAN")
                protected String cean;

                @XmlElement(required = true)
                protected String xProd;

                @XmlElement(name = "NCM")
                protected String ncm;

                @XmlElement(name = "CFOP", required = true)
                protected String cfop;

                @XmlElement(required = true)
                protected String uCom;

                @XmlElement(required = true)
                protected String qCom;

                @XmlElement(required = true)
                protected String vUnCom;

                @XmlElement(required = true)
                protected String vProd;

                @XmlElement(required = true)
                protected String indRegra;

                protected String vDesc;

                protected String vOutro;

                @XmlElement(required = true)
                protected String vItem;

                protected String vRatDesc;

                protected String vRatAcr;

                protected List<CFe.InfCFe.Det.Prod.ObsFiscoDet> obsFiscoDet;

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {"xTextoDet"})
                @Getter
                @Setter
                public static class ObsFiscoDet {

                    @XmlElement(required = true)
                    protected String xTextoDet;

                    @XmlAttribute(name = "xCampoDet", required = true)
                    protected String xCampoDet;

                }

            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"cnpj", "xNome", "xFant", "enderEmit", "ie", "im", "cRegTrib", "cRegTribISSQN", "indRatISSQN"})
        @Getter
        @Setter
        public static class Emit {

            @XmlElement(name = "CNPJ", required = true)
            protected String cnpj;

            @XmlElement(required = true)
            protected String xNome;

            protected String xFant;

            @XmlElement(required = true)
            protected CFe.InfCFe.Emit.EnderEmit enderEmit;

            @XmlElement(name = "IE", required = true)
            protected String ie;

            @XmlElement(name = "IM")
            protected String im;

            @XmlElement(required = true)
            protected String cRegTrib;

            protected String cRegTribISSQN;

            @XmlElement(required = true)
            protected String indRatISSQN;

            public String getCNPJ() {
                return cnpj;
            }

            public void setCNPJ(String s) {
                this.cnpj = s;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"xLgr", "nro", "xCpl", "xBairro", "xMun", "cep"})
            @Getter
            @Setter
            public static class EnderEmit {

                @XmlElement(required = true)
                protected String xLgr;

                protected String nro;

                protected String xCpl;

                @XmlElement(required = true)
                protected String xBairro;

                @XmlElement(required = true)
                protected String xMun;

                @XmlElement(name = "CEP", required = true)
                protected String cep;

            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"xLgr", "nro", "xCpl", "xBairro", "xMun", "uf"})
        @Getter
        @Setter
        public static class Entrega {

            @XmlElement(required = true)
            protected String xLgr;

            @XmlElement(required = true)
            protected String nro;

            protected String xCpl;

            @XmlElement(required = true)
            protected String xBairro;

            @XmlElement(required = true)
            protected String xMun;

            @XmlElement(name = "UF", required = true)
            protected String uf;

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"cuf", "cnf", "mod", "nserieSAT", "ncFe", "dEmi", "hEmi", "cdv", "tpAmb", "cnpj", "signAC", "assinaturaQRCODE", "numeroCaixa"})
        @Getter
        @Setter
        public static class Ide {

            @XmlElement(name = "cUF", required = true)
            protected String cuf;

            @XmlElement(name = "cNF", required = true)
            protected String cnf;

            @XmlElement(required = true)
            protected String mod;

            @XmlElement(required = true)
            protected String nserieSAT;

            @XmlElement(name = "nCFe", required = true)
            protected String ncFe;

            @XmlElement(required = true)
            protected String dEmi;

            @XmlElement(required = true)
            protected String hEmi;

            @XmlElement(name = "cDV", required = true)
            protected String cdv;

            @XmlElement(required = true)
            protected String tpAmb;

            @XmlElement(name = "CNPJ", required = true)
            protected String cnpj;

            @XmlElement(required = true)
            protected String signAC;

            @XmlElement(required = true)
            protected String assinaturaQRCODE;

            @XmlElement(required = true)
            protected String numeroCaixa;

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"infCpl", "obsFisco"})
        @Getter
        @Setter
        public static class InfAdic {

            protected String infCpl;

            protected List<CFe.InfCFe.InfAdic.ObsFisco> obsFisco;

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"xTexto"})
            @Getter
            @Setter
            public static class ObsFisco {

                @XmlElement(required = true)
                protected String xTexto;

                @XmlAttribute(name = "xCampo", required = true)
                protected String xCampo;

            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"mp", "vTroco"})
        @Getter
        @Setter
        public static class Pgto {

            @XmlElement(name = "MP", required = true)
            protected List<CFe.InfCFe.Pgto.MP> mp;

            @XmlElement(required = true)
            protected String vTroco;

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"cmp", "vmp", "cAdmC"})
            @Getter
            @Setter
            public static class MP {

                @XmlElement(name = "cMP", required = true)
                protected String cmp;

                @XmlElement(name = "vMP", required = true)
                protected String vmp;

                protected String cAdmC;

            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"icmsTot", "vcFe", "issqNtot", "descAcrEntr", "vcFeLei12741"})
        @Getter
        @Setter
        public static class Total {

            @XmlElement(name = "ICMSTot")
            protected CFe.InfCFe.Total.ICMSTot icmsTot;

            @XmlElement(name = "vCFe", required = true)
            protected String vcFe;

            @XmlElement(name = "ISSQNtot")
            protected CFe.InfCFe.Total.ISSQNtot issqNtot;

            @XmlElement(name = "DescAcrEntr")
            protected CFe.InfCFe.Total.DescAcrEntr descAcrEntr;

            @XmlElement(name = "vCFeLei12741")
            protected String vcFeLei12741;

            public String getVCFe() {
                return vcFe;
            }

            public void setVCFe(String s) {
                this.vcFe = s;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"vDescSubtot", "vAcresSubtot"})
            @Getter
            @Setter
            public static class DescAcrEntr {

                protected String vDescSubtot;

                protected String vAcresSubtot;

            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"vicms", "vProd", "vDesc", "vpis", "vcofins", "vpisst", "vcofinsst", "vOutro"})
            @Getter
            @Setter
            public static class ICMSTot {

                @XmlElement(name = "vICMS", required = true)
                protected String vicms;

                @XmlElement(required = true)
                protected String vProd;

                @XmlElement(required = true)
                protected String vDesc;

                @XmlElement(name = "vPIS", required = true)
                protected String vpis;

                @XmlElement(name = "vCOFINS", required = true)
                protected String vcofins;

                @XmlElement(name = "vPISST", required = true)
                protected String vpisst;

                @XmlElement(name = "vCOFINSST", required = true)
                protected String vcofinsst;

                @XmlElement(required = true)
                protected String vOutro;

            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"vbc", "viss", "vpis", "vcofins", "vpisst", "vcofinsst"})
            @Getter
            @Setter
            public static class ISSQNtot {

                @XmlElement(name = "vBC", required = true)
                protected String vbc;

                @XmlElement(name = "vISS", required = true)
                protected String viss;

                @XmlElement(name = "vPIS", required = true)
                protected String vpis;

                @XmlElement(name = "vCOFINS", required = true)
                protected String vcofins;

                @XmlElement(name = "vPISST", required = true)
                protected String vpisst;

                @XmlElement(name = "vCOFINSST", required = true)
                protected String vcofinsst;

            }

        }

    }

}
