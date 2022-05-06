package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "TRANSACAO_INTEGRACAO_BANCARIA_CONTA_PARCELA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class TransacaoIntegracaoBancariaContaParcela extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a integração bancária")
    @JoinColumn(name = "ID_TRANSACAO_INTEGRACAO_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TransacaoIntegracaoBancaria idTransacaoIntegracaoBancaria;

    @JoinColumn(name = "ID_CONTA_PARCELA", referencedColumnName = "ID")
    @ManyToOne
    private ContaParcela idContaParcela;
    
    @NotNull(message = "Informe se foi processado")
    @Column(name = "PROCESSADO")
    private String processado = "N";

    @Override
    public String toString() {
        return "TransacaoIntegracaoBancariaContaParcela{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }
    
    public static Optional<TransacaoIntegracaoBancariaContaParcela> contains(List<TransacaoIntegracaoBancariaContaParcela> list, TransacaoIntegracaoBancariaContaParcela cp) {
        return list.stream()
                .filter(tbcp -> tbcp.getIdTransacaoIntegracaoBancaria().equals(cp.getIdTransacaoIntegracaoBancaria()) && tbcp.getIdContaParcela().equals(cp.getIdContaParcela()))
                .findAny();
    }

}
