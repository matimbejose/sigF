/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.sgfinancas.entidades.dto.SmsDTO;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.util.MicroServiceUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class SmsService {

    public void send(SmsDTO sms) {
        try {
            sms.setNumero(StringUtil.removerCaracteresTelefone(sms.getNumero()));
            MicroServiceUtil.doJsonPost(MicroServiceUtil.MicroServicos.SMS, "/public/v1/envio", sms, new HashMap<>());
        } catch (Exception e) {
            Logger.getLogger(SmsService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void send(String mensagem, String numero) {
        send(new SmsDTO(mensagem, numero));
    }

    public void enviarLinkPagamento(String numero, String idTransacao) throws UnsupportedEncodingException {
        StringBuilder mensagem = new StringBuilder();
        String id = URLEncoder.encode(idTransacao, StandardCharsets.UTF_8.toString());
        mensagem.append("Link de pagamento SGFinancas \n")
                .append(SystemPreferencesUtil.getPropOrThrow("defaults.linkPagamento", () -> {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "O link de pagamento não foi configurado.");
                    return new CadastroException("O link para pagamento não pôde ser criado. Favor entrar em contato com o suporte.", null);
                }))
                .append(id);

        send(mensagem.toString(), numero);
    }

}
