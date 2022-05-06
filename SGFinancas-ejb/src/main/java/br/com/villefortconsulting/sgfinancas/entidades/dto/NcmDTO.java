package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NcmDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Ncm capitulo;

    private Ncm posicao;

    private Ncm subposicaoUm;

    private Ncm subposicaoDois;

    private Ncm item;

    private Ncm subitem;

    private List<Ncm> posicoes;

    private List<Ncm> subposicoesUm;

    private List<Ncm> subposicoesDois;

    private List<Ncm> itens;

    private List<Ncm> subitens;

}
