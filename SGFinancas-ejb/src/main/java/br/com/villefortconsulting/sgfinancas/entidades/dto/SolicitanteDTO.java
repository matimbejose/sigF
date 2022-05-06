package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitanteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer numero;

    private String nome;

    private String indicacao;

    private String status;

    private String origem;

    private String email;

    private String telefone;

    private String empresa;

    private String acompanhamento;

    private String cpfCnpj;

    private String areaAtuacao;

    private String cargo;

    private String cidade;

    private String uf;

    private String nomeIndicador;

    private String turmaPessoaIndicadora;

    public SolicitanteDTO(Integer numero, String nome) {
        this.numero = numero;
        this.nome = nome;
    }

    public SolicitanteDTO(Integer numero, String nome, String empresa, String cargo, String areaAtuacao) {
        this.numero = numero;
        this.nome = nome;
        this.empresa = empresa;
        this.cargo = cargo;
        this.areaAtuacao = areaAtuacao;
    }

    public SolicitanteDTO(Integer numero, String nome, String status, String origem, String email, String telefone, String cpfCnpj, String empresa, String cargo, String areaAtuacao, String cidade, String uf, String indicacao) {
        this.numero = numero;
        this.nome = nome;
        this.status = status;
        this.origem = origem;
        this.email = email;
        this.telefone = telefone;
        this.cpfCnpj = cpfCnpj;
        this.empresa = empresa;
        this.cargo = cargo;
        this.areaAtuacao = areaAtuacao;
        this.cidade = cidade;
        this.uf = uf;
        this.indicacao = indicacao;
    }

}
