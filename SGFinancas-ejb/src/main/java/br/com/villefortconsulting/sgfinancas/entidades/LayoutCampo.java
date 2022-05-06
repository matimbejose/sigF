package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Basic;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "LAYOUT_CAMPO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class LayoutCampo extends EntityId implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull(message = "Informe a descrição")
    @Size(max = 255, message = "A descrição deverá estar entre 1 e 255 caracteres.")
    @Column(name = "DESCRICAO")
    private String descricao;

    @Basic(optional = false)
    @NotNull(message = "Informe o tipo do dado")
    @Size(max = 1, message = "Campo tipo dado deve ter 1 caractere")
    @Column(name = "TIPO_DADO")
    private String tipoDado;

    @Column(name = "TAMANHO")
    private Integer tamanho;

    @Column(name = "POSICAO_INICIAL")
    private Integer posicaoInicial;

    @Column(name = "POSICAO_ARQUIVO")
    private Integer posicaoArquivo;

    @Basic(optional = false)
    @NotNull(message = "Informe se campo do layout é obrigatório")
    @Size(max = 1, message = "Campo obrigatório deve ter 1 caractere")
    @Column(name = "OBRIGATORIO")
    private String obrigatorio;

    @Size(max = 1, message = "Campo separador de casas decimais deve ter no maximo 1 caractere")
    @Column(name = "SEPARADOR_CASAS_DECIMAIS")
    private String separadorCasasDecimais;

    @Size(max = 10, message = "A mascara data deve ter no máximo 10 caracteres")
    @Column(name = "MASCARA_DATA")
    private String mascaraData;

    @Size(max = 255, message = "O texto fixo deve ter no máximo 255 caracteres")
    @Column(name = "TEXTO_FIXO")
    private String textoFixo;

    @Column(name = "PREENCHE_ESPACO_VAZIO")
    @Size(max = 2, message = "Campo preenche espaco vazio deve ter no máximo 2 caracteres")
    private String preencheEspacoVazio;

    @Column(name = "LAYOUT_CAMPO_NOVO")
    private String layoutCampoNovo;

    @JoinColumn(name = "ID_LAYOUT", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Layout idLayout;

    @NotNull(message = "Informe o tipo do layout")
    @Column(name = "TIPO")
    private String tipo;

    public LayoutCampo(Integer id) {
        this.id = id;
    }

    public LayoutCampo(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public LayoutCampo(String descricao, String obrigatorio, String tipoDado) {
        this.obrigatorio = obrigatorio;
        this.descricao = descricao;
        this.tipoDado = tipoDado;
    }

    public LayoutCampo(String descricao, String obrigatorio, String tipoDado, String tipo) {
        super();
        this.descricao = descricao;
        this.obrigatorio = obrigatorio;
        this.tipoDado = tipoDado;
        this.layoutCampoNovo = "N";
        this.tipo = tipo;
    }

    @Override
    public LayoutCampo clone() {
        try {
            return (LayoutCampo) super.clone();
        } catch (CloneNotSupportedException ex) {
            return new LayoutCampo();
        }
    }

    @Override
    public String toString() {
        return "LayoutCampo{" + "id=" + id + '}';
    }

}
