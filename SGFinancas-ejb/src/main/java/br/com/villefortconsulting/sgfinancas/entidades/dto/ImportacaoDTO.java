package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.enums.EnumTipoDadoImportacao;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImportacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String nomeCampo;

    private EnumTipoDadoImportacao tipo;

    private boolean obrigatorio;

    private String[] opcoes;

    private String padrao;

    private List<ImportacaoDTO> campos;

}
