package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Audited
@Entity
@Table(name = "USUARIO")
@Builder
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@AllArgsConstructor
public class Usuario extends EntityId implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o login")
    @Column(name = "LOGIN")
    @Size(min = 3, max = 50, message = "Você precisa especificar um login com no mínimo 3 caracteres e no máximo 50")
    private String login;

    @NotNull(message = "Informe o nome")
    @Column(name = "NOME")
    @Size(min = 3, max = 200, message = "Você precisa especificar um nome com no mínimo 3 caracteres e no máximo 50")
    private String nome;

    @NotNull(message = "Informe a senha")
    @Column(name = "SENHA")
    @Size(min = 8, max = 100, message = "Você precisa especificar um nome com no mínimo 8 caracteres e no máximo 100")
    private String senha;

    @Column(name = "EMAIL")
    @Size(max = 255, message = "Você precisa especificar um e-mail com no máximo 255")
    private String email;

    @NotNull(message = "Informe se a conta esta expirada")
    @Size(max = 1, message = "Conta expirada com no máximo 1 caracter")
    @Column(name = "CONTA_EXPIRADA")
    private String contaExpirada;

    @NotNull(message = "Informe se a conta esta bloqueada")
    @Size(max = 1, message = "Conta bloqueada com no máximo 1 caracter")
    @Column(name = "CONTA_BLOQUEADA")
    private String contaBloqueada;

    @Size(max = 20)
    @Column(name = "TEMA")
    private String tema;

    @Size(max = 25)
    @Column(name = "MENU_MODE")
    private String menuMode;

    @NotNull(message = "Informe se o cadastro foi feito ou não no credenciamento.")
    @Size(max = 1)
    @Column(name = "CAD_CRED")
    private String cadCredenciamento;

    @NotNull(message = "Informe o perfil")
    @JoinColumn(name = "ID_PERFIL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Perfil idPerfil;

    @JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "ID")
    @ManyToOne
    private Funcionario idFuncionario;

    @JoinColumn(name = "ID_CONTABILIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Contabilidade idContabilidade;

    @NotNull(message = "Voce precisa informar se o usuário é administrador.")
    @Column(name = "ADMINISTRATOR")
    private boolean administrator;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario", orphanRemoval = true)
    private List<UsuarioPermissao> usuarioPermissaoList = new LinkedList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario", orphanRemoval = true)
    private List<UsuarioGrupoEmpresa> usuarioGrupoEmpresaList = new LinkedList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario", orphanRemoval = true)
    private List<UsuarioAcessoRapido> usuarioAcessoRapidoList = new LinkedList<>();

    @Builder.Default
    @OneToMany(mappedBy = "idUsuario")
    private List<LoginAcesso> loginAcessoList = new LinkedList<>();

    @Transient
    private Long qtdMensagensNaoLidas;

    @Lob
    @Column(name = "FOTO")
    private byte[] foto;

    @Size(max = 20, message = "Tamanho máximo para o telefone: 20")
    @Column(name = "TELEFONE")
    private String telefone;

    @Column(name = "PODE_MUDAR_PRECO_UNITARIO_VENDA")
    private String podeMudarPrecoUnitarioVenda;

    @Column(name = "RECEBE_EMAIL_IUGU")
    private String receberEmailIUGU;

    @Column(name = "RECEBE_EMAIL_ACESSO_EMPRESA")
    private String receberEmailAcessoEmpresa;

    @Transient
    private String recebeEmailComunicacao;

    @Transient
    private String tenat;

    @Transient
    private LoginAcesso loginAcesso;

    @Transient
    private Integer qtdEmpresas;

    @Transient
    private boolean senhaExpirada;

    @Transient
    private boolean telaBloqueada;

    @Transient
    private String uuid;

    @Transient
    private boolean precisaAtualizarPermissao = false;

    @Transient
    private boolean leuTermo = true;

    public Usuario() {
        this.podeMudarPrecoUnitarioVenda = "N";
        this.contaBloqueada = "N";
        this.contaExpirada = "N";
        this.receberEmailIUGU = "N";
        this.receberEmailAcessoEmpresa = "N";
        this.administrator = false;
        this.usuarioPermissaoList = new LinkedList<>();
        this.usuarioGrupoEmpresaList = new LinkedList<>();
        this.usuarioAcessoRapidoList = new LinkedList<>();
        this.loginAcessoList = new LinkedList<>();
    }

    public Usuario(Integer id, String nome) {
        this.podeMudarPrecoUnitarioVenda = "N";
        this.id = id;
        this.nome = nome;
        this.contaBloqueada = "N";
        this.contaExpirada = "N";
        this.receberEmailIUGU = "N";
        this.receberEmailAcessoEmpresa = "N";
        this.administrator = false;
    }

    public Usuario(Integer id) {
        this.podeMudarPrecoUnitarioVenda = "N";
        this.id = id;
        this.contaBloqueada = "N";
        this.contaExpirada = "N";
        this.receberEmailIUGU = "N";
        this.receberEmailAcessoEmpresa = "N";
        this.administrator = false;
    }

    public Usuario(Integer id, String login, String nome) {
        this.podeMudarPrecoUnitarioVenda = "N";
        this.id = id;
        this.login = login;
        this.nome = nome;
        this.contaBloqueada = "N";
        this.contaExpirada = "N";
        this.receberEmailIUGU = "N";
        this.receberEmailAcessoEmpresa = "N";
        this.administrator = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return contaExpirada.equalsIgnoreCase("S");
    }

    @Override
    public boolean isAccountNonLocked() {
        return contaBloqueada.equalsIgnoreCase("S");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return contaExpirada.equalsIgnoreCase("S");
    }

    @Override
    public boolean isEnabled() {
        return contaBloqueada.equalsIgnoreCase("N");
    }

    public void addPermissao(Permissao permissao) {
        UsuarioPermissao usuarioPermissao = new UsuarioPermissao();
        usuarioPermissao.setIdUsuario(this);
        usuarioPermissao.setIdPermissao(permissao);

        if (!usuarioPermissaoList.contains(usuarioPermissao)) {
            usuarioPermissaoList.add(usuarioPermissao);
        }
    }

    public void removePermissao(UsuarioPermissao usuarioPermissao) {
        usuarioPermissaoList.remove(usuarioPermissao);
    }

    public void addGrupoEmpresa(GrupoEmpresa grupoEmpresa) {
        UsuarioGrupoEmpresa usuarioGrupoEmpresa = new UsuarioGrupoEmpresa();
        usuarioGrupoEmpresa.setIdGrupoEmpresa(grupoEmpresa);
        usuarioGrupoEmpresa.setIdUsuario(this);

        if (!usuarioGrupoEmpresaList.contains(usuarioGrupoEmpresa)) {
            usuarioGrupoEmpresaList.add(usuarioGrupoEmpresa);
        }
    }

    public void removeGrupoEmpresa(UsuarioGrupoEmpresa usuarioGrupoEmpresa) {
        usuarioGrupoEmpresaList.remove(usuarioGrupoEmpresa);
    }

    public String getFoto64() {
        if (foto == null || foto.length == 0) {
            return null;
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(foto);
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + '}';
    }

    public void merge(Usuario update) {
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
                } catch (NoSuchMethodException ex) {
                    //Ignorar esse erro
                } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
