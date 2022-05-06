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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "SOLICITACAO_CADASTRO_CLIENTE_VEICULO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class SolicitacaoCadastroClienteVeiculo extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_SOLICITACAO_CADASTRO_CLIENTE", referencedColumnName = "ID")
    @ManyToOne
    private SolicitacaoCadastroCliente idSolicitacaoCadastroCliente;

    @JoinColumn(name = "ID_MODELO_INFORMACAO", referencedColumnName = "ID")
    @ManyToOne
    private ModeloInformacao idModeloInformacao;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @JoinColumn(name = "ID_COR_VEICULO", referencedColumnName = "ID")
    @ManyToOne
    private CorVeiculo idCorVeiculo;

    @JoinColumn(name = "ID_COMBUSTIVEL", referencedColumnName = "ID")
    @ManyToOne
    private Combustivel idCombustivel;

    @Column(name = "ANO_FABRICACAO")
    private Integer anoFabricacao;

    @Size(max = 10)
    @Column(name = "PLACA")
    private String placa;

    @Size(max = 25)
    @Column(name = "CAMBIO")
    private String cambio;

    @Column(name = "NRO_PORTAS")
    private Integer nroPortas;

    @Column(name = "NRO_PASSAGEIROS")
    private Integer nroPassageiros;

    @Size(max = 15)
    @Column(name = "RENAVAM")
    private String renavam;

    @Size(max = 17)
    @Column(name = "CHASSI")
    private String chassi;

    @Column(name = "VALOR_FIPE")
    private Double valorFipe;

    @Size(max = 1)
    @Column(name = "STAUS")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_MODIFICACAO")
    private Date dataModificacao;

    @Size(max = 200)
    @Column(name = "OBSERVACAO")
    private String observacao;

    @Size(max = 500)
    @Column(name = "MOTIVO_CANCELAMENTO")
    private String motivoCancelamento;

}
