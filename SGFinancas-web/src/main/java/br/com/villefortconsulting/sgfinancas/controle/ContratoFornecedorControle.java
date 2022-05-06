package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.controle.apoio.ContratoControleBase;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContrato;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
public class ContratoFornecedorControle extends ContratoControleBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
    }

    @Override
    public EnumTipoContrato getTipoContrato() {
        return EnumTipoContrato.FORNECEDOR;
    }

}
