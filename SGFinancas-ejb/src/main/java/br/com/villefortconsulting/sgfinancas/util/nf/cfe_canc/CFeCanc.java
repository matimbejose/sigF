package br.com.villefortconsulting.sgfinancas.util.nf.cfe_canc;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"infCFe", "any"})
@XmlRootElement(name = "CFeCanc")
@Getter
@Setter
public class CFeCanc {

    @XmlElement(required = true)
    protected CFeCanc.InfCFe infCFe;

    @XmlAnyElement(lax = true)
    protected Object any;

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"dEmi", "hEmi", "ide", "emit", "dest", "total", "infAdic"})
    @Getter
    @Setter
    public static class InfCFe {

        @XmlElement(required = true)
        protected String dEmi;

        @XmlElement(required = true)
        protected String hEmi;

        @XmlElement(required = true)
        protected CFeCanc.InfCFe.Ide ide;

        @XmlElement(required = true)
        protected CFeCanc.InfCFe.Emit emit;

        @XmlElement(required = true)
        protected CFeCanc.InfCFe.Dest dest;

        @XmlElement(required = true)
        protected CFeCanc.InfCFe.Total total;

        protected CFeCanc.InfCFe.InfAdic infAdic;

        @XmlAttribute(name = "versao", required = true)
        protected String versao;

        @XmlAttribute(name = "Id", required = true)
        protected String id;

        @XmlAttribute(name = "chCanc", required = true)
        protected String chCanc;

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"cnpj", "cpf"})
        @Getter
        @Setter
        public static class Dest {

            @XmlElement(name = "CNPJ")
            protected String cnpj;

            @XmlElement(name = "CPF")
            protected String cpf;

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"cnpj", "xNome", "xFant", "enderEmit", "ie", "im"})
        @Getter
        @Setter
        public static class Emit {

            @XmlElement(name = "CNPJ", required = true)
            protected String cnpj;

            @XmlElement(required = true)
            protected String xNome;

            protected String xFant;

            @XmlElement(required = true)
            protected CFeCanc.InfCFe.Emit.EnderEmit enderEmit;

            @XmlElement(name = "IE", required = true)
            protected String ie;

            @XmlElement(name = "IM")
            protected String im;

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
        @XmlType(name = "", propOrder = {"cuf", "cnf", "mod", "nserieSAT", "ncFe", "dEmi", "hEmi", "cdv", "cnpj", "signAC", "assinaturaQRCODE", "numeroCaixa"})
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
        @XmlType(name = "", propOrder = {"obsFisco"})
        @Getter
        @Setter
        public static class InfAdic {

            protected List<CFeCanc.InfCFe.InfAdic.ObsFisco> obsFisco;

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
        @XmlType(name = "", propOrder = {"vcFe"})
        @Getter
        @Setter
        public static class Total {

            @XmlElement(name = "vCFe", required = true)
            protected String vcFe;

        }

    }

}
