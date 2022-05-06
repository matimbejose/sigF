package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONTRATO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Contrato extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne
    private Cliente idCliente;

    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne
    private Fornecedor idFornecedor;

    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto idCentroCusto;

    @JoinColumn(name = "ID_TURMA", referencedColumnName = "ID")
    @ManyToOne
    private Turma idTurma;

    @JoinColumn(name = "ID_CONTA", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Conta idConta;

    @NotNull(message = "Informe a conta bancária")
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancaria;

    @NotNull(message = "Informe o plano de conta")
    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoConta;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @Size(max = 1, message = "Tipo de contrato deve possuir no máximo 1 caracter")
    @Column(name = "TIPO_CONTRATO")
    private String tipoContrato;

    @Column(name = "DATA_FIM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;

    @NotNull(message = "Informe a data de inicio do contrato")
    @Column(name = "DATA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCancelamento;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    private String situacao;

    @NotNull(message = "Informe a data de vencimento para o pagamento")
    @Column(name = "DATA_VENCIMENTO_PAGAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimentoPagamento;

    @Column(name = "OBSERVACAO")
    @Size(max = 2000, message = "Máximo de 2000 caracteres para a observação.")
    private String observacao;

    @NotNull(message = "Informe se usa atualização automática com sim ou não")
    @Column(name = "USA_ATUALIZACAO_AUTOMATICA")
    @Size(max = 1, message = "Máximo de 1 caractere para a atualização automática.")
    private String usaAtualizacaoAutomatica;

    @NotNull(message = "Informe o valor do contrato")
    @Column(name = "VALOR_CONTRATO")
    private Double valorContrato;

    @Column(name = "DESCONTO")
    private Double desconto;

    @Column(name = "DATA_VENCIMENTO_INSTALACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimentoInstalacao;

    @Column(name = "TAXA_INSTALACAO")
    private Double taxaInstalacao;

    @Column(name = "DATA_VENCIMENTO_ADESAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimentoAdesao;

    @Column(name = "TAXA_ADESAO")
    private Double taxaAdesao;

    @NotNull(message = "Informe o numero de parcelas")
    @Column(name = "NUMERO_PARCELAS")
    private Integer numeroParcelas;

    @Column(name = "PRAZO_PARA_NOTIFICACAO")
    private Integer prazoParaNotificacao;

    @Column(name = "JUSTIFICATIVA_ITERRUPCAO")
    private String justificativaInterrupcao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContrato", orphanRemoval = true)
    private List<ContratoClienteVeiculo> contratoClienteVeiculoList = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContrato", orphanRemoval = true)
    private List<ContratoServico> contratoServicoList = new LinkedList<>();

    @Transient
    private List<ParcelaDTO> listParcelaDTO = new LinkedList<>();

    public int obterNumeroParcelasPagas() {
        if (listParcelaDTO != null && !listParcelaDTO.isEmpty()) {
            return (int) listParcelaDTO.stream().filter(p -> p.getDataPagamento() != null).count();
        } else {
            return 0;
        }
    }

    public int obterNumeroUltimaParcelaPaga() {
        if (listParcelaDTO != null && !listParcelaDTO.isEmpty()) {
            IntStream parcelasPagas = listParcelaDTO.stream()
                    .filter(parcela -> parcela.getDataPagamento() != null)
                    .mapToInt(ParcelaDTO::getNumParcela);
            try {
                return parcelasPagas.max().getAsInt() - 1;
            } catch (Exception ex) {
                Logger.getLogger(Contrato.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Double obterValorParcelasPagas() {
        if (listParcelaDTO != null && !listParcelaDTO.isEmpty()) {
            return listParcelaDTO.stream()
                    .filter(parcela -> parcela.getDataPagamento() != null)
                    .mapToDouble(ParcelaDTO::getValorPago).sum();
        } else {
            return 0d;
        }
    }

    public Double obterValorBrutoDasParcelasPagas() {
        if (listParcelaDTO != null && !listParcelaDTO.isEmpty()) {
            return listParcelaDTO.stream()
                    .filter(parcela -> parcela.getDataPagamento() != null)
                    .mapToDouble(ParcelaDTO::getValor).sum();
        } else {
            return 0d;
        }
    }

    public Double obterValorBrutoDasParcelasVencidas() {
        if (listParcelaDTO != null && !listParcelaDTO.isEmpty()) {
            return listParcelaDTO.stream()
                    .filter(parcela -> parcela.getDataVencimento().compareTo(DataUtil.getHoje()) < 0)
                    .mapToDouble(ParcelaDTO::getValor).sum();
        } else {
            return 0d;
        }
    }

    public List<ParcelaDTO> obterParcelasJaPagas() {
        if (listParcelaDTO != null && !listParcelaDTO.isEmpty()) {
            return listParcelaDTO.stream().filter(p -> p.getDataPagamento() != null).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<ParcelaDTO> obterParcelasVencidas() {
        if (listParcelaDTO != null && !listParcelaDTO.isEmpty()) {
            return listParcelaDTO.stream()
                    .filter(parcela -> parcela.getDataVencimento().compareTo(DataUtil.getHoje()) < 0)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<ParcelaDTO> obterParcelasVencidasOuPagas() {
        if (listParcelaDTO != null && !listParcelaDTO.isEmpty()) {
            return listParcelaDTO.stream()
                    .filter(parcela -> parcela.getDataPagamento() != null || parcela.getDataVencimento().compareTo(DataUtil.getHoje()) < 0)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<ParcelaDTO> getListParcelaDTO() {
        return listParcelaDTO.stream().sorted((p1, p2) -> p1.getNumParcela().compareTo(p2.getNumParcela())).collect(Collectors.toList());
    }

    public List<ParcelaDTO> getListParcelaDTODesordenada() {
        return listParcelaDTO;
    }

    @Override
    public String toString() {
        return "Contrato{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
