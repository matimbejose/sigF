/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.servicos.getnet;

import br.com.villefortconsulting.sgfinancas.entidades.dto.CallbackDebitoDTO;
import br.com.villefortconsulting.util.MicroServiceUtil;
import java.io.IOException;

public class GetnetApi extends BasicApi {

    private static final long serialVersionUID = 1L;

    public ResponseApi paymentCredit(RequestApi request) throws IOException {
        return convertResponse(MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.GETNET, "v1/public/credito/pagamento", request, getHeader()));
    }

    public ResponseApi paymentDebit(RequestApi request) throws IOException {
        return convertResponse(MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.GETNET, "v1/public/debito/pagamento", request, getHeader()));
    }

    public ResponseApi paymentBoleto(RequestApi request) throws IOException {
        return convertResponse(MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.GETNET, "v1/public/boleto/pagamento", request, getHeader()));
    }

    public ResponseApi finshDebit(CallbackDebitoDTO request) throws IOException {
        return convertResponse(MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.GETNET, "v1/public/debito/finalizar", request, getHeader()));
    }

    public ResponseApi estorno(String idPayment) throws IOException {
        return convertResponse(MicroServiceUtil.doGet(MicroServiceUtil.MicroServicos.GETNET, "v1/public/credito/estonar/" + idPayment));
    }

}
