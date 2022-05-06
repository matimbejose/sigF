package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "SOLICITACAO_CADASTRO_CLIENTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class SolicitacaoCadastroCliente extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne
    private Cliente idCliente;

    @Size(max = 60)
    @Column(name = "NOME")
    private String nome;

    @Size(max = 20)
    @Column(name = "CPFCNPJ")
    private String cpfCnpj;

    @Size(max = 20)
    @Column(name = "CNH")
    private String cnh;

    @Size(max = 5)
    @Column(name = "CATEGORIA_CNH")
    private String categoriaCnh;

    @Size(max = 20)
    @Column(name = "CELULAR")
    private String celular;

    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;

    @Embedded
    private Endereco endereco = new Endereco();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSolicitacaoCadastroCliente", orphanRemoval = true)
    private List<SolicitacaoCadastroClienteVeiculo> solicitacaoCadastroClienteVeiculoList = new ArrayList<>();

}
