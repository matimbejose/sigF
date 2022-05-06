package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.util.NumeroUtil;
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
@Table(name = "ORDEM_DE_SERVICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class OrdemDeServico extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Cliente idCliente;

    @Size(max = 200)
    @Column(name = "OBSERVACAO")
    private String observacao;

    @JoinColumn(name = "ID_FUNCIONARIO_ENTRADA", referencedColumnName = "ID")
    @ManyToOne
    private Funcionario idFuncionarioEntrada;

    @JoinColumn(name = "ID_FUNCIONARIO_SAIDA", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Funcionario idFuncionarioSaida;

    @Column(name = "VALOR")
    private Double valor;

    @Size(max = 2)
    @Column(name = "FORMA_PAGAMENTO")
    private String formaPagamento;

    @OneToMany(mappedBy = "idOrdemDeServico", cascade = CascadeType.ALL)
    private List<StatusOrdemDeServico> statusOrdemDeServico = new LinkedList<>();

    @OneToMany(mappedBy = "idOrdemDeServico", cascade = CascadeType.ALL)
    private List<ItensOrdemDeServico> itensOrdemDeServico = new LinkedList<>();

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @Column(name = "HORARIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horario;

    @JoinColumn(name = "ID_CONTA", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Conta idConta;

    public OrdemDeServico(Integer id) {
        this.id = id;
    }

    @Column(name = "DATA_INICIO")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @Column(name = "DATA_TERMINO")
    @Temporal(TemporalType.DATE)
    private Date dataTermino;

    public String getValorFormatado() {
        return NumeroUtil.formatarCasasDecimais(valor, 2, false);
    }

}
