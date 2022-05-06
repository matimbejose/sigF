package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PRODUTO_CATEGORIA_SUBCATEGORIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ProdutoCategoriaSubcategoria extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a categoria")
    @JoinColumn(name = "ID_PRODUTO_CATEGORIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ProdutoCategoria idProdutoCategoria;

    @NotNull(message = "Informe a descrição")
    @Size(max = 100, message = "Descrição deverá ter no máximo 100 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

    @Size(max = 500, message = "Observação deverá ter no máximo 500 caracteres.")
    @Column(name = "OBSERVACAO")
    private String observacao;

    @Transient
    private String controleEdicao;

    @Override
    public String toString() {
        return "ProdutoCategoriaSubcategoria{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    public void merge(ProdutoCategoriaSubcategoria update) {
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
