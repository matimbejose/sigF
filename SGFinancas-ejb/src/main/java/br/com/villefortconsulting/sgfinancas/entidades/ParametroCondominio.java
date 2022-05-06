package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PARAMETRO_CONDOMINIO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ParametroCondominio extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "VALOR_CONDOMINIO")
    @NotNull(message = "Informe o valor do condomínio")
    private Double valorCondominio;

    @Column(name = "VALOR_TAXA_EXTRA")
    private Double valorTaxaExtra;

    @Column(name = "MULTA_ATRASO_PAGAMENTO")
    @NotNull(message = "Informe o valor da multa por atraso")
    private Double valorMutaAtrasoPagamento;

    @Column(name = "JUROS_MORA_DIA")
    @NotNull(message = "Informe o valor do juros mora dia")
    private Double valorJurosMoraDia;

    @Column(name = "DATA_FIM_TAXA_EXTRA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataFimTaxaExtra;

    @Column(name = "QTD_MENSALIDADES_INADIMPLENCIA")
    private Integer qtdMensalidadesInadimplencia;

    @Column(name = "DIA_VENCIMENTO_MENSALIDADE")
    @NotNull(message = "Informe o dia de vencimento da mensalidade")
    private Integer diaVencimentoMensalidade;

    @Column(name = "INSTRUCAO_BOLETO_1")
    private String instrucaoBoleto1;

    @Column(name = "INSTRUCAO_BOLETO_2")
    private String instrucaoBoleto2;

    @Column(name = "INSTRUCAO_BOLETO_3")
    private String instrucaoBoleto3;

    @Column(name = "INSTRUCAO_BOLETO_4")
    private String instrucaoBoleto4;

    @Override
    public String toString() {
        return "ParametroCondominio{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
