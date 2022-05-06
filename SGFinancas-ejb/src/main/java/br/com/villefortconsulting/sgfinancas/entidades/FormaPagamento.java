package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Table(name = "FORMA_PAGAMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class FormaPagamento extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 250, message = "Máximo de 250 caracteres para descrição")
    private String descricao;

    @Column(name = "CODIGO_NFE")
    private String codigoNfe;

    @Column(name = "BANDEIRAS")
    private String bandeiras;

    @Column(name = "ATIVO")
    private String ativo;

    public FormaPagamento() {
        this.ativo = "S";
    }

    public FormaPagamento(Integer id) {
        this.id = id;
    }

    public List<String> getBandeirasList() {
        List<String> list = new ArrayList<>();

        if (bandeiras != null && !bandeiras.trim().isEmpty()) {
            list.addAll(Arrays.asList(bandeiras.split(",")));
        }

        return list;
    }

    public void setBandeirasList(List<String> list) {
        if (list == null || list.isEmpty()) {
            bandeiras = null;
        } else {
            String aux = list.stream().map(s -> "," + s).reduce("", String::concat);
            if (aux.length() > 0) {
                aux = aux.substring(1);
            }
            bandeiras = aux;
        }
    }

    public EnumMeioDePagamento getEnumNfe() {
        return EnumMeioDePagamento.retornaEnumSelecionado(codigoNfe);
    }

    public void setEnumNfe(EnumMeioDePagamento e) {
        if (e == null) {
            codigoNfe = null;
            return;
        }
        codigoNfe = e.getCodigo();
    }

    @Override
    public String toString() {
        return "FormaPagamento{" + "id=" + id + '}';
    }

    public void merge(FormaPagamento update) {
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
