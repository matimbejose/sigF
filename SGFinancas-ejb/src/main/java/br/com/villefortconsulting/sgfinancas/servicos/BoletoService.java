package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.cnab.LayoutCnab;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.BoletoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.BoletoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.BoletoException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.BoletoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.BoletoUtil;
import br.com.villefortconsulting.sgfinancas.util.CampoLivreCefSIGCB;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.ParametrosBancariosMap;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeCobranca;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.hsbc.TipoIdentificadorCNR;
import org.primefaces.PrimeFaces;
import org.springframework.util.StringUtils;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoService extends BasicService<br.com.villefortconsulting.sgfinancas.entidades.Boleto, BoletoRepositorio, BoletoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private ContaService contaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private UsuarioService usuarioService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new BoletoRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(BoletoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idContaParcela", "idContaParcela");
        criteria.createAlias("idContaParcela.idConta", "idConta");
        criteria.createAlias("idConta.idPlanoConta", "idPlanoConta");

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(filter.getDescricao())) {
            Criterion c1 = Restrictions.ilike("nossoNumero", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion c2 = Restrictions.ilike("idPlanoConta.descricao", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion c3 = Restrictions.ilike("idConta.observacao", filter.getDescricao(), MatchMode.ANYWHERE);

            criteria.add(Restrictions.disjunction(c1, c2, c3));
        }

        if (filter.getContaBancaria() != null) {
            criteria.add(Restrictions.eq("idContaBancaria", filter.getContaBancaria()));
        } else {
            criteria.add(Restrictions.isNull("id"));
        }

        addRestrictionTo(criteria, "dataVencimento", filter.getData());

        return criteria;
    }

    public Integer buscarNumeroViaBoleto(ContaParcela parcela) {
        Long numeroVia = repositorio.buscarNumeroViaBoleto(parcela);
        if (numeroVia == null) {
            return 1;
        } else {
            return numeroVia.intValue() + 1;
        }
    }

    public void gerarBoletoPara(TransacaoGetnet transacao) throws BoletoException {
        if (transacao.getLinkBoleto() == null || transacao.getLinkBoleto().isEmpty()) {
            throw new BoletoException("Não foi possível salvar o boleto", null);
        }
        PrimeFaces.current().executeScript("window.open(`" + transacao.getLinkBoleto() + "`, `_blank`)");
    }

    public ContaParcela emitirBoletoParcela(ContaParcela parcela, Empresa empresa, Usuario usuarioLogado, Integer numeroVia) throws BoletoException, FileException, IOException {

        ContaBancaria contaSistema = parcela.getIdContaBancaria();

        BancosSuportados banco = BancosSuportados.suportados.get(BoletoUtil.completaCampo(contaSistema.getIdBanco().getNumero(), 3));

        Cliente cliente = parcela.getIdConta().getIdCliente();

        // Popular informações iniciais
        BoletoDTO boletoDTO = preencherBoletoDTO(parcela, empresa, contaSistema, cliente, numeroVia);

        // cedente
        Cedente cedente = new Cedente(boletoDTO.getNomeCedente(), boletoDTO.getCpfCNPJCedente());

        // sacado
        Sacado sacado = preencherSacado(boletoDTO);

        // sacador avalista
        org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria contaBancaria = preencherContaBancaria(banco, contaSistema);

        // titulo
        Titulo titulo = preencherTitulo(boletoDTO, contaBancaria, sacado, cedente);

        // instrucoes
        Boleto boleto = preencherBoleto(boletoDTO, banco, titulo, contaSistema, cliente);

        // gerar fatura
        br.com.villefortconsulting.sgfinancas.entidades.Boleto boletoSistema = salvarBoletoSistema(boleto, contaSistema, parcela, boletoDTO, usuarioLogado, numeroVia);
        parcela.setIdBoleto(boletoSistema);

        // salvar parcela
        return contaService.salvarContaParcela(parcela);
    }

    private br.com.villefortconsulting.sgfinancas.entidades.Boleto salvarBoletoSistema(Boleto boleto, ContaBancaria contaSistema, ContaParcela parcela, BoletoDTO boletoDTO, Usuario usuarioLogado, Integer numeroVia) throws FileException, IOException {
        String nomeArquivo = StringUtil.removerAcentos("boleto_" + numeroVia + "_" + contaSistema.getDescricao());
        File file = downloadBoleto(boleto, nomeArquivo);
        Documento documento = documentoService.criarDocumento(usuarioLogado, nomeArquivo, file);
        return salvar(preencherBoletoSistema(parcela, boletoDTO, documento, boleto.getTitulo(), contaSistema, numeroVia, usuarioLogado.getTenat()));
    }

    private File downloadBoleto(Boleto boleto, String nomeArquivo) {
        BoletoViewer boletoViewer = new BoletoViewer(boleto, this.getClass().getResourceAsStream("BoletoTemplatePersonalizacaoSimples.pdf"));

        return boletoViewer.getPdfAsFile(nomeArquivo + ".pdf");
    }

    private BoletoDTO preencherBoletoDTO(ContaParcela parcela, Empresa empresa, ContaBancaria contaSistema, Cliente cliente, Integer numeroVia) throws BoletoException {

        Cidade enderecoSacado = empresa.getEndereco().getIdCidade();

        validarBoleto(empresa, cliente, contaSistema, enderecoSacado);

        try {

            BoletoDTO boletoDTO = new BoletoDTO();

            // cedente
            boletoDTO.setNomeCedente(cliente.getNome());
            boletoDTO.setCpfCNPJCedente(cliente.getCpfCNPJ());

            // sacado
            boletoDTO.setNomeSacado(empresa.getDescricao());
            boletoDTO.setCpfCnpjSacado(empresa.getCnpj());

            // endereco do sacado
            boletoDTO.setCidadeSacado(enderecoSacado);
            boletoDTO.setBairroSacado(empresa.getEndereco().getBairro());
            boletoDTO.setLogadouroSacado(empresa.getEndereco().getEndereco());
            boletoDTO.setCepSacado(empresa.getEndereco().getCep());

            // sacador avalista
            boletoDTO.setNomeSacAval(empresa.getDescricao());
            boletoDTO.setCpfCnpjSacAval(empresa.getCnpj());

            // conta bancaria
            boletoDTO.setContaBancaria(contaSistema);

            // titulo
            boletoDTO.setDataDocTitulo(DataUtil.getHoje());
            boletoDTO.setDataVencTitulo(parcela.getDataVencimento());
            boletoDTO.setValorTitulo(parcela.getValorTotal());
            boletoDTO.setDescTitulo(parcela.getDesconto());
            boletoDTO.setAcresTitulo(parcela.getJuros());
            boletoDTO.setValCobradoTitulo(parcela.getValorTotal());

            // FIX-ME: Informacoes genericas
            boletoDTO.setMoraTitulo(0d);
            boletoDTO.setDeducaoTitulo(0d);
            boletoDTO.setTipoDocTitulo(TipoDeTitulo.FAT_FATURA.name());

            boletoDTO.setNumDocTitulo(parcela.getId().toString() + BoletoUtil.completaCampo(numeroVia.toString(), 3));

            ParametroSistema parametroSistema = parametroSistemaService.getInstrucaoBoleto(empresa);

            if (parametroSistema != null) {
                boletoDTO.setInstrucaoTitulo1(parametroSistema.getInstrucao1());
                boletoDTO.setInstrucaoTitulo2(parametroSistema.getInstrucao2());
                boletoDTO.setInstrucaoTitulo3(parametroSistema.getInstrucao3());
                boletoDTO.setInstrucaoTitulo4(parametroSistema.getInstrucao4());
                boletoDTO.setInstrucaoTitulo5(parametroSistema.getInstrucao5());
                boletoDTO.setInstrucaoTitulo6(parametroSistema.getInstrucao6());
                boletoDTO.setInstrucaoTitulo7(parametroSistema.getInstrucao7());
                boletoDTO.setInstrucaoTitulo8(parametroSistema.getInstrucao8());
            }

            return boletoDTO;
        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto: " + ex.getMessage(), ex);
        }
    }

    private static void validarBoleto(Empresa empresa, Cliente cliente, ContaBancaria contaBancaria, Cidade enderecoSacado) throws BoletoException {

        if (cliente == null) {
            throw new BoletoException("Antes de emitir o boleto, informe o cliente.", null);
        }

        if ("PJ".equals(cliente.getTipo())) {
            if (!CpfCnpjUtil.validarCNPJ(cliente.getCpfCNPJ())) {
                throw new BoletoException("CNPJ do cliente inválido!", null);
            }
        } else if (!CpfCnpjUtil.validarCPF(cliente.getCpfCNPJ())) {
            throw new BoletoException("CPF do cliente inválido!", null);
        }

        if (!CpfCnpjUtil.validarCNPJ(empresa.getCnpj())) {
            throw new BoletoException("CNPJ da empresa inválido!", null);
        }

        if (contaBancaria == null) {
            throw new BoletoException("Conta bancária não informada!", null);
        }

        if (StringUtils.isEmpty(contaBancaria.getAgencia())) {
            throw new BoletoException("Agência não informada", null);
        }

        if (StringUtils.isEmpty(contaBancaria.getConta()) || StringUtils.isEmpty(contaBancaria.getDigitoConta())) {
            throw new BoletoException("Número da conta bancária inválida", null);
        }

        if (enderecoSacado == null) {
            throw new BoletoException("Endereço da empresa não informada!", null);
        }
    }

    private static Sacado preencherSacado(BoletoDTO boletoDTO) throws BoletoException {

        try {

            Endereco enderecoSac = new Endereco();

            Cidade cidadeSacado = boletoDTO.getCidadeSacado();
            enderecoSac.setUF(UnidadeFederativa.valueOf(cidadeSacado.getIdUF().getDescricao()));
            enderecoSac.setLocalidade(cidadeSacado.getDescricao());
            enderecoSac.setBairro(boletoDTO.getBairroSacado());
            enderecoSac.setCep(boletoDTO.getCepSacado());
            enderecoSac.setLogradouro(boletoDTO.getLogadouroSacado());
            if (boletoDTO.getNumSacado() != null) {
                enderecoSac.setNumero(boletoDTO.getNumSacado().toString());
            }

            // sacado
            Sacado sacado = new Sacado(boletoDTO.getNomeSacado(), boletoDTO.getCpfCnpjSacado());
            sacado.addEndereco(enderecoSac);

            return sacado;
        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados da empresa: " + ex.getMessage(), ex);
        }
    }

    private static org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria preencherContaBancaria(BancosSuportados banco, ContaBancaria conta) throws BoletoException {
        try {
            // FIX-ME validar bancos suportados
            org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria contaBancaria = buscaContaBancaria(conta.getIdBanco().getNumero());

            if (StringUtils.isEmpty(conta.getDigitoAgencia())) {
                contaBancaria.setAgencia(new Agencia(Integer.parseInt(conta.getAgencia())));
            } else {
                contaBancaria.setAgencia(new Agencia(Integer.parseInt(conta.getAgencia()), conta.getDigitoAgencia()));
            }

            if (banco == BancosSuportados.BANCO_SANTANDER) {
                if (StringUtils.isEmpty(conta.getCedente()) || conta.getCedente().length() < 6) {
                    StringBuilder mensagemCedente = new StringBuilder();
                    mensagemCedente.append("Código do cedente inválido ou não informado no cadastro da conta bancária. ");
                    mensagemCedente.append("(OBS.: Código cedente é um número que serve para podermos fazer emissão de boleto )");
                    mensagemCedente.append("bancário em nome da sua empresa. Caso você não possua este código (que não é ");
                    mensagemCedente.append("necessariamente o número da sua conta), basta ligar para o gerente do seu banco ");
                    mensagemCedente.append("e solicitar esta informação.");
                    throw new BoletoException(mensagemCedente.toString(), null);
                }

                String numeroConta = conta.getCedente().substring(0, conta.getCedente().length() - 1);
                String digitoVerificador = String.valueOf(conta.getCedente().charAt(conta.getCedente().length() - 1));
                contaBancaria.setNumeroDaConta(new NumeroDaConta(Integer.parseInt(numeroConta), digitoVerificador));
            } else if (StringUtils.isEmpty(conta.getDigitoConta())) {
                contaBancaria.setNumeroDaConta(new NumeroDaConta(Integer.parseInt(conta.getConta())));
            } else {
                contaBancaria.setNumeroDaConta(new NumeroDaConta(Integer.parseInt(conta.getConta()), conta.getDigitoConta()));
            }
            contaBancaria.setCarteira(new Carteira(conta.getCarteira()));

            return contaBancaria;
        } catch (BoletoException | NumberFormatException ex) {
            throw new BoletoException("Falha ao preencher dados da conta bancária: " + ex.getMessage(), ex);
        }
    }

    private static org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria buscaContaBancaria(String codigoCompensacao) throws BoletoException {

        try {
            // Busca banco pelo codigo de compensacao
            if (BancosSuportados.isSuportado(codigoCompensacao)) {
                BancosSuportados bancoSuportados = BancosSuportados.suportados.get(codigoCompensacao);
                return new org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria(bancoSuportados.create());
            } else {
                throw new BoletoException("Banco não suportado", null);
            }

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do banco: " + ex.getMessage(), ex);
        }
    }

    private static Titulo preencherTitulo(BoletoDTO boletoDTO, org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria contaBancaria, Sacado sacado, Cedente cedente) throws BoletoException {
        try {

            Titulo titulo = new Titulo(contaBancaria, sacado, cedente);

            titulo.setNumeroDoDocumento(boletoDTO.getNumDocTitulo());
            titulo.setDataDoDocumento(boletoDTO.getDataDocTitulo());
            titulo.setDataDoVencimento(boletoDTO.getDataVencTitulo());
            titulo.setTipoDeDocumento(TipoDeTitulo.valueOf(boletoDTO.getTipoDocTitulo()));
            titulo.setValor(NumeroUtil.converterBigDecimal(boletoDTO.getValorTitulo()));
            titulo.setDesconto(NumeroUtil.converterBigDecimal(boletoDTO.getDescTitulo()));
            titulo.setDeducao(NumeroUtil.converterBigDecimal(boletoDTO.getDeducaoTitulo()));
            titulo.setMora(NumeroUtil.converterBigDecimal(boletoDTO.getMoraTitulo()));
            titulo.setAcrecimo(NumeroUtil.converterBigDecimal(boletoDTO.getAcresTitulo()));
            titulo.setValorCobrado(NumeroUtil.converterBigDecimal(boletoDTO.getValCobradoTitulo()));

            return titulo;
        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do título: " + ex.getMessage(), ex);
        }
    }

    private br.com.villefortconsulting.sgfinancas.entidades.Boleto preencherBoletoSistema(ContaParcela parcela, BoletoDTO boletoDTO, Documento documento, Titulo titulo, ContaBancaria contaBancaria, Integer numeroVia, String tenatID) {
        br.com.villefortconsulting.sgfinancas.entidades.Boleto boleto = new br.com.villefortconsulting.sgfinancas.entidades.Boleto();
        boleto.setIdContaParcela(parcela);
        boleto.setIdContaBancaria(contaBancaria);
        boleto.setIdDocumento(documento);
        boleto.setIdClienteSacado(parcela.getIdConta().getIdCliente());
        boleto.setValor(boletoDTO.getValorTitulo());
        boleto.setValorDesconto(boletoDTO.getDescTitulo());
        boleto.setJuros(boletoDTO.getAcresTitulo());
        boleto.setDataEmissao(DataUtil.getHoje());
        boleto.setDataVencimento(parcela.getDataVencimento());
        boleto.setNossoNumero(titulo.getNossoNumero());
        boleto.setNumeroVia(numeroVia);
        boleto.setDiasProtesto(40);
        boleto.setTenatID(adHocTenant.getTenantID());
        if (boleto.getTenatID() == null) {
            boleto.setTenatID(tenatID);
        }

        return boleto;
    }

    private static Boleto preencherBoleto(BoletoDTO boletoDTO, BancosSuportados banco, Titulo titulo, ContaBancaria conta, Cliente cliente) throws BoletoException {
        try {

            Boleto boleto = null;

            switch (banco) {
                case BANCO_ITAU:
                    boleto = preencherBoletoItau(titulo, conta);
                    break;
                case BANCO_SANTANDER:
                    boleto = preencherBoletoSantander(titulo, conta);
                    break;
                case CAIXA_ECONOMICA_FEDERAL:
                    boleto = preencherBoletoCaixa(titulo, conta);
                    break;
                case BANCO_BRADESCO:
                    boleto = preencherBoletoBradesco(titulo, conta);
                    break;
                case BANCO_DO_BRASIL:
                    boleto = preencherBoletoBB(titulo, conta);
                    break;
                case HSBC:
                    boleto = preencherBoletoHsbc(titulo, conta, cliente);
                    break;
                case MERCANTIL_DO_BRASIL:
                    boleto = preencherBoletoMercantil(titulo, conta);
                    break;
                default:
                    throw new BoletoException(" Banco não suportado para gerar boleto. ", null);
            }

            boleto.setInstrucaoAoSacado(boletoDTO.getInstrucaoSacTitulo());
            boleto.setInstrucao1(boletoDTO.getInstrucaoTitulo1());
            boleto.setInstrucao2(boletoDTO.getInstrucaoTitulo2());
            boleto.setInstrucao3(boletoDTO.getInstrucaoTitulo3());
            boleto.setInstrucao4(boletoDTO.getInstrucaoTitulo4());
            boleto.setInstrucao5(boletoDTO.getInstrucaoTitulo5());
            boleto.setInstrucao6(boletoDTO.getInstrucaoTitulo6());
            boleto.setInstrucao7(boletoDTO.getInstrucaoTitulo7());
            boleto.setInstrucao8(boletoDTO.getInstrucaoTitulo8());

            return boleto;
        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto: " + ex.getMessage(), ex);
        }
    }

    private static Boleto preencherBoletoCaixa(Titulo titulo, ContaBancaria contaBancaria) throws BoletoException {

        // Validar campos utilizados boleto CEF
        if (contaBancaria.getCarteira() == null) {
            throw new BoletoException("Carteira não informada para conta bancária CEF", null);
        }
        if (contaBancaria.getCedente() == null || contaBancaria.getCedente().length() < 8) {
            StringBuilder mensagemCedente = new StringBuilder();
            mensagemCedente.append("Código do cedente inválido ou não informado no cadastro da conta bancária. ");
            mensagemCedente.append("(OBS.: Código cedente é um número que serve para podermos fazer emissão de boleto )");
            mensagemCedente.append("bancário em nome da sua empresa. Caso você não possua este código (que não é ");
            mensagemCedente.append("necessariamente o número da sua conta), basta ligar para o gerente do seu banco ");
            mensagemCedente.append("e solicitar esta informação.");
            throw new BoletoException(mensagemCedente.toString(), null);
        }

        // Dados do titulo CEF
        try {
            titulo.setNossoNumero(BoletoUtil.completaCampoCaixa(titulo.getNumeroDoDocumento(), 17, (contaBancaria.getCarteira() == 2) ? "COMREG" : "SEMREG"));
            titulo.setDigitoDoNossoNumero(BoletoUtil.calculeDVModulo11(titulo.getNossoNumero()).toString());
            titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
            titulo.setAceite(Titulo.Aceite.A);

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do título da CEF: " + ex.getMessage(), ex);
        }

        // Dados adicionais boleto CEF
        try {
            CampoLivreCefSIGCB campoLivreCefSIGCB = new CampoLivreCefSIGCB(contaBancaria.getCedente().substring(0, 6), contaBancaria.getCedente().substring(7), titulo.getNossoNumero());

            Boleto boleto = new Boleto(titulo, campoLivreCefSIGCB);
            boleto.addTextosExtras("txtFcAgenciaCodigoCedente", contaBancaria.getAgencia() + "/" + contaBancaria.getCedente());
            boleto.addTextosExtras("txtRsAgenciaCodigoCedente", contaBancaria.getAgencia() + "/" + contaBancaria.getCedente());
            boleto.addTextosExtras("txtFcCarteira", (contaBancaria.getCarteira() == 2) ? "RG" : "SR");
            boleto.setLocalPagamento("PREFERENCIALMENTE NAS CASAS LOTÉRICAS ATÉ O VALOR LIMITE");

            return boleto;

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto da CEF: " + ex.getMessage(), ex);

        }
    }

    private static Boleto preencherBoletoBradesco(Titulo titulo, ContaBancaria contaBancaria) throws BoletoException {

        // Dados do titulo bradesco
        if (contaBancaria.getCarteira() == null) {
            throw new BoletoException("Carteira não informada para conta bancária Bradesco", null);
        }
        try {
            titulo.setNossoNumero(LayoutCnab.completaCampo(titulo.getNumeroDoDocumento(), 11, "numerico"));
            titulo.setDigitoDoNossoNumero(BoletoUtil.retornaDigitoNossoNumero(titulo.getNossoNumero(), contaBancaria.getCarteira().toString()));

            titulo.setTipoDeDocumento(TipoDeTitulo.OUTROS);
            titulo.setAceite(Titulo.Aceite.N);

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do título Bradesco: " + ex.getMessage(), ex);
        }

        // Dados adicionais do boleto
        try {
            Boleto boleto = new Boleto(titulo);
            boleto.setLocalPagamento("Pagável em qualquer agência bancária até o Vencimento.");
            return boleto;

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto Bradesco: " + ex.getMessage(), ex);

        }
    }

    private static Boleto preencherBoletoBB(Titulo titulo, ContaBancaria contaBancaria) throws BoletoException {

        // Dados adicionais do titulo
        if (contaBancaria.getCarteira() == null) {
            throw new BoletoException("Carteira não informada para conta bancária BB", null);
        }
        try {
            titulo.setNossoNumero(BoletoUtil.completaCampoBB(titulo.getNumeroDoDocumento(), 17, contaBancaria.getCedente()));
            titulo.setDigitoDoNossoNumero(BoletoUtil.retornaDigitoNossoNumero(titulo.getNossoNumero(), contaBancaria.getCarteira().toString()));

            titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
            titulo.setAceite(Titulo.Aceite.N);

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do título BB: " + ex.getMessage(), ex);
        }

        // Dados adicionais boleto
        try {
            Boleto boleto = new Boleto(titulo);
            boleto.setLocalPagamento("Pagável em qualquer agência bancária até o Vencimento.");

            return boleto;

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto BB " + ex.getMessage(), ex);

        }
    }

    private static Boleto preencherBoletoItau(Titulo titulo, ContaBancaria contaBancaria) throws BoletoException {

        // Dados do titulo
        if (contaBancaria.getCarteira() == null) {
            throw new BoletoException("Carteira não informada para conta bancária Itaú", null);
        }
        try {
            titulo.setNossoNumero(BoletoUtil.completaCampo(titulo.getNumeroDoDocumento(), 8));
            titulo.setDigitoDoNossoNumero(BoletoUtil.calcularDvNossoNumItau(contaBancaria.getAgencia(), contaBancaria.getConta(), contaBancaria.getCarteira().toString(), titulo.getNossoNumero()));

            titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
            titulo.setAceite(Titulo.Aceite.N);

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do título Itaú: " + ex.getMessage(), ex);
        }

        // Dados adicionais boleto
        try {
            Boleto boleto = new Boleto(titulo);
            boleto.addTextosExtras("txtFcNossoNumero", contaBancaria.getCarteira() + "/" + titulo.getNossoNumero() + "-" + titulo.getDigitoDoNossoNumero());
            boleto.setLocalPagamento("ATÉ O VENCIMENTO PAGUE PREFERENCIALMENTE NO ITAU");

            return boleto;

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto da Itaú: " + ex.getMessage(), ex);

        }
    }

    private static Boleto preencherBoletoSantander(Titulo titulo, ContaBancaria contaBancaria) throws BoletoException {

        // Validar campos utilizados boleto Santander
        if (contaBancaria.getCedente() == null) {
            StringBuilder mensagemCedente = new StringBuilder();
            mensagemCedente.append("Código do cedente inválido ou não informado no cadastro da conta bancária. ");
            mensagemCedente.append("(OBS.: Código cedente é um número que serve para podermos fazer emissão de boleto )");
            mensagemCedente.append("bancário em nome da sua empresa. Caso você não possua este código (que não é ");
            mensagemCedente.append("necessariamente o número da sua conta), basta ligar para o gerente do seu banco ");
            mensagemCedente.append("e solicitar esta informação.");
            throw new BoletoException(mensagemCedente.toString(), null);
        }

        // Dados do titulo
        try {
            titulo.setNossoNumero(BoletoUtil.completaCampo(titulo.getNumeroDoDocumento(), 7));
            titulo.setDigitoDoNossoNumero(BoletoUtil.calcularDvNossoNumSantander(titulo.getNossoNumero()));

            titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
            titulo.setAceite(Titulo.Aceite.N);

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do título Santander: " + ex.getMessage(), ex);
        }

        // Dados adicionais boleto
        try {

            Boleto boleto = new Boleto(titulo);
            boleto.addTextosExtras("txtFcNossoNumero", titulo.getNossoNumero());
            boleto.addTextosExtras("txtFcAgenciaCodigoCedente", contaBancaria.getAgencia() + "/" + contaBancaria.getCedente());
            boleto.addTextosExtras("txtRsAgenciaCodigoCedente", contaBancaria.getAgencia() + "/" + contaBancaria.getCedente());
            boleto.addTextosExtras("txtFcNossoNumero", contaBancaria.getCarteira() + "/" + titulo.getNossoNumero() + "-" + titulo.getDigitoDoNossoNumero());
            boleto.setLocalPagamento("ATÉ O VENCIMENTO PAGUE PREFERENCIALMENTE NO SANTANDER");

            return boleto;

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto da Santander: " + ex.getMessage(), ex);

        }
    }

    private static Boleto preencherBoletoHsbc(Titulo titulo, ContaBancaria contaBancaria, Cliente cliente) throws BoletoException {

        // Validar campos utilizados boleto hsbc
        if (contaBancaria.getCedente() == null || contaBancaria.getCedente().length() < 7) {
            StringBuilder mensagemCedente = new StringBuilder();
            mensagemCedente.append("Código do cedente inválido ou não informado no cadastro da conta bancária. ");
            mensagemCedente.append("(OBS.: Código cedente é um número que serve para podermos fazer emissão de boleto )");
            mensagemCedente.append("bancário em nome da sua empresa. Caso você não possua este código (que não é ");
            mensagemCedente.append("necessariamente o número da sua conta), basta ligar para o gerente do seu banco ");
            mensagemCedente.append("e solicitar esta informação.");
            throw new BoletoException(mensagemCedente.toString(), null);
        }

        // Dados do titulo
        try {

            String codCedente = BoletoUtil.completaCampo(contaBancaria.getCedente(), 13);
            String nossoNumero = BoletoUtil.completaCampo(cliente.getId().toString(), 13);
            String data = new SimpleDateFormat("ddMMyy").format(titulo.getDataDoVencimento());
            String dv1 = BoletoUtil.calcularDv1NossoNumHSBC(nossoNumero);
            String dv2 = BoletoUtil.calcularDv2NossoNumHSBC(nossoNumero + dv1 + "4", codCedente, data);

            titulo.setNossoNumero(BoletoUtil.completaCampo(titulo.getNumeroDoDocumento(), 8));
            titulo.setDigitoDoNossoNumero(dv1 + "4" + dv2);

            titulo.getContaBancaria().setCarteira(new Carteira(1, TipoDeCobranca.SEM_REGISTRO));
            titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
            titulo.setAceite(Titulo.Aceite.N);
            titulo.setParametrosBancarios(new ParametrosBancariosMap(TipoIdentificadorCNR.class.getName(), TipoIdentificadorCNR.COM_VENCIMENTO));

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do título HSBC: " + ex.getMessage(), ex);
        }

        // Dados adicionais boleto
        try {
            Boleto boleto = new Boleto(titulo);
            boleto.addTextosExtras("txtFcCarteira", "CNR");
            boleto.addTextosExtras("txtFcAceite", "NÃO");
            boleto.addTextosExtras("txtFcNossoNumero", titulo.getNossoNumero() + titulo.getDigitoDoNossoNumero());
            boleto.addTextosExtras("txtRcNossoNumero", titulo.getNossoNumero() + titulo.getDigitoDoNossoNumero());
            boleto.addTextosExtras("txtFcAgenciaCodigoCedente", contaBancaria.getCedente());
            boleto.addTextosExtras("txtRsAgenciaCodigoCedente", contaBancaria.getCedente());
            boleto.addTextosExtras("txtRsEspecieDocumento", "");
            boleto.setLocalPagamento("PAGAR PREFERENCIALMENTE EM AGÊNCIA DO HSBC");

            return boleto;

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto do HSBC: " + ex.getMessage(), ex);

        }
    }

    private static Boleto preencherBoletoMercantil(Titulo titulo, ContaBancaria contaBancaria) throws BoletoException {

        // Dados do titulo mercantil
        try {
            titulo.setNossoNumero(BoletoUtil.completaCampo(titulo.getNumeroDoDocumento(), 10));
            titulo.setDigitoDoNossoNumero(BoletoUtil.calcularDvNossoNumMercantil(contaBancaria.getAgencia(), titulo.getNossoNumero()));

            titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
            titulo.setAceite(Titulo.Aceite.N);
        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do título Mercantil: " + ex.getMessage(), ex);
        }

        // Dados adicionais do boleto
        try {
            Boleto boleto = new Boleto(titulo);
            boleto.setLocalPagamento("Pagável em qualquer agência bancária até o Vencimento.");
            return boleto;

        } catch (Exception ex) {
            throw new BoletoException("Falha ao preencher dados do boleto Mercantil: " + ex.getMessage(), ex);

        }
    }

    public br.com.villefortconsulting.sgfinancas.entidades.Boleto buscarBoletoPorNossoNumero(String nossoNumero, ContaBancaria contaBancaria) {
        return repositorio.buscarBoletoPorNossoNumero(nossoNumero, contaBancaria);
    }

}
