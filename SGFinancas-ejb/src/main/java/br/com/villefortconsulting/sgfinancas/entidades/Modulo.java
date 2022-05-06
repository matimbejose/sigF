package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "MODULO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class Modulo extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o nome do modulo")
    @Size(max = 100, message = "O campo deverá estar entre 1 e 200 caracteres")
    @Column(name = "NOME")
    private String nome;

    @Size(max = 200, message = "O campo deverá estar entre 1 e 200 caracteres")
    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "VALOR_ADESAO")
    private Double valorAdesao;

    @Column(name = "VALOR_MENSALIDADE")
    private Double valorMensalidade;

    @Column(name = "ATIVO")
    private String ativo = "S";

    @Column(name = "PERMITE_RENOVACAO")
    private String permiteRenovacao = "S";

    @Column(name = "DESCONTINUADO")
    private String descontinuado = "N";

    @Column(name = "OBRIGATORIO")
    private String obrigatorio = "N";

    @Column(name = "FLAG")
    private String flag;

    @Transient
    private boolean adquirido = false;

    @Transient
    private int foo = 0;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idModulo", orphanRemoval = true)
    private List<ModuloPermissao> moduloPermissaoList = new LinkedList<>();

    public Modulo(Integer id) {
        this.id = id;
    }

    public void addPermissao(Permissao permissao) {
        ModuloPermissao moduloPermissao = new ModuloPermissao();
        moduloPermissao.setIdModulo(this);
        moduloPermissao.setIdPermissao(permissao);
        moduloPermissao.setNova(true);

        if (!moduloPermissaoList.contains(moduloPermissao)) {
            moduloPermissaoList.add(moduloPermissao);
        }
    }

    public void removePermissao(ModuloPermissao moduloPermissao) {
        moduloPermissaoList.remove(moduloPermissao);
    }

    public String getStyleClass() {
        StringBuilder sb = new StringBuilder();
        if ("S".equals(obrigatorio)) {
            sb.append("obrigatorio ");
        }
        if (adquirido) {
            sb.append("adquirido ");
        }
        return sb.toString();
    }

    public Double getValorFinal(Double qteDiasRestantes) {
        Double mensalidade = (valorMensalidade / 30) * qteDiasRestantes;
        if (adquirido) {
            return mensalidade;
        }
        return valorAdesao + mensalidade;
    }

}
