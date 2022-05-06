package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.servicos.ModeloImportacaoService;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoLayout;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeloImportacaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ModeloImportacaoService modeloImportacaoService;

    public List<EnumTipoLayout> getListModelo() {
        List<EnumTipoLayout> modelos = new LinkedList<>();
        modelos.add(EnumTipoLayout.CLIENTE);
        modelos.add(EnumTipoLayout.FORNECEDOR);
        modelos.add(EnumTipoLayout.SERVICO);
        modelos.add(EnumTipoLayout.PRODUTO);

        return modelos;
    }

    public StreamedContent downloadModelo(String tipo) {
        try {
            return modeloImportacaoService.downloadModelo(tipo);
        } catch (Exception ex) {
            return null;
        }
    }

}
