package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "GRUPO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@XmlRootElement
@Inheritance
public class Grupo extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe a tipo.")
    @Column(name = "TIPO")
    private String tipo;

    @NotNull(message = "Informe se o grupo é de gestão interna.")
    @Column(name = "GESTAO_INTERNA")
    private String gestaoInterna;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGrupo", orphanRemoval = true)
    private List<GrupoPermissao> grupoPermissaoList = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGrupo", orphanRemoval = true)
    private List<GrupoEmpresa> grupoEmpresaList = new LinkedList<>();

    public Grupo() {
        this.gestaoInterna = "N";
    }

    public Grupo(Integer id) {
        this.id = id;
        this.gestaoInterna = "N";
    }

    public Grupo(String tipo) {
        this.tipo = tipo;
        this.gestaoInterna = "N";
    }

    public Grupo(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.gestaoInterna = "N";
    }

    @XmlTransient
    @JsonIgnore
    public List<GrupoPermissao> getGrupoPermissaoList() {
        return grupoPermissaoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<GrupoEmpresa> getGrupoEmpresaList() {
        return grupoEmpresaList;
    }

    public void addPermissao(Permissao permissao) {
        GrupoPermissao grupoPermissao = new GrupoPermissao();
        grupoPermissao.setIdGrupo(this);
        grupoPermissao.setIdPermissao(permissao);

        if (!grupoPermissaoList.contains(grupoPermissao)) {
            grupoPermissaoList.add(grupoPermissao);
        }
    }

    public void removePermissao(GrupoPermissao grupoPermissao) {
        grupoPermissaoList.remove(grupoPermissao);
    }

    public void addEmpresa(Empresa empresa) {
        GrupoEmpresa grupoEmpresa = new GrupoEmpresa();
        grupoEmpresa.setIdGrupo(this);
        grupoEmpresa.setIdEmpresa(empresa);

        if (!grupoEmpresaList.contains(grupoEmpresa)) {
            grupoEmpresaList.add(grupoEmpresa);
        }
    }

    public void removeEmpresa(GrupoEmpresa grupoEmpresa) {
        grupoEmpresaList.remove(grupoEmpresa);
    }

    public void addUsuarioGrupoEmpresa(Usuario usuario, Empresa empresa) {
        GrupoEmpresa grupoEmpresa = new GrupoEmpresa();
        grupoEmpresa.setIdGrupo(this);
        grupoEmpresa.setIdEmpresa(empresa);
        grupoEmpresa.addUsuario(usuario);

        if (!grupoEmpresaList.contains(grupoEmpresa)) {
            grupoEmpresaList.add(grupoEmpresa);
        }
    }

    public void removeUsuarioGrupoEmpresa(GrupoEmpresa grupoEmpresa) {
        grupoEmpresaList.remove(grupoEmpresa);
    }

    @Override
    public String toString() {
        return "Grupo{" + "id=" + id + '}';
    }

}
