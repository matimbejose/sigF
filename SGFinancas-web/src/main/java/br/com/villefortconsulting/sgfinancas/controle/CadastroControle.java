package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.enums.EnumTipoDadoImportacao;
import br.com.villefortconsulting.exception.MessageListException;
import br.com.villefortconsulting.sgfinancas.controle.conversores.GenericConverter;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImportacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.UsuarioMapper;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusCadastro;
import br.com.villefortconsulting.sgfinancas.util.ImportadorConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.component.selectcheckboxmenu.SelectCheckboxMenu;
import org.primefaces.component.selectonemenu.SelectOneMenu;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastroControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @Inject
    private EmpresaControle empresaControle;

    @Inject
    private GenericConverter genericConverter;

    @Inject
    private UsuarioMapper usuarioMapper;

    // Visão geral
    private Empresa empresa;

    private UsuarioDTO usuarioDTO;

    private List<StatusCadastroDTO> listaCadastro;

    // Importacao
    private ImportacaoDTO importacaoDTO;

    private String codificacao = "ISO-8859-1";

    private String delimitador;

    private boolean pularLinhasIncompletas;

    private String bidings;

    private Class<?> importerClass;

    private transient ImportadorConsumer<List<DtoId>, List<EntityId>> importadorDoControle;

    private String id;

    private String type;

    public String invokeStatusMethod(StatusCadastroDTO status) {
        try {
            return status.getCallBack().get();
        } catch (Exception ex) {
            return null;
        }
    }

    private static List<ImportacaoDTO> convertFileds(Field[] campos, String prefix) {
        List<ImportacaoDTO> lista = new ArrayList<>();
        Arrays.asList(campos).stream()
                .filter(campo -> campo.getAnnotation(Importavel.class) != null)
                .forEach(campo -> {
                    ImportacaoDTO aux = new ImportacaoDTO();
                    Importavel annotation = campo.getAnnotation(Importavel.class);

                    if (annotation.tipo() != EnumTipoDadoImportacao.OBJECT) {
                        aux.setNome(annotation.nome());
                        aux.setNomeCampo(prefix + campo.getName());
                        aux.setObrigatorio(annotation.obrigatorio());
                        aux.setOpcoes(annotation.opcoes());
                        aux.setPadrao(annotation.padrao());
                        aux.setTipo(annotation.tipo());
                        lista.add(aux);
                    } else {
                        lista.addAll(convertFileds(campo.getType().getDeclaredFields(), campo.getName() + "."));
                    }
                });
        return lista;
    }

    public String doStartImport(StatusCadastroDTO status) {
        try {
            if (status == null) {
                throw new CadastroException("Este usuário não tem permissão para realizar essa importação.", null);
            }
            importerClass = status.getClasseDto();
            importadorDoControle = status.getImportador();
            if (importadorDoControle == null || !importerClass.isAnnotationPresent(Importavel.class)) {
                throw new CadastroException("Importação simplificada não configurada para este cadastro, favor entrar em contato com o suporte ou "
                        + "abrir a listagem do mesmo e realizar a importação manual.", null);
            }
            importacaoDTO = new ImportacaoDTO();
            importacaoDTO.setNome(importerClass.getAnnotation(Importavel.class).nome());
            importacaoDTO.setCampos(new ArrayList<>());
            importacaoDTO.setCampos(convertFileds(importerClass.getDeclaredFields(), ""));
            return "/restrito/generic/importador.xhtml";
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CadastroControle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String doStartExport(StatusCadastroDTO status) {
        try {
            if (status == null) {
                throw new CadastroException("Este usuário não tem permissão para realizar essa importação.", null);
            }
            importerClass = status.getClasseDto();
            importadorDoControle = status.getImportador();
            if (importadorDoControle == null || !importerClass.isAnnotationPresent(Importavel.class)) {
                throw new CadastroException("Exportação simplificada não configurada para este cadastro, favor entrar em contato com o suporte ou "
                        + "abrir a listagem do mesmo e realizar a importação manual.", null);
            }
            throw new UnsupportedOperationException("Exportação não implementada");
        } catch (Exception ex) {
            Logger.getLogger(CadastroControle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String doFinishImport() {
        try {
            if (bidings == null || bidings.isEmpty()) {
                throw new CadastroException("Informe e configure o arquivo para a importação!", null);
            }
            if (importadorDoControle == null) {
                throw new CadastroException("Não foi possível realizar a importação!", null);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getDefault());
            List<DtoId> listaImportacao = mapper.readValue(bidings, mapper.getTypeFactory().constructCollectionType(List.class, importerClass));
            if (listaImportacao.isEmpty()) {
                throw new CadastroException("Arquivo de importação vazio. Linhas em vermelho não serão importadas, pois campos obrigatórios não foram preenchidos.", null);
            }
            importadorDoControle.accept(listaImportacao);
            createFacesSuccessMessage("Arquivo importado!");
            return visaoGeral();
        } catch (MessageListException ex) {
            ex.getMessages().forEach(this::createFacesErrorMessage);
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CadastroControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível realizar a importação!");
        }
        return "importador.xhtml";
    }

    public String visaoGeral() {
        usuarioDTO = usuarioMapper.toDTO(getUsuarioLogado());
        empresa = empresaService.getEmpresa();
        listaCadastro = new ArrayList<>();

        listaCadastro.add(new StatusCadastroDTO("Empresa", true, true, empresaControle::doStartAlterarFromVisaoGeral, void.class));

        Arrays.asList("planoConta", "cliente", "produtoCategoria", "produto", "servico", "contaBancaria", "fornecedor", "funcionario",
                "formaPagamento", "transportadora", "contabilidade", "unidadeMedida", "usuario", "centroCusto").stream()
                .map(CadastroControle::getControllerForModel)
                .map(BasicControl::getImportConfig)
                .filter(Objects::nonNull)
                .forEachOrdered(listaCadastro::add);

        // Ao modificar essa lista, adicionar a permissão no menu lateral também
        return "/restrito/cadastro/visaoGeral.xhtml";
    }

    public List<StatusCadastroDTO> getListaCadastro() {
        visaoGeral();
        return listaCadastro;
    }

    public List<StatusCadastroDTO> getListaCadastroNotOk() {
        visaoGeral();
        return listaCadastro.stream()
                .filter(sc -> sc.getStatus() == EnumStatusCadastro.OBRIGATORIO_VAZIO)
                .collect(Collectors.toList());
    }

    public String getImportacaoDtoAsJson() {
        return new Gson().toJson(importacaoDTO);
    }

    public void downloadModel() {
        String[] conteudo = new String[]{"", "", ""};
        String sep = "";
        for (ImportacaoDTO dto : importacaoDTO.getCampos()) {
            conteudo[0] += sep + dto.getNome();
            conteudo[1] += sep + getTypeLabel(dto);
            conteudo[2] += sep + (dto.isObrigatorio() ? "Obrigatório" : "Opcional");
            sep = "\";\"";
        }
        try {
            StringBuilder arquivo = new StringBuilder("\"");
            sep = "";
            for (String s : conteudo) {
                arquivo.append(sep).append(s);
                sep = "\"\n\"";
            }
            arquivo.append("\"");
            byte[] excel = arquivo.toString().getBytes(StandardCharsets.ISO_8859_1);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("text/csv");
            ec.setResponseContentLength(excel.length);
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"Modelo de importação de " + importacaoDTO.getNome() + ".csv\"");
            OutputStream output = ec.getResponseOutputStream();
            output.write(excel);
            fc.responseComplete();
        } catch (Exception ex) {
            Logger.getLogger(CadastroControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível gerar o modelo de importação.");
        }
    }

    private static String getTypeLabel(ImportacaoDTO dto) {
        switch (dto.getTipo()) {
            case STRING:
                return "Texto";
            case INTEGER:
                return "Número";
            case DOUBLE:
                return "Número (com virgula)";
            case ENUM:
                return "Opção da lista";
            case ID_TABELA:
                return "Opção do banco";
            case DATE:
                return "Data";
            default:
                return "";
        }
    }

    public void showAddModal(String entityName, Object context) {
        BasicControl bc = getControllerForModel(entityName);
        bc.initDtoCadastro(context);
        UIComponent uic = loadMenu();
        if (uic != null) {
            String clientId = uic.getClientId();
            id = clientId.startsWith("form:") ? ("widget_" + clientId.replace(':', '_')) : clientId;
        } else {// Permitir uic == null para poder chamar o botão de adicionar sem estar vinculado a um selectOneMenu
            id = null;
            Logger.getLogger(getClass().getName()).log(Level.WARNING, () -> "CadastroControle::loadMenu retornou null. EntityName: " + entityName);
        }
        type = entityName;
        HashMap<String, Object> options = new HashMap<>();
        options.put("width", "800px");
        options.put("height", "400px");
        options.put("resizable", false);
        options.put("draggable", true);
        options.put("modal", true);
        showModal("/restrito/" + entityName + "/modals/cadastro.xhtml", options);
    }

    public void doFinishModal() {
        try {
            BasicControl bc = getControllerForModel(type);
            EntityId entidade = bc.doFinishImport(Arrays.asList(bc.getDtoCadastro())).get(0);
            String entityId = genericConverter.getAsString(null, null, entidade);
            PrimeFaces.current().executeScript("window.parent.autoSelectItem(`" + id + "`, `" + entityId + "`)");
            closeModal();
        } catch (Exception e) {
            createFacesErrorMessage("Fornecedor já cadastrado.");
        }
    }

    private static UIComponent loadMenu() {
        UIComponent component = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        UIComponent previous = null;
        for (UIComponent c : component.getParent().getChildren()) {
            if (c.equals(component)) {
                break;
            }
            previous = c;
        }
        if (previous == null || !(previous instanceof SelectOneMenu || previous instanceof SelectCheckboxMenu)) {
            return null;
        }
        return previous;
    }

    private static BasicControl getControllerForModel(String type) {
        if ("combustivel".equals(type) || "cor".equals(type)) {
            type = "veiculo";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, "#{" + type + "Controle}", BasicControl.class);
    }

    public List<String> getListaCadastroFiller() {
        return Arrays.asList("", "", "", "", "", "");
    }

}
