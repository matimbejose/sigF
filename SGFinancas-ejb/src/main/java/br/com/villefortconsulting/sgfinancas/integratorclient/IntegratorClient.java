package br.com.villefortconsulting.sgfinancas.integratorclient;

import br.com.villefortconsulting.sgfinancas.integratorclient.dto.AccountDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.AccountResponseDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.BalanceDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.CashoutRequestDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.TransferBetweenHoldersDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.TransferResponseRequestDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.dto.UsuarioResponsavelDTO;
import br.com.villefortconsulting.sgfinancas.integratorclient.exception.ContaDigitalException;
import br.com.villefortconsulting.sgfinancas.integratorclient.exception.CriarContaDigitalException;
import static br.com.villefortconsulting.util.MicroServiceUtil.MicroServicos.INTEGRATOR;
import static br.com.villefortconsulting.util.MicroServiceUtil.getUrlForEnviroment;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class IntegratorClient {

    public static final String URI_ACCOUNT = "account";

    public static final String URI_REQUEST_TRANSFERENCIA = "transfer-operation/request-between-holders";

    public static final String URI_CONFIRMACAO_TRANSFERENCIA = "transfer-operation/execute-between-holders";

    public static final String URI_ACCOUNT_CHANGE_PHONE = "account/change-phone";

    public static final String URI_BUSCA_SALDO = "partner/balance";

    public static final String URI_ACCOUNT_USER = "account/user";

    private static final String CPF_OWNER_VILLEFORT = "570.994.480-72";

    private RestTemplate restTemplate = new RestTemplate();

    public AccountResponseDTO createAccount(AccountDTO accountDTO) throws IOException, CriarContaDigitalException {

        HttpEntity<?> entity = buildHeaderEntity(accountDTO, new HttpHeaders());
        try {
            ObjectMapper mapper = new ObjectMapper();

            ResponseEntity<String> response = restTemplate.exchange(
                    getUrlForEnviroment(INTEGRATOR, URI_ACCOUNT),
                    HttpMethod.POST, entity, String.class, new HashMap<>());

            return mapper.readValue(response.getBody(), AccountResponseDTO.class);

        } catch (HttpClientErrorException ex) {
            throw new CriarContaDigitalException(ex);
        } catch (Exception e) {
            throw new CriarContaDigitalException(new HttpClientErrorException(HttpStatus.OK));
        }
    }

    public CashoutRequestDTO createRequestTransferencia(TransferBetweenHoldersDTO contaDestino) throws IOException, ContaDigitalException {

        HttpEntity<?> entity = buildHeaderEntity(contaDestino, new HttpHeaders());
        try {
            return restTemplate.exchange(
                    getUrlForEnviroment(INTEGRATOR, URI_REQUEST_TRANSFERENCIA),
                    HttpMethod.POST, entity, CashoutRequestDTO.class, new HashMap<>()).getBody();

        } catch (HttpClientErrorException ex) {
            throw new ContaDigitalException(ex);
        }
    }

    public TransferResponseRequestDTO confirmarTransferencia(String sms) throws IOException, ContaDigitalException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("sms-code", sms);

        try {
            return restTemplate.exchange(
                    getUrlForEnviroment(INTEGRATOR, URI_CONFIRMACAO_TRANSFERENCIA),
                    HttpMethod.POST, buildHeaderEntity(null, headers), TransferResponseRequestDTO.class, new HashMap<>()).getBody();

        } catch (HttpClientErrorException ex) {
            throw new ContaDigitalException(ex);
        }

    }

    public String mudarTelefoneUserIntegrator(String cpf, String telefone) throws IOException, ContaDigitalException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("cpf-document", cpf);
        headers.set("mobile-phone", telefone);

        HttpEntity<?> entity = buildHeaderEntity(null, headers);

        try {
            return restTemplate.exchange(
                    getUrlForEnviroment(INTEGRATOR, URI_ACCOUNT_CHANGE_PHONE),
                    HttpMethod.PUT, entity, String.class, new HashMap<>()).getBody();

        } catch (HttpClientErrorException ex) {
            throw new ContaDigitalException(ex);
        }

    }

    public BalanceDTO getSaldoContaDigital() throws IOException, ContaDigitalException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("owner-cpf", CPF_OWNER_VILLEFORT);

        HttpEntity<?> entity = buildHeaderEntity(null, headers);
        try {
            return restTemplate.exchange(
                    getUrlForEnviroment(INTEGRATOR, URI_BUSCA_SALDO),
                    HttpMethod.GET, entity, BalanceDTO.class, new HashMap<>()).getBody();

        } catch (HttpClientErrorException ex) {
            throw new ContaDigitalException(ex);
        }
    }

    public UsuarioResponsavelDTO getUsuarioResponsavel(String cpf) throws IOException, ContaDigitalException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("owner-cpf", cpf);
        HttpEntity<?> entity = buildHeaderEntity(null, headers);

        try {
            return restTemplate.exchange(
                    getUrlForEnviroment(INTEGRATOR, URI_ACCOUNT_USER),
                    HttpMethod.GET, entity, UsuarioResponsavelDTO.class, new HashMap<>()).getBody();

        } catch (HttpClientErrorException ex) {
            throw new ContaDigitalException(ex);
        }

    }

    private static HttpEntity<?> buildHeaderEntity(Object dto, HttpHeaders headers) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);

        return new HttpEntity<>(dto, headers);
    }

}
