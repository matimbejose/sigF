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
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CLIENTE_MOVIMENTACAO_ALTERACAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class ClienteMovimentacaoAlteracao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CLIENTE_MOVIMENTACAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ClienteMovimentacao idClienteMovimentacao;

    @JoinColumn(name = "ID_USUARIO_SOLICITACAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuarioSolicitacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_ALTERACAO")
    @NotNull(message = "Informe a data de altercao")
    private Date dataAlteracao;

    @JoinColumn(name = "ID_USUARIO_FINALIZACAO", referencedColumnName = "ID")
    @ManyToOne
    private Usuario idUsuarioFinalizacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_FINALIZACAO")
    private Date dataFinalizacao;

    @Column(name = "TIPO")
    @NotNull(message = "Informe o tipo de alteração")
    private String tipo;

    @Column(name = "STATUS")
    @NotNull(message = "Informe o status da alteração")
    private String status;

}
