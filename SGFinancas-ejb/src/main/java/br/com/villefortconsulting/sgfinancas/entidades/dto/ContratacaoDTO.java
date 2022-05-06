package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> modulos;

    private String tipoPagamentoSistema;

    private PagamentoSistemaDTO dadosPagamento;

}
