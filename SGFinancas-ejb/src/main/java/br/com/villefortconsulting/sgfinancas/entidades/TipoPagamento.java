package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
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
@Table(name = "TIPO_PAGAMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class TipoPagamento extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "O campo deverá estar entre 1 e 200 caracteres")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe se é permitido parcelar a conta")
    @Size(max = 1)
    @Column(name = "PERMITE_PARCELAMENTO")
    private String permiteParcelamento;

    @NotNull
    @Size(max = 1)
    @Column(name = "ATIVO")
    private String ativo;

    public TipoPagamento() {
        ativo = "S";
    }

    public TipoPagamento(Integer id) {
        this.id = id;
        ativo = "S";
    }

    @Override
    public String toString() {
        return "TipoPagamento{" + "id=" + id + '}';
    }

}
