package br.com.villefortconsulting.sgfinancas.entidades.embeddable;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Cidade idCidade;

    @Column(name = "CEP")
    @Size(max = 15, message = "Máximo de 15 caracteres para o cep")
    private String cep;

    @Column(name = "ENDERECO")
    @Size(max = 200, message = "Máximo de 200 caracteres para o endereço")
    private String endereco;

    @Column(name = "NUMERO")
    @Size(max = 10, message = "Máximo de 50 caracteres para o número")
    private String numero;

    @Column(name = "BAIRRO")
    @Size(max = 50, message = "Máximo de 200 caracteres para o bairro")
    private String bairro;

    @Column(name = "COMPLEMENTO")
    @Size(max = 100, message = "Máximo de 200 caracteres para o complemento")
    private String complemento;

    public String getEnderecoCompleto() {
        if (StringUtils.isEmpty(endereco)) {
            return "";
        }
        String end = endereco;
        if (StringUtils.isNotEmpty(numero)) {
            end += ", " + numero;
        }
        if (StringUtils.isNotEmpty(complemento)) {
            end += " - " + complemento;
        }
        if (StringUtils.isNotEmpty(bairro)) {
            end += ", " + bairro;
        }
        if (idCidade != null) {
            end += ", " + idCidade.getDescricao() + " - " + idCidade.getIdUF().getDescricao();
        }
        return end;
    }

    public boolean isBlank() {
        return idCidade == null
                || cep == null || "".equals(cep)
                || endereco == null || "".equals(endereco)
                || numero == null || "".equals(numero)
                || bairro == null || "".equals(bairro)
                || complemento == null;
    }

}
