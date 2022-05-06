package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "NF_CORRECAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class NFCorrecao extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Column(name = "NRO_SEQUENCIA")
    @NotNull(message = "Informe o número sequencial")
    private Integer nroSequencia;

    @NotNull(message = "Informe a nota fiscal")
    @JoinColumn(name = "ID_NF", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NF idNf;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para situação")
    private String situacao;

    @NotNull(message = "Informe a data do evento")
    @Column(name = "DATA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;

    @Column(name = "DESCRICAO_CORRECAO")
    @Size(max = 1000, message = "Máximo de 1000 caracteres para descrição da correção")
    private String descricaoCorrecao;

    @Column(name = "XML_ENVIO")
    private String xmlEnvio;

    @Column(name = "XML_RETORNO")
    private String xmlRetorno;

    @Column(name = "XML_ARMAZENAMENTO")
    private String xmlArmazenamento;

    @Override
    public String toString() {
        return "NF{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    @Override
    public NFCorrecao clone() {
        try {
            NFCorrecao novaCorrecao = (NFCorrecao) super.clone();
            novaCorrecao.setId(null);
            novaCorrecao.setIdNf(null);
            return novaCorrecao;
        } catch (CloneNotSupportedException ex) {
            return new NFCorrecao();
        }
    }

}
