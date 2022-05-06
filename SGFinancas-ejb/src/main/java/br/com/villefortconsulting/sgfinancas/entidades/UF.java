package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "UF")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class UF extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 2, message = "Máximo de 2 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe o nome completo")
    @Column(name = "NOME_COMPLETO")
    @Size(max = 50, message = "Máximo de 50 caracteres para o nome completo")
    private String nomeCompleto;

    @NotNull(message = "Informe o CUF")
    @Column(name = "CUF")
    @Size(max = 2, message = "Máximo de 2 caracteres para o CUF")
    private String cuf;

    @Column(name = "PAIS")
    private Integer pais;

    @Column(name = "ALIQUOTA")
    private Double aliquota;

    @Column(name = "PARTICIPA_PROTOCOLO")
    @Size(max = 1, message = "Máximo de 2 caracteres para o participaProtocolo")
    private String participaProtocolo;

    @Column(name = "MVA")
    private Double mva;

    @Override
    public String toString() {
        return "UF{" + "id=" + id + '}';
    }

}
