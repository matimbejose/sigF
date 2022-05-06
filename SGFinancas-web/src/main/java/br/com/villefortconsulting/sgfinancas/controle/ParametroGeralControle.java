package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroGeral;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.DocumentoMapper;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroGeralService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParametroGeralControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ParametroGeralService service;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    @Inject
    protected DocumentoMapper documentoMapper;

    private ParametroGeral parametroGeralSelecionado;

    public static final String PATH_CADASTRO = "/restrito/parametroGeral/cadastroParametroGeral.xhtml";

    private List<ParametroDTO> listaServerProperties;

    private transient List<AnexoDTO> listaAnexos;

    private List<ParametroDTO> variaveisEmail;

    @PostConstruct
    public void postConstruct() {
        parametroGeralSelecionado = service.getParametro();

        if (parametroGeralSelecionado == null) {
            parametroGeralSelecionado = new ParametroGeral();
        }
        updateList();
        listaAnexos = new ArrayList<>();
        if (parametroGeralSelecionado.getIdDocumentoAnexoEmailCadastro() != null && !parametroGeralSelecionado.getIdDocumentoAnexoEmailCadastro().getDocumentoAnexoList().isEmpty()) {
            listaAnexos = parametroGeralSelecionado.getIdDocumentoAnexoEmailCadastro().getDocumentoAnexoList().stream()
                    .map(documentoMapper::toDTO)
                    .collect(Collectors.toList());
        }
        variaveisEmail = new ArrayList<>();
        variaveisEmail.add(new ParametroDTO("{{NOME_EMPRESA}}", "Nome da empresa cadastrada", ""));
        variaveisEmail.add(new ParametroDTO("{{EMAIL_EMPRESA}}", "Email da empresa cadastrada", ""));
        variaveisEmail.add(new ParametroDTO("{{DATA_CADASTRO}}", "Data do cadastro da empresa", ""));
        variaveisEmail.add(new ParametroDTO("{{USUARIO_CADASTRO}}", "Nome do usuário cadsatrado", ""));
    }

    public List<ProdutoCategoria> getCategorias() {
        return produtoService.listarCategoria();
    }

    public void updateList() {
        SystemPreferencesUtil.reset();
        Map<String, String> mapa = SystemPreferencesUtil.getProps("");
        listaServerProperties = new ArrayList<>();
        mapa.forEach((k, v) -> {
            if (!k.startsWith("sec.")) {
                listaServerProperties.add(new ParametroDTO(k, v, ""));
            }
        });
        Collections.sort(listaServerProperties, (a, b) -> a.getName().compareTo(b.getName()));
    }

    public String doFinishAdd() {
        try {
            Documento removerDocumento = null;
            if (!listaAnexos.isEmpty()) {
                Documento doc;
                if (parametroGeralSelecionado.getIdDocumentoAnexoEmailCadastro() == null) {
                    doc = documentoService.criarDocumento(getUsuarioLogado(), "Anexos do contrato");
                } else {
                    doc = documentoService.buscar(parametroGeralSelecionado.getIdDocumentoAnexoEmailCadastro().getId());
                }
                doc = documentoAnexoService.persistirAnexoDTO(doc, getUsuarioLogado(), listaAnexos);
                parametroGeralSelecionado.setIdDocumentoAnexoEmailCadastro(doc);
                documentoService.salvar(doc);
            } else if (parametroGeralSelecionado.getIdDocumentoAnexoEmailCadastro() != null) {
                removerDocumento = parametroGeralSelecionado.getIdDocumentoAnexoEmailCadastro();
                parametroGeralSelecionado.setIdDocumentoAnexoEmailCadastro(null);
            }
            service.salvar(parametroGeralSelecionado);
            if (removerDocumento != null) {
                documentoService.remover(removerDocumento);
            }
            createFacesSuccessMessage("Parâmetro salvo.");
        } catch (Exception ex) {
            createFacesErrorMessage("Não foi possível salvar o parâmetro. Consulte o log para mais detalhes.");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "cadastroParametroGeral.xhtml";
    }

    public String doDiscartChanges() {
        postConstruct();
        return "cadastroParametroGeral.xhtml";
    }

    public void setPart(FileUploadEvent event) {
        AnexoDTO anexo = new AnexoDTO();
        try {
            anexo.setConteudo("data:" + event.getFile().getContentType() + ";base64," + Base64.getEncoder().encodeToString(IOUtils.toByteArray(event.getFile().getInputstream())));
            anexo.setDataEnvio(new Date());
            anexo.setIdUsuarioEnvio(getUsuarioLogado().getId());
            anexo.setNomeUsuarioEnvio(getUsuarioLogado().getNome());
            anexo.setMimeType(event.getFile().getContentType());
            anexo.setNome(event.getFile().getFileName());
            listaAnexos.add(anexo);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String removerArquivos() {
        listaAnexos.clear();
        return "cadastroParametroGeral.xhtml";
    }

    public String removerArquivo(AnexoDTO anexo) {
        if (listaAnexos.contains(anexo)) {
            listaAnexos.remove(anexo);
        }
        return "cadastroParametroGeral.xhtml";
    }

}
