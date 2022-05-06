package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PRODUTO_CATEGORIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ProdutoCategoria extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 500, message = "Descrição deverá ter entre 500 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "ATIVO")
    private String ativo;

    @Valid
    @NotNull(message = "Informe ao menos 1 produto para venda")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProdutoCategoria", orphanRemoval = true)
    private List<ProdutoCategoriaSubcategoria> listProdutoCategoriaSubcategoria = new LinkedList<>();

    public ProdutoCategoria() {
        this.ativo = "S";
    }

    @Override
    public String toString() {
        return "ProdutoCategoria{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    public void merge(ProdutoCategoria update) {
        if (update == null || !this.getClass().isAssignableFrom(update.getClass())) {
            return;
        }
        for (Method method : this.getClass().getMethods()) {
            if (method.getDeclaringClass().equals(this.getClass()) && method.getName().startsWith("get")) {
                try {
                    Method toMetod = this.getClass().getMethod(method.getName().replace("get", "set"), method.getReturnType());
                    Object value = method.invoke(update, (Object[]) null);
                    if (value != null) {
                        toMetod.invoke(this, value);
                    }
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
