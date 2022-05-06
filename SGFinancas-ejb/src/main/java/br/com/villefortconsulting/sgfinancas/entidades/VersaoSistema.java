package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "VERSAO_SISTEMA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class VersaoSistema extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o número da versão")
    @Size(max = 10, message = "O tamanho máximo para a versão é 10 caracteres.")
    @Column(name = "VERSAO")
    private String versao;

    @NotNull
    @Column(name = "DATA_INCLUSAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInclusao;

    @Size(max = 5000, message = "O tamanho máximo para a descrição é 5000 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

}
