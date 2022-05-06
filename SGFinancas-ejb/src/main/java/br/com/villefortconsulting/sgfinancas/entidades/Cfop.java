package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "CFOP")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Cfop extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 220, message = "Máximo de 220 caracteres para descrição")
    private String descricao;

    @Column(name = "APLICACAO")
    @Size(max = 5000, message = "Máximo de 5000 caracteres para descrição")
    private String aplicacao;

    @NotNull(message = "Informe o código")
    @Column(name = "CODIGO")
    @Size(max = 4, message = "Máximo de 4 caracteres para código")
    private String codigo;

    @Column(name = "TIPO")
    @Size(max = 1, message = "Máximo de 1 caracter para tipo")
    private String tipo;

    @Column(name = "INDICADOR_NFE")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador nfe")
    private String indicadorNfe;

    @Column(name = "INDICADOR_COMUNICACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador comunicação")
    private String indicadorComunicacao;

    @Column(name = "INDICADOR_TRANSPORTE")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador transporte")
    private String indicadorTransporte;

    @Column(name = "INDICADOR_DEVOLUCAO")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador devolução")
    private String indicadorDevolucao;

    @Column(name = "INDICADOR_RETORNO")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador retorno")
    private String indicadorRetorno;

    @Column(name = "INDICADOR_ANULACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador anulação")
    private String indicadorAnulacao;

    @Column(name = "INDICADOR_REMESSA")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador remessa")
    private String indicadorRemessa;

    @Column(name = "INDICADOR_SERVICO")
    @Size(max = 1, message = "Máximo de 1 caracter para indicador serviço")
    private String indicadorServico;

    @JoinColumn(name = "ID_UF", referencedColumnName = "ID")
    @ManyToOne
    private UF idUf;

    @Override
    public String toString() {
        return "Cfop{" + "id=" + id + '}';
    }

    public boolean isValid() {
        return this.indicadorAnulacao != null && this.indicadorDevolucao != null && this.indicadorComunicacao != null
                && this.indicadorNfe != null && this.indicadorRemessa != null && this.indicadorRetorno != null
                && this.indicadorTransporte != null;
    }

}
