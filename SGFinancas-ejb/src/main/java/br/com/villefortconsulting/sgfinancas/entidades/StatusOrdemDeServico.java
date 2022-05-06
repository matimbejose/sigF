package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
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
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "STATUS_ORDEM_DE_SERVICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class StatusOrdemDeServico extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 2)
    @Column(name = "STATUS")
    private String status;

    @Column(name = "HORARIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horario;

    @JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "ID")
    @ManyToOne
    private Funcionario idFuncionario;

    @JoinColumn(name = "ID_ORDEM_DE_SERVICO", referencedColumnName = "ID")
    @ManyToOne
    private OrdemDeServico idOrdemDeServico;

    public StatusOrdemDeServico(Integer id) {
        this.id = id;
    }

    public StatusOrdemDeServico(String status, Funcionario idFuncionario, OrdemDeServico idOrdemDeServico, String tenantID) {
        this.status = status;
        this.horario = new Date();
        this.idFuncionario = idFuncionario;
        this.idOrdemDeServico = idOrdemDeServico;
        this.tenatID = tenantID;
    }

    public StatusOrdemDeServico(Integer id, String status, Funcionario idFuncionario, OrdemDeServico idOrdemDeServico) {
        this.id = id;
        this.status = status;
        this.horario = new Date();
        this.idFuncionario = idFuncionario;
        this.idOrdemDeServico = idOrdemDeServico;
    }

    public StatusOrdemDeServico(String status, Funcionario idFuncionario, OrdemDeServico idOrdemDeServico, Date horario) {
        this.status = status;
        this.horario = horario;
        this.idFuncionario = idFuncionario;
        this.idOrdemDeServico = idOrdemDeServico;
    }

    public StatusOrdemDeServico(Integer id, String status, Funcionario idFuncionario, OrdemDeServico idOrdemDeServico, Date horario) {
        this.id = id;
        this.status = status;
        this.horario = horario;
        this.idFuncionario = idFuncionario;
        this.idOrdemDeServico = idOrdemDeServico;
    }

    public EnumStatusOrdemDeServico getStatusEnum() {
        return EnumStatusOrdemDeServico.retornaEnumSelecionado(status);
    }

    public void setStatusEnum(EnumStatusOrdemDeServico status) {
        this.status = status.getCodigo();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
