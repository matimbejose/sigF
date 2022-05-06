package br.com.villefortconsulting.sgfinancas.nfs.mg.bh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for input complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="input">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nfseCabecMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nfseDadosMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "input", propOrder = {
    "nfseCabecMsg",
    "nfseDadosMsg"
})
public class Input {

    @XmlElement(required = true)
    protected String nfseCabecMsg;

    @XmlElement(required = true)
    protected String nfseDadosMsg;

    /**
     * Gets the value of the nfseCabecMsg property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getNfseCabecMsg() {
        return nfseCabecMsg;
    }

    /**
     * Sets the value of the nfseCabecMsg property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setNfseCabecMsg(String value) {
        this.nfseCabecMsg = value;
    }

    /**
     * Gets the value of the nfseDadosMsg property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getNfseDadosMsg() {
        return nfseDadosMsg;
    }

    /**
     * Sets the value of the nfseDadosMsg property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setNfseDadosMsg(String value) {
        this.nfseDadosMsg = value;
    }

}
