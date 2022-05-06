package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONTABILIDADE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Contabilidade extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "Descrição deve possuir no máximo 200 caracteres")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe o nome fantasia")
    @Size(max = 300, message = "Nome fantasia deve possuir no máximo 300 caracteres")
    @Column(name = "NOME_FANTASIA")
    private String nomeFantasia;

    @Size(max = 200, message = "CNPJ deve possuir no máximo 20 caracteres")
    @Column(name = "CNPJ")
    private String cnpj;

    @Size(max = 300, message = "Endereço deve possuir no máximo 300 caracteres")
    @Column(name = "ENDERECO")
    private String endereco;

    @Size(max = 100, message = "Bairro deve possuir no máximo 100 caracteres")
    @Column(name = "BAIRRO")
    private String bairro;

    @Size(max = 10, message = "CEP deve possuir no máximo 10 caracteres")
    @Column(name = "CEP")
    private String cep;

    @Size(max = 100, message = "Responsável deve possuir no máximo 100 caracteres")
    @Column(name = "RESPONSAVEL")
    private String responsavel;

    @Size(max = 100, message = "Telefone deve possuir no máximo 100 caracteres")
    @Column(name = "FONE")
    private String fone;

    @Size(max = 100, message = "Email deve possuir no máximo 100 caracteres")
    @Column(name = "EMAIL")
    private String email;

    @NotNull(message = "Informe se a prefeitura está ativa")
    @Size(max = 1, message = "Ativo deve possuir no máximo 1 caracter")
    @Column(name = "ATIVO")
    private String ativo;

    @Size(max = 50, message = "Inscrição estadual deve possuir no máximo 50 caracter")
    @Column(name = "INSCRICAO_ESTADUAL")
    private String inscricaoEstadual;

    @NotNull(message = "Informe se é insento ou não")
    @Size(max = 2, message = "Indicador inscrição estadual deve possuir no máximo 2 caracteres")
    @Column(name = "INDICADOR_INSCRICAO_ESTADUAL")
    private String indicadorInscricaoEstadual;

    @Column(name = "INSCRICAO_MUNICIPAL")
    @Size(max = 50, message = "Inscrição municipal deve possuir no máximo 50 caracteres")
    private String inscricaoMunicipal;

    @Size(max = 10, message = "Número do endereço deve possuir no máximo 10 caracteres")
    @Column(name = "NUMERO_ENDERECO")
    private String numeroEndereco;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Cidade idCidade;

    @JoinColumn(name = "ID_LAYOUT", referencedColumnName = "ID")
    @ManyToOne
    private Layout idLayout;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private Documento idDocumento;

    @Lob
    @Column(name = "LOGO")
    private byte[] logo;

    public Contabilidade() {
        this.ativo = "S";
    }

    @Override
    public String toString() {
        return "Contabilidade{" + "id=" + id + '}';
    }

}
