package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "SITUACAO_TRIBUTARIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class SituacaoTributaria extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "Descrição deverá ter entre 200 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe o código CST")
    @Size(max = 100, message = "Código cst deverá ter entre 100 caracteres.")
    @Column(name = "COD_CST")
    private String codCst;

    @NotNull(message = "Informe o código Simples nacional")
    @Size(max = 1, message = "Simples nacional deverá ter entre 1 caracteres.")
    @Column(name = "SIMPLES_NACIONAL")
    private String simplesNascional;

    @Column(name = "APLICACAO")
    @Size(max = 5000, message = "Aplicação deverá ter entre 5000 caracteres.")
    private String aplicacao;

    @Override
    public String toString() {
        return "SituacaoTributaria{" + "id=" + id + '}';
    }

}
