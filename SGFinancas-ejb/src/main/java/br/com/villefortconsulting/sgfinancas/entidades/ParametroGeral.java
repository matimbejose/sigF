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
@Table(name = "PARAMETRO_GERAL")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ParametroGeral extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "PRAZO_USAR_SEM_COMPRAR")
    @NotNull(message = "Informe o código númerico da nota fiscal")
    private Integer prazoUsarSemComprar;

    @Column(name = "URL_PAGAMENTO_PRODUCAO")
    private String callbackProducao;

    @Column(name = "URL_PAGAMENTO_HOMOLOGACAO")
    private String callbackHomologacao;

    @Column(name = "TAXA_GETNET")
    private Double taxaGetnet;

    @Column(name = "CORPO_EMAIL_CADASTRO")
    @Size(max = 8000, message = "O tamanho máximo da mensagem deve ser de 8.000 caracteres")
    private String corpoEmailCadastro;

    @JoinColumn(name = "ID_DOCUMENTO_ANEXO_EMAIL_CADASTRO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumentoAnexoEmailCadastro;

    @Column(name = "MEIO_PAGAMENTO_PADRAO")
    @Size(max = 10, message = "O meio de pagamento padrão deve ter no máximo 10 caracteres")
    private String meioPagamentoPadrao;

    @Override
    public String toString() {
        return "ParametroGeral{" + "id=" + id + '}';
    }

}
