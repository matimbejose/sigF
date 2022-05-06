package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "SOLICITACAO_ADIANTAMENTO_CONTA_PARCELA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class SolicitacaoAdiantamentoContaParcela extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_SOLICITACAO_ADIANTAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private SolicitacaoAdiantamento idSolicitacaoAdiantamento;

    @JoinColumn(name = "ID_CONTA_PARCELA", referencedColumnName = "ID")
    @ManyToOne
    private ContaParcela idContaParcela;

}
