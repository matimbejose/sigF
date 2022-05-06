package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaContatoCliente;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaContatoClienteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaContatoClienteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private Empresa empresaSelecionada;

    private Date dataProximoContato;

    private EmpresaContatoCliente empresaContatoClienteSelecionado;

    private LazyDataModel<EmpresaContatoCliente> model;

    private EmpresaContatoClienteFiltro filtro = new EmpresaContatoClienteFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                empresaService::quantidadeRegistrosFiltradosEmpresaContatoCliente,
                empresaService::getListaFiltradaEmpresaContatoCliente,
                filter -> {
                    filter.setEmpresa(empresaSelecionada);
                    filter.setDataProximoContato(dataProximoContato);
                });
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CLIENTE.getChave()).getDescricao());
        return "cadastroEmpresaContatoCliente.xhtml";
    }

    @Override
    public void cleanCache() {
        empresaContatoClienteSelecionado = new EmpresaContatoCliente();
        empresaContatoClienteSelecionado.setIdUsuarioRegistro(getUsuarioLogado());
        empresaContatoClienteSelecionado.setIdEmpresa(empresaSelecionada);
        empresaContatoClienteSelecionado.setDataRegistro(DataUtil.getHoje());
    }

    public String doShowContatos() {
        dataProximoContato = null;
        return "/restrito/contatoCliente/listaEmpresaContatoCliente.xhtml";
    }

    public String doShowContatosHoje() {
        dataProximoContato = DataUtil.getHoje();
        empresaSelecionada = empresaService.getEmpresa();
        return "/restrito/contatoCliente/listaEmpresaContatoCliente.xhtml";
    }

    public String doStartAdd() {
        cleanCache();
        return "/restrito/contatoCliente/cadastroEmpresaContatoCliente.xhtml";
    }

    public String doStartAlterar() {
        return "/restrito/contatoCliente/cadastroEmpresaContatoCliente.xhtml";
    }

    public String doFinishAdd() {

        empresaService.salvarEmpresaContatoCliente(empresaContatoClienteSelecionado);

        createFacesSuccessMessage("Contato salvo com sucesso!");
        return "/restrito/contatoCliente/listaEmpresaContatoCliente.xhtml";
    }

    public String doFinishExcluir() {
        empresaService.removeEmpresaContatoCliente(empresaContatoClienteSelecionado);
        return "/restrito/contatoCliente/listaEmpresaContatoCliente.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaEmpresaContatoCliente.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return empresaService.getDadosAuditoriaByIDEmpresaContatoCliente(empresaContatoClienteSelecionado);
    }

}
