package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class FormularioRespostaDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private FormularioDTO formulario;

    private VeiculoDTO clienteVeiculo;

    private DocumentoDTO documento;

    private Date dataResposta;

    private List<FormularioRespostaItemSecaoDTO> listFormularioRespostaItemSecao;

}
