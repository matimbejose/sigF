package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoMensalidadeModuloDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ModuloDTO modulo;

    private List<PermissaoDTO> permissoes;

}
