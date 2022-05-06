package br.com.villefortconsulting.sgfinancas.util.nf;

import br.com.swconsultoria.nfe.schema.envConfRecebto.TUfEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.SignatureType;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnderEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEndereco;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TVeiculo;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TUf;
import br.com.swconsultoria.nfe.schema_4.retEnviNFe.TLocal;
import br.com.villefortconsulting.sgfinancas.entidades.Cfop;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Csosn;
import br.com.villefortconsulting.sgfinancas.entidades.Cst;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.entidades.OrigemMercadoria;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.EntityIdHolder;
import br.com.villefortconsulting.util.NumeroUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"squid:S00116"})
public class NFe {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfNFeSupl {

        @NotNull
        String qrCode;

        @NotNull
        String urlChave;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfNFe {

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Transp {

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Vol {

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Lacres {

                    @NotNull
                    String nLacre;

                }

                String qVol;

                String esp;

                String marca;

                String nVol;

                Double pesoL;

                Double pesoB;

                @Setter(AccessLevel.PRIVATE)
                List<Lacres> lacres;

                public List<Lacres> getLacres() {
                    if (lacres == null) {
                        lacres = new ArrayList<>();
                    }
                    return lacres;
                }

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Transporta {

                String CNPJ;

                String CPF;

                String xNome;

                String IE;

                String xEnder;

                String xMun;

                TUf UF;

                public String getCpfCnpj() {
                    return CNPJ != null ? CNPJ : CPF;
                }

                public void setCpfCnpj(String cpfCnpj) {
                    cpfCnpj = CpfCnpjUtil.removerMascaraCnpj(cpfCnpj);
                    CNPJ = null;
                    CPF = null;
                    if (cpfCnpj.length() == 14) {
                        CNPJ = cpfCnpj;
                    } else if (cpfCnpj.length() == 11) {
                        CPF = cpfCnpj;
                    }
                }

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class RetTransp {

                Double VServ;

                Double VBCRet;

                Double pICMSRet;

                Double VICMSRet;

                String CFOP;

                @NotNull
                Integer cMunFG;

            }

            @NotNull
            String modFrete;

            Transporta transporta;

            RetTransp retTransp;

            TVeiculo VeicTransp;

            @Setter(AccessLevel.PRIVATE)
            List<TVeiculo> reboque;

            String Vagao;

            String balsa;

            @Setter(AccessLevel.PRIVATE)
            List<Vol> Vol;

            public List<TVeiculo> getReboque() {
                if (reboque == null) {
                    reboque = new ArrayList<>();
                }
                return reboque;
            }

            public List<Vol> getVol() {
                if (Vol == null) {
                    Vol = new ArrayList<>();
                }
                return Vol;
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Total {

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class RetTrib {

                Double VRetPIS;

                Double VRetCOFINS;

                Double VRetCSLL;

                Double VBCIRRF;

                Double VIRRF;

                Double VBCRetPrev;

                Double VRetPrev;

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ISSQNtot {

                Double VServ;

                Double VBC;

                Double VISS;

                Double VPIS;

                Double VCOFINS;

                @NotNull
                String dCompet;

                Double VDeducao;

                Double VOutro;

                Double VDescIncond;

                Double VDescCond;

                Double VISSRet;

                String cRegTrib;

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ICMSTot {

                Double VBC = 0d;

                Double VICMS = 0d;

                Double VICMSDeson = 0d;

                Double VFCPUFDest = 0d;

                Double VICMSUFDest = 0d;

                Double VICMSUFRemet = 0d;

                Double VFCP = 0d;

                Double VBCST = 0d;

                Double VST = 0d;

                Double VFCPST = 0d;

                Double VFCPSTRet = 0d;

                Double VProd = 0d;

                Double VFrete = 0d;

                Double VSeg = 0d;

                Double VDesc = 0d;

                Double VII = 0d;

                Double VIPI = 0d;

                Double VIPIDevol = 0d;

                Double VPIS = 0d;

                Double VCOFINS = 0d;

                Double VOutro = 0d;

                Double VNF = 0d;

                public void reset() {
                    this.VBC = 0d;
                    this.VICMS = 0d;
                    this.VICMSDeson = 0d;
                    this.VFCPUFDest = 0d;
                    this.VICMSUFDest = 0d;
                    this.VICMSUFRemet = 0d;
                    this.VFCP = 0d;
                    this.VBCST = 0d;
                    this.VST = 0d;
                    this.VFCPST = 0d;
                    this.VFCPSTRet = 0d;
                    this.VProd = 0d;
                    this.VFrete = 0d;
                    this.VSeg = 0d;
                    this.VDesc = 0d;
                    this.VII = 0d;
                    this.VIPI = 0d;
                    this.VIPIDevol = 0d;
                    this.VPIS = 0d;
                    this.VCOFINS = 0d;
                    this.VOutro = 0d;
                    this.VNF = 0d;
                }

                public void addVBC(Double val) {
                    VBC = NumeroUtil.somar(VBC, val);
                }

                public void addVICMS(Double val) {
                    VICMS = NumeroUtil.somar(VICMS, val);
                }

                public void addVICMSDeson(Double val) {
                    VICMSDeson = NumeroUtil.somar(VICMSDeson, val);
                }

                public void addVFCPUFDest(Double val) {
                    VFCPUFDest = NumeroUtil.somar(VFCPUFDest, val);
                }

                public void addVICMSUFDest(Double val) {
                    VICMSUFDest = NumeroUtil.somar(VICMSUFDest, val);
                }

                public void addVICMSUFRemet(Double val) {
                    VICMSUFRemet = NumeroUtil.somar(VICMSUFRemet, val);
                }

                public void addVFCP(Double val) {
                    VFCP = NumeroUtil.somar(VFCP, val);
                }

                public void addVBCST(Double val) {
                    VBCST = NumeroUtil.somar(VBCST, val);
                }

                public void addVST(Double val) {
                    VST = NumeroUtil.somar(VST, val);
                }

                public void addVFCPST(Double val) {
                    VFCPST = NumeroUtil.somar(VFCPST, val);
                }

                public void addVFCPSTRet(Double val) {
                    VFCPSTRet = NumeroUtil.somar(VFCPSTRet, val);
                }

                public void addVProd(Double val) {
                    VProd = NumeroUtil.somar(VProd, val);
                }

                public void addVFrete(Double val) {
                    VFrete = NumeroUtil.somar(VFrete, val);
                }

                public void addVSeg(Double val) {
                    VSeg = NumeroUtil.somar(VSeg, val);
                }

                public void addVDesc(Double val) {
                    VDesc = NumeroUtil.somar(VDesc, val);
                }

                public void addVII(Double val) {
                    VII = NumeroUtil.somar(VII, val);
                }

                public void addVIPI(Double val) {
                    VIPI = NumeroUtil.somar(VIPI, val);
                }

                public void addVIPIDevol(Double val) {
                    VIPIDevol = NumeroUtil.somar(VIPIDevol, val);
                }

                public void addVPIS(Double val) {
                    VPIS = NumeroUtil.somar(VPIS, val);
                }

                public void addVCOFINS(Double val) {
                    VCOFINS = NumeroUtil.somar(VCOFINS, val);
                }

                public void addVOutro(Double val) {
                    VOutro = NumeroUtil.somar(VOutro, val);
                }

                public void addVNF(Double val) {
                    VNF = NumeroUtil.somar(VNF, val);
                }

                public Double getVTotTrib() {
                    return NumeroUtil.somar(this.VBC,
                            this.VICMS,
                            this.VICMSDeson,
                            this.VFCPUFDest,
                            this.VICMSUFDest,
                            this.VICMSUFRemet,
                            this.VFCP,
                            this.VBCST,
                            this.VST,
                            this.VFCPST,
                            this.VFCPSTRet,
                            this.VProd,
                            this.VFrete,
                            this.VSeg,
                            this.VDesc,
                            this.VII,
                            this.VIPI,
                            this.VIPIDevol,
                            this.VPIS,
                            this.VCOFINS,
                            this.VOutro,
                            this.VNF);
                }

            }

            ICMSTot ICMSTot;

            ISSQNtot ISSQNtot;

            RetTrib retTrib;

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Pag {

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class DetPag {

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Card {

                    @NotNull
                    String tpIntegra;

                    String CNPJ;

                    String tBand;

                    String cAut;

                }

                String indPag;

                @NotNull
                String tPag;

                Double VPag;

                Card card;

            }

            @NotNull
            @Setter(value = AccessLevel.PRIVATE)
            List<DetPag> detPag;

            Double VTroco;

            public List<DetPag> getDetPag() {
                if (detPag == null) {
                    detPag = new ArrayList<>();
                }
                return detPag;
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class InfAdic {

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ProcRef {

                @NotNull
                String nProc;

                @NotNull
                String indProc;

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ObsFisco {

                @NotNull
                String xTexto;

                String xCampo;

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ObsCont {

                @NotNull
                String xTexto;

                String xCampo;

            }

            String infAdFisco;

            String infCpl;

            @Setter(AccessLevel.PRIVATE)
            List<ObsCont> obsCont;

            @Setter(AccessLevel.PRIVATE)
            List<ObsFisco> obsFisco;

            @Setter(AccessLevel.PRIVATE)
            List<ProcRef> procRef;

            public List<ObsCont> getObsCont() {
                if (obsCont == null) {
                    obsCont = new ArrayList<>();
                }
                return obsCont;
            }

            public List<ObsFisco> getObsFisco() {
                if (obsFisco == null) {
                    obsFisco = new ArrayList<>();
                }
                return obsFisco;
            }

            public List<ProcRef> getProcRef() {
                if (procRef == null) {
                    procRef = new ArrayList<>();
                }
                return procRef;
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Ide {

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class NFref {

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class RefNFP {

                    String CUF;

                    String AAMM;

                    String CNPJ;

                    String CPF;

                    String IE;

                    @NotNull
                    String mod;

                    @NotNull
                    String serie;

                    String NNF;

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class RefNF {

                    String CUF;

                    String AAMM;

                    String CNPJ;

                    @NotNull
                    String mod;

                    @NotNull
                    String serie;

                    String NNF;

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class RefECF {

                    @NotNull
                    String mod;

                    String NECF;

                    String NCOO;

                }

                String refNFe;

                RefNF refNF;

                RefNFP refNFP;

                String refCTe;

                RefECF refECF;

            }

            String CUF;

            String CNF;

            @NotNull
            String natOp;

            @NotNull
            String mod;

            @NotNull
            String serie;

            String NNF;

            @NotNull
            String dhEmi;

            String dhSaiEnt;

            @NotNull
            String tpNF;

            @NotNull
            String idDest;

            @NotNull
            String cMunFG;

            @NotNull
            String tpImp;

            @NotNull
            String tpEmis;

            String CDV;

            @NotNull
            String tpAmb;

            @NotNull
            String finNFe;

            @NotNull
            String indFinal;

            @NotNull
            String indPres;

            @NotNull
            String procEmi;

            String VerProc;

            String dhCont;

            String xJust;

            @Setter(AccessLevel.PRIVATE)
            List<NFref> nFref;

            public List<NFref> getNFref() {
                if (nFref == null) {
                    nFref = new ArrayList<>();
                }
                return nFref;
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Exporta {

            TUfEmi UFSaidaPais;

            @NotNull
            String xLocExporta;

            String xLocDespacho;

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Emit {

            String CNPJ;

            String CPF;

            @NotNull
            String xNome;

            String xFant;

            @NotNull
            TEnderEmi enderEmit;

            String IE;

            String IEST;

            String IM;

            String CNAE;

            String CRT;

            public String getCpfCnpj() {
                return CNPJ != null ? CNPJ : CPF;
            }

            public void setCpfCnpj(String cpfCnpj) {
                cpfCnpj = CpfCnpjUtil.removerMascaraCnpj(cpfCnpj);
                CNPJ = null;
                CPF = null;
                if (cpfCnpj.length() == 14) {
                    CNPJ = cpfCnpj;
                } else if (cpfCnpj.length() == 11) {
                    CPF = cpfCnpj;
                }
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Det {

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Prod {

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class VeicProd {

                    @NotNull
                    String tpOp;

                    @NotNull
                    String chassi;

                    @NotNull
                    String cCor;

                    @NotNull
                    String xCor;

                    @NotNull
                    String pot;

                    @NotNull
                    String cilin;

                    @NotNull
                    Double pesoL;

                    @NotNull
                    Double pesoB;

                    @NotNull
                    String nSerie;

                    @NotNull
                    String tpComb;

                    @NotNull
                    String nMotor;

                    String CMT;

                    @NotNull
                    String dist;

                    @NotNull
                    Integer anoMod;

                    @NotNull
                    Integer anoFab;

                    @NotNull
                    String tpPint;

                    @NotNull
                    String tpVeic;

                    @NotNull
                    String espVeic;

                    String VIN;

                    @NotNull
                    String condVeic;

                    @NotNull
                    String cMod;

                    @NotNull
                    String cCorDENATRAN;

                    @NotNull
                    String lota;

                    @NotNull
                    String tpRest;

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Rastro {

                    @NotNull
                    String nLote;

                    @NotNull
                    String qLote;

                    @NotNull
                    Date dFab;

                    @NotNull
                    Date dVal;

                    String cAgreg;

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Med {

                    @NotNull
                    String cProdANVISA;

                    String VPMC;

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class DetExport {

                    @Data
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ExportInd {

                        String NRE;

                        @NotNull
                        String chNFe;

                        @NotNull
                        String qExport;

                    }

                    String nDraw;

                    ExportInd exportInd;

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class DI {

                    @Data
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Adi {

                        @NotNull
                        String nAdicao;

                        @NotNull
                        String nSeqAdic;

                        @NotNull
                        String cFabricante;

                        Double VDescDI;

                        String nDraw;

                    }

                    String NDI;

                    String DDI;

                    @NotNull
                    String xLocDesemb;

                    TUfEmi UFDesemb;

                    @NotNull
                    String dDesemb;

                    @NotNull
                    String tpViaTransp;

                    String VAFRMM;

                    @NotNull
                    String tpIntermedio;

                    String CNPJ;

                    TUfEmi UFTerceiro;

                    @NotNull
                    String cExportador;

                    @NotNull
                    @Setter(AccessLevel.PRIVATE)
                    List<Adi> adi;

                    public List<Adi> getAdi() {
                        if (adi == null) {
                            adi = new ArrayList<>();
                        }
                        return adi;
                    }

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Comb {

                    @Data
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Encerrante {

                        @NotNull
                        String nBico;

                        String nBomba;

                        @NotNull
                        String nTanque;

                        String VEncIni;

                        String VEncFin;

                    }

                    @Data
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class CIDE {

                        String QBCProd;

                        String VAliqProd;

                        String VCIDE;

                    }

                    @NotNull
                    String cProdANP;

                    @NotNull
                    String descANP;

                    String PGLP;

                    String PGNn;

                    String PGNi;

                    String VPart;

                    String CODIF;

                    String qTemp;

                    TUf UFCons;

                    CIDE CIDE;

                    Encerrante encerrante;

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Arma {

                    @NotNull
                    String tpArma;

                    @NotNull
                    String nSerie;

                    @NotNull
                    String nCano;

                    @NotNull
                    String descr;

                }

                @NotNull
                String cProd;

                String CEAN;

                @NotNull
                String xProd;

                @Setter(AccessLevel.PRIVATE)
                List<String> NVE;

                String indEscala;

                String CNPJFab;

                String cBenef;

                String EXTIPI;

                String CFOP;

                @NotNull
                String uCom;

                @NotNull
                Double qCom = 0d;

                Double VUnCom;

                String CEANTrib;

                @NotNull
                String uTrib;

                @NotNull
                String qTrib;

                Double VUnTrib = 0d;

                Double VFrete;

                Double VSeg;

                Double VDesc;

                Double VOutro;

                @NotNull
                String indTot;

                @Setter(AccessLevel.PRIVATE)
                List<DI> DI;

                @Setter(AccessLevel.PRIVATE)
                List<DetExport> detExport;

                String xPed;

                String nItemPed;

                String NFCI;

                @Setter(AccessLevel.PRIVATE)
                List<Rastro> rastro;

                VeicProd VeicProd;

                Med med;

                @Setter(AccessLevel.PRIVATE)
                List<Arma> arma;

                Comb comb;

                String NRECOPI;

                EntityIdHolder<Ncm> idNcm = new EntityIdHolder<>();

                EntityIdHolder<Cfop> idCfop = new EntityIdHolder<>();

                EntityIdHolder<Produto> idProduto = new EntityIdHolder<>();

                public Prod() {
                    NVE = new ArrayList<>();
                    DI = new ArrayList<>();
                    detExport = new ArrayList<>();
                    rastro = new ArrayList<>();
                    arma = new ArrayList<>();
                    idNcm.setValue(new Ncm());
                    idCfop.setValue(new Cfop());
                    idCfop.setSetter(objCfop -> {
                        if (objCfop != null) {
                            CFOP = objCfop.getCodigo();
                        }
                    });
                    idProduto.setValue(new Produto());
                    idProduto.setSetter(idProd -> {
                        if (idProd != null) {
                            idCfop.setValue(idProd.getIdCfop());
                            idNcm.setValue(idProd.getIdNcm());
                            VUnTrib = idProd.getValorVenda();
                        }
                    });
                }

                public Double getVProd() {
                    return qCom * VUnTrib;
                }

                public String getNCM() {
                    return idProduto.getValue().getIdNcm().getCodigo();
                }

                public String getCEST() {
                    return idProduto.getValue().getIdNcm().getCest();
                }

                public String getCFOP() {
                    return idCfop != null && idCfop.getValue() != null ? idCfop.getValue()
                            .getCodigo() : CFOP;
                }

                public Double getVDesc() {
                    return VDesc == null || VDesc == 0d ? null : VDesc;
                }

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ImpostoDevol {

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class IPI {

                    Double VIPIDevol;

                }

                @NotNull
                Double pDevol;

                IPI IPI;

            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public abstract static class TipoImposto {

                public abstract Double getTotal();

            }

            @Data
            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public abstract static class TipoICMSBase {

                @NotNull
                String orig;

            }

            @Data
            @EqualsAndHashCode(callSuper = false)
            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public abstract static class TipoICMS extends TipoICMSBase {

                String CST;

                public Double getVBC() {
                    return 0d;
                }

                public Double getVICMS() {
                    return 0d;
                }

                public Double getVFCP() {
                    return 0d;
                }

            }

            @Data
            @EqualsAndHashCode(callSuper = false)
            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public abstract static class TipoICMS2 extends TipoICMS {

                Double VBC;

                Double VICMS;

                Double VFCP;

                @Override
                public Double getVBC() {
                    return VBC;
                }

                @Override
                public Double getVICMS() {
                    return VICMS;
                }

                @Override
                public Double getVFCP() {
                    return VFCP;
                }

            }

            @Data
            @EqualsAndHashCode(callSuper = false)
            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public abstract static class TipoICMSSN extends TipoICMSBase {

                String CSOSN;

            }

            @Setter
            @EqualsAndHashCode(callSuper = false)
            public abstract static class TipoImpostoComDet extends TipoImposto {

                Det det;

                public Det obterDet() {
                    return det;
                }

            }

            public abstract static class ImpostoComCST extends TipoICMS {

                TipoImpostoComDet detParent;

                public ImpostoComCST() {
                }

                public ImpostoComCST(TipoImpostoComDet detParent) {
                    this.detParent = detParent;
                }

                @Override
                public String getCST() {
                    if (detParent != null && detParent.obterDet() != null && detParent.obterDet().getIdCst() != null && detParent.obterDet().getIdCst().getValue() != null) {
                        return detParent.obterDet().getIdCst().getValue().getCodigo();
                    }
                    return CST;
                }

                @Override
                public void setCST(String cst) {
                    this.CST = cst;
                }

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Imposto<T extends TipoImposto> {

                @Data
                @EqualsAndHashCode(callSuper = false)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class PISST extends TipoImposto {

                    Double VBC;

                    Double pPIS;

                    String QBCProd;

                    String VAliqProd;

                    Double VPIS;

                    @Override
                    public Double getTotal() {
                        return 0d;
                    }

                }

                @Getter
                @Setter
                @NoArgsConstructor
                @EqualsAndHashCode(callSuper = false)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class PIS extends TipoImpostoComDet {

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @NoArgsConstructor
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class PISQtde extends ImpostoComCST {

                        String QBCProd;

                        Double VAliqProd;

                        Double VPIS;

                        public PISQtde(PIS detParent) {
                            super(detParent);
                        }

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @NoArgsConstructor
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class PISOutr extends ImpostoComCST {

                        Double VBC;

                        Double pPIS;

                        String QBCProd;

                        Double VAliqProd;

                        Double VPIS;

                        public PISOutr(PIS detParent) {
                            super(detParent);
                        }

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @NoArgsConstructor
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class PISNT extends ImpostoComCST {

                        public PISNT(PIS detParent) {
                            super(detParent);
                        }

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @NoArgsConstructor
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class PISAliq extends ImpostoComCST {

                        Double VBC;

                        Double pPIS;

                        Double VPIS;

                        public PISAliq(PIS detParent) {
                            super(detParent);
                        }

                    }

                    PISAliq PISAliq;

                    PISQtde PISQtde;

                    PISNT PISNT;

                    PISOutr PISOutr;

                    String tipoPIS;

                    public PIS(Det det) {
                        this.det = det;
                    }

                    @Override
                    public Double getTotal() {
                        Double tot = 0d;
                        if (PISAliq != null) {
                            tot += PISAliq.getVPIS();
                        }
                        if (PISQtde != null) {
                            tot += PISQtde.getVPIS();
                        }
                        if (PISOutr != null) {
                            tot += PISOutr.getVPIS();
                        }
                        return tot;
                    }

                }

                @Data
                @EqualsAndHashCode(callSuper = false)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class ISSQN extends TipoImposto {

                    Double VBC;

                    Double VAliq;

                    Double VISSQN;

                    @NotNull
                    String cMunFG;

                    @NotNull
                    String cListServ;

                    Double VDeducao;

                    Double VOutro;

                    Double VDescIncond;

                    Double VDescCond;

                    Double VISSRet;

                    @NotNull
                    Double indISS;

                    String cServico;

                    String cMun;

                    String cPais;

                    String nProcesso;

                    @NotNull
                    String indIncentivo;

                    EntityIdHolder<Cidade> idCidade = new EntityIdHolder<>();

                    public ISSQN() {
                        idCidade = new EntityIdHolder<>();
                        idCidade.setSetter(cidade -> {
                            if (cidade != null) {
                                this.cMun = cidade.getCodigoIBGE();

                            }
                        });
                    }

                    @Override
                    public Double getTotal() {
                        return 0d;
                    }

                }

                @Data
                @EqualsAndHashCode(callSuper = false)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class II extends TipoImposto {

                    Double VBC;

                    Double VDespAdu;

                    Double VII;

                    Double VIOF;

                    @Override
                    public Double getTotal() {
                        return 0d;
                    }

                }

                @Data
                @EqualsAndHashCode(callSuper = false)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class ICMSUFDest extends TipoImposto {

                    Double VBCUFDest;

                    Double VBCFCPUFDest;

                    Double PFCPUFDest;

                    Double PICMSUFDest;

                    Double pICMSInter;

                    Double pICMSInterPart;

                    Double VFCPUFDest;

                    Double VICMSUFDest;

                    Double VICMSUFRemet;

                    @Override
                    public Double getTotal() {
                        return 0d;
                    }

                }

                @Data
                @EqualsAndHashCode(callSuper = false)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class ICMS extends TipoImpostoComDet {

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMSST extends TipoICMS {

                        Double VBCSTRet;

                        Double VICMSSTRet;

                        Double VBCSTDest;

                        Double VICMSSTDest;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMSSN900 extends TipoICMSSN {

                        String modBC;

                        Double VBC;

                        Double PRedBC;

                        Double PICMS;

                        Double VICMS;

                        String modBCST;

                        Double PMVAST;

                        Double PRedBCST;

                        Double VBCST;

                        Double PICMSST;

                        Double VICMSST;

                        Double VBCFCPST;

                        Double PFCPST;

                        Double VFCPST;

                        Double pCredSN;

                        Double VCredICMSSN;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMSSN500 extends TipoICMSSN {

                        Double VBCSTRet;

                        Double PST;

                        Double VICMSSTRet;

                        Double VBCFCPSTRet;

                        Double PFCPSTRet;

                        Double VFCPSTRet;

                        Double pRedBCEfet;

                        Double VBCEfet;

                        Double pICMSEfet;

                        Double VICMSEfet;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMSSN202 extends TipoICMSSN {

                        @NotNull
                        String modBCST;

                        Double PMVAST;

                        Double pRedBCST;

                        Double VBCST;

                        Double PICMSST;

                        Double VICMSST;

                        Double VBCFCPST;

                        Double PFCPST;

                        Double VFCPST;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMSSN201 extends TipoICMSSN {

                        @NotNull
                        String modBCST;

                        Double PMVAST;

                        Double pRedBCST;

                        Double VBCST;

                        Double PICMSST;

                        Double VICMSST;

                        Double VBCFCPST;

                        Double PFCPST;

                        Double VFCPST;

                        @NotNull
                        Double pCredSN;

                        Double VCredICMSSN;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMSSN102 extends TipoICMSSN {
                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMSSN101 extends TipoICMSSN {

                        @NotNull
                        Double pCredSN;

                        Double VCredICMSSN;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMSPart extends TipoICMS {

                        @NotNull
                        String modBC;

                        Double VBC;

                        Double pRedBC;

                        Double pICMS;

                        Double VICMS;

                        @NotNull
                        String modBCST;

                        Double PMVAST;

                        Double pRedBCST;

                        Double VBCST;

                        Double PICMSST;

                        Double VICMSST;

                        Double PBCOp;

                        TUf UFST;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS90 extends TipoICMS2 {

                        String modBC;

                        Double pRedBC;

                        Double pICMS;

                        Double VBCFCP;

                        Double PFCP;

                        String modBCST;

                        Double PMVAST;

                        Double pRedBCST;

                        Double VBCST;

                        Double PICMSST;

                        Double VICMSST;

                        Double VBCFCPST;

                        Double PFCPST;

                        Double VFCPST;

                        Double VICMSDeson;

                        String motDesICMS;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS70 extends TipoICMS2 {

                        @NotNull
                        String modBC;

                        @NotNull
                        Double pRedBC;

                        Double pICMS;

                        Double VBCFCP;

                        Double PFCP;

                        @NotNull
                        String modBCST;

                        Double PMVAST;

                        Double pRedBCST;

                        Double VBCST;

                        Double PICMSST;

                        Double VICMSST;

                        Double VBCFCPST;

                        Double PFCPST;

                        Double VFCPST;

                        Double VICMSDeson;

                        String motDesICMS;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS60 extends TipoICMS {

                        Double VBCSTRet;

                        Double PST;

                        Double VICMSSTRet;

                        Double VBCFCPSTRet;

                        Double PFCPSTRet;

                        Double VFCPSTRet;

                        Double pRedBCEfet;

                        Double VBCEfet;

                        Double pICMSEfet;

                        Double VICMSEfet;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS51 extends TipoICMS2 {

                        String modBC;

                        Double pRedBC;

                        Double pICMS;

                        Double VICMSOp;

                        Double pDif;

                        Double VICMSDif;

                        Double VBCFCP;

                        Double PFCP;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS40 extends TipoICMS {

                        Double VICMSDeson;

                        String motDesICMS;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS30 extends TipoICMS {

                        @NotNull
                        String modBCST;

                        Double PMVAST;

                        Double pRedBCST;

                        Double VBCST;

                        Double PICMSST;

                        Double VICMSST;

                        Double VBCFCPST;

                        Double PFCPST;

                        Double VFCPST;

                        Double VICMSDeson;

                        String motDesICMS;

                        @Override
                        public Double getVBC() {
                            return VBCST;
                        }

                        @Override
                        public Double getVICMS() {
                            return VICMSST;
                        }

                        @Override
                        public Double getVFCP() {
                            return VFCPST;
                        }

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS20 extends TipoICMS2 {

                        @NotNull
                        String modBC;

                        @NotNull
                        Double pRedBC;

                        Double pICMS;

                        Double VBCFCP;

                        Double PFCP;

                        Double VICMSDeson;

                        String motDesICMS;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS10 extends TipoICMS2 {

                        @NotNull
                        String modBC;

                        Double pICMS;

                        Double VBCFCP;

                        Double PFCP;

                        @NotNull
                        String modBCST;

                        Double PMVAST;

                        Double pRedBCST;

                        Double VBCST;

                        Double PICMSST;

                        Double VICMSST;

                        Double VBCFCPST;

                        Double PFCPST;

                        Double VFCPST;

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class ICMS00 extends TipoICMS2 {

                        @NotNull
                        String modBC;

                        Double pICMS;

                        Double PFCP;

                    }

                    ICMS00 ICMS00;

                    ICMS10 ICMS10;

                    ICMS20 ICMS20;

                    ICMS30 ICMS30;

                    ICMS40 ICMS40;

                    ICMS51 ICMS51;

                    ICMS60 ICMS60;

                    ICMS70 ICMS70;

                    ICMS90 ICMS90;

                    ICMSPart ICMSPart;

                    ICMSST ICMSST;

                    ICMSSN101 ICMSSN101;

                    ICMSSN102 ICMSSN102;

                    ICMSSN201 ICMSSN201;

                    ICMSSN202 ICMSSN202;

                    ICMSSN500 ICMSSN500;

                    ICMSSN900 ICMSSN900;

                    @Override
                    public Double getTotal() {
                        if (ICMS00 != null) {
                            return ICMS00.getVICMS();
                        }
                        if (ICMS10 != null) {
                            return ICMS10.getVICMS();
                        }
                        if (ICMS20 != null) {
                            return ICMS20.getVICMS();
                        }
                        if (ICMS30 != null) {
                            return ICMS30.getVICMSST();
                        }
                        if (ICMS40 != null) {
                            return ICMS40.getVICMSDeson();
                        }
                        if (ICMS51 != null) {
                            return ICMS51.getVICMS();
                        }
                        if (ICMS60 != null) {
                            return ICMS60.getVICMSEfet();
                        }
                        if (ICMS70 != null) {
                            return ICMS70.getVICMS();
                        }
                        if (ICMS90 != null) {
                            return ICMS90.getVICMS();
                        }
                        if (ICMSPart != null) {
                            return ICMSPart.getVICMS();
                        }
                        return 0d;
                    }

                    public TipoICMSBase getImpostoPreenchido() {
                        if (ICMS00 != null) {
                            return ICMS00;
                        }
                        if (ICMS10 != null) {
                            return ICMS10;
                        }
                        if (ICMS20 != null) {
                            return ICMS20;
                        }
                        if (ICMS30 != null) {
                            return ICMS30;
                        }
                        if (ICMS40 != null) {
                            return ICMS40;
                        }
                        if (ICMS51 != null) {
                            return ICMS51;
                        }
                        if (ICMS60 != null) {
                            return ICMS60;
                        }
                        if (ICMS70 != null) {
                            return ICMS70;
                        }
                        if (ICMS90 != null) {
                            return ICMS90;
                        }
                        return getIcmsStPreenchido();
                    }

                    public TipoICMSSN getIcmsStPreenchido() {
                        if (ICMSSN101 != null) {
                            return ICMSSN101;
                        }
                        if (ICMSSN102 != null) {
                            return ICMSSN102;
                        }
                        if (ICMSSN201 != null) {
                            return ICMSSN201;
                        }
                        if (ICMSSN202 != null) {
                            return ICMSSN202;
                        }
                        if (ICMSSN500 != null) {
                            return ICMSSN500;
                        }
                        if (ICMSSN900 != null) {
                            return ICMSSN900;
                        }
                        return null;
                    }

                    public TipoICMS getIcmsByCst(String cod) {
                        switch (cod) {
                            case "00":
                                if (ICMS00 == null) {
                                    ICMS00 = new ICMS00();
                                }
                                return ICMS00;
                            case "10":
                                if (ICMS10 == null) {
                                    ICMS10 = new ICMS10();
                                }
                                return ICMS10;
                            case "20":
                                if (ICMS20 == null) {
                                    ICMS20 = new ICMS20();
                                }
                                return ICMS20;
                            case "30":
                                if (ICMS30 == null) {
                                    ICMS30 = new ICMS30();
                                }
                                return ICMS30;
                            case "40":
                                if (ICMS40 == null) {
                                    ICMS40 = new ICMS40();
                                }
                                return ICMS40;
                            case "51":
                                if (ICMS51 == null) {
                                    ICMS51 = new ICMS51();
                                }
                                return ICMS51;
                            case "60":
                                if (ICMS60 == null) {
                                    ICMS60 = new ICMS60();
                                }
                                return ICMS60;
                            case "70":
                                if (ICMS70 == null) {
                                    ICMS70 = new ICMS70();
                                }
                                return ICMS70;
                            case "90":
                                if (ICMS90 == null) {
                                    ICMS90 = new ICMS90();
                                }
                                return ICMS90;
                            default:
                                return null;
                        }
                    }

                    public TipoICMSSN getIcmsByCsosn(String cod) {
                        switch (cod) {
                            case "101":
                                if (ICMSSN101 == null) {
                                    ICMSSN101 = new ICMSSN101();
                                }
                                return ICMSSN101;
                            case "102":
                                if (ICMSSN102 == null) {
                                    ICMSSN102 = new ICMSSN102();
                                }
                                return ICMSSN102;
                            case "201":
                                if (ICMSSN201 == null) {
                                    ICMSSN201 = new ICMSSN201();
                                }
                                return ICMSSN201;
                            case "202":
                                if (ICMSSN202 == null) {
                                    ICMSSN202 = new ICMSSN202();
                                }
                                return ICMSSN202;
                            case "500":
                                if (ICMSSN500 == null) {
                                    ICMSSN500 = new ICMSSN500();
                                }
                                return ICMSSN500;
                            case "900":
                                if (ICMSSN900 == null) {
                                    ICMSSN900 = new ICMSSN900();
                                }
                                return ICMSSN900;
                            default:
                                return null;
                        }
                    }

                }

                @Data
                @EqualsAndHashCode(callSuper = false)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class COFINSST extends TipoImposto {

                    Double VBC;

                    Double PCOFINS;

                    Double QBCProd;

                    Double VAliqProd;

                    Double VCOFINS;

                    @Override
                    public Double getTotal() {
                        return 0d;
                    }

                }

                @Getter
                @Setter
                @NoArgsConstructor
                @EqualsAndHashCode(callSuper = false)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class COFINS extends TipoImpostoComDet {

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @NoArgsConstructor
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class COFINSQtde extends ImpostoComCST {

                        Double QBCProd;

                        Double VAliqProd;

                        Double VCOFINS;

                        public COFINSQtde(COFINS detParent) {
                            super(detParent);
                        }

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @NoArgsConstructor
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class COFINSOutr extends ImpostoComCST {

                        Double VBC;

                        Double PCOFINS;

                        Double QBCProd;

                        Double VAliqProd;

                        Double VCOFINS;

                        public COFINSOutr(COFINS detParent) {
                            super(detParent);
                        }

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @NoArgsConstructor
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class COFINSNT extends ImpostoComCST {

                        public COFINSNT(COFINS detParent) {
                            super(detParent);
                        }

                    }

                    @Data
                    @EqualsAndHashCode(callSuper = false)
                    @NoArgsConstructor
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class COFINSAliq extends ImpostoComCST {

                        Double VBC;

                        Double PCOFINS;

                        Double VCOFINS;

                        public COFINSAliq(COFINS detParent) {
                            super(detParent);
                        }

                    }

                    COFINSAliq COFINSAliq;

                    COFINSQtde COFINSQtde;

                    COFINSNT COFINSNT;

                    COFINSOutr COFINSOutr;

                    String tipoCOFINS;

                    public COFINS(Det det) {
                        this.det = det;
                    }

                    @Override
                    public Double getTotal() {
                        Double tot = 0d;
                        if (COFINSAliq != null) {
                            tot += COFINSAliq.getVCOFINS();
                        }
                        if (COFINSQtde != null) {
                            tot += COFINSQtde.getVCOFINS();
                        }
                        if (COFINSOutr != null) {
                            tot += COFINSOutr.getVCOFINS();
                        }
                        return tot;
                    }

                }

                @JsonIgnore
                Map<String, T> content = new HashMap<>();

                public PISST getPISST() {
                    return (PISST) content.get("PISST");
                }

                public void setPISST(PISST val) {
                    if (val == null) {
                        content.remove("PISST");
                    } else {
                        content.put("PISST", (T) val);
                    }
                }

                public PIS getPIS() {
                    return (PIS) content.get("PIS");
                }

                public void setPIS(PIS val) {
                    if (val == null) {
                        content.remove("PIS");
                    } else {
                        content.put("PIS", (T) val);
                    }
                }

                public ISSQN getISSQN() {
                    return (ISSQN) content.get("ISSQN");
                }

                public void setISSQN(ISSQN val) {
                    if (val == null) {
                        content.remove("ISSQN");
                    } else {
                        content.put("ISSQN", (T) val);
                    }
                }

                public II getII() {
                    return (II) content.get("II");
                }

                public void setII(II val) {
                    if (val == null) {
                        content.remove("II");
                    } else {
                        content.put("II", (T) val);
                    }
                }

                public ICMS getICMS() {
                    return (ICMS) content.get("ICMS");
                }

                public void setICMS(ICMS val) {
                    if (val == null) {
                        content.remove("ICMS");
                    } else {
                        content.put("ICMS", (T) val);
                    }
                }

                public COFINSST getCOFINSST() {
                    return (COFINSST) content.get("COFINSST");
                }

                public void setCOFINSST(COFINSST val) {
                    if (val == null) {
                        content.remove("COFINSST");
                    } else {
                        content.put("COFINSST", (T) val);
                    }
                }

                public COFINS getCOFINS() {
                    return (COFINS) content.get("COFINS");
                }

                public void setCOFINS(COFINS val) {
                    if (val == null) {
                        content.remove("COFINS");
                    } else {
                        content.put("COFINS", (T) val);
                    }
                }

                public Double getVTotTrib() {
                    return content.values().stream()
                            .filter(Objects::nonNull)
                            .map(TipoImposto::getTotal)
                            .filter(Objects::nonNull)
                            .reduce(0d, (a, v) -> a + v);
                }

            }

            @NotNull
            Prod prod;

            @NotNull
            Imposto imposto;

            ImpostoDevol impostoDevol;

            String infAdProd;

            Integer nItem;

            EntityIdHolder<Cst> idCst = new EntityIdHolder<>();

            EntityIdHolder<Csosn> idCsosn = new EntityIdHolder<>();

            public Det() {
                imposto = new Imposto();
                idCst.setValue(new Cst());
                idCsosn.setValue(new Csosn());
                idCsosn.setSetter(csosn -> {
                    Imposto.ICMS icms = this.imposto.getICMS();
                    if (icms == null) {
                        this.imposto.setICMS(new Imposto.ICMS());
                        icms = this.imposto.getICMS();
                    }
                    if (csosn != null) {
                        TipoICMSSN icmsSn = icms.getIcmsByCsosn(csosn.getCodigo());
                        if (icmsSn != null) {
                            icmsSn.setCSOSN(csosn.getCodigo());
                            OrigemMercadoria om = prod.getIdProduto().getValue().getIdOrigemMercadoria();
                            if (om != null) {
                                icmsSn.setOrig(om.getCodOrigemMercadoria() + "");
                            }
                        }
                    }
                });
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Dest {

            String CNPJ;

            String CPF;

            String idEstrangeiro;

            String xNome;

            TEndereco enderDest;

            @NotNull
            String indIEDest;

            String IE;

            String ISUF;

            String IM;

            String email;

            public String getCpfCnpj() {
                return CNPJ != null ? CNPJ : CPF;
            }

            public void setCpfCnpj(String cpfCnpj) {
                cpfCnpj = CpfCnpjUtil.removerMascaraCnpj(cpfCnpj);
                CNPJ = null;
                CPF = null;
                if (cpfCnpj.length() == 14) {
                    CNPJ = cpfCnpj;
                } else if (cpfCnpj.length() == 11) {
                    CPF = cpfCnpj;
                }
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Compra {

            String XNEmp;

            String xPed;

            String xCont;

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Cobr {

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Fat {

                Integer nFat;

                Double VOrig;

                Double VDesc;

                Double VLiq;

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Dup {

                Integer nDup;

                Date dVenc;

                Double VDup;

            }

            Fat fat;

            @Setter(AccessLevel.PRIVATE)
            List<Dup> dup;

            public List<Dup> getDup() {
                if (dup == null) {
                    dup = new ArrayList<>();
                }
                return dup;
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Cana {

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ForDia {

                @NotNull
                String qtde;

                String dia;

            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Deduc {

                @NotNull
                String xDed;

                String VDed;

            }

            @NotNull
            String safra;

            @NotNull
            String ref;

            @NotNull
            @Setter(AccessLevel.PRIVATE)
            List<ForDia> forDia;

            @NotNull
            String qTotMes;

            @NotNull
            String qTotAnt;

            @NotNull
            String qTotGer;

            @Setter(AccessLevel.PRIVATE)
            List<Deduc> deduc;

            @NotNull
            String VFor;

            @NotNull
            String VTotDed;

            @NotNull
            String VLiqFor;

            public List<ForDia> getForDia() {
                if (forDia == null) {
                    forDia = new ArrayList<>();
                }
                return forDia;
            }

            public List<Deduc> getDeduc() {
                if (deduc == null) {
                    deduc = new ArrayList<>();
                }
                return deduc;
            }

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Avulsa {

            String CNPJ;

            @NotNull
            String xOrgao;

            @NotNull
            String matr;

            @NotNull
            String xAgente;

            String fone;

            TUfEmi UF;

            String NDAR;

            String dEmi;

            String VDAR;

            @NotNull
            String repEmi;

            String dPag;

        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AutXML {

            String CNPJ;

            String CPF;

        }

        @NotNull
        Ide ide;

        @NotNull
        Emit emit;

        Avulsa avulsa;

        Dest dest;

        TLocal retirada;

        TLocal entrega;

        @Setter(AccessLevel.PRIVATE)
        List<AutXML> autXML;

        @Setter(AccessLevel.PRIVATE)
        @NotNull
        List<Det> det;

        @NotNull
        Total total;

        @NotNull
        Transp transp;

        Cobr cobr;

        @NotNull
        Pag pag;

        InfAdic infAdic;

        Exporta exporta;

        Compra compra;

        Cana cana;

        String versao;

        String id;

        public List<AutXML> getAutXML() {
            if (autXML == null) {
                autXML = new ArrayList<>();
            }
            return autXML;
        }

        public List<Det> getDet() {
            if (det == null) {
                det = new ArrayList<>();
            }
            return det;
        }

    }

    @NotNull
    InfNFe infNFe;

    InfNFeSupl infNFeSupl;

    SignatureType signature;

}
