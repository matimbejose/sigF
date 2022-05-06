package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PAGAMENTO_MENSALIDADE_MODULO_PERMISSAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class PagamentoMensalidadeModuloPermissao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JoinColumn(name = "ID_PAGAMENTO_MENSALIDADE_MODULO", referencedColumnName = "ID")
    @ManyToOne
    private PagamentoMensalidadeModulo idPagamentoMensalidadeModulo;

    @NotNull
    @JoinColumn(name = "ID_PERMISSAO", referencedColumnName = "ID")
    @ManyToOne
    private Permissao idPermissao;

    public PagamentoMensalidadeModuloPermissao(PagamentoMensalidadeModulo idPagamentoMensalidadeModulo, Permissao idPermissao) {
        this.idPagamentoMensalidadeModulo = idPagamentoMensalidadeModulo;
        this.idPermissao = idPermissao;
        this.tenatID = idPagamentoMensalidadeModulo.getTenatID();
    }

}
