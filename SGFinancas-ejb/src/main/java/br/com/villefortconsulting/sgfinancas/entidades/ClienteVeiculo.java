package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CLIENTE_VEICULO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class ClienteVeiculo extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idCliente;

    @JoinColumn(name = "ID_MODELO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Modelo idModelo;

    @NotNull(message = "Informe o ano de fabricação do veículo")
    @Column(name = "ANO_FABRICACAO")
    private Integer anoFabricacao;

    @NotNull(message = "Informe o ano do modelo do veículo")
    @Column(name = "ANO_MODELO")
    private Integer anoModelo;

    @JoinColumn(name = "ID_COMBUSTIVEL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Combustivel idCombustivel;

    @Column(name = "PLACA")
    @Size(max = 10, message = "Máximo de 5000 caracteres para descrição do NFS-e")
    private String placa;

    @JoinColumn(name = "ID_COR_VEICULO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CorVeiculo idCorVeiculo;

    @Column(name = "RENAVAM")
    @Size(max = 50, message = "O tamanho máximo para o renavam é de 50 caracteres")
    private String renavam;

    @Column(name = "CHASSI")
    @Size(max = 50, message = "O tamanho máximo para o chassi é de 50 caracteres")
    private String chassi;

    @Column(name = "CAMBIO")
    @Size(max = 1)
    private String cambio;

    @Column(name = "PORTAS")
    private Integer portas;

    @Column(name = "NUMERO_PASSAGEIROS")
    private Integer numeroPassageiros;

    @Column(name = "VALOR_PROTEGIDO")
    private Double valorProtegido;

    @Column(name = "ATIVO")
    private String ativo;

    @Transient
    private String tipo;

    public String getNome() {
        return idModelo.getFipeNome() + " " + idCorVeiculo.getDescricao() + " - " + placa;
    }

}
