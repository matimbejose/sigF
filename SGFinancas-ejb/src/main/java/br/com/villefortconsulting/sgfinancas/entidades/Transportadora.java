package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
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
@Table(name = "TRANSPORTADORA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Transportadora extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Cidade idCidade;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 200, message = "Máximo de 200 caracteres para descrição")
    private String descricao;

    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private PlanoConta idPlanoConta;

    @NotNull(message = "Informe o CNPJ")
    @Size(max = 20, message = "Cnpj deverá ter entre 20 caracteres.")
    @Column(name = "CNPJ")
    private String cnpj;

    @Column(name = "INSCRICAO_MUNICIPAL")
    @Size(max = 50, message = "Inscrição municipal deverá ter entre 50 caracteres.")
    private String inscricaoMunicipal;

    @Column(name = "INSCRICAO_ESTADUAL")
    @Size(max = 50, message = "Inscrição estadual deverá ter entre 50 caracteres.")
    private String inscricaoEstadual;

    @Column(name = "ISENTO_ICMS")
    private String isentoIcms;

    @Column(name = "CEP")
    @Size(max = 20, message = "Cep deverá ter entre 20 caracteres.")
    private String cep;

    @Column(name = "ENDERECO")
    @Size(max = 200, message = "Endereço deverá ter entre 200 caracteres.")
    private String endereco;

    @Column(name = "NUMERO")
    @Size(max = 10, message = "Número deverá ter entre 10 caracteres.")
    private String numero;

    @Column(name = "BAIRRO")
    @Size(max = 50, message = "Bairro deverá ter entre 50 caracteres.")
    private String bairro;

    @Column(name = "COMPLEMENTO")
    @Size(max = 100, message = "Complemento deverá ter entre 100 caracteres.")
    private String complemento;

    @Column(name = "FONE_COMERCIAL")
    @Size(max = 50, message = "telefone comercial deverá ter entre 50 caracteres.")
    private String foneComercial;

    @Column(name = "EMAIL")
    @Size(max = 50, message = "Email deverá ter entre 50 caracteres.")
    private String email;

    @Column(name = "FONE_RESIDENCIAL")
    @Size(max = 50, message = "telefone residencial deverá ter entre 50 caracteres.")
    private String foneResidencial;

    @Column(name = "CONTATO")
    @Size(max = 50, message = "contato deverá ter entre 50 caracteres.")
    private String contato;

    @Column(name = "CELULAR")
    @Size(max = 50, message = "celular deverá ter entre 50 caracteres.")
    private String celular;

    @Column(name = "OBSERVACAO")
    @Size(max = 50, message = "Observação deverá ter entre 50 caracteres.")
    private String observacao;

    @Column(name = "VALOR_FRETE")
    private double valorFrete;

    @Column(name = "ATIVO")
    @Size(max = 1)
    private String ativo;

    @Override
    public String toString() {
        return "Transportadora{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
