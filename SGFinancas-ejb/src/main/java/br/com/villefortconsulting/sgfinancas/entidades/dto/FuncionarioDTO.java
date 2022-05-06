package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.enums.EnumTipoDadoImportacao;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "funcionário")
public class FuncionarioDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String ativo;

    private PlanoContaDTO planoConta;

    @Importavel(nome = "Nome", obrigatorio = true)
    private String nome;

    @Importavel(nome = "Cargo")
    private String cargo;

    private String nomeMae;

    @Importavel(nome = "CPF", obrigatorio = true)
    private String cpf;

    private String senha;

    @Importavel(nome = "Email", obrigatorio = true)
    private String email;

    private String matricula;

    @Importavel(nome = "É vendedor/orçamentista?", obrigatorio = true, tipo = EnumTipoDadoImportacao.ENUM,
            opcoes = {"Sim", "Não"}, padrao = "Não")
    private String ehOrcamentista;

    @Importavel(nome = "Data de nascimento", obrigatorio = true, tipo = EnumTipoDadoImportacao.DATE)
    private Date dataNascimento;

    private Date dataDemissao;

    @Importavel(nome = "Data de contratação", obrigatorio = true, tipo = EnumTipoDadoImportacao.DATE)
    private Date dataContratacao;

    @Importavel(nome = "Tipo de contratação", obrigatorio = true, tipo = EnumTipoDadoImportacao.ENUM,
            opcoes = {"Funcionário", "Estágiario", "Contratado", "Demitido", "Menor aprendiz", "RH"}, padrao = "Funcionário")
    private String tipoContratacao;

    @Importavel(nome = "Salário base", tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double salarioBase;

    private String telefoneResidencial;

    private String telefoneCelular;

    private String cep;

    private String numero;

    private String endereco;

    private String bairro;

    private String complemento;

    private String temHorarioEspecial;

    @Importavel(nome = "Hora especial inicio")
    private String horaEspecial;

    @Importavel(nome = "Hora especial fim")
    private String horaEspecialFinal;

    private byte[] foto;

    private Double limiteDeDesconto;

    private Integer codIBGE;

    private String cidade;

    private String estado;

    private List<ServicoDTO> funcionarioServicoList;

    private List<FuncionarioAtendimentoDTO> funcionarioAtendimentoList;

    public FuncionarioDTO(Integer id) {
        this.id = id;
    }

}
