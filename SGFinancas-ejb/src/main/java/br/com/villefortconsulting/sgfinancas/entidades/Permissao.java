package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

@Audited
@Entity
@Table(name = "PERMISSAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement
@Inheritance
public class Permissao extends EntityId implements Serializable, GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRICAO")
    private String descricao;

    @Size(max = 500)
    @Column(name = "DESCRICAO_DETALHADA")
    private String descricaoDetalhada;

    @OneToMany(mappedBy = "idPermissao")
    private List<PermissaoPerfil> permissaoPerfilList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPermissao")
    private List<GrupoPermissao> grupoPermissaoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPermissao")
    private List<UsuarioPermissao> usuarioPermissaoList;

    public Permissao(Integer id) {
        this.id = id;
    }

    public Permissao(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.descricaoDetalhada = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<PermissaoPerfil> getPermissaoPerfilList() {
        return permissaoPerfilList;
    }

    @XmlTransient
    @JsonIgnore
    public List<GrupoPermissao> getGrupoPermissaoList() {
        return grupoPermissaoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<UsuarioPermissao> getUsuarioPermissaoList() {
        return usuarioPermissaoList;
    }

    @Override
    public String getAuthority() {
        return descricao;
    }

    @Override
    public String toString() {
        return "Permissao{" + "id=" + id + '}';
    }

}
