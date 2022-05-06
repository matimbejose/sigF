package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.util.StringUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "MODULO_PACOTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ModuloPacote extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o nome do modulo")
    @Size(max = 100, message = "O campo deverá estar entre 1 e 200 caracteres")
    @Column(name = "NOME")
    private String nome;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "VALOR_ADESAO")
    private Double valorAdesao;

    @Column(name = "VALOR_MENSALIDADE")
    private Double valorMensalidade;

    @Column(name = "ATIVO")
    private String ativo = "S";

    @Column(name = "DATA_VENCIMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimento;

    @NotNull(message = "Informe ao menos módulo para o pacote")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idModuloPacote", orphanRemoval = true)
    private List<ModuloPacoteModulo> moduloPacoteModuloList = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idModuloPacote", orphanRemoval = true)
    private List<ModuloPacoteCategoriaEmpresa> moduloPacoteCategoriaEmpresaList = new LinkedList<>();

    public String getCategorias() {
        if (moduloPacoteCategoriaEmpresaList == null || moduloPacoteCategoriaEmpresaList.isEmpty()) {
            return "";
        }
        return StringUtil.listaParaTexto(moduloPacoteCategoriaEmpresaList.stream()
                .map(ModuloPacoteCategoriaEmpresa::getIdCategoriaEmpresa)
                .map(CategoriaEmpresa::getDescricao)
                .collect(Collectors.toList()), ", ", " ou ");
    }

}
