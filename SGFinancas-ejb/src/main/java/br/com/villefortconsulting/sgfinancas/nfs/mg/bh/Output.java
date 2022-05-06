package br.com.villefortconsulting.sgfinancas.nfs.mg.bh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for output complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="output">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="outputXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "output", propOrder = {
    "outputXML"
})
public class Output {

    @XmlElement(required = true)
    protected String outputXML;

    /**
     * Gets the value of the outputXML property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getOutputXML() {
        return outputXML;
    }

    /**
     * Sets the value of the outputXML property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setOutputXML(String value) {
        this.outputXML = value;
    }

}
