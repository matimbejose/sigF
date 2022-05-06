package br.com.villefortconsulting.sgfinancas.controle.conversores;

import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoServicoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

@Named
@RequestScoped
public class ProdutoServicoDTOConverter implements Converter {

    @EJB
    private ProdutoService produtoService;

    @EJB
    private ServicoService servicoService;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }

        try {
            String[] partes = valor.split("-");
            if (partes.length != 2) {
                return null;
            }
            if (partes[0].equals("P")) {
                return new ProdutoServicoDTO(produtoService.buscar(Integer.parseInt(partes[1])));
            } else if (partes[0].equals("S")) {
                return new ProdutoServicoDTO(servicoService.buscar(Integer.parseInt(partes[1])));
            }
        } catch (Exception ex) {
            Logger.getLogger(ProdutoServicoDTOConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (!(o instanceof ProdutoServicoDTO)) {
            return "";
        }

        return ((ProdutoServicoDTO) o).getIdProdutoServico();
    }

}
