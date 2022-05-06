package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.cnab.ArquivoRetorno;
import br.com.villefortconsulting.sgfinancas.entidades.Boleto;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Remessa;
import br.com.villefortconsulting.sgfinancas.entidades.Retorno;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ConciliacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.RetornoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.RetornoRepositorio;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import org.apache.commons.io.FileUtils;
import org.hibernate.Criteria;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RetornoService extends BasicService<Retorno, RetornoRepositorio, RetornoFiltro> {

    private static final long serialVersionUID = 1L;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private BoletoService boletoService;

    @EJB
    private RemessaService remessaService;

    @EJB
    private ContaService contaService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new RetornoRepositorio(em, adHocTenant);
    }

    public Retorno updloadArquivo(Retorno retorno, Usuario usuarioLogado, Part part) throws FileException {
        retorno.setIdUsuarioResponsavel(usuarioLogado);
        retorno.setIdDocumento(documentoService.criarDocumento(usuarioLogado, part.getName(), part));
        retorno.setDataImportacao(DataUtil.getHoje());
        retorno.setTenatID(adHocTenant.getTenantID());

        return salvar(retorno);
    }

    @Override
    public Criteria getModel(RetornoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addEqRestrictionTo(criteria, "idContaBancaria", filter.getContaBancaria());
        addRestrictionTo(criteria, "dataEmissao", filter.getData());

        return criteria;
    }

    public List<ConciliacaoDTO> buscarParcelasRetorno(Retorno retorno) {
        // Lendo do arquivo de retorno o nosso numero correspondente as parcelas a serem baixadas
        Collection<String> listaNossoNumero = extrairListaNossoNumero(retorno);

        List<ConciliacaoDTO> listaConciliacaoDTO = new LinkedList<>();
        for (String nossoNumero : listaNossoNumero) {
            Boleto boleto = boletoService.buscarBoletoPorNossoNumero(nossoNumero, retorno.getIdContaBancaria());

            ConciliacaoDTO c = new ConciliacaoDTO();
            c.setNossoNumero(nossoNumero);
            c.setBoleto(boleto);

            listaConciliacaoDTO.add(c);
        }

        return listaConciliacaoDTO;
    }

    public Retorno processarRetorno(Retorno retorno, List<ConciliacaoDTO> listaConciliacaoDTO) throws ContaException {
        List<Boleto> boletos = listaConciliacaoDTO.stream().map(ConciliacaoDTO::getBoleto).distinct().collect(Collectors.toList());

        Remessa remessa = null;
        int i = 0;
        for (Boleto boleto : boletos) {
            if (i == 0) {
                remessa = boleto.getIdRemessa();
                remessa.setProcessada("S");

                remessaService.salvar(remessa);
                i++;
            }

            ContaParcela parcela = boleto.getIdContaParcela();
            contaService.pagarParcela(parcela, boleto.getValorPago());

            boleto.setProcessada("S");
            boletoService.salvar(boleto);
        }

        retorno.setIdRemessa(remessa);
        retorno.setProcessada("S");
        return salvar(retorno);
    }

    private Collection<String> extrairListaNossoNumero(Retorno retorno) {
        try {
            DocumentoAnexo arquivo = documentoAnexoService.buscarUltimoAnexoDocumento(retorno.getIdDocumento());
            File file = FileUtil.convertByteToFile(arquivo.readFromFile(), arquivo.getNomeArquivo());
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            ArquivoRetorno ff = ArquivoRetorno.newInstance(lines);

            return ff.getColecaoDeNossoNumero();
        } catch (Exception ex) {
            throw new CadastroException("Erro ao extrair conteúdo do arquivo de retorno: " + ex.getMessage(), ex);
        }
    }

}
