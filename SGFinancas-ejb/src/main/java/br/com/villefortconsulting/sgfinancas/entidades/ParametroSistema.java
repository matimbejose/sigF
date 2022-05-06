package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConfirmacaoAgendamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PARAMETRO_SISTEMA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ParametroSistema extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CATEGORIA_IMPORTACAO_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private ProdutoCategoria idCategoriaImportacaoProduto;

    @JoinColumn(name = "ID_CATEGORIA_IMPORTACAO_SERVICO", referencedColumnName = "ID")
    @ManyToOne
    private ProdutoCategoria idCategoriaImportacaoServico;

    @NotNull
    @JoinColumn(name = "ID_PLANO_CONTA_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaContaBancaria;

    @NotNull
    @JoinColumn(name = "ID_PLANO_CONTA_CLIENTE", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaCliente;

    @NotNull
    @JoinColumn(name = "ID_PLANO_CONTA_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaFornecedor;

    @NotNull
    @JoinColumn(name = "ID_PLANO_CONTA_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaProduto;

    @NotNull
    @JoinColumn(name = "ID_PLANO_CONTA_SERVICO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaServico;

    @NotNull
    @JoinColumn(name = "ID_PLANO_CONTA_TRANSPORTADORA", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaTransportadora;

    @JoinColumn(name = "ID_PLANO_CONTA_FUNCIONARIO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaFuncionario;

    @Column(name = "NUMERO_NOTA_FISCAL")
    private Integer numNotaFiscal;

    @Column(name = "NUMERO_NOTA_FISCAL_SERVICO")
    private Integer numNotaFiscalServico;

    @Column(name = "SERIE_ENTRADA")
    private Integer serieEntrada;

    @Column(name = "SERIE_SAIDA")
    private Integer serieSaida;

    @Column(name = "VERSAO_PROCESSO_NF")
    private Double versaoProcessoNF;

    @JoinColumn(name = "ID_FORMULARIO", referencedColumnName = "ID")
    @ManyToOne
    private Formulario idFormulario;

    @Column(name = "ENVIO_NOTA_PRODUTO")
    @Size(max = 250, message = "Envio da nota de produto deve ter no máximo 2 caracteres.")
    private String envioNotaProduto;

    @Column(name = "ENVIO_NOTA_SERVICO")
    @Size(max = 250, message = "Envio da nota de serviço deve ter no máximo 2 caracteres.")
    private String envioNotaServico;

    @Column(name = "INSTRUCAO_1")
    @Size(max = 250, message = "Instrução 1 deverá ter entre 250 caracteres.")
    private String instrucao1;

    @Column(name = "INSTRUCAO_2")
    @Size(max = 250, message = "Instrução 2 deverá ter entre 250 caracteres.")
    private String instrucao2;

    @Column(name = "INSTRUCAO_3")
    @Size(max = 250, message = "Instrução 3 deverá ter entre 250 caracteres.")
    private String instrucao3;

    @Column(name = "INSTRUCAO_4")
    @Size(max = 250, message = "Instrução 4 deverá ter entre 250 caracteres.")
    private String instrucao4;

    @Column(name = "INSTRUCAO_5")
    @Size(max = 250, message = "Instrução 5 deverá ter entre 250 caracteres.")
    private String instrucao5;

    @Column(name = "INSTRUCAO_6")
    @Size(max = 250, message = "Instrução 6 deverá ter entre 250 caracteres.")
    private String instrucao6;

    @Column(name = "INSTRUCAO_7")
    @Size(max = 250, message = "Instrução 7 deverá ter entre 250 caracteres.")
    private String instrucao7;

    @Column(name = "INSTRUCAO_8")
    @Size(max = 250, message = "Instrução 8 deverá ter entre 250 caracteres.")
    private String instrucao8;

    @NotNull
    @Column(name = "PERIODO_ALMOCO")
    private Integer periodoAlmoco;

    @NotNull
    @Column(name = "PERIODO_ALMOCO_ESTAGIARIO")
    private Integer periodoAlmocoEstagiario;

    @NotNull
    @Column(name = "HORA_INICIO_JORNADA")
    private String horaInicioJornada;

    @NotNull
    @Column(name = "HORA_FIM_JORNADA")
    private String horaFimJornada;

    @NotNull
    @Column(name = "PERIODO_ALMOCO_MENOR_APRENDIZ")
    private Integer periodoAlmocoMenorAprendiz;

    @Column(name = "PERIODO_ALMOCO_GESTAO_PESSOA")
    private Integer periodoAlmocoGestaoPessoa;

    @NotNull(message = "Informe o período da jornada de trabalho diário do funcionário")
    @Column(name = "HORA_TRABALHADA_FUNCIONARIO")
    private String horaTrabalhadaFuncionario;

    @NotNull(message = "Informe o período da jornada de trabalho diário do estagiário")
    @Column(name = "HORA_TRABALHADA_ESTAGIARIO")
    private String horaTrabalhadaEstagiario;

    @NotNull(message = "Informe o período da jornada de trabalho diário do funcionário")
    @Column(name = "HORA_TRABALHADA_MENOR_APRENDIZ")
    private String horaTrabalhadaMenorAprendiz;

    @Column(name = "GERA_CONTA_UNICA_EM_ORCAMENTO")
    private String geraContaUnicaEmOrcamento;

    @Column(name = "STATUS_OS_GERADA_POR_ORCAMENTO")
    private String statusOSGeradaPorOrcamento;

    @Column(name = "NFCE_TOKEN")
    private String nfceToken;

    @Column(name = "NFCE_CSC")
    private String nfceCsc;

    @Column(name = "NFE_MS_TOKEN")
    private String nfeMsToken;

    @JoinColumn(name = "ID_PLANO_CONTA_PADRAO_PDV", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaPadraoPdv;

    @JoinColumn(name = "ID_CENTRO_CUSTO_PADRAO_PDV", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto idCentroCustoPadraoPdv;

    @JoinColumn(name = "ID_CONTA_BANCARIA_PADRAO_PDV", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancariaPadraoPdv;

    @JoinColumn(name = "ID_PLANO_CONTA_VENDA_PADRAO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaVendaPadrao;

    @JoinColumn(name = "ID_PLANO_CONTA_COMPRA_PADRAO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaCompraPadrao;

    @Column(name = "OBSERVACAO_PADRAO_PDV")
    private String observacaoPadraoPdv;

    @Column(name = "OBSERVACAO_IMPRESSAO_ORCAMENTO")
    private String observacaoImpressaoOrcamento;

    @Column(name = "TITULO_RECIBO")
    private String tituloRecibo;

    @Column(name = "NUMERO_RECIBO")
    private Integer numeroRecibo;

    @JoinColumn(name = "APP_PLANO_CONTA_PADRAO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta appPlanoContaPadrao;

    @JoinColumn(name = "APP_CONTA_BANCARIA_PADRAO", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria appContaBancariaPadrao;

    @NotNull(message = "Informe ao menos um tipo produto para venda")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idParametroSistema", orphanRemoval = true)
    private List<TipoProdutoUso> tipoProdutoUsoList = new LinkedList<>();

    @Column(name = "PERMITE_VENDA_SEM_ESTOQUE_PRODUTOS_NORMAIS")
    private String permiteVendaSemEstoqueProdutosNormais;

    @Column(name = "PERMITE_VENDA_SEM_ESTOQUE_PRODUTOS_COMPOSTOS")
    private String permiteVendaSemEstoqueProdutosCompostos;

    @Column(name = "HABILITA_AGENDA")
    private String habilitaAgenda;

    @Column(name = "ENVIA_SMS_CLIENTE_AGENDAMENTO")
    private String enviaSmsClienteAgendamento;

    @Column(name = "ENVIA_SMS_CLIENTE_ALTERACAO_AGENDAMENTO")
    private String enviaSmsClienteAlteracaoAgendamento;

    @Column(name = "ENVIA_SMS_CLIENTE_UM_DIA_ANTES")
    private String enviaSmsClienteUmDiaAntes;

    @Column(name = "ENVIA_SMS_FUNCIONARIO_CONFIRMACAO")
    private String enviaSmsFuncionarioConfirmacao;

    @Column(name = "ENVIA_SMS_EMPRESA_SOLICITACAO")
    private String enviaSmsEmpresaSolicitacao;

    @Column(name = "TIPO_CONFIRMACAO_AGENDAMENTO")
    private String tipoConfirmacaoAgendamento;

    @Column(name = "BUSCA_MVA")
    private String buscaMva;

    @Column(name = "CELULAR_ENVIO_SMS")
    private String celularEnvioSms;

    @Column(name = "TOKEN_NFS")
    private String tokenNFS;

    @Column(name = "AGENDA_INTERVALO_HORARIO")
    private Integer agendaIntervaloHorario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idParametroSistema", orphanRemoval = true)
    private List<RecorrenciaAgendamento> recorrenciaAgendamentoList = new LinkedList<>();

    @Column(name = "ENVIA_NOTIFICACAO_CONTAS")
    private String enviaNotificacaoContas;

    @Column(name = "CRIAR_USUARIO_PARA_CLIENTE_CADASTRADO")
    private String criarUsuarioParaClienteCadastrado;

    @Column(name = "NOTIFICACAO_SMS_ENVIAR")
    private String notificacaoSmsEnviar;

    @Column(name = "NOTIFICACAO_SMS_NUMEROS")
    private String notificacaoSmsNumeros;

    @Column(name = "PRAZO_NOTIFICACAO")
    private Integer prazoNotificacao;

    @Transient
    @JsonIgnore
    private Boolean needToUpdateNFCeInfo = false;

    @JsonIgnore
    public List<TipoProdutoUso> getTipoProdutoCompraList() {
        return tipoProdutoUsoList.stream()
                .filter(item -> item.getTipoUso().equals("C"))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<TipoProdutoUso> getTipoProdutoVendaList() {
        return tipoProdutoUsoList.stream()
                .filter(item -> item.getTipoUso().equals("V"))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public EnumStatusOrdemDeServico getStatusNovaOS() {
        return EnumStatusOrdemDeServico.retornaEnumSelecionado(statusOSGeradaPorOrcamento);
    }

    @JsonIgnore
    public void setStatusNovaOS(EnumStatusOrdemDeServico e) {
        statusOSGeradaPorOrcamento = e.getCodigo();
    }

    @JsonIgnore
    public EnumTipoConfirmacaoAgendamento getTipoConfirmacao() {
        return EnumTipoConfirmacaoAgendamento.retornaEnumSelecionado(tipoConfirmacaoAgendamento);
    }

    @JsonIgnore
    public void setTipoConfirmacao(EnumTipoConfirmacaoAgendamento e) {
        tipoConfirmacaoAgendamento = e.getTipo();
    }

    public ParametroSistema incrementNumeroRecibo() {
        ++numeroRecibo;
        return this;
    }

    @Override
    public String toString() {
        return "ParametroSistema{" + "id=" + id + '}';
    }

}
