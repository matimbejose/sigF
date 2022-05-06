package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "FUNCIONARIO_SERVICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class FuncionarioServico extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Funcionario idFuncionario;

    @JoinColumn(name = "ID_SERVICO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Servico idServico;

    @Override
    public String toString() {
        return "FuncionarioServico{" + "id=" + id + "tenat=" + tenatID + '}';
    }

    public static Optional<FuncionarioServico> contains(List<FuncionarioServico> list, FuncionarioServico item) {
        return list.stream()
                .filter(fs -> fs.getIdServico().getId().equals(item.getIdServico().getId()) && fs.getIdFuncionario().getId().equals(item.getIdFuncionario().getId()))
                .findAny();
    }

}
