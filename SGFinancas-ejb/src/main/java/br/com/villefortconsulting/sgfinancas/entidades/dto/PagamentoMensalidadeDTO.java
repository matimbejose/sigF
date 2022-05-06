package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoMensalidadeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double valorPago;

    private Date dataPagamento;

    private Date dataValidade;

    private List<PagamentoMensalidadeModuloDTO> modulos;

}
