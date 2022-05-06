package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PAGAMENTO_MENSALIDADE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class PagamentoMensalidade extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private Empresa idEmpresa;

    @NotNull
    @Column(name = "VALOR_PAGO")
    private Double valorPago;

    @Column(name = "DATA_PAGAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;

    @NotNull
    @Column(name = "DATA_VALIDADE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataValidade;

    @JoinColumn(name = "ID_TRANSACAO_GETNET", referencedColumnName = "ID")
    @OneToOne
    private TransacaoGetnet idTransacaoGetnet;

    @NotNull
    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "REFERENCIA_FITPAG")
    private String referenciaFitPag;

    @NotNull(message = "Informe o usuário que gerou o pedido de renovação/contratação")
    @JoinColumn(name = "ID_USUARIO_GERACAO", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Usuario idUsuarioGeracao;

    @JoinColumn(name = "ID_USUARIO_GERACAO_PAGAMENTO", referencedColumnName = "ID")
    @OneToOne
    private Usuario idUsuarioPagamento;

    @JoinColumn(name = "ID_MODULO_PACOTE", referencedColumnName = "ID")
    @OneToOne
    private ModuloPacote idModuloPacote;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPagamentoMensalidade", orphanRemoval = true)
    private List<PagamentoMensalidadeModulo> pagamentoMensalidadeModuloList = new LinkedList<>();

    @Transient
    private boolean processado = false;

    public boolean isDentroDaVigenciaDoContrato() {
        return dataValidade.after(new Date());
    }

}
