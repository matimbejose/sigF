package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private ClienteDTO cliente;

    private MarcaDTO marca;

    private ModeloDTO modelo;

    private Integer anoFabricacao;

    private Integer anoModelo;

    private DescricaoDTO combustivel;

    private String placa;

    private DescricaoDTO corVeiculo;

    private String renavam;

    private String chassi;

    private String cambio;

    private Integer portas;

    private Integer numeroPassageiros;

    private Double valorProtegido;

    private String ativo;

}
