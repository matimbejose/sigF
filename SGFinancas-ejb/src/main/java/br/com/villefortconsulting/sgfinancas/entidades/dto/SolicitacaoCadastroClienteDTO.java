package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCadastroClienteDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private ClienteDTO cliente;

    private String nome;

    private String cpfCnpj;

    private String cnh;

    private String categoriaCnh;

    private String celular;

    private String email;

    private Integer codIbgeCidade;

    private String cep;

    private String endereco;

    private String numero;

    private String bairro;

    private String complemento;

    private String tenantID;

    private List<SolicitacaoCadastroClienteVeiculoDTO> veiculos;

    public boolean isAnexosOk() {
        return veiculos != null && !veiculos.isEmpty() && veiculos.stream().allMatch(SolicitacaoCadastroClienteVeiculoDTO::isAnexosOk);
    }

}
