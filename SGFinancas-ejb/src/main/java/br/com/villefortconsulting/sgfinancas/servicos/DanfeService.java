package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.util.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.primefaces.model.StreamedContent;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DanfeService extends BasicService<EntityId, BasicRepository<EntityId>, BasicFilter<EntityId>> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    /*
     * Ver link http://www.javac.com.br/jc/posts/list/286.page
     */
    public byte[] gerarDanfeBtye(Empresa empresa, NF nf) throws JRException {
        JasperReport jr = JasperCompileManager.compileReport(ClienteService.class.getResourceAsStream("danfeR.jrxml"));
        InputStream is = new ByteArrayInputStream(nf.getXmlArmazenamento().getBytes(StandardCharsets.UTF_8));

        Map<String, Object> param = new HashMap<>();
        if (empresa.getLogo() != null) {
            InputStream image = new ByteArrayInputStream(empresa.getLogo());
            param.put("Logo", image);
        }

        JRXmlDataSource xml = new JRXmlDataSource(is, "/nfeProc/NFe/infNFe/det");
        JasperPrint jp = JasperFillManager.fillReport(jr, param, xml);
        return JasperExportManager.exportReportToPdf(jp);
    }

    public StreamedContent gerarDanfe(Empresa empresa, NF nf) throws JRException, FileException {
        return FileUtil.downloadFile(gerarDanfeBtye(empresa, nf), "application/pdf", "danfe.pdf");
    }

}
