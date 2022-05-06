package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemEstoque;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoEstoque;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "ESTOQUE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Estoque extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_COMPRA_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private CompraProduto idCompraProduto;

    @JoinColumn(name = "ID_VENDA_PRODUTO", referencedColumnName = "ID")
    @OneToOne
    private VendaProduto idVendaProduto;

    @JoinColumn(name = "ID_PRODUCAO_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private ProducaoProduto idProducaoProduto;

    @NotNull(message = "Informe o produto")
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private Produto idProduto;

    @NotNull(message = "Informe o número da parcela")
    @Column(name = "QUANTIDADE")
    private Double quantidade;

    @NotNull(message = "Informe a data")
    @Column(name = "DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @NotNull(message = "Informe o tipo de operação")
    @Column(name = "TIPO_OPERACAO")
    private String tipoOperacao;

    @Column(name = "PRIMEIRO_CADASTRO")
    @Size(max = 1)
    private String primeiroCadastro;

    @NotNull(message = "Informe o saldo anterior")
    @Column(name = "SALDO_ANTERIOR")
    private Double saldoAnterior;

    @Column(name = "SALDO")
    private Double saldo;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "TOTAL_OPERACAO")
    private Double totalOperacao;

    public void inverterMovimento() {
        EnumTipoEstoque tipo = EnumTipoEstoque.retornaEnumSelecionado(tipoOperacao);
        if (tipo == null) {
            tipoOperacao = EnumTipoEstoque.REMOVIDO.getTipo();
        } else {
            switch (tipo) {
                case ENTRADA:
                    tipoOperacao = EnumTipoEstoque.SAIDA.getTipo();
                    break;
                case SAIDA:
                    tipoOperacao = EnumTipoEstoque.ENTRADA.getTipo();
                    break;
                default:
                    tipoOperacao = EnumTipoEstoque.REMOVIDO.getTipo();
                    break;
            }
        }
        EnumOrigemEstoque origemEstoque = EnumOrigemEstoque.retornaEnumSelecionado(origem);
        if (origemEstoque == EnumOrigemEstoque.ENTRADA_LANCADA) {
            origem = EnumOrigemEstoque.SAIDA_LANCADA.getOrigem();
        } else if (origemEstoque == EnumOrigemEstoque.SAIDA_LANCADA) {
            origem = EnumOrigemEstoque.ENTRADA_LANCADA.getOrigem();
        }
    }

    @Override
    public Estoque clone() {
        try {
            Estoque novo = (Estoque) super.clone();
            novo.id = null;
            return novo;
        } catch (CloneNotSupportedException ex) {
            return new Estoque();
        }
    }

    @Override
    public String toString() {
        return "Estoque{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
