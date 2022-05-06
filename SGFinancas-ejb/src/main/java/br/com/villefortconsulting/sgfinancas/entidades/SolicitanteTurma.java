package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
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
@Table(name = "SOLICITANTE_TURMA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class SolicitanteTurma extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o aluno")
    @JoinColumn(name = "ID_SOLICITANTE", referencedColumnName = "ID")
    @ManyToOne
    private Solicitante idSolicitante;

    @NotNull(message = "Informe a turma")
    @JoinColumn(name = "ID_TURMA", referencedColumnName = "ID")
    @ManyToOne
    private Turma idTurma;

    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private Contrato idContrato;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    @Size(max = 1, message = "Você precisa especificar uma situação com no máximo 1 caracter")
    private String situacao;

    @NotNull(message = "Informe a data de vencimento")
    @Column(name = "DATA_INSCRICAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInscricao;

    @Column(name = "OPCAO_PAGAMENTO")
    @Size(max = 1, message = "Você precisa especificar uma opção de pagamento com no máximo 1 caracter")
    private String opcaoPagamento;

    @Column(name = "VALOR")
    private Double valor;

    @Column(name = "DESCONTO")
    private Double desconto;

    @Override
    public String toString() {
        return "SolicitanteTurma{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
