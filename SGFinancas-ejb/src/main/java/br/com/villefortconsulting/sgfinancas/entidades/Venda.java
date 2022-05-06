package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoItemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Audited
@Entity
@Table(name = "VENDA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@AllArgsConstructor
@Inheritance
public class Venda extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @Valid
    @NotNull(message = "Informe o cliente")
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idCliente;

    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private ContaBancaria idContaBancaria;

    @NotNull(message = "Informe um plano de conta")
    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PlanoConta idPlanoConta;

    @JoinColumn(name = "ID_USUARIO_VENDEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuarioVendedor;

    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto idCentroCusto;

    @Column(name = "DATA_VENCIMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimento;

    @Column(name = "DATA_VENDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVenda;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCancelamento;

    @NotNull(message = "Informe o valor")
    @Column(name = "VALOR")
    private Double valor;

    @Column(name = "CONDICAO_PAGAMENTO")
    @Size(max = 1, message = "Condição de pagamento deverá ter entre 1 caracteres.")
    private String condicaoPagamento;

    @NotNull(message = "Informe a situacao")
    @Size(max = 1, message = "Máximo de 1 caracter para situação")
    @Column(name = "SITUACAO")
    private String situacao;

    @NotNull(message = "Informe o status da negociação")
    @Size(max = 2, message = "Máximo de 2 caracteres para status da negociação")
    @Column(name = "STATUS_NEGOCIACAO")
    private String statusNegociacao;

    @Size(max = 2, message = "Máximo de 2 caracteres para status da negociação")
    @Column(name = "STATUS_OS")
    private String statusOS;

    @Column(name = "NUM_PARCELA")
    private Integer numParcela;

    @Column(name = "DESCONTO")
    private Double desconto;

    @Column(name = "VALOR_FRETE")
    private Double valorFrete;

    @Column(name = "FORMA_PAGAMENTO")
    @Size(max = 2000, message = "Forma de pagamento deverá ter entre 2000 caracteres.")
    private String formaPagamento;

    @Column(name = "OBSERVACAO")
    @Size(max = 2000, message = "Observação deverá ter entre 2000 caracteres.")
    private String observacao;

    @Column(name = "OBSERVACAO_CLIENTE")
    @Size(max = 500, message = "Observação do cliente deverá ter no máximo 500 caracteres.")
    private String observacaoCliente;

    @Column(name = "OBSERVACAO_PARCELA")
    @Size(max = 200, message = "Observação de parcela deverá ter entre 200 caracteres.")
    private String observacaoParcela;

    @Column(name = "VALOR_DESCONTO")
    private Double valorDesconto;

    @Column(name = "KM")
    private Double km;

    @Column(name = "COMISSAO_VENDEDOR")
    private Double comissaoVendedor;

    @JoinColumn(name = "ID_CONTA", referencedColumnName = "ID")
    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    private Conta idConta;

    @Valid
    @NotNull(message = "Informe ao menos 1 produto para venda")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVenda", orphanRemoval = true)
    private List<VendaProduto> vendaProdutoList = new LinkedList<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVenda", orphanRemoval = true)
    private List<VendaServico> vendaServicoList = new LinkedList<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVenda", orphanRemoval = true)
    private List<VendaItemOrdemDeServico> vendaItemOrdemDeServicoList = new LinkedList<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVenda", orphanRemoval = true)
    private List<VendaFormaPagamento> vendaFormaPagamentoList = new LinkedList<>();

    @Column(name = "TIPO_ITEM_VENDA")
    @NotNull(message = "Informe o tipo dos itens da venda")
    private String tipoItemVenda;

    @Column(name = "PONTUACAO")
    private Double pontuacao;

    @Column(name = "PONTOS_UTILIZADOS")
    private Double pontosUtilizados;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "ID_ORIGEM")
    private String idOrigem;

    @Column(name = "REPRESENTANTE_EMPRESA")
    private String representanteEmpresa;

    @Column(name = "TELEFONE_CLIENTE")
    private String telefoneCliente;

    @JoinColumn(name = "ID_FORMA_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private FormaPagamento idFormaPagamento;

    @Column(name = "DATA_VALIDADE_ORCAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataValidadeOrcamento;

    @Column(name = "DESC_TERMOS_CONDICOES")
    @Size(max = 300, message = "Descrição dos Termos eCondição de pagamento deverá ter entre 300 caracteres.")
    private String termosCondicoes;

    @OneToOne(mappedBy = "idVenda")
    private TransacaoGetnet idTransacao;

    @OneToMany(mappedBy = "idVenda")
    private List<NF> nfList;

    @JoinColumn(name = "ID_CLIENTE_VEICULO", referencedColumnName = "ID")
    @ManyToOne
    private ClienteVeiculo idClienteVeiculo;

    @Column(name = "DATA_INICIO_EXECUCAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicioExecucao;

    @JoinColumn(name = "ID_RESPONSAVEL_EXECUCAO", referencedColumnName = "ID")
    @ManyToOne
    private Usuario idResponsavelExecucao;

    @Column(name = "MOTIVO_CANCELAMENTO_OS")
    @Size(max = 200, message = "Informe o motivo de cancelamento da ordem de serviço.")
    private String motivoCancelamentoOS;

    @JoinColumn(name = "ID_ORCAMENTO_OS_ORIGEM", referencedColumnName = "ID")
    @OneToOne(optional = true, fetch = FetchType.LAZY)
    private Venda idOrcamentoOSOrigem;

    @NotAudited
    @OneToOne(mappedBy = "idOrcamentoOSOrigem")
    private Venda idVendaDestino;

    @OneToOne(mappedBy = "idVenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private VendaSeguradora idVendaSeguradora;

    @Column(name = "DATA_FINALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFinalizacao;

    @Transient
    private List<ParcelaDTO> listParcelaDTO = new LinkedList<>();

    @Transient
    private String controle;

    @Transient
    private Double valorPago;

    @Transient
    private Boolean darBaixa = false;

    @Transient
    private Boolean emitirNFCe = false;

    @Transient
    private Boolean podeVerificarEstoque = true;

    public Venda() {
        this.situacao = "A";
    }

    public void addProduto(VendaProduto vendaProduto) {
        vendaProduto.setIdVenda(this);
        vendaProdutoList.add(vendaProduto);
    }

    public void removeProduto(VendaProduto vendaProduto) {
        vendaProdutoList.remove(vendaProduto);
    }

    public void addServico(VendaServico vendaServico) {
        vendaServico.setIdVenda(this);
        vendaServicoList.add(vendaServico);
    }

    public void removeServico(VendaServico vendaServico) {
        vendaServicoList.remove(vendaServico);
    }

    public boolean isDarBaixa() {
        return darBaixa;
    }

    public boolean isEmitirNFCe() {
        return emitirNFCe;
    }

    public boolean isOrcamento() {
        return this.statusNegociacao.equals(EnumTipoVenda.ORCAMENTO.getSituacao())
                || this.statusNegociacao.equals(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao())
                || this.statusNegociacao.equals(EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao());
    }

    public Double getValorFrete() {
        return NumeroUtil.somar(valorFrete);
    }

    public Double getValorDesconto() {
        return NumeroUtil.somar(valorDesconto);
    }

    public Double getValorPago() {
        return NumeroUtil.somar(valorPago);
    }

    public String getStatusOrcamento() {
        EnumOrigemVenda e = EnumOrigemVenda.retornaEnumSelecionado(origem);
        if (e == null) {
            return null;
        }
        return e.getDescricaoOrcamento();
    }

    @Override
    public String toString() {
        return "Venda{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    @Override
    public Venda clone() {
        try {
            Venda novo = (Venda) super.clone();
            if (idVendaSeguradora != null) {
                novo.setIdVendaSeguradora(this.idVendaSeguradora.clone());
            }
            List<VendaProduto> novaVendaProdutoList = new ArrayList<>();
            List<VendaServico> novaVendaServicoList = new ArrayList<>();
            List<VendaItemOrdemDeServico> novaVendaItemOrdemDeServicoList = new ArrayList<>();
            List<VendaFormaPagamento> novaVendaVendaFormaPagamentoList = new ArrayList<>();

            for (VendaProduto produto : novo.getVendaProdutoList()) {
                VendaProduto produtoNovo = produto.clone();
                produtoNovo.setId(null);
                produtoNovo.setIdVenda(novo);
                novaVendaProdutoList.add(produtoNovo);
            }
            for (VendaServico servico : novo.getVendaServicoList()) {
                VendaServico servicoNovo = servico.clone();
                servicoNovo.setId(null);
                servicoNovo.setIdVenda(novo);
                novaVendaServicoList.add(servicoNovo);
            }
            for (VendaItemOrdemDeServico itemOS : novo.getVendaItemOrdemDeServicoList()) {
                VendaItemOrdemDeServico itemOSNovo = itemOS.clone();
                itemOSNovo.setId(null);
                itemOSNovo.setIdVenda(novo);
                novaVendaItemOrdemDeServicoList.add(itemOSNovo);
            }
            for (VendaFormaPagamento itemOS : novo.getVendaFormaPagamentoList()) {
                VendaFormaPagamento itemFPNovo = itemOS.clone();
                itemFPNovo.setId(null);
                itemFPNovo.setIdVenda(novo);
                novaVendaVendaFormaPagamentoList.add(itemFPNovo);
            }

            novo.setId(null);
            novo.setIdOrcamentoOSOrigem(null);
            novo.setIdConta(null);
            novo.setVendaProdutoList(novaVendaProdutoList);
            novo.setVendaServicoList(novaVendaServicoList);
            novo.setVendaItemOrdemDeServicoList(novaVendaItemOrdemDeServicoList);
            novo.setVendaFormaPagamentoList(novaVendaVendaFormaPagamentoList);
            novo.setNfList(new ArrayList<>());
            if (novo.getIdVendaSeguradora() != null) {
                novo.getIdVendaSeguradora().setIdVenda(novo);
            }
            return novo;
        } catch (CloneNotSupportedException ex) {
            return new Venda();
        }
    }

    @PrePersist
    public void prePersist() {
        if (!vendaProdutoList.isEmpty() && !vendaServicoList.isEmpty()) {
            tipoItemVenda = EnumTipoItemVenda.PRODUTO_SERVICO.getTipo();
        } else if (!vendaProdutoList.isEmpty()) {
            tipoItemVenda = EnumTipoItemVenda.PRODUTO.getTipo();
        } else {
            tipoItemVenda = EnumTipoItemVenda.SERVICO.getTipo();
        }
    }

}
