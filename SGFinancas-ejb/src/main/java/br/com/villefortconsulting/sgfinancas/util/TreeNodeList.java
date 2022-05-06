package br.com.villefortconsulting.sgfinancas.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class TreeNodeList implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String title;

    private final String planoConta;

    private final List<TreeNodeItem> items;

    private final String type;

    public TreeNodeList(String title, String planoConta, String type) {
        this.title = title;
        this.planoConta = planoConta;
        this.items = new ArrayList<>();
        this.type = type;
        addFirst(title);
    }

    public TreeNodeList(String title, String planoConta, List<TreeNodeItem> items, String type) {
        this.title = title;
        this.planoConta = planoConta;
        this.items = new ArrayList<>();
        this.type = type;
        addFirst(title);
        this.items.addAll(items);
    }

    private void addFirst(String title) {
        this.items.add(new TreeNodeItem(title, "header"));
    }

    public void addItem(TreeNodeItem item) {
        items.add(item);
    }

    public void addItem(String value) {
        items.add(new TreeNodeItem(value));
    }

}
