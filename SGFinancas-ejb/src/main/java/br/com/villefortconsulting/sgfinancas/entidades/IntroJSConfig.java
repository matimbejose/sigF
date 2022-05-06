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

@Entity
@Table(name = "INTROJS_CONFIG")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class IntroJSConfig extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o id da página")
    @Size(max = 100, message = "Tamanho máximo do id 100 caracteres")
    @Column(name = "PAGE_ID")
    private String pageId;

    @NotNull(message = "Informe se o tutorial tem a exibição forçada")
    @Size(max = 1)
    @Column(name = "FORCA_EXIBICAO")
    private String forcaExibicao;

    @NotNull(message = "Informe o número de revisão")
    @Column(name = "REVISAO")
    private Integer revisao;

    @NotNull(message = "Informe o js de configuração")
    @Column(name = "JS")
    private String js;

    public String getScript() {
        if (js == null || js.trim().isEmpty()) {
            return "";
        }
        String append = "";

        if ("S".equals(forcaExibicao)) {
            append = "startIntroJS(" + (revisao != null ? revisao.toString() : "1") + ", `" + pageId + "`)";
        }

        return "addIntroJS();"
                + "waitForIntrosJS(() => {"
                + "configIntroJS();"
                + js
                + ";showIntroJsBtn();"
                + append
                + "})";
    }

    @Override
    public String toString() {
        return "IntroJSConfig{id=" + id + '}';
    }

}
