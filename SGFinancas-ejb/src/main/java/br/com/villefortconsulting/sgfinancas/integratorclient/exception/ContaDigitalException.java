package br.com.villefortconsulting.sgfinancas.integratorclient.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.ejb.ApplicationException;
import org.springframework.web.client.HttpClientErrorException;

@ApplicationException(rollback = true)
public class ContaDigitalException extends Exception {

    private final transient List<String> messagens;

    private final ObjectMapper mapper = new ObjectMapper();

    public ContaDigitalException(HttpClientErrorException ex) throws IOException {
        ApiError apiError = mapper.readValue(new String(ex.getResponseBodyAsByteArray(), StandardCharsets.UTF_8), ApiError.class);
        this.messagens = apiError.getMessages();
    }

    public List<String> getMessagens() {
        return messagens;
    }

}
