package br.com.villefortconsulting.sgfinancas.nfs.mg.bh;

import br.gov.pbh.schema.nfse.CancelarNfseEnvio;
import br.gov.pbh.schema.nfse.ConsultarNfseEnvio;
import br.gov.pbh.schema.nfse.ConsultarSituacaoLoteRpsEnvio;
import br.gov.pbh.schema.nfse.GerarNfseEnvio;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private final static QName CONSULTARSITUACAOLOTERPSREQUEST_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarSituacaoLoteRpsRequest");

    private final static QName CONSULTARNFSERESPONSE_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarNfseResponse");

    private final static QName CONSULTARLOTERPSRESPONSE_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarLoteRpsResponse");

    private final static QName RECEPCIONARLOTERPSREQUEST_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "RecepcionarLoteRpsRequest");

    private final static QName CONSULTARNFSEPORFAIXAREQUEST_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarNfsePorFaixaRequest");

    private final static QName GERARNFSEREQUEST_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "GerarNfseRequest");

    private final static QName CONSULTARNFSEPORFAIXARESPONSE_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarNfsePorFaixaResponse");

    private final static QName GERARNFSERESPONSE_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "GerarNfseResponse");

    private final static QName CONSULTARSITUACAOLOTERPSRESPONSE_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarSituacaoLoteRpsResponse");

    private final static QName RECEPCIONARLOTERPSRESPONSE_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "RecepcionarLoteRpsResponse");

    private final static QName CONSULTARNFSEPORRPSRESPONSE_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarNfsePorRpsResponse");

    private final static QName CONSULTARNFSEREQUEST_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarNfseRequest");

    private final static QName CONSULTARNFSEPORRPSREQUEST_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarNfsePorRpsRequest");

    private final static QName CONSULTARLOTERPSREQUEST_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "ConsultarLoteRpsRequest");

    private final static QName CANCELARNFSERESPONSE_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "CancelarNfseResponse");

    private final static QName CANCELARNFSEREQUEST_QNAME = new QName("http://ws.bhiss.pbh.gov.br", "CancelarNfseRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.pbh.bhiss.ws
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Output }
     *
     * @return
     */
    public Output createOutput() {
        return new Output();
    }

    /**
     * Create an instance of {@link Input }
     *
     * @return
     */
    public Input createInput() {
        return new Input();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarSituacaoLoteRpsRequest")
    public JAXBElement<Input> createConsultarSituacaoLoteRpsRequest(Input value) {
        return new JAXBElement<>(CONSULTARSITUACAOLOTERPSREQUEST_QNAME, Input.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarSituacaoLoteRpsRequest")
    public JAXBElement<ConsultarSituacaoLoteRpsEnvio> createConsultarSituacaoLoteRpsRequest(ConsultarSituacaoLoteRpsEnvio value) {
        return new JAXBElement<>(CONSULTARSITUACAOLOTERPSREQUEST_QNAME, ConsultarSituacaoLoteRpsEnvio.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarNfseResponse")
    public JAXBElement<Output> createConsultarNfseResponse(Output value) {
        return new JAXBElement<>(CONSULTARNFSERESPONSE_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarLoteRpsResponse")
    public JAXBElement<Output> createConsultarLoteRpsResponse(Output value) {
        return new JAXBElement<>(CONSULTARLOTERPSRESPONSE_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "RecepcionarLoteRpsRequest")
    public JAXBElement<Input> createRecepcionarLoteRpsRequest(Input value) {
        return new JAXBElement<>(RECEPCIONARLOTERPSREQUEST_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarNfsePorFaixaRequest")
    public JAXBElement<Input> createConsultarNfsePorFaixaRequest(Input value) {
        return new JAXBElement<>(CONSULTARNFSEPORFAIXAREQUEST_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "GerarNfseRequest")
    public JAXBElement<Input> createGerarNfseRequest(Input value) {
        return new JAXBElement<>(GERARNFSEREQUEST_QNAME, Input.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "GerarNfseRequest")
    public JAXBElement<GerarNfseEnvio> createGerarNfseRequest(GerarNfseEnvio value) {
        return new JAXBElement<>(GERARNFSEREQUEST_QNAME, GerarNfseEnvio.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarNfsePorFaixaResponse")
    public JAXBElement<Output> createConsultarNfsePorFaixaResponse(Output value) {
        return new JAXBElement<>(CONSULTARNFSEPORFAIXARESPONSE_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "GerarNfseResponse")
    public JAXBElement<Output> createGerarNfseResponse(Output value) {
        return new JAXBElement<>(GERARNFSERESPONSE_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarSituacaoLoteRpsResponse")
    public JAXBElement<Output> createConsultarSituacaoLoteRpsResponse(Output value) {
        return new JAXBElement<>(CONSULTARSITUACAOLOTERPSRESPONSE_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "RecepcionarLoteRpsResponse")
    public JAXBElement<Output> createRecepcionarLoteRpsResponse(Output value) {
        return new JAXBElement<>(RECEPCIONARLOTERPSRESPONSE_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarNfsePorRpsResponse")
    public JAXBElement<Output> createConsultarNfsePorRpsResponse(Output value) {
        return new JAXBElement<>(CONSULTARNFSEPORRPSRESPONSE_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarNfseRequest")
    public JAXBElement<Input> createConsultarNfseRequest(Input value) {
        return new JAXBElement<>(CONSULTARNFSEREQUEST_QNAME, Input.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarNfseRequest")
    public JAXBElement<ConsultarNfseEnvio> createConsultarNfseRequest(ConsultarNfseEnvio value) {
        return new JAXBElement<>(CONSULTARNFSEREQUEST_QNAME, ConsultarNfseEnvio.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarNfsePorRpsRequest")
    public JAXBElement<Input> createConsultarNfsePorRpsRequest(Input value) {
        return new JAXBElement<>(CONSULTARNFSEPORRPSREQUEST_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "ConsultarLoteRpsRequest")
    public JAXBElement<Input> createConsultarLoteRpsRequest(Input value) {
        return new JAXBElement<>(CONSULTARLOTERPSREQUEST_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "CancelarNfseResponse")
    public JAXBElement<Output> createCancelarNfseResponse(Output value) {
        return new JAXBElement<>(CANCELARNFSERESPONSE_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}}
     *
     * @param value
     * @return
     */
    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "CancelarNfseRequest")
    public JAXBElement<Input> createCancelarNfseRequest(Input value) {
        return new JAXBElement<>(CANCELARNFSEREQUEST_QNAME, Input.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ws.bhiss.pbh.gov.br", name = "CancelarNfseRequest")
    public JAXBElement<CancelarNfseEnvio> createCancelarNfseRequest(CancelarNfseEnvio value) {
        return new JAXBElement<>(CANCELARNFSEREQUEST_QNAME, CancelarNfseEnvio.class, null, value);
    }

}
