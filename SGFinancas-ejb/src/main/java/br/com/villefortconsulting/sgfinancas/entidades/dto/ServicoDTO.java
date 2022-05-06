package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.Time;
import br.com.villefortconsulting.enums.EnumTipoDadoImportacao;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "Serviço")
public class ServicoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Nome", obrigatorio = true)
    private String descricao;

    @Importavel(nome = "Valor", obrigatorio = true, tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double valor;

    @Importavel(nome = "Custo", tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double custo;

    private Double pontos;

    private Integer categoria;

    @Importavel(nome = "Categoria", obrigatorio = true, tipo = EnumTipoDadoImportacao.ID_TABELA, padrao = "Importação")
    private String categoriaImportacao;

    private Integer planoDeContas;

    private DocumentoDTO fotos;

    private Map<Integer, Double> servicoProdutoMap;

    private List<Integer> funcionarioServicoList;

    private String permiteSelecaoProfissional;

    private Date tempoExecucao;

    private Integer recorrencia;

    public void setTempoExecucao(Date tempoExecucao) {
        this.tempoExecucao = tempoExecucao;
    }

    public void setTempoExecucao(String tempoExecucaoStr) {
        if (tempoExecucaoStr == null || tempoExecucaoStr.isEmpty()) {
            this.tempoExecucao = null;
            return;
        }
        if (tempoExecucaoStr.split(":").length == 2) {
            tempoExecucaoStr += ":00";
        }
        this.tempoExecucao = new Time(tempoExecucaoStr).toDate();
    }

    public String getTempoExecucao() {
        if (tempoExecucao == null) {
            return "";
        }
        return new Time(tempoExecucao).toString();
    }

    public Date getTempoExecucaoAsDate() {
        return tempoExecucao;
    }

}
