package br.com.villefortconsulting.sgfinancas.controle.apoio;

import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoNota;
import br.com.villefortconsulting.util.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlElementSignatureController {

    public static String normalizarXML(String xml) throws FileException, IOException, SAXException, ParserConfigurationException, TransformerException {
        Document doc = openXml(FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", XmlUtil.removeAcentos(xml))));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String gerarHash(byte[] arquivo, String tipoNota) throws IOException, SAXException, ParserConfigurationException, NfeException, NoSuchAlgorithmException {
        // Open the XML to be signed
        Document doc = openXml(arquivo);

        // Sign the "infNFe" element using a dummy key in order to gerarHash the "to sign data", which is the byte array
        // that needs to be used as input to the signature algorithm to be performed with Web PKI
        XMLSignature sig = null;
        if (EnumTipoNota.PRODUTO.getTipo().equals(tipoNota)) {
            sig = signNotaFiscalProduto(doc);
        } else if (EnumTipoNota.SERVICO.getTipo().equals(tipoNota)) {
            sig = signNotaFiscalServico(doc, "InfRps");
        } else if (EnumTipoNota.SERVICO_LOTE.getTipo().equals(tipoNota)) {
            sig = signNotaFiscalServico(doc, "LoteRps");
        } else if (EnumTipoNota.CANCELAMENTO_PRODUTO.getTipo().equals(tipoNota)) {
            sig = signCancelamentoNotaFiscalProduto(doc);
        } else if (EnumTipoNota.CANCELAMENTO_SERVICO.getTipo().equals(tipoNota)) {
            sig = signCancelamentoNotaFiscalServico(doc);
        } else if (EnumTipoNota.EVENTO_PRODUTO.getTipo().equals(tipoNota)) {
            sig = signEvento(doc);
        } else {
            throw new NfeException("Tipo de nota inválido", null);
        }

        // Extract the "to sign data"
        byte[] toSignData = IOUtils.toByteArray(sig.getSignedInfo().getCanonicalizedData());

        // Compute the digest of the "to sign data" (called the "to sign hash")
        byte[] toSignHash = MessageDigest.getInstance("SHA-1").digest(toSignData);

        return Base64.getEncoder().encodeToString(toSignHash);
    }

    public static String obterXmlCancelamentoNotaFiscalServicoA3(String certificateBase64, String signatureBase64, byte[] arquivo) throws IOException, SAXException, ParserConfigurationException, NfeException, TransformerException {
        // Open the XML to be signed
        Document doc = openXml(arquivo);

        // Sign the "infNFe" element using a dummy key
        signCancelamentoNotaFiscalServico(doc);

        // Set actual signature value computed with Web PKI on the page
        Element signatureValue = (Element) doc.getElementsByTagNameNS(XMLSignature.XMLNS, "SignatureValue").item(0);
        signatureValue.setTextContent(signatureBase64);

        // Add the X509Certificate containing the encoded certificate acquired with Web PKI on the page
        Element signatureElement = (Element) doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
        Element keyInfo = createKeyInfo(doc, certificateBase64);
        signatureElement.appendChild(keyInfo);

        // Encode the signed XML
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String obterXmlCancelamentoNotaFiscalProdutoA3(String certificateBase64, String signatureBase64, byte[] arquivo) throws IOException, SAXException, ParserConfigurationException, TransformerException, NfeException {
        // Open the XML to be signed
        Document doc = openXml(arquivo);

        // Sign the "infNFe" element using a dummy key
        signCancelamentoNotaFiscalProduto(doc);

        // Set actual signature value computed with Web PKI on the page
        Element signatureValue = (Element) doc.getElementsByTagNameNS(XMLSignature.XMLNS, "SignatureValue").item(0);
        signatureValue.setTextContent(signatureBase64);

        // Add the X509Certificate containing the encoded certificate acquired with Web PKI on the page
        Element signatureElement = (Element) doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
        Element keyInfo = createKeyInfo(doc, certificateBase64);
        signatureElement.appendChild(keyInfo);

        // Encode the signed XML
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String assinarXmlNotaFiscalProdutoA3(String certificateBase64, String signatureBase64, byte[] arquivo) throws IOException, SAXException, ParserConfigurationException, TransformerException, NfeException {
        // Open the XML to be signed
        Document doc = openXml(arquivo);

        // Sign the "infNFe" element using a dummy key
        signNotaFiscalProduto(doc);

        // Set actual signature value computed with Web PKI on the page
        Element signatureValue = (Element) doc.getElementsByTagNameNS(XMLSignature.XMLNS, "SignatureValue").item(0);
        signatureValue.setTextContent(signatureBase64);

        // Add the X509Certificate containing the encoded certificate acquired with Web PKI on the page
        Element signatureElement = (Element) doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
        Element keyInfo = createKeyInfo(doc, certificateBase64);
        signatureElement.appendChild(keyInfo);

        // Encode the signed XML
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String assinarXmlCartaCorrecaoA3(String certificateBase64, String signatureBase64, byte[] arquivo) throws IOException, SAXException, ParserConfigurationException, TransformerException, NfeException {
        // Open the XML to be signed
        Document doc = openXml(arquivo);

        // Sign the "infNFe" element using a dummy key
        signEvento(doc);

        // Set actual signature value computed with Web PKI on the page
        Element signatureValue = (Element) doc.getElementsByTagNameNS(XMLSignature.XMLNS, "SignatureValue").item(0);
        signatureValue.setTextContent(signatureBase64);

        // Add the X509Certificate containing the encoded certificate acquired with Web PKI on the page
        Element signatureElement = (Element) doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
        Element keyInfo = createKeyInfo(doc, certificateBase64);
        signatureElement.appendChild(keyInfo);

        // Encode the signed XML
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String assinarXmlNotaFiscalServicoA3Fase1(String certificateBase64, String signatureBase64, byte[] arquivo) throws IOException, SAXException, ParserConfigurationException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeySpecException, XMLSignatureException, MarshalException, TransformerException {
        Document doc = openXml(arquivo);

        // Once again, sign the inner element using a dummy key
        signWithDummyKey(doc, "InfRps");

        // Replace Signature elements with certificate and signature value, both with Base64-encoding
        replaceSignatureElements(doc, certificateBase64, signatureBase64);

        // Save the signed XML
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));

        // Save the XML with both elements signed
        buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String assinarXmlNotaFiscalServicoA3Fase2(String certificateBase64, String signatureBase64, byte[] arquivo) throws IOException, SAXException, ParserConfigurationException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeySpecException, XMLSignatureException, MarshalException, TransformerException {
        Document doc = openXml(arquivo);

        // Once again, sign the inner element using a dummy key
        signWithDummyKey(doc, "LoteRps");

        // Replace Signature elements with certificate and signature value, both with Base64-encoding
        replaceSignatureElements(doc, certificateBase64, signatureBase64);

        // Save the signed XML
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));

        // Save the XML with both elements signed
        buffer = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(buffer));
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    private static XMLSignature signWithDummyKey(Document doc, String localName) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeySpecException, XMLSignatureException, javax.xml.crypto.MarshalException {
        XMLSignatureFactory sigFac = XMLSignatureFactory.getInstance("DOM");

        // Get the infNFe element and its ID
        Element toSignElement = (Element) doc.getElementsByTagNameNS("http://www.abrasf.org.br/nfse.xsd", localName).item(0);
        String toSignElementId = toSignElement.getAttribute("Id");

        // Reference the infNFe element by its ID with:
        // - Transformations: "Enveloped" and canonicalization (Canonical XML 1.0)
        // - Digest algorithm: SHA-1
        List<Transform> refTransforms = new ArrayList<>();
        refTransforms.add(sigFac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
        refTransforms.add(sigFac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null));
        Reference ref = sigFac.newReference(
                "#" + toSignElementId,
                sigFac.newDigestMethod(DigestMethod.SHA1, null),
                refTransforms,
                null,
                null
        );

        // Specify a SignedInfo with:
        // - Canonicalization: Canonical XML 1.0
        // - Signature algorithm: RSA with SHA-1
        // - References: infNFe element
        SignedInfo si = sigFac.newSignedInfo(
                sigFac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                sigFac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                Collections.singletonList(ref)
        );

        // Sign with dummy key
        DOMSignContext dsc = new DOMSignContext(getDummyPrivateKey(), toSignElement.getParentNode() /* signature will be added as sibling to the element being signed */);
        dsc.setIdAttributeNS(toSignElement, null, "Id");
        XMLSignature signature = sigFac.newXMLSignature(si, null);
        signature.sign(dsc);

        // Return XMLSignature
        return signature;
    }

    private static Document openXml(byte[] arquivo) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(arquivo));
    }

    private static XMLSignature signNotaFiscalProduto(Document doc) throws NfeException {

        try {

            XMLSignatureFactory sigFac = XMLSignatureFactory.getInstance("DOM");

            // Get the infNFe element and its ID
            Element infNFeElement = (Element) doc.getElementsByTagNameNS("http://www.portalfiscal.inf.br/nfe", "infNFe").item(0);

            // Assinar elemento
            SignedInfo si = assinarElemento(infNFeElement, sigFac);

            // Sign with dummy key
            DOMSignContext dsc = new DOMSignContext(getDummyPrivateKey(), doc.getDocumentElement().getElementsByTagName("NFe").item(0));
            dsc.setIdAttributeNS(infNFeElement, null, "Id");
            XMLSignature signature = sigFac.newXMLSignature(si, null);
            signature.sign(dsc);

            return signature;
        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    private static XMLSignature signEvento(Document doc) throws NfeException {
        try {
            XMLSignatureFactory sigFac = XMLSignatureFactory.getInstance("DOM");

            // Get the infNFe element and its ID
            Element infNFeElement = (Element) doc.getElementsByTagNameNS("http://www.portalfiscal.inf.br/nfe", "infEvento").item(0);

            // Assinar elemento
            SignedInfo si = assinarElemento(infNFeElement, sigFac);

            // Sign with dummy key
            DOMSignContext dsc = new DOMSignContext(getDummyPrivateKey(), doc.getElementsByTagName("evento").item(0));
            dsc.setIdAttributeNS(infNFeElement, null, "Id");
            XMLSignature signature = sigFac.newXMLSignature(si, null);
            signature.sign(dsc);

            return signature;
        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    private static XMLSignature signCancelamentoNotaFiscalProduto(Document doc) throws NfeException {

        try {
            XMLSignatureFactory sigFac = XMLSignatureFactory.getInstance("DOM");

            // Get the infNFe element and its ID
            Element infNFeElement = (Element) doc.getElementsByTagName("infEvento").item(0);

            SignedInfo si = assinarElemento(infNFeElement, sigFac);

            // Sign with dummy key
            DOMSignContext dsc = null;

            dsc = new DOMSignContext(getDummyPrivateKey(), doc.getDocumentElement().getElementsByTagName("evento").item(0));

            dsc.setIdAttributeNS(infNFeElement, null, "Id");
            XMLSignature signature = sigFac.newXMLSignature(si, null);
            signature.sign(dsc);
            // Return XMLSignature
            return signature;
        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    private static XMLSignature signNotaFiscalServico(Document doc, String tag) throws NfeException {
        try {
            // Sign with dummy key
            DOMSignContext dsc = null;
            if (null == tag) {
                throw new NfeException("", null);
            } else {
                switch (tag) {
                    case "LoteRps":
                        dsc = new DOMSignContext(getDummyPrivateKey(), doc.getElementsByTagName("GerarNfseEnvio").item(0));
                        break;
                    case "InfRps":
                        dsc = new DOMSignContext(getDummyPrivateKey(), doc.getElementsByTagName("Rps").item(0));
                        break;
                    default:
                        throw new NfeException("", null);
                }
            }
            // Get the infNFe element and its ID
            Element infNFeElement = (Element) doc.getElementsByTagName(tag).item(0);
            dsc.setIdAttributeNS(infNFeElement, null, "Id");
            XMLSignatureFactory sigFac = XMLSignatureFactory.getInstance("DOM");
            SignedInfo si = assinarElemento(infNFeElement, sigFac);
            XMLSignature signature = sigFac.newXMLSignature(si, null);
            signature.sign(dsc);
            // Return XMLSignature
            return signature;
        } catch (NfeException | NoSuchAlgorithmException | InvalidKeySpecException | javax.xml.crypto.MarshalException | XMLSignatureException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    private static XMLSignature signCancelamentoNotaFiscalServico(Document doc) throws NfeException {
        try {
            XMLSignatureFactory sigFac = XMLSignatureFactory.getInstance("DOM");

            // Get the infNFe element and its ID
            Element infNFeElement = (Element) doc.getElementsByTagName("InfPedidoCancelamento").item(0);

            SignedInfo si = assinarElemento(infNFeElement, sigFac);

            // Sign with dummy key
            DOMSignContext dsc = new DOMSignContext(getDummyPrivateKey(), doc.getDocumentElement().getElementsByTagName("Pedido").item(0));
            dsc.setIdAttributeNS(infNFeElement, null, "Id");
            XMLSignature signature = sigFac.newXMLSignature(si, null);
            signature.sign(dsc);
            // Return XMLSignature
            return signature;
        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    // This method returns a hardcoded dummy private key used to simulate signatures using Java (see usages above)
    private static PrivateKey getDummyPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIzrH1f5nzfbvvNHub6C1KvigS8CHHS/By5Ls4VTxQLOn9is+5cbKSD2iAfWT6vVAUSPv8TfhmvnpRqPJ4Bk3vrrVX5mdNuDoTzCQSbD/5iennFl4l+lemmu2Nudar1juEfxQfvFG2hvm8GQ/5u76w/iPREO8g7PzioKQRNZGOv/AgMBAAECgYAGq4JaSahtnmsVXMm/6LVkRV5T+Uebhwcx+8dNgj+K+Hi8asOlzVVPCBw8MrqmqXhb5GnxSZs1NEuuTCRUgXHEYI1nX289FI8sazIOu9UxxjfxvbED0d1y6dK4NWPOUFe/1fxZTgXSJXEOJ8cReI4/UpG8f76o9Tf7M5dj8t/rEQJBANbLA79N9jVoUZ+BvH6ryhxsrU8/0kXy+DippFpQsNcUPfz4TE9CjqH6u7t8RrmVXUp3JsMWm/fWWlQNwsJCFBsCQQCn8/SPKyUIjFsDLtU8QVpbwoW6TpRl3IwYdq6WdAdBXt29zfPtaz2OpNow2jaBSP+bJuEYmDabz4DZza3Sw93tAkEAyYhUbMPGllfZ9fJxnNys1zy05B26urz9X5T0S3VYZ4VroBaM6vVFBQBP8trpNSnLDZp8eSGWl9S8jg8XRNNhLwJAbew3+uAFC/Q4uPuU6ivnxLiqp4Y4j/Zp5rT+jVABU6KQRGKgLJqMnmh8uY6IL9OkH1qx5lPxIccMkQCRrKku/QJAA68E7uSQqOv8QayMebQrAIlHc/T6gePrwMr/e+b5aXQgSiTrcyB1C1zPnNZb/zd4ErrM7/4y2JiZ3ksk1I/SSg==";
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64));
        return KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);
    }

    private static Element createKeyInfo(Document doc, String certificateBase64) {
        Element x509Certificate = doc.createElementNS(XMLSignature.XMLNS, "X509Certificate");
        x509Certificate.setTextContent(certificateBase64);

        Element x509Data = doc.createElementNS(XMLSignature.XMLNS, "X509Data");
        x509Data.appendChild(x509Certificate);

        Element keyInfo = doc.createElementNS(XMLSignature.XMLNS, "KeyInfo");
        keyInfo.appendChild(x509Data);

        return keyInfo;
    }

    private static void replaceSignatureElements(Document doc, String certificateBase64, String signatureBase64) {
        // Locate the last Signature element in the document
        NodeList sigElementsList = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        Element signatureElement = (Element) sigElementsList.item(sigElementsList.getLength() - 1);

        // Set actual signature value computed with Web PKI on the page
        Element signatureValueElement = (Element) signatureElement.getElementsByTagNameNS(XMLSignature.XMLNS, "SignatureValue").item(0);
        signatureValueElement.setTextContent(signatureBase64);

        // Add the X509Certificate containing the encoded certificate acquired with Web PKI on the page
        Element keyInfo = createKeyInfo(doc, certificateBase64);
        signatureElement.appendChild(keyInfo);
    }

    private static SignedInfo assinarElemento(Element infNFeElement, XMLSignatureFactory sigFac) throws NfeException {
        try {
            String infNFeId = infNFeElement.getAttribute("Id");

            // Reference the infNFe element by its ID with:
            // - Transformations: "Enveloped" and canonicalization (Canonical XML 1.0)
            // - Digest algorithm: SHA-1
            List<Transform> refTransforms = new ArrayList<>();
            refTransforms.add(sigFac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
            refTransforms.add(sigFac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null));
            Reference ref = sigFac.newReference(
                    "#" + infNFeId,
                    sigFac.newDigestMethod(DigestMethod.SHA1, null),
                    refTransforms,
                    null,
                    null
            );

            // Specify a SignedInfo with:
            // - Canonicalization: Canonical XML 1.0
            // - Signature algorithm: RSA with SHA-1
            // - References: infNFe element
            return sigFac.newSignedInfo(
                    sigFac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    sigFac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(ref)
            );
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException ex) {
            throw new NfeException("Falha ao assinar elemento", ex);
        }
    }

}
