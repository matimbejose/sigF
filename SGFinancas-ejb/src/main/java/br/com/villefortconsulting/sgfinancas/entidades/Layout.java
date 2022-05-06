package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "LAYOUT")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class Layout extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull(message = "Informe a descrição")
    @Size(max = 255, message = "A descrição deverá estar entre 1 e 255 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

    @Basic(optional = false)
    @NotNull(message = "Informe o tipo de layout")
    @Size(max = 2)
    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "QTD_LINHAS_CABECALHO")
    private Integer qtdLinhasCabecalho;

    @Column(name = "QTD_LINHAS_RODAPE")
    private Integer qtdLinhasRodape;

    @Basic(optional = false)
    @NotNull(message = "Informe o separador de campo")
    @Size(max = 10)
    @Column(name = "SEPARADOR_CAMPO")
    private String separadorCampo;

    @Size(max = 10, message = "O filtro deve ter no máximo 10 caracteres.")
    @Column(name = "FILTRO_LINHA")
    private String filtroLinha;

    @Size(max = 5000)
    @Column(name = "OBSERVACAO")
    private String observacao;

    @Column(name = "NOME_ARQUIVO_EXPORTACAO")
    private String nomeArquivoExportacao;

    @Column(name = "FORMATO_ARQUIVO_EXPORTACAO")
    private String formatoArquivoExportacao;

    @Column(name = "CABECALHO_RODAPE")
    private String cabecalhoRodape;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "idLayout")
    private List<LayoutCampo> layoutCampoList = new LinkedList<>();

    public Layout(Integer id) {
        this.id = id;
    }

    public String getSeparadorCampoTrantandoEspaco() {
        return "N".equalsIgnoreCase(separadorCampo) ? "" : separadorCampo;
    }

    public void addLayoutCampo(LayoutCampo layoutCampo) {
        layoutCampo.setIdLayout(this);
        layoutCampoList.add(layoutCampo);
    }

    public void removeLayoutCampo(LayoutCampo layoutCampo) {
        layoutCampoList.remove(layoutCampo);
    }

    @Override
    public String toString() {
        return "Layout{" + "id=" + id + '}';
    }

}
