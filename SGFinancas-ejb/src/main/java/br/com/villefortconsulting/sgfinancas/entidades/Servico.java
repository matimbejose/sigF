package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "SERVICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Servico extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "O campo deverá estar entre 1 e 200 caracteres")
    @Column(name = "DESCRICAO")
    private String descricao;

    @JoinColumn(name = "ID_PRODUTO_CATEGORIA", referencedColumnName = "ID")
    @ManyToOne
    private ProdutoCategoria idProdutoCategoria;

    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private PlanoConta idPlanoConta;

    @Column(name = "VALOR_VENDA")
    private Double valorVenda;

    @Column(name = "CUSTO_SERVICO")
    private Double custoServico;

    @Column(name = "PONTOS")
    private Double pontos;

    @Column(name = "ATIVO")
    private String ativo;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idServico", orphanRemoval = true)
    private List<FuncionarioServico> funcionarioServicoList = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idServico", orphanRemoval = true)
    private List<ServicoProduto> servicoProdutoList = new LinkedList<>();

    @Column(name = "PERMITE_SELECAO_PROFISSIONAL")
    private String permiteSelecaoProfissional;

    @Column(name = "TEMPO_EXECUCAO")
    @Temporal(TemporalType.TIME)
    private Date tempoExecucao;

    @Column(name = "RECORRENCIA")
    private Integer recorrencia;

    public Servico() {
        this.ativo = "S";
        this.permiteSelecaoProfissional = "N";
    }

    public Servico(Integer id) {
        this.id = id;
        ativo = "S";
        this.permiteSelecaoProfissional = "N";
    }

    @Override
    public String toString() {
        return "Servico{" + "id=" + id + ", tenatID=" + tenatID + '}';
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
