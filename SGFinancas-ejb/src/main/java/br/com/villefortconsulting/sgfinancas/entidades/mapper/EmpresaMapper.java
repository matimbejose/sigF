package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.CategoriaEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModuloPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CategoriaEmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAtualizacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaNfsDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoMensalidadeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoMensalidadeModuloDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PermissaoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.CategoriaEmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class EmpresaMapper {

    @Inject
    protected CategoriaEmpresaService categoriaEmpresaService;

    @Inject
    protected CidadeService cidadeService;

    @Mapping(target = "atividadePrincipal", ignore = true)
    @Mapping(target = "dataSituacao", ignore = true)
    @Mapping(target = "complemento", source = "endereco.complemento")
    @Mapping(target = "cidade", source = "endereco.idCidade.descricao")
    @Mapping(target = "codCidadeIBGE", source = "endereco.idCidade.codigoIBGE")
    @Mapping(target = "nome", source = "descricao")
    @Mapping(target = "atividadesSecundarias", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "qsa", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "situacao", ignore = true)
    @Mapping(target = "logradouro", source = "endereco.endereco")
    @Mapping(target = "numero", source = "endereco.numero")
    @Mapping(target = "municipio", source = "endereco.idCidade.descricao")
    @Mapping(target = "fantasia", source = "nomeFantasia")
    @Mapping(target = "porte", ignore = true)
    @Mapping(target = "abertura", ignore = true)
    @Mapping(target = "naturezaJuridica", ignore = true)
    @Mapping(target = "uf", source = "endereco.idCidade.idUF.descricao")
    @Mapping(target = "bairro", source = "endereco.bairro")
    @Mapping(target = "cep", source = "endereco.cep")
    @Mapping(target = "telefone", source = "fone")
    @Mapping(target = "ultimaAtualizacao", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "efr", ignore = true)
    @Mapping(target = "motivoSituacao", ignore = true)
    @Mapping(target = "situacaoEspecial", ignore = true)
    @Mapping(target = "dataSituacaoEspecial", ignore = true)
    @Mapping(target = "capitalSocial", ignore = true)
    @Mapping(target = "message", ignore = true)
    @Mapping(target = "dtSituacaoCadastral", ignore = true)
    @Mapping(target = "entidadeFederativoResponsavel", ignore = true)
    @Mapping(target = "motivoSituacaoCadastral", ignore = true)
    @Mapping(target = "numeroDeInscricao", ignore = true)
    @Mapping(target = "categoria", source = "idCategoriaEmpresa.descricao")
    @Mapping(target = "periodoTeste", expression = "java(\"G\".equals(empresa.getTipoConta()))")
    public abstract EmpresaDTO toDTO(Empresa empresa);

    @Mapping(target = "descricaoUfCidade", source = "endereco.idCidade.idUF.descricao")
    @Mapping(target = "codigoIBGECidade", source = "endereco.idCidade.codigoIBGE")
    @Mapping(target = "bairro", source = "endereco.bairro")
    @Mapping(target = "cep", source = "endereco.cep")
    @Mapping(target = "numeroEndereco", source = "endereco.numero")
    @Mapping(target = "complementoEndereco", source = "endereco.complemento")
    @Mapping(target = "endereco", source = "endereco.endereco")
    @Mapping(target = "ambiente", ignore = true)
    @Mapping(target = "token", ignore = true)
    public abstract EmpresaNfsDTO toNfsDTO(Empresa empresa);

    @Mapping(target = "modulos", source = "pagamentoMensalidadeModuloList")
    public abstract PagamentoMensalidadeDTO toDTO(PagamentoMensalidade pm);

    @Mapping(target = "modulo", source = "idModulo")
    @Mapping(target = "permissoes", source = "pagamentoMensalidadeModuloPermissaoList")
    @Mapping(target = "modulo.listaPermissoes", ignore = true)
    public abstract PagamentoMensalidadeModuloDTO toDTO(PagamentoMensalidadeModulo pmm);

    @Mapping(target = "nome", source = "pmmp.idPermissao.descricao")
    @Mapping(target = "descricao", source = "pmmp.idPermissao.descricaoDetalhada")
    @Mapping(target = "possuiPermissao", constant = "true")
    public abstract PermissaoDTO toDTO(PagamentoMensalidadeModuloPermissao pmmp);

    @Mapping(target = "id", source = "id")
    public abstract CategoriaEmpresaDTO toDTO(CategoriaEmpresa categoria);

    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "tipoUso", ignore = true)
    @Mapping(target = "tipoConta", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idCnae", ignore = true)
    @Mapping(target = "idDocumento", ignore = true)
    @Mapping(target = "idContabilidade", ignore = true)
    @Mapping(target = "listClassificacaoEmpresa", ignore = true)
    @Mapping(target = "listGrupoEmpresa", ignore = true)
    @Mapping(target = "listEmpresaCnae", ignore = true)
    @Mapping(target = "loginAcessoList", ignore = true)
    @Mapping(target = "telefone", ignore = true)
    @Mapping(target = "dataNascimentoRepresentante", ignore = true)
    @Mapping(target = "celularRepresentante", ignore = true)
    @Mapping(target = "cpfRepresentante", ignore = true)
    @Mapping(target = "codigoIBGE", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "planoTransferencia", ignore = true)
    @Mapping(target = "sexoRepresentante", ignore = true)
    @Mapping(target = "contatoTitular", ignore = true)
    @Mapping(target = "tipoSocialEmpresa", ignore = true)
    @Mapping(target = "idContaBancariaDigital", ignore = true)
    @Mapping(target = "prazoUsarSemComprar", ignore = true)
    @Mapping(target = "dataCredenciamento", ignore = true)
    @Mapping(target = "cnpj", source = "cpfCnpj")
    @Mapping(target = "endereco.cep", source = "cep")
    @Mapping(target = "endereco.idCidade", expression = "java(cidadeService.buscarPeloCodigoIBGE(empresaAtualizacaoDTO.getCodCidade()))")
    @Mapping(target = "endereco.endereco", source = "endereco")
    @Mapping(target = "endereco.numero", source = "numero")
    @Mapping(target = "endereco.complemento", source = "complemento")
    @Mapping(target = "endereco.bairro", source = "bairro")
    @Mapping(target = "idCategoriaEmpresa", expression = "java(dto.getIdCategoriaEmpresa() != null ? categoriaEmpresaService.buscar(dto.getIdCategoriaEmpresa()) : null)")
    public abstract Empresa update(@MappingTarget Empresa empresa, EmpresaAtualizacaoDTO dto);

}
