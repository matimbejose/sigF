package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Layout;
import br.com.villefortconsulting.sgfinancas.entidades.LayoutCampo;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoLayout;
import br.com.villefortconsulting.util.FileUtil;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import org.primefaces.model.StreamedContent;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ModeloImportacaoService extends BasicService<EntityId, BasicRepository<EntityId>, BasicFilter<EntityId>> {

    private static final long serialVersionUID = 1L;

    @EJB
    private LayoutService layoutService;

    public StreamedContent downloadModelo(String tipo) throws FileException {
        Layout layout = layoutService.getLayoutPorTipo(tipo);
        List<LayoutCampo> list = layoutService.getLayoutCampos(layout);

        String cabecalho = "";
        cabecalho = list.stream()
                .map(lc -> lc.getDescricao() + ";")
                .reduce(cabecalho, String::concat);
        cabecalho += ";";

        List<String> texto = new LinkedList<>();
        texto.add(cabecalho);

        String nome = "modelo " + EnumTipoLayout.getDescricaoPorTipo(tipo);
        String extensao = "csv";
        File file = FileUtil.createTxtFile(nome, extensao, texto);
        // A codificação padrão para csv no Excel e no LibreOffice é ISO-8859-1
        byte[] conteudo = new String(FileUtil.convertFileToBytes(file), StandardCharsets.UTF_8).getBytes(StandardCharsets.ISO_8859_1);
        return FileUtil.downloadFile(conteudo, "text/csv", nome + "." + extensao);
    }

}
