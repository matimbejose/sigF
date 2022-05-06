package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumFinalidadeNF;
import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportacaoMovimentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idIntegracao;

    private String nomeDestinatario;

    private String cpfCnpjDestinatario;

    private Date dataMovimentacao;

    private Double valor;

    private Double valorDesconto;

    private CentroCusto centroCusto;

    private EnumTipoClienteMovimentacao origem;

    private EnumFinalidadeNF finalidade;

    private String notaReferencia;

    private String motivoErro;

    private String observacao;
    
    @Builder.Default
    private TipoVinculoClienteEnum tipoVinculo = TipoVinculoClienteEnum.SEM_CLIENTE;

    private Cliente cliente;

    @Builder.Default
    private PlanoContaEnum tipoPlanoConta = PlanoContaEnum.OUTROS;

    private PlanoConta planoConta;

    @Builder.Default
    private Boolean importar = true;

    @Builder.Default
    private Boolean geraFinanceiro = false;

    @Getter
    @AllArgsConstructor
    public enum PlanoContaEnum {

        IUGU(false, cc -> cc.getIdPlanoContaIugu()),
        REDE(true, cc -> cc.getIdPlanoContaIugu()),
        DINHEIRO(true, cc -> cc.getIdPlanoContaIugu()),
        OUTROS(true, cc -> cc.getIdPlanoContaIugu());

        private final boolean geraFinanceiro;

        private final Function<CentroCusto, PlanoConta> getter;

    }

    @Getter
    @AllArgsConstructor
    public enum TipoVinculoClienteEnum {

        OK("fa fa-2x fa-check text-success", "Cliente vinculado", true),
        AJUSTADO("fa fa-2x fa-check text-warning", "Cliente ajustado", false),
        SEM_CLIENTE("fa fa-2x fa-times-circle text-danger", "Cliente não vinculado", false),
        CLIENTE_DIVERSO("fa fa-2x fa-exclamation-circle text-warning", "Importar em clientes diversos.", false),
        ERRO_LEITURA("fa fa-2x fa-file-excel-o text-danger", "Erro de leitura de arquivo", true);

        private final String icone;

        private final String titulo;

        private final boolean finalState;

    }

}
