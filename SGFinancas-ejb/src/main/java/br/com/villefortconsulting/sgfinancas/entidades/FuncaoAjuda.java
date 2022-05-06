package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "FUNCAO_AJUDA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class FuncaoAjuda extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "CHAVE")
    private String chave;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Override
    public String toString() {
        return "FuncaoAjuda{" + "id=" + id + '}';
    }

}
