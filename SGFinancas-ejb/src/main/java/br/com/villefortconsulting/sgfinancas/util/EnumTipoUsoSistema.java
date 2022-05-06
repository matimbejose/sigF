package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoUsoSistema {

    PRODUTOS_APENAS("P", "Vende produtos", true, false),
    SERVICOS_APENAS("S", "Vende serviços", false, true),
    AMBOS("A", "Vende produtos e serviços", true, true),
    MEI("M", "Sou MEI", true, true);

    private final String tipo;

    private final String descricao;

    private final boolean vendeProdutos;

    private final boolean vendeServicos;

    public static EnumTipoUsoSistema retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public String getProdutoServicoLabel() {
        StringBuilder sb = new StringBuilder();
        if (vendeProdutos) {
            sb.append("produto");
            if (vendeServicos) {
                sb.append("/");
            }
        }
        if (vendeServicos) {
            sb.append("serviço");
        }
        return sb.toString();
    }

    public String getProdutoServicoTitleLabel() {
        StringBuilder sb = new StringBuilder();
        if (vendeProdutos) {
            sb.append("Produto");
            if (vendeServicos) {
                sb.append("/");
            }
        }
        if (vendeServicos) {
            sb.append("Serviço");
        }
        return sb.toString();
    }

    public String getProdutoServicoLabelPlural() {
        StringBuilder sb = new StringBuilder();
        if (vendeProdutos) {
            sb.append("produtos");
            if (vendeServicos) {
                sb.append("/");
            }
        }
        if (vendeServicos) {
            sb.append("serviços");
        }
        return sb.toString();
    }

}
