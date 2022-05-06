package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportadoraDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private ContatoDTO contato;

    private String descricao;

    private PlanoContaDTO planoConta;

    private String cnpj;

    private String inscricaoMunicipal;

    private String inscricaoEstadual;

    private String isentoIcms;

    private String observacao;

    private Double valorFrete;

}
