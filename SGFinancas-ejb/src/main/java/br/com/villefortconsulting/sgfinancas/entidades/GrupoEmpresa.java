package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "GRUPO_EMPRESA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement
@Inheritance
public class GrupoEmpresa extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o grupo")
    @JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Grupo idGrupo;

    @NotNull(message = "Informe a empresa")
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;

    @JoinColumn(name = "ID_USUARIO_RESPONSAVEL", referencedColumnName = "ID")
    @ManyToOne
    private Usuario idUsuarioResponsavel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGrupoEmpresa", orphanRemoval = true)
    private List<UsuarioGrupoEmpresa> usuarioGrupoEmpresaList = new LinkedList<>();

    public GrupoEmpresa(Grupo idGrupo, Empresa idEmpresa) {
        this.idGrupo = idGrupo;
        this.idEmpresa = idEmpresa;
    }

    public GrupoEmpresa(Grupo idGrupo, Empresa idEmpresa, Usuario idUsuarioResponsavel) {
        this.idGrupo = idGrupo;
        this.idEmpresa = idEmpresa;
        this.idUsuarioResponsavel = idUsuarioResponsavel;
    }

    public List<UsuarioGrupoEmpresa> getUsuarioEmpresaGrupoList() {
        return usuarioGrupoEmpresaList;
    }

    public void setUsuarioGrupoEmpresaList(List<UsuarioGrupoEmpresa> usuarioGrupoEmpresaList) {
        this.usuarioGrupoEmpresaList = usuarioGrupoEmpresaList;
    }

    public void addUsuario(Usuario usuario) {
        UsuarioGrupoEmpresa usuarioGrupoEmpresa = new UsuarioGrupoEmpresa();
        usuarioGrupoEmpresa.setIdGrupoEmpresa(this);
        usuarioGrupoEmpresa.setIdUsuario(usuario);

        if (!usuarioGrupoEmpresaList.contains(usuarioGrupoEmpresa)) {
            usuarioGrupoEmpresaList.add(usuarioGrupoEmpresa);
        }
    }

    public void removeUsuario(UsuarioGrupoEmpresa usuarioGrupoEmpresa) {
        usuarioGrupoEmpresaList.remove(usuarioGrupoEmpresa);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.idGrupo);
        return 83 * hash + Objects.hashCode(this.idEmpresa);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrupoEmpresa other = (GrupoEmpresa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.idGrupo, other.idGrupo)) {
            return false;
        }
        return Objects.equals(this.idEmpresa, other.idEmpresa);
    }

    @Override
    public String toString() {
        return "GrupoEmpresa{" + "id=" + id + '}';
    }

}
