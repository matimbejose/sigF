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
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PONTO_VENDA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class PontoVenda extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "MODELO")
    @Size(max = 50, message = "Máximo de 50 caracteres para o modelo")
    private String modelo;

    @NotNull(message = "Informe a série ECF")
    @Column(name = "SERIE_ECF")
    @Size(max = 30, message = "Máximo de 30 caracteres para o SérieECF")
    private String serieECF;

    @NotNull(message = "Informe o número da loja")
    @Column(name = "NUM_LOJA")
    @Size(max = 20, message = "Máximo de 20 caracteres para o número da loja")
    private String numLoja;

    @NotNull(message = "Informe a data de abertura")
    @Column(name = "DATA_ABERTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAbertura;

    @NotNull(message = "Informe a data do cupom")
    @Column(name = "DATA_CUPOM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCupom;

    @NotNull(message = "Informe o COO")
    @Column(name = "COO")
    @Size(max = 30, message = "Máximo de 30 caracteres para o COO")
    private String coo;

    @NotNull(message = "Informe a série da nota fiscal")
    @Column(name = "SERIE_NFC_CONT")
    @Size(max = 10, message = "Máximo de 10 caracteres para a série da nota fiscal")
    private String serieNfcCont;

    @NotNull(message = "Informe o número da nota fiscal")
    @Column(name = "NUMERO_NFCE")
    @Size(max = 20, message = "Máximo de 20 caracteres para o número da nota fiscal")
    private Integer numeroNfce;

    @NotNull(message = "Informe o status do ponto de venda")
    @Column(name = "STATUS")
    @Size(max = 1, message = "Máximo de 1 caractere para status do ponto de venda")
    private String status;

    @NotNull(message = "Informe se ponto de venda utiliza  balança")
    @Column(name = "UTILIZA_BALANCA")
    @Size(max = 1, message = "Máximo de 1 caractere para utilização de balança")
    private String utilizaBalanca;

    @Override
    public String toString() {
        return "PontoVenda{" + "id=" + id + '}';
    }

}
