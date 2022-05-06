package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaFormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.VendaItemOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaSeguradora;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OsRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaFormaPagamentoCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaFormaPagamentoRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaItemOrdemServicoRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaProdutoCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaProdutoRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaSeguradoraDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaServicoCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaServicoRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.servicos.CentroCustoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.CsosnService;
import br.com.villefortconsulting.sgfinancas.servicos.CstService;
import br.com.villefortconsulting.sgfinancas.servicos.FormaPagamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.NcmService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.util.ListUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {EntityMapper.class}, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class VendaMapper {

    @Inject
    ContaService contaService;

    @Inject
    ContaBancariaService contaBancariaService;

    @Inject
    ParametroSistemaService parametroSistemaService;

    @Inject
    PlanoContaService planoContaService;

    @Inject
    VendaService vendaService;

    @Inject
    CentroCustoService centroCustoService;

    @Inject
    ClienteService clienteService;

    @Inject
    FormaPagamentoService formaPagamentoService;

    @Inject
    NcmService ncmService;

    @Inject
    CstService cstService;

    @Inject
    CsosnService csosnService;

    @Inject
    ProdutoService produtoService;

    @Inject
    ServicoService servicoService;

    @Mapping(target = "tipo", expression = "java(br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda.VENDA)")
    @Mapping(target = "cliente.id", source = "idCliente")
    @Mapping(target = "usuarioVendedor.id", source = "idUsuarioVendedor")
    @Mapping(target = "planoConta.id", source = "idPlanoConta")
    @Mapping(target = "centroCusto.id", source = "idCentroCusto")
    @Mapping(target = "situacaoSigla", source = "situacao")
    @Mapping(target = "condicaoPagamento", source = "formaPagamento")
    @Mapping(target = "produtos", ignore = true)
    @Mapping(target = "servicos", ignore = true)
    @Mapping(target = "itensOS", ignore = true)
    @Mapping(target = "formasPagamento", source = "vendaFormaPagamentoList")
    @Mapping(target = "situacaoDescricao", ignore = true)
    @Mapping(target = "emitiuNFe", ignore = true)
    @Mapping(target = "emitiuNFSe", ignore = true)
    @Mapping(target = "contaBancaria", source = "idContaBancaria")
    public abstract VendaRestDTO toDTO(Venda venda);

    @Mapping(target = "tipo", expression = "java(br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda.VENDA)")
    @Mapping(target = "cliente", source = "idCliente")
    @Mapping(target = "usuarioVendedor", source = "idUsuarioVendedor")
    @Mapping(target = "planoConta", source = "idPlanoConta")
    @Mapping(target = "centroCusto", source = "idCentroCusto")
    @Mapping(target = "situacaoSigla", source = "situacao")
    @Mapping(target = "condicaoPagamento", source = "formaPagamento")
    @Mapping(target = "produtos", source = "vendaProdutoList")
    @Mapping(target = "servicos", source = "vendaServicoList")
    @Mapping(target = "itensOS", source = "vendaItemOrdemDeServicoList")
    @Mapping(target = "formasPagamento", source = "vendaFormaPagamentoList")
    @Mapping(target = "situacaoDescricao", ignore = true)
    @Mapping(target = "emitiuNFe", ignore = true)
    @Mapping(target = "emitiuNFSe", ignore = true)
    @Mapping(target = "contaBancaria", source = "idContaBancaria")
    @Mapping(target = "vendaSeguradora", source = "idVendaSeguradora")
    public abstract VendaRestDTO toDTOFull(Venda venda);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idDocumento", ignore = true)
    @Mapping(target = "idCliente", source = "cliente")
    @Mapping(target = "idContaBancaria", source = "contaBancaria")
    @Mapping(target = "idConta", source = "conta")
    @Mapping(target = "idPlanoConta", source = "planoConta")
    @Mapping(target = "idUsuarioVendedor", ignore = true)
    @Mapping(target = "idCentroCusto", source = "centroCusto")
    @Mapping(target = "situacao", constant = "A")
    @Mapping(target = "statusNegociacao", source = "tipo")
    @Mapping(target = "numParcela", ignore = true)
    @Mapping(target = "observacaoParcela", ignore = true)
    @Mapping(target = "vendaProdutoList", ignore = true)
    @Mapping(target = "vendaServicoList", ignore = true)
    @Mapping(target = "vendaItemOrdemDeServicoList", ignore = true)
    @Mapping(target = "vendaFormaPagamentoList", ignore = true)
    @Mapping(target = "tipoItemVenda", ignore = true)
    @Mapping(target = "pontuacao", constant = "0d")
    @Mapping(target = "pontosUtilizados", constant = "0d")
    @Mapping(target = "listParcelaDTO", ignore = true)
    @Mapping(target = "controle", ignore = true)
    @Mapping(target = "valorPago", constant = "0d")
    @Mapping(target = "darBaixa", constant = "false")
    @Mapping(target = "emitirNFCe", constant = "false")
    @Mapping(target = "origem", ignore = true)
    @Mapping(target = "idOrigem", ignore = true)
    @Mapping(target = "representanteEmpresa", ignore = true)
    @Mapping(target = "idVendaSeguradora", expression = "java(toEntity(vendaCadastroRestDTO.getVendaSeguradora(), venda))")
    public abstract Venda toEntity(VendaCadastroRestDTO vendaCadastroRestDTO);

    @Mapping(target = "clienteSeguradora", source = "idClienteSeguradora")
    public abstract VendaSeguradoraDTO toDTO(VendaSeguradora vendaSeguradora);

    @Mapping(target = "id", source = "vendaSeguradoraDTO.id")
    @Mapping(target = "idClienteSeguradora", source = "vendaSeguradoraDTO.clienteSeguradora")
    @Mapping(target = "idVenda", source = "venda")
    public abstract VendaSeguradora toEntity(VendaSeguradoraDTO vendaSeguradoraDTO, Venda venda);

    @Mapping(target = "produto", source = "dadosProduto.idProduto")
    @Mapping(target = "ncm", source = "dadosProduto.idNcm.codigo")
    @Mapping(target = "cfop", source = "dadosProduto.idCfop.codigo")
    @Mapping(target = "cst", source = "dadosProduto.idCst.codigo")
    @Mapping(target = "csosn", source = "dadosProduto.idCsosn.codigo")
    @Mapping(target = "quantidade", source = "dadosProduto.quantidade")
    @Mapping(target = "valorVenda", source = "dadosProduto.valor")
    @Mapping(target = "detalhesItem", source = "dadosProduto.detalhesItem")
    @Mapping(target = "desconto", source = "dadosProduto.desconto")
    public abstract VendaProdutoRestDTO toDTO(VendaProduto vendaProduto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "idVenda", source = "venda")
    @Mapping(target = "dadosProduto.idProduto", expression = "java( produtoService.buscar(vendaProdutoCadastroRestDTO.getProduto()) )")
    @Mapping(target = "dadosProduto.idNcm", expression = "java( vendaProdutoCadastroRestDTO.getNcm() == null ? null : ncmService.buscar(Integer.valueOf(vendaProdutoCadastroRestDTO.getNcm())) )")
    @Mapping(target = "dadosProduto.idCfop", ignore = true)
    @Mapping(target = "dadosProduto.idCst", expression = "java( vendaProdutoCadastroRestDTO.getCst() == null ? null : cstService.buscar(Integer.valueOf(vendaProdutoCadastroRestDTO.getCst())) )")
    @Mapping(target = "dadosProduto.idCsosn", expression = "java( vendaProdutoCadastroRestDTO.getCsosn() == null ? null : csosnService.buscar(Integer.valueOf(vendaProdutoCadastroRestDTO.getCsosn())) )")
    @Mapping(target = "dadosProduto.quantidade", source = "dto.quantidade")
    @Mapping(target = "dadosProduto.valor", source = "dto.valorVenda")
    @Mapping(target = "dadosProduto.desconto", source = "dto.desconto")
    @Mapping(target = "dadosProduto.detalhesItem", source = "dto.detalhesItem")
    @Mapping(target = "tenatID", source = "venda.tenatID")
    @Mapping(target = "controle", ignore = true)
    public abstract VendaProduto toEntity(VendaProdutoCadastroRestDTO dto, Venda venda);

    @Mapping(target = "servico", source = "idServico")
    public abstract VendaServicoRestDTO toDTO(VendaServico vendaServico);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "desconto", source = "dto.desconto")
    @Mapping(target = "idVenda", source = "venda")
    @Mapping(target = "idServico", expression = "java(servicoService.buscar(dto.getServico()) )")
    @Mapping(target = "tenatID", source = "venda.tenatID")
    @Mapping(target = "custo", expression = "java( vendaServico.getIdServico().getCustoServico() != null ? vendaServico.getIdServico().getCustoServico() : 0d )", dependsOn = {"idServico"})
    @Mapping(target = "controle", ignore = true)
    public abstract VendaServico toEntity(VendaServicoCadastroRestDTO dto, Venda venda);

    @Mapping(target = "formaPagamento", source = "idFormaPagamento")
    public abstract VendaFormaPagamentoRestDTO toDTO(VendaFormaPagamento vendaFormaPagamento);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "desconto", source = "dto.desconto")
    @Mapping(target = "condicaoPagamento", source = "dto.condicaoPagamento")
    @Mapping(target = "idVenda", source = "venda")
    @Mapping(target = "idFormaPagamento", expression = "java( formaPagamentoService.buscar(dto.getFormaPagamento()!= null? dto.getFormaPagamento(): venda.getVendaFormaPagamentoList().get(0).getIdFormaPagamento().getId() ))")
    @Mapping(target = "tenatID", source = "venda.tenatID")
    @Mapping(target = "formaEscolhida", constant = "N")
    public abstract VendaFormaPagamento toEntity(VendaFormaPagamentoCadastroRestDTO dto, Venda venda);

    public abstract VendaItemOrdemServicoRestDTO toDTO(VendaItemOrdemDeServico vendaItemOrdemDeServico);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "observacao", source = "dto.observacao")
    @Mapping(target = "valor", source = "dto.valor")
    @Mapping(target = "desconto", source = "dto.desconto")
    @Mapping(target = "idVenda", source = "venda")
    @Mapping(target = "tenatID", source = "venda.tenatID")
    public abstract VendaItemOrdemDeServico toEntity(VendaItemOrdemServicoRestDTO dto, Venda venda);

    public List<VendaProduto> toVendaProdutoEntityList(List<VendaProdutoCadastroRestDTO> lista, Venda venda) {
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista.stream().map(dto -> toEntity(dto, venda)).collect(Collectors.toList());
    }

    public List<VendaServico> toVendaServicoEntityList(List<VendaServicoCadastroRestDTO> lista, Venda venda) {
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista.stream().map(dto -> toEntity(dto, venda)).collect(Collectors.toList());
    }

    public List<VendaItemOrdemDeServico> toVendaItemOrdemDeServicoEntityList(List<VendaItemOrdemServicoRestDTO> lista, Venda venda) {
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista.stream().map(dto -> toEntity(dto, venda)).collect(Collectors.toList());
    }

    public List<VendaFormaPagamento> toVendaFormaPagamentoEntityList(List<VendaFormaPagamentoCadastroRestDTO> lista, Venda venda) {
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista.stream().map(dto -> toEntity(dto, venda)).collect(Collectors.toList());
    }

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "vendaFormaPagamentoList", ignore = true)
    @Mapping(target = "vendaItemOrdemDeServicoList", ignore = true)
    @Mapping(target = "vendaProdutoList", ignore = true)
    @Mapping(target = "vendaServicoList", ignore = true)
    @Mapping(target = "idVendaSeguradora", ignore = true)
    @Mapping(target = "idConta", ignore = true)
    protected abstract Venda updateVenda(@MappingTarget Venda venda, Venda vendaFromDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idVenda", source = "venda")
    @Mapping(target = "tenatID", source = "venda.tenatID")
    protected abstract VendaSeguradora updateVendaSeguradora(@MappingTarget VendaSeguradora vendaSeguradora, VendaSeguradora vendaSeguradoraFromDto, Venda venda);

    public Venda toEntityFromDB(VendaCadastroRestDTO dto, String tenantID) throws IllegalAccessException, InvocationTargetException {
        Venda vendaFromDto = toEntity(dto);
        vendaFromDto.setIdContaBancaria(contaBancariaService.getContaBancariaPadrao(dto));
        vendaFromDto.setIdPlanoConta(planoContaService.getPlanoContaPadrao(dto));
        Venda venda = dto.getId() != null ? vendaService.buscar(dto.getId()) : new Venda();
        venda.setTenatID(tenantID);
        updateVenda(venda, vendaFromDto);
        if (dto.getConta() != null) {
            venda.setIdConta(contaService.buscar(dto.getConta()));
        }
        if (dto.getVendaSeguradora() != null) {
            VendaSeguradora vendaSeguradoraFromDto = toEntity(dto.getVendaSeguradora(), venda);
            VendaSeguradora vendaSeguradora = dto.getVendaSeguradora().getId() != null ? vendaService.buscarVendaSeguradora(dto.getVendaSeguradora().getId()) : new VendaSeguradora();
            updateVendaSeguradora(vendaSeguradora, vendaSeguradoraFromDto, venda);
            venda.setIdVendaSeguradora(vendaSeguradora);
        } else {
            venda.setIdVendaSeguradora(null);
        }

        if (dto.getContaBancaria() != null) {
            venda.setIdContaBancaria(contaBancariaService.buscar(dto.getContaBancaria()));
        }
        if (dto.getCentroCusto() != null) {
            venda.setIdCentroCusto(centroCustoService.buscar(dto.getCentroCusto()));
        }
        if (dto.getCliente() != null) {
            venda.setIdCliente(clienteService.buscar(dto.getCliente()));
        } else if (venda.getIdCliente() == null) {
            throw new CadastroException("Informe o cliente", null);
        }
        if( !dto.getFormasPagamento().isEmpty()){
            venda.setFormaPagamento(dto.getFormasPagamento().get(0).getCondicaoPagamento());
        }

        if ("VD".equals(venda.getStatusNegociacao()) && !dto.getFormasPagamento().isEmpty()) {
            venda.setFormaPagamento(dto.getFormasPagamento().get(0).getCondicaoPagamento());
        }
        venda.setStatusNegociacao(EnumTipoVenda.retornaEnumSelecionado(dto.getTipo()).getSituacao());
        if (dto.getId() != null) {
            List<VendaFormaPagamento> listVFP = ListUtil.persist(vendaService.listarVendaFormaPagamento(venda), toVendaFormaPagamentoEntityList(dto.getFormasPagamento(), venda), VendaFormaPagamento::contains);
            List<VendaItemOrdemDeServico> listVIODS = ListUtil.persist(vendaService.listarVendaItensOrdemDeServico(venda), toVendaItemOrdemDeServicoEntityList(dto.getItensOS(), venda), VendaItemOrdemDeServico::contains);
            List<VendaProduto> listVP = ListUtil.persist(vendaService.listarVendaProduto(venda), toVendaProdutoEntityList(dto.getProdutos(), venda), VendaProduto::contains);
            List<VendaServico> listVS = ListUtil.persist(vendaService.listarVendaServico(venda), toVendaServicoEntityList(dto.getServicos(), venda), VendaServico::contains);
            venda.getVendaFormaPagamentoList().clear();
            venda.getVendaItemOrdemDeServicoList().clear();
            venda.getVendaProdutoList().clear();
            venda.getVendaServicoList().clear();
            venda.getVendaFormaPagamentoList().addAll(listVFP);
            venda.getVendaItemOrdemDeServicoList().addAll(listVIODS);
            venda.getVendaProdutoList().addAll(listVP);
            venda.getVendaServicoList().addAll(listVS);
        } else {
            ListUtil.persist(venda.getVendaFormaPagamentoList(), toVendaFormaPagamentoEntityList(dto.getFormasPagamento(), venda), VendaFormaPagamento::contains);
            ListUtil.persist(venda.getVendaItemOrdemDeServicoList(), toVendaItemOrdemDeServicoEntityList(dto.getItensOS(), venda), VendaItemOrdemDeServico::contains);
            ListUtil.persist(venda.getVendaProdutoList(), toVendaProdutoEntityList(dto.getProdutos(), venda), VendaProduto::contains);
            ListUtil.persist(venda.getVendaServicoList(), toVendaServicoEntityList(dto.getServicos(), venda), VendaServico::contains);
        }

        venda.setDesconto(NumeroUtil.somar(
                venda.getVendaItemOrdemDeServicoList().stream()
                        .mapToDouble(VendaItemOrdemDeServico::getDesconto).sum(),
                venda.getVendaProdutoList().stream()
                        .map(VendaProduto::getDadosProduto)
                        .mapToDouble(v -> v.getDesconto() != null ? v.getDesconto() : 0).sum(),
                venda.getVendaServicoList().stream()
                        .mapToDouble(v -> v.getDesconto() != null ? v.getDesconto() : 0).sum()));
        venda.setValor(NumeroUtil.somar(
                venda.getVendaItemOrdemDeServicoList().stream()
                        .map(VendaItemOrdemDeServico::getValor)
                        .filter(Objects::nonNull)
                        .reduce(0d, (a, v) -> a + v),
                venda.getVendaProdutoList().stream()
                        .map(VendaProduto::getDadosProduto)
                        .map(DadosProduto::getValorTotal)
                        .filter(Objects::nonNull)
                        .reduce(0d, (a, v) -> a + v),
                venda.getVendaServicoList().stream()
                        .map(vs -> vs.getValorVenda() * vs.getQuantidade())
                        .reduce(0d, (a, v) -> a + v)));
        venda.setDataVenda(new Date());
        venda.setCondicaoPagamento(EnumTipoPagamento.CREDITO.getTipo());
        venda.setValorDesconto(venda.getDesconto());
        venda.prePersist();

        return venda;
    }

    @Mapping(target = "podeReabrir", ignore = true)
    public abstract OsRestDTO toOsDTO(VendaRestDTO dto);

}
