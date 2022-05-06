package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "forma de pagamento")
public class FormaPagamentoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Descricao", obrigatorio = true)
    private String descricao;

    private List<String> bandeirasList;

    private String codigoNfe;

    public FormaPagamentoDTO(Integer id, String descricao, List<String> bandeirasList) {
        this.id = id;
        this.descricao = descricao;
        this.bandeirasList = bandeirasList;
    }

    public String getBandeiras() {
        if (bandeirasList == null) {
            return "";
        }
        String aux = bandeirasList.stream().map(bandeira -> "," + bandeira).reduce("", String::concat);
        if (aux.length() > 1) {
            aux = aux.substring(1);
        }
        return aux;
    }

    public void setBandeiras(String list) {
        if (list == null) {
            bandeirasList = new ArrayList<>();
        } else {
            bandeirasList = Arrays.asList(list.split(","));
        }
    }

    public String getDescricaoNfe() {
        if (codigoNfe == null) {
            return null;
        }
        EnumMeioDePagamento e = EnumMeioDePagamento.retornaEnumSelecionado(codigoNfe);
        return e == null ? null : e.getDescricao();
    }

    public EnumMeioDePagamento getEnumNfe() {
        return EnumMeioDePagamento.retornaEnumSelecionado(codigoNfe);
    }

    public void setEnumNfe(EnumMeioDePagamento e) {
        if (e == null) {
            codigoNfe = null;
            return;
        }
        codigoNfe = e.getCodigo();
    }

}
