package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PermissaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioAppCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioDTO;
import br.com.villefortconsulting.sgfinancas.servicos.PerfilService;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {EntityMapper.class}, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class UsuarioMapper {

    @Inject
    protected PerfilService perfilService;

    @Mapping(target = "cargo", source = "idFuncionario.cargo")
    @Mapping(target = "cpf", source = "idFuncionario.cpf")
    @Mapping(target = "perfilSigla", source = "idPerfil.tipo")
    @Mapping(target = "perfilNome", source = "idPerfil.descricao")
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "dataNascimento", ignore = true)
    @Mapping(target = "veiculoList", ignore = true)
    public abstract UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "contaExpirada", constant = "N")
    @Mapping(target = "usuarioPermissaoList", ignore = true)
    @Mapping(target = "recebeEmailComunicacao", ignore = true)
    @Mapping(target = "usuarioGrupoEmpresaList", ignore = true)
    @Mapping(target = "qtdMensagensNaoLidas", ignore = true)
    @Mapping(target = "foto", expression = "java(new byte[]{})")
    @Mapping(target = "idPerfil", expression = "java(perfilByTipo(usuarioDTO.getPerfil()))")
    @Mapping(target = "tenat", ignore = true)
    @Mapping(target = "senhaExpirada", ignore = true)
    @Mapping(target = "tema", ignore = true)
    @Mapping(target = "telaBloqueada", ignore = true)
    @Mapping(target = "loginAcesso", ignore = true)
    @Mapping(target = "administrator", constant = "N")
    @Mapping(target = "cadCredenciamento", constant = "N")
    @Mapping(target = "qtdEmpresas", ignore = true)
    @Mapping(target = "loginAcessoList", ignore = true)
    @Mapping(target = "idContabilidade", ignore = true)
    @Mapping(target = "idFuncionario.id", source = "funcionario")
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "menuMode", ignore = true)
    @Mapping(target = "usuarioAcessoRapidoList", ignore = true)
    public abstract Usuario toEntity(UsuarioCadastroDTO usuarioDTO);

    @Mapping(target = "foto", expression = "java(new byte[]{})")
    @Mapping(target = "idPerfil", expression = "java(perfilByTipo(usuarioDTO.getPerfil()))")
    @Mapping(target = "contaExpirada", constant = "N")
    @Mapping(target = "contaBloqueada", constant = "N")
    @Mapping(target = "cadCredenciamento", constant = "N")
    @Mapping(target = "administrator", constant = "N")
    @Mapping(target = "senhaExpirada", constant = "N")
    @Mapping(target = "telaBloqueada", constant = "N")
    public abstract Usuario toEntity(UsuarioAppCadastroDTO usuarioDTO);

    @Mapping(target = "nome", source = "descricao")
    @Mapping(target = "descricao", source = "descricaoDetalhada")
    @Mapping(target = "possuiPermissao", expression = "java(false)")
    public abstract PermissaoDTO toDTO(Permissao permissao);

    protected Perfil perfilByTipo(String tipo) {
        return perfilService.getPerfil(tipo);
    }

}
