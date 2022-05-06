package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumCancelamentoNotaNFS;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.primefaces.model.StreamedContent;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotaServicoService extends BasicService<EntityId, BasicRepository<EntityId>, BasicFilter<EntityId>> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    /*
     * Ver link http://www.javac.com.br/jc/posts/list/286.page
     */
    public StreamedContent gerarNotaFiscalServico(Empresa empresa, NFS notaFiscalServico) throws JRException, FileException {
        byte[] bFile = gerarNotaFiscalServicoByte(notaFiscalServico, empresa);

        return FileUtil.downloadFile(bFile, "application/pdf", "notaServico.pdf");
    }

    public byte[] gerarNotaFiscalServicoByte(NFS notaFiscalServico, Empresa empresa) throws JRException {
        JasperReport jr = JasperCompileManager.compileReport(ClienteService.class.getResourceAsStream("NotaServico.jrxml"));
        InputStream is = new ByteArrayInputStream(notaFiscalServico.getXmlArmazenamento().getBytes(StandardCharsets.UTF_8));
        Map<String, Object> param = new HashMap<>();
        if (empresa.getLogo() != null) {
            InputStream image = new ByteArrayInputStream(empresa.getLogo());
            param.put("LOGO", image);
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        param.put("BRASAO_PREFEITURA", classLoader.getResourceAsStream("images/brasao.png"));
        param.put("BH_NOTA_10", classLoader.getResourceAsStream("images/bh_nota_10.png"));
        param.put("CODIGO(CTISS)", notaFiscalServico.getIdCtiss().getCodigo() + " / " + notaFiscalServico.getIdCtiss().getDescricao());
        param.put("SUBITEM_LISTA", notaFiscalServico.getIdCtiss().getSubitem() + " / " + notaFiscalServico.getIdCtiss().getDescricao());
        param.put("COD_MUNICIPIO", notaFiscalServico.getIdCidadeCliente().getCodigoIBGE() + " / " + notaFiscalServico.getIdCidadeCliente().getDescricao());
        param.put("MUNICIPIO", notaFiscalServico.getIdCidadeCliente().getDescricao());
        param.put("MUNICIPIO2", empresa.getEndereco().getIdCidade().getDescricao());
        param.put("COD_MUNICIPIO2", notaFiscalServico.getIdMunicipioISSQN().getCodigoIBGE() + " / " + notaFiscalServico.getIdMunicipioISSQN().getDescricao());
        boolean ehCancelada = notaFiscalServico.getSituacao().equals(EnumSituacaoNF.CANCELADA.getSituacao());
        param.put("cancelamento", ehCancelada);
        if (ehCancelada) {
            param.put("DATA_CANCELAMENTO", "EM  " + new SimpleDateFormat("dd/MM/yyyy").format(notaFiscalServico.getDataCancelamento()) + " - " + EnumCancelamentoNotaNFS.getDescricao(notaFiscalServico.getCodigoCancelamento()));
        }
        return JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(jr, param, new JRXmlDataSource(is, "/CompNfse/Nfse/InfNfse")));
    }

}
