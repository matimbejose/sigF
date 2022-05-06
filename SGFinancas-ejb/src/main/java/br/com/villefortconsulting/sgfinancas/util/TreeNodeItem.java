package br.com.villefortconsulting.sgfinancas.util;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNodeItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

    private final String style;

    public TreeNodeItem(String value) {
        this.value = value;
        this.style = "";
    }

    public TreeNodeItem(String value, String style) {
        this.value = value;
        this.style = style;
    }

}
