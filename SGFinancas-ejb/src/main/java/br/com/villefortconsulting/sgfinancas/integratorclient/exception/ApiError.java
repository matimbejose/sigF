package br.com.villefortconsulting.sgfinancas.integratorclient.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private int code;

    private HttpStatus status;

    private String exception;

    private String key;

    private List<String> messages;

}
