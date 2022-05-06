package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
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
@Table(name = "CONTROLE_ACESSO_IP")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ControleAcessoIp extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "O campo deverá estar entre 1 e 200 caracteres")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe a descrição")
    @Size(max = 50, message = "O campo deverá estar entre 1 e 50 caracteres")
    @Column(name = "FAIXA_FINAL")
    private String faixaFinal;

    @NotNull(message = "Informe a descrição")
    @Size(max = 50, message = "O campo deverá estar entre 1 e 50 caracteres")
    @Column(name = "FAIXA_INICIAL")
    private String faixaInicial;

    @NotNull(message = "Informe a tipo.")
    @Size(max = 2)
    @Column(name = "TIPO")
    private String tipo;

    public ControleAcessoIp(Integer id) {
        this.id = id;
    }

    public ControleAcessoIp(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "ControleAcessoIp{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
