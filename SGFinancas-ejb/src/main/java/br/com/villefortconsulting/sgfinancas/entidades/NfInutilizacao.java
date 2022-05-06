package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
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
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "NF_INUTILIZACAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class NfInutilizacao extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ANO")
    @Min(0)
    @Max(99)
    private Integer ano;

    @JoinColumn(name = "ID_UF", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UF idUf;

    @Column(name = "MODELO")
    private Integer modelo;

    @Column(name = "SERIE")
    private Integer serie;

    @Column(name = "NUMERO_INICIAL")
    private Integer numeroInicial;

    @Column(name = "NUMERO_FINAL")
    private Integer numeroFinal;

    @Size(max = 250)
    @Column(name = "MOTIVO")
    private String motivo;

    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    @Column(name = "DATA")
    @Temporal(TemporalType.DATE)
    private Date data;

    public NfInutilizacao(Integer id) {
        this.id = id;
    }

    public NfInutilizacao(Integer ano, UF idUf, Integer modelo, Integer serie, Integer numeroInicial, Integer numeroFinal, String motivo, Usuario idUsuario) {
        this.ano = ano;
        this.idUf = idUf;
        this.modelo = modelo;
        this.serie = serie;
        this.numeroInicial = numeroInicial;
        this.numeroFinal = numeroFinal;
        this.motivo = motivo;
        this.idUsuario = idUsuario;
        this.data = new Date();
    }

}
