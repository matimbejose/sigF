package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoContaCadastroDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String codigoOrigem;

    private String descricao;

    private String registroPadrao;

    private String tipo;

    private String tipoIndicador;

    private String tipoBalanco;

    private String codigo;

    private String podeTerFilho;

    private String bloqueiaFilhoContaReceber;

    private String bloqueiaFilhoContaPagar;

    private PlanoConta idContaPai;

    private PlanoConta idContaContrapartida;

    private String mostraTelaInicial;

    private String codigoContabilidade;

    private String codigoPai;

    private String planoAntigo;

    public PlanoContaCadastroDTO(String codigoOrigem) {
        this.codigoOrigem = codigoOrigem;
        registroPadrao = "N";
        tipoIndicador = "S";
        mostraTelaInicial = "N";
        podeTerFilho = "N";
    }

    public void setIdContaPai(PlanoConta contaPai) {
        idContaPai = contaPai;
        tipo = contaPai.getTipo();
        tipoBalanco = contaPai.getTipoBalanco();
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
        if (codigo != null) {
            this.codigo = StringUtil.adicionarCaracterEsquerda(codigo, "0", 3);
        }
    }

}
