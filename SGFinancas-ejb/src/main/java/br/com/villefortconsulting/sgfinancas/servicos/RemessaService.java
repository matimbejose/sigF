package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Remessa;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.RemessaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.BoletoException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.RemessaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.RemessaUtil;
import br.com.villefortconsulting.util.DataUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
import org.apache.commons.io.FileUtils;
import org.hibernate.Criteria;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;
import org.jrimum.texgit.Texgit;
import org.jrimum.texgit.TexgitException;
import org.springframework.util.StringUtils;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RemessaService extends BasicService<Remessa, RemessaRepositorio, RemessaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private BoletoService boletoService;

    @EJB
    private DocumentoService documentoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new RemessaRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(RemessaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addEqRestrictionTo(criteria, "idContaBancaria", filter.getContaBancaria());
        addRestrictionTo(criteria, "dataEmissao", filter.getData());

        return criteria;
    }

    public void gerarArquivoRemessa(Empresa empresa, List<br.com.villefortconsulting.sgfinancas.entidades.Boleto> boletos, Usuario usuarioLogado) throws BoletoException {

        try {

            // Conta bancaria sistema
            ContaBancaria contaSistema = boletos.get(0).getIdContaBancaria();
            String codigoCompensacao = contaSistema.getIdBanco().getNumero();

            // Conta bancaria ffebraban
            BancosSuportados banco = BancosSuportados.suportados.get(codigoCompensacao);
            org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria contaFebraban = converterContaSistemaEmContaFebraban(banco, contaSistema);

            // Carrega o arquivo XML de acordo com o banco
            FlatFile<Record> ff = Texgit.createFlatFile(buscarLayoutCnab(banco));

            // Cabecalho
            ff.addRecord(RemessaUtil.createHeader(ff, contaFebraban, empresa.getDescricao(), banco));

            // Corpo
            int numeroSequencialRegistro = 2; //Número de linhas no arquivo. Header é a primeira linha.
            for (br.com.villefortconsulting.sgfinancas.entidades.Boleto boleto : boletos) {
                ff.addRecord(RemessaUtil.transacaoTitulo(ff, banco, empresa, boleto, contaSistema, contaFebraban, numeroSequencialRegistro));
                numeroSequencialRegistro++;
            }

            //Grava o rodape
            ff.addRecord(RemessaUtil.createTrillerRecord(ff, banco, boletos, numeroSequencialRegistro));

            // Cria arquivo de remeesa
            String arquivo = "REM" + numeroSequencialRegistro;
            File file = new File(arquivo, ".REM");
            FileUtils.writeLines(file, ff.write(), "\r\n");
            Documento documento = documentoService.criarDocumento(usuarioLogado, arquivo + "REM", file);

            // Salva remessa
            Remessa remessa = new Remessa();
            remessa.setIdDocumento(documento);
            remessa.setQtdParcelas(boletos.size());
            remessa.setDataEmissao(DataUtil.getHoje());
            remessa.setIdUsuarioResponsavel(usuarioLogado);
            remessa.setIdContaBancaria(contaSistema);
            remessa.setTenatID(adHocTenant.getTenantID());

            salvar(remessa);

            // Atualiza os boletos
            boletos.stream()
                    .map(boleto -> {
                        boleto.setIdRemessa(remessa);
                        boleto.setGerouRemessa("S");
                        return boleto;
                    })
                    .forEachOrdered(boletoService::salvar);
        } catch (FileException | BoletoException | IOException | TexgitException ex) {
            throw new BoletoException(ex.getCause().getMessage(), ex);
        }
    }

    private static org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria converterContaSistemaEmContaFebraban(BancosSuportados banco, ContaBancaria contaSistema) throws BoletoException {
        org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta numeroDaConta;
        if (banco == BancosSuportados.BANCO_SANTANDER) {
            if (StringUtils.isEmpty(contaSistema.getCedente())) {
                StringBuilder mensagemCedente = new StringBuilder();
                mensagemCedente.append("Código do cedente inválido ou não informado no cadastro da conta bancária. ");
                mensagemCedente.append("(OBS.: Código cedente é um número que serve para podermos fazer emissão de boleto )");
                mensagemCedente.append("bancário em nome da sua empresa. Caso você não possua este código (que não é ");
                mensagemCedente.append("necessariamente o número da sua conta), basta ligar para o gerente do seu banco ");
                mensagemCedente.append("e solicitar esta informação.");
                throw new BoletoException(mensagemCedente.toString(), null);
            }

            numeroDaConta = new NumeroDaConta(Integer.parseInt(contaSistema.getCedente()));
        } else if (StringUtils.isEmpty(contaSistema.getDigitoConta())) {
            numeroDaConta = new NumeroDaConta(Integer.parseInt(contaSistema.getConta()));
        } else {
            numeroDaConta = new NumeroDaConta(Integer.parseInt(contaSistema.getConta()), contaSistema.getDigitoConta());
        }

        Carteira carteira = new Carteira(contaSistema.getCarteira());

        org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria contaFebraban = new org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria();
        org.jrimum.domkee.financeiro.banco.febraban.Agencia agencia = (StringUtils.isEmpty(contaSistema.getDigitoAgencia())) ? new Agencia(Integer.parseInt(contaSistema.getAgencia())) : new Agencia(Integer.parseInt(contaSistema.getAgencia()), contaSistema.getDigitoAgencia());
        contaFebraban.setAgencia(agencia);
        contaFebraban.setNumeroDaConta(numeroDaConta);
        contaFebraban.setCarteira(carteira);

        return contaFebraban;
    }

    private File buscarLayoutCnab(BancosSuportados banco) throws BoletoException {
        String arquivo = "";
        switch (banco) {
            case BANCO_ITAU:
                arquivo = "leiaute_remessa_itau_400.txg.xml";
                break;
            case BANCO_SANTANDER:
                arquivo = "leiaute_remessa_santander_400.txg.xml";
                break;
            case CAIXA_ECONOMICA_FEDERAL:
                arquivo = "leiaute_remessa_caixa_400.txg.xml";
                break;
            case BANCO_BRADESCO:
                arquivo = "leiaute_remessa_bradesco_400.txg.xml";
                break;
            case BANCO_DO_BRASIL:
                arquivo = "leiaute_remessa_brasil_400.txg.xml";
                break;
            case HSBC:
                arquivo = "leiaute_remessa_hsbc_400.txg.xml";
                break;
            case BANCOOB:
                arquivo = "leiaute_remessa_sicoob_400.txg.xml";
                break;
            default:
                throw new BoletoException(" Banco não suportado para gerar remessa. ", null);
        }
        String diretorio = this.getClass().getResource("").getPath().replaceAll("%20", " ").replaceAll("/servicos", "/cnab");
        return new File(diretorio + "/" + arquivo);
    }

}
