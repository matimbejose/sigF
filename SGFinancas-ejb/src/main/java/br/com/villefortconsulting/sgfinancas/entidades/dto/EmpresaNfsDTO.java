package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.basic.BasicEmpresa;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaNfsDTO extends BasicEmpresa {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String nomeFantasia;

    private String cnpj;

    private String endereco;

    private String bairro;

    private String senhaCertificado;

    private String cep;

    private String responsavel;

    private String fone;

    private String celular;

    private String email;

    private String ativo;

    private String tipo;

    private String tipoConta;

    private String regimeTributario;

    private String inscricaoEstadual;

    private String indicadorInscricaoEstadual;

    private String inscricaoMunicipal;

    private String nire;

    private String numRegistroCartorio;

    private String numeroEndereco;

    private String complementoEndereco;

    private Double faturamento;

    private Date dataConstituicao;

    private Date dataCredenciamento;

    private String tipoCertificadoDigital;

    private String acessoPrivado;

    private String nomeCertificadoDigital;

    private String podePedirAdiantamento;

    private String telefone;

    private Integer codigoIBGE;

    private String descricaoUfCidade;

    private String codigoIBGECidade;

    private String ambiente;

    private String token;

    @Override
    public boolean hasCidade() {
        return true;
    }

}
