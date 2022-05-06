package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "TIPO_PRODUTO_USO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Builder
@Inheritance
@NoArgsConstructor
public class TipoProdutoUso extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o nome")
    @Column(name = "TIPO_USO")
    private String tipoUso;

    @Column(name = "TIPO_PRODUTO")
    private String tipoProduto;

    @JoinColumn(name = "ID_PARAMETRO_SISTEMA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ParametroSistema idParametroSistema;

    public TipoProdutoUso(String tipoUso, String tipoProduto, ParametroSistema idParametroSistema) {
        this.tipoUso = tipoUso;
        this.tipoProduto = tipoProduto;
        this.idParametroSistema = idParametroSistema;
        this.tenatID = idParametroSistema.getTenatID();
    }

    public String getTipoProdutoName() {
        return EnumTipoProdutoVenda.retornaEnumSelecionado(tipoProduto).getDescricao();
    }

    public static Optional<TipoProdutoUso> contains(List<TipoProdutoUso> list, TipoProdutoUso item) {
        return list.stream()
                .filter(tpu -> item.getTipoProduto().equals(tpu.getTipoProduto()) && item.getTipoUso().equals(tpu.getTipoUso()))
                .findAny();
    }

    @Override
    public String toString() {
        return "TipoProdutoUso{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
