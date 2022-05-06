package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PERFIL")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class Perfil extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    @Column(name = "DESCRICAO")
    private String descricao;

    @Size(max = 2)
    @Column(name = "TIPO")
    @NotNull
    private String tipo;

    @OneToMany(mappedBy = "idPerfil")
    private List<Usuario> usuarioList;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "idPerfil", orphanRemoval = true)
    private List<PermissaoPerfil> permissaoPerfilList = new LinkedList<>();

    public Perfil(Integer id) {
        this.id = id;
    }

    public Perfil(String tipo) {
        this.tipo = tipo;
    }

    public void removePermissao(PermissaoPerfil obj) {
        permissaoPerfilList.remove(obj);
    }

    public void addPermissao(Permissao permissao) {
        boolean verificarExistePerfil = false;
        PermissaoPerfil permissaoPerfil = new PermissaoPerfil();
        permissaoPerfil.setIdPerfil(this);
        permissaoPerfil.setIdPermissao(permissao);
        for (PermissaoPerfil listPerfilPermissao : permissaoPerfilList) {
            if (listPerfilPermissao.getIdPermissao().equals(permissaoPerfil.getIdPermissao())) {
                verificarExistePerfil = true;
            }
        }
        if (!verificarExistePerfil) {
            permissaoPerfilList.add(permissaoPerfil);
        }
    }

    public Boolean getEhCredor() {
        return EnumTipoUsuario.ANTECIPADOR.getTipo().equals(this.tipo);
    }

    public Boolean getEhUsuarioMaster() {
        return EnumTipoUsuario.MASTER_USUARIO.getTipo().equals(this.tipo);
    }

    public Boolean getEhSuporte() {
        return EnumTipoUsuario.ADMINISTRADOR.getTipo().equals(this.tipo);
    }

    public Boolean getEhVendedor() {
        return EnumTipoUsuario.VENDEDOR.getTipo().equals(this.tipo);
    }

    public Boolean getEhMasterVendedor() {
        return EnumTipoUsuario.MASTER_VENDEDOR.getTipo().equals(this.tipo);
    }

    public Boolean getEhContabilidade() {
        return EnumTipoUsuario.CONTABILIDADE.getTipo().equals(this.tipo);
    }

    public Boolean getEhMasterContabilidade() {
        return EnumTipoUsuario.MASTER_CONTABILIDADE.getTipo().equals(this.tipo);
    }

    public Boolean getEhUsuarioComum() {
        return EnumTipoUsuario.USUARIO_COMUM.getTipo().equals(this.tipo);
    }

    public Boolean getEhFuncionario() {
        return EnumTipoUsuario.FUNCIONARIO.getTipo().equals(this.tipo);
    }

    @Override
    public String toString() {
        return "Perfil{" + "id=" + id + '}';
    }

}
