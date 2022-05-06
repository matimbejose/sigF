package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.util.FileUtil;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebPkiControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    private String certificateField;

    private String signatureField;

    private static final String XML = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe Id=\"NFe35141214314050000662550010001084271182362300\" versao=\"2.00\"><ide><cUF>35</cUF><cNF>18236230</cNF><natOp>VENDA MERC.SUJ.ST A CTB.SUBST</natOp><indPag>2</indPag><mod>55</mod><serie>1</serie><nNF>108427</nNF><dEmi>2014-12-12</dEmi><dSaiEnt>2014-12-12</dSaiEnt><tpNF>1</tpNF><cMunFG>3509205</cMunFG><tpImp>1</tpImp><tpEmis>1</tpEmis><cDV>0</cDV><tpAmb>1</tpAmb><finNFe>1</finNFe><procEmi>0</procEmi><verProc>GNFe257.500 PointSys</verProc></ide><emit><CNPJ>20658903000171</CNPJ><xNome>LACUNA SOFTWARE LTDA EPP</xNome><enderEmit><xLgr>CLN 110 BLOCO A</xLgr><nro>102</nro><xBairro>ASA NORTE</xBairro><cMun>5300108</cMun><xMun>BRASILIA</xMun><UF>DF</UF><CEP>70753510</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>6130333230</fone></enderEmit><IE>0144408646118</IE><CRT>3</CRT></emit><dest><CNPJ>00000000000191</CNPJ><xNome>BANCO DO BRASIL SA</xNome><enderDest><xLgr>ST SAUN SETOR DE AUTARQUIAS NORTE</xLgr><nro>SN</nro><xCpl>QUADRA05 BLOCO B - TORRE I</xCpl><xBairro>ASA NORTE</xBairro><cMun>5300108</cMun><xMun>BRASILIA</xMun><UF>DF</UF><CEP>07760000</CEP><cPais>1058</cPais><xPais>Brasil</xPais></enderDest><IE /></dest><det nItem=\"1\"><prod><cProd>415185</cProd><cEAN>0734646489805</cEAN><xProd>PARAFUSO PHILIPS</xProd><NCM>84433115</NCM><CFOP>6108</CFOP><uCom>PC</uCom><qCom>1.0000</qCom><vUnCom>2.7000000000</vUnCom><vProd>2.70</vProd><cEANTrib>0734646489805</cEANTrib><uTrib>PC</uTrib><qTrib>1.0000</qTrib><vUnTrib>2.7000000000</vUnTrib><vFrete>0.01</vFrete><indTot>1</indTot></prod><imposto><vTotTrib>0.98</vTotTrib><ICMS><ICMS60><orig>0</orig><CST>60</CST><vBCSTRet>0.00</vBCSTRet><vICMSSTRet>0.00</vICMSSTRet></ICMS60></ICMS><PIS><PISAliq><CST>01</CST><vBC>2.77</vBC><pPIS>1.65</pPIS><vPIS>0.01</vPIS></PISAliq></PIS><COFINS><COFINSAliq><CST>01</CST><vBC>2.77</vBC><pCOFINS>7.60</pCOFINS><vCOFINS>0.21</vCOFINS></COFINSAliq></COFINS></imposto><infAdProd>Vl. Aprox. Tributo 0,98(35,17%)</infAdProd></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vBCST>0.00</vBCST><vST>0.00</vST><vProd>2.70</vProd><vFrete>0.01</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vPIS>0.01</vPIS><vCOFINS>0.21</vCOFINS><vOutro>0.00</vOutro><vNF>2.77</vNF><vTotTrib>0.98</vTotTrib></ICMSTot></total><transp><modFrete>0</modFrete><transporta><CNPJ>01743404000138</CNPJ><xNome>FAVORITA TRANSPORTES LTDA</xNome><xEnder>R INHAUMA</xEnder><xMun>SAO PAULO</xMun><UF>SP</UF></transporta><vol><qVol>1</qVol><esp>DIG</esp><pesoL>0.327</pesoL><pesoB>0.327</pesoB></vol></transp><infAdic><infCpl>MERCADORIA DE ST CONFORME ART.313 DO RICMS DECRETO 45490/00 Total Aproximado de Tributos R 0.98 ( 35.17 % )</infCpl></infAdic></infNFe></NFe>";

    private byte[] xmlAssinado;

    public String doStart() {
        xmlAssinado = null;
        return "/restrito/webPki/webPki.xhtml?faces-redirect=true";
    }

    public void assinarXml() {
        try {
            String xmlText = "";
            xmlAssinado = FileUtil.createTxtByte("xmlAssinado", ".xml", xmlText);
        } catch (Exception ex) {
            Logger.getLogger(WebPkiControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StreamedContent downloadXmlAssinado() {
        try {
            return FileUtil.downloadFile(xmlAssinado, "application/xml", "xmlAssinado.xml");
        } catch (Exception ex) {
            return null;
        }
    }

    public String getHash() {
        try {
            FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", XML));
            return "";
        } catch (Exception ex) {
            Logger.getLogger(WebPkiControle.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
