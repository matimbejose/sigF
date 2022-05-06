package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
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
@Table(name = "ACESSO_RAPIDO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class AcessoRapido extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 50, message = "Máximo de 50 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe o ícone")
    @Column(name = "ICONE")
    @Size(max = 50, message = "Máximo de 50 caracteres para o ícone")
    private String icone;

    @NotNull(message = "Informe o link")
    @Column(name = "LINK")
    @Size(max = 100, message = "Máximo de 100 caracteres para o link")
    private String link;

    @Column(name = "TOOLTIP")
    @Size(max = 250, message = "Máximo de 250 caracteres para o tooltip")
    private String tooltip;

    @Column(name = "PERMISSAO")
    private String permissao;

    @Override
    public String toString() {
        return "AcessoRapido{" + "id=" + id + '}';
    }

}
