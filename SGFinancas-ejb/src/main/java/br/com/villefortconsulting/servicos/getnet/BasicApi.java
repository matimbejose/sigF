/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.servicos.getnet;

import br.com.villefortconsulting.util.MicroServiceUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class BasicApi implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Token token() throws IOException {
        byte[] acesso = Base64.getEncoder().encode(SystemPreferencesUtil.getPropOrThrow("sec.getnet.credencial", () -> new IllegalStateException("Os parâmetros gerais não estão configurados corretamente.")).getBytes(StandardCharsets.UTF_8));
        Map<String, String> headerListMap = new HashMap<>();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        headerListMap.put("Authorization", "Basic " + new String(acesso, StandardCharsets.UTF_8));

        SystemPreferencesUtil.getProps("sec.getnet.param").forEach((key, value) -> body.add(key.substring(key.lastIndexOf('.') + 1), value));

        ResponseEntity<String> response = MicroServiceUtil.doFormDataPost(MicroServiceUtil.MicroServicos.GETNET, "oauth/token", body, headerListMap);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.getBody(), Token.class);
    }

    public static Map getHeader() throws IOException {
        Map<String, String> header = new HashMap<>();

        Token token = token();

        header.put("Authorization", "Bearer " + token.getToken());

        return header;
    }

    public static ResponseApi convertResponse(ResponseEntity<String> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response.getBody(), ResponseApi.class);
        } catch (Exception e) {
            ErroApi erro = mapper.readValue(response.getBody(), ErroApi.class);
            throw new IOException(erro.getMessage());
        }
    }

}
