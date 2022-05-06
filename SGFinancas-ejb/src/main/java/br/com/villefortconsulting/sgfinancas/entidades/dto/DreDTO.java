package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DreDTO extends ValorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String conta;

    private String tipo;

    private String cor;

    private String codigo;

    private String codigoOrdenacao;

    private Integer index;

    private List<DreDTO> children = new ArrayList<>();

    public DreDTO(String conta) {
        this.conta = conta;
        children = new ArrayList<>();
    }

    public boolean canDelete() {
        return !(children.isEmpty() ? hasValue() : children.stream().noneMatch(DreDTO::hasValue));
    }

}
