package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.util.StringUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ITEM_SECAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class ItemSecao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 200, message = "Máximo de 200 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe a situação da avaría")
    @Column(name = "ATIVO")
    @Size(max = 1)
    private String ativo;

    @JoinColumn(name = "ID_SECAO", referencedColumnName = "ID")
    @ManyToOne
    private Secao idSecao;

    @JoinColumn(name = "ID_ITEM_SECAO", referencedColumnName = "ID")
    @ManyToOne
    private ItemSecao idItemSecao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idItemSecao", orphanRemoval = true)
    private List<ItemSecao> itemSecaoSubItemSecaoList = new LinkedList<>();

    @Transient
    private String key;

    public ItemSecao(boolean ativo, Secao secao) {
        this.ativo = ativo ? "S" : "N";
        this.idSecao = secao;
        this.key = StringUtil.gerarStringAleatoria(10);
    }

    public Boolean getActive() {
        return "S".equals(ativo);
    }

    public void setActive(boolean b) {
        ativo = b ? "S" : "N";
    }

    @Override
    public String toString() {
        return "ItemSecao{" + "id=" + id + ", tenatID=" + tenatID + "}";
    }

}
