package br.com.villefortconsulting.sgfinancas.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Data
@Entity
@Audited
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PLANO_TRANSFERENCIA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlanoTransferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "DIAS")
    private Integer dias;

    @Basic(optional = false)
    @Column(name = "ANTECIPADO")
    private String antecipado;

    @Basic(optional = false)
    @Column(name = "DIAS_SPLIT")
    private Integer diasSplit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getDiasSplit() {
        return diasSplit;
    }

    public void setDiasSplit(Integer diasSplit) {
        this.diasSplit = diasSplit;
    }

    public String getAntecipado() {
        return antecipado;
    }

    public void setAntecipado(String antecipado) {
        this.antecipado = antecipado;
    }

}
