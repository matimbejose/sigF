package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBoletoMicroServico {

    private static final long serialVersionUID = 1L;

    private String idBoleto;

    private String codigoBanco;

    private String codigoBarra;

    private String linhaDigitavel;

    private Date dataVenciamento;

    private String situacao;

    private String urlBoleto;

}
