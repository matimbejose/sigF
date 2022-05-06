package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Combustivel;
import br.com.villefortconsulting.sgfinancas.entidades.CorVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoCadastroDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private DadosVeiculo dadosVeiculo;

    private TipoDescricao tipo;

    public void setTipo(String nome) {
        tipo = TipoDescricao.valueOf(nome.toUpperCase());
    }

    public enum TipoDescricao {
        COMBUSTIVEL, COR, VEICULO

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class DadosVeiculo implements Serializable {

        private static final long serialVersionUID = 1L;

        private Cliente cliente;

        private Marca marca;

        private Modelo modelo;

        private Integer anoFabricacao;

        private Integer anoModelo;

        private Combustivel combustivel;

        private String placa;

        private CorVeiculo corVeiculo;

        private String renavam;

        private String chassi;

        private Double valorProtegido;

        private Integer portas;

        private Integer numeroPassageiros;

        private String cambio;

        private String tipo;

    }

}
