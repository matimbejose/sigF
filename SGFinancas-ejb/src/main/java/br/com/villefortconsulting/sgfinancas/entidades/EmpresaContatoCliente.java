package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "EMPRESA_CONTATO_CLIENTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class EmpresaContatoCliente extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o usuário")
    @JoinColumn(name = "ID_USUARIO_REGISTRO_CONTATO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuarioRegistro;

    @NotNull(message = "Informe a empresa")
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;

    @NotNull(message = "Informe o nome do contato")
    @Column(name = "NOME_CONTATO")
    @Size(max = 200, message = "Máximo de 200 caracteres para o nome do contato")
    private String nomeContato;

    @NotNull(message = "Informe o telefone")
    @Column(name = "TELEFONE")
    @Size(max = 20, message = "Máximo de 20 caracteres para o telefone")
    private String telefone;

    @Column(name = "EMAIL")
    @Size(max = 100, message = "Máximo de 100 caracteres para o email")
    private String email;

    @Column(name = "OBSERVACAO")
    @Size(max = 500, message = "Máximo de 500 caracteres para a observação")
    private String observacao;

    @NotNull(message = "Informe a data do contato")
    @Column(name = "DATA_CONTATO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataContato;

    @NotNull(message = "Informe a data do registro")
    @Column(name = "DATA_REGISTRO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataRegistro;

    @Column(name = "DATA_PROXIMO_CONTATO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataProximoContato;

    @Override
    public String toString() {
        return "EmpresaContatoCliente{" + "id=" + id + '}';
    }

}
