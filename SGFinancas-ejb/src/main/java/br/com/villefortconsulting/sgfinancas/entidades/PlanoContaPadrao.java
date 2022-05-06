package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
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
@Table(name = "PLANO_CONTA_PADRAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Getter
@Setter
@Inheritance
public class PlanoContaPadrao extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "Descrição deverá ter entre 200 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe se é um registro padrão")
    @Size(max = 1, message = "Registro padrão deverá ter entre 1 caracteres.")
    @Column(name = "REGISTRO_PADRAO")
    private String registroPadrao;

    @NotNull(message = "Informe o tipo")
    @Size(max = 1, message = "Tipo deverá ter entre 1 caracteres.")
    @Column(name = "TIPO")
    private String tipo;

    @NotNull(message = "Informe o tipo indicador")
    @Column(name = "TIPO_INDICADOR")
    @Size(max = 1, message = "Tipo de indicador deverá ter entre 1 caracteres.")
    private String tipoIndicador;

    @NotNull(message = "Informe o tipo de balanço")
    @Size(max = 1, message = "Tipo de balanço deverá ter entre 1 caracteres.")
    @Column(name = "TIPO_BALANCO")
    private String tipoBalanco;

    @NotNull(message = "Informe o código")
    @Column(name = "CODIGO")
    private String codigo;

    @NotNull(message = "Informe o código")
    @Column(name = "CODIGO_PAI")
    private String codigoPai;

    @Column(name = "MOSTRA_TELA_INICIAL")
    @Size(max = 1, message = "Mostrar tela inicial deverá ter entre 1 caracteres.")
    private String mostraTelaInicial;

    @NotNull(message = "Informe se pode incluir contas filhas")
    @Column(name = "PODE_TER_FILHO")
    private String podeTerFilho;

    @Override
    public String toString() {
        return "PlanoConta{" + "id=" + id + '}';
    }

}
