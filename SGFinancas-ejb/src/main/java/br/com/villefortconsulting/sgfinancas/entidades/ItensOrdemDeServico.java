package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "ITENS_ORDEM_DE_SERVICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ItensOrdemDeServico extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    @Column(name = "NOME_ITEM")
    private String nomeItem;

    @Size(max = 255)
    @Column(name = "OBSERVACAO")
    private String observacao;

    @JoinColumn(name = "ID_ORDEM_DE_SERVICO", referencedColumnName = "ID")
    @ManyToOne
    private OrdemDeServico idOrdemDeServico;

    @Transient
    private String controle;

    public ItensOrdemDeServico(OrdemDeServico idOrdemDeServico, String nomeItem, String observacao) {
        this.idOrdemDeServico = idOrdemDeServico;
        this.nomeItem = nomeItem;
        this.observacao = observacao;
    }

    public ItensOrdemDeServico(Integer id) {
        this.id = id;
    }

}
