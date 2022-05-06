package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "COMPRA_PRODUTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NoArgsConstructor
@Getter
@Setter
@Inheritance
public class CompraProduto extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a compra")
    @JoinColumn(name = "ID_COMPRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Compra idCompra;

    @Embedded
    private DadosProduto dadosProduto = new DadosProduto();

    @Column(name = "DEVOLVIDO")
    private String devolvido;

    @Transient
    private String controle;

    @Transient
    private String descricaoProdutoXML;

    @Transient
    private UnidadeMedida idUnidadeMedida;

    @Transient
    private String codigoBarras;

    @Transient
    private String ncm;

    @Override
    public String toString() {
        return "CompraProduto{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    public boolean comparaParaImportacao(CompraProduto cp2) {
        return this.dadosProduto.getValor().equals(cp2.getDadosProduto().getValor())
                && this.dadosProduto.getDesconto().equals(cp2.getDadosProduto().getDesconto())
                && this.dadosProduto.getQuantidade().equals(cp2.getDadosProduto().getQuantidade())
                && this.dadosProduto.getDetalhesItem().equals(cp2.getDadosProduto().getDetalhesItem())
                && this.dadosProduto.getIdNcm().equals(cp2.getDadosProduto().getIdNcm())
                && this.dadosProduto.getIdCfop().equals(cp2.getDadosProduto().getIdCfop())
                && this.dadosProduto.getIdCsosn().equals(cp2.getDadosProduto().getIdCsosn())
                && this.dadosProduto.getIdCst().equals(cp2.getDadosProduto().getIdCst());
    }

    public static Optional<CompraProduto> contains(List<CompraProduto> list, CompraProduto item) {
        return list.stream()
                .filter(vp -> vp.getIdCompra().equals(item.getIdCompra()) && vp.getDadosProduto().getIdProduto().getId().equals(item.getDadosProduto().getIdProduto().getId()))
                .findAny();
    }

}
