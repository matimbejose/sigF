package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaSeguradoraDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private ClienteDTO clienteSeguradora;

    private Double valorFranquia;

    private String numeroSinistro;

}
