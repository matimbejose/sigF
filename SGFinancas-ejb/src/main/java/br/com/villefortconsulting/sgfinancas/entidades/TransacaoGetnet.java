/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.servicos.getnet.ResponseApi;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "TRANSACAO_GETNET")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class TransacaoGetnet extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_PAGAMENTO_SISTEMA", referencedColumnName = "ID")
    @OneToOne
    private PagamentoSistema idPagamentoSistema;

    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @OneToOne
    private Venda idVenda;

    @JoinColumn(name = "ID_BOLETO", referencedColumnName = "ID")
    @OneToOne
    private Boleto idBoleto;

    @Size(max = 1)
    @Column(name = "SITUACAO")
    private String situacao;

    @Size(max = 25)
    @Column(name = "NUMERO_CARTAO")
    private String numeroCartao;

    @Size(max = 50)
    @Column(name = "NOME_CARTAO")
    private String nomeCartao;

    @Size(max = 50)
    @Column(name = "ID_PAGAMENTO")
    private String idPagamento;

    @Size(max = 50)
    @Column(name = "ID_AUTORIZACAO")
    private String idAutorizacao;

    @Column(name = "DATA_SOLICITACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSolicitacao;

    @Column(name = "DATA_VENCIMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimento;

    @Column(name = "DATA_AUTORIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAutorizacao;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCancelamento;

    @Size(max = 100)
    @Column(name = "LINK_BOLETO")
    private String linkBoleto;

    @Size(max = 100)
    @Column(name = "MENSAGEM")
    private String mensagem;

    @Size(max = 50)
    @Column(name = "ID_CANCELAMENTO")
    private String idCancelamento;

    @Size(max = 50)
    @Column(name = "MD")
    private String md;

    @Size(max = 60)
    @Column(name = "LINHA_DIGITAVEL")
    private String linhaDigitavel;

    @OneToMany(mappedBy = "idTransacaoGetnet")
    private List<PagamentoMensalidade> pagamentoMensalidadeList = new LinkedList<>();

    @Transient
    private transient ResponseApi responseDebito;

    @Override
    public String toString() {
        return "TransacaoGetnet{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
