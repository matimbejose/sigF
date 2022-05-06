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
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONDICAO_PAGAMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class CondicaoPagamento extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 250, message = "Máximo de 250 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe a quantidade de parcelas")
    @Column(name = "QTDE_PARCELAS")
    private Integer qtdeParcelas;

    @NotNull(message = "Informe se as parcelas são iguais")
    @Column(name = "PARCELAS_IGUAIS")
    private String parcelasIguais;

    @NotNull(message = "Informe a quantidade de dias de carência para primeira parcela")
    @Column(name = "DIAS_CARENCIA_PARCELA")
    private Integer diasCarenciaParcela;

    @NotNull(message = "Informe o intervalo de dias entre as parcelas")
    @Column(name = "INTERVALO_DIAS_PARCELA")
    private Integer intervaloDiasParcela;

    @NotNull(message = "Informe o multiplicador do preco de venda")
    @Column(name = "FATOR_PARCELA")
    private Double fatorParcela;

    @NotNull(message = "Informe o dia do mês fora para a primeira parcela")
    @Column(name = "DIA_MES_FORA_PARECELA")
    private Integer diaMesForaParcela;

    @Override
    public String toString() {
        return "CondicaoPagamento{" + "id=" + id + '}';
    }

}
