package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MSEmailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String assunto;

    private String mensagem;

    private String remetente;

    private List<DestinatarioDTO> cc;

    private List<DestinatarioDTO> cco;

    private Map<String, File> anexos = new HashMap<>();

    private Map<String, File> anexosInline = new HashMap<>();

    private boolean forcarEnvio = false;

    private String authUsername;

}
