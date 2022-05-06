package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioAtendimento;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FuncionarioAtendimentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FuncionarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FuncionarioMinDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoDTO;
import br.com.villefortconsulting.util.FileUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface FuncionarioMapper {

    @Mapping(target = "planoConta", source = "idPlanoConta")
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "codIBGE", source = "idCidade.codigoIBGE")
    @Mapping(target = "cidade", source = "idCidade.descricao")
    @Mapping(target = "estado", source = "idCidade.idUF.descricao")
    FuncionarioDTO toDTO(Funcionario funcionario);

    @Mapping(target = "thumbnail", expression = "java(createThumb(funcionario.getFoto()))")
    FuncionarioMinDTO toMinDTO(Funcionario funcionario);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idCidade", ignore = true)
    @Mapping(target = "idPlanoConta", ignore = true)
    @Mapping(target = "funcionarioAtendimentoList", ignore = true)
    @Mapping(target = "funcionarioServicoList", ignore = true)
    Funcionario toEntity(FuncionarioDTO funcionarioDTO);

    @Mapping(target = "horaInicial", expression = "java(fa.getHoraInicial())")
    @Mapping(target = "horaFinal", expression = "java(fa.getHoraFinal())")
    FuncionarioAtendimentoDTO toDTO(FuncionarioAtendimento fa);

    @Mapping(target = "id", expression = "java(dto.getId())")
    @Mapping(target = "tenatID", expression = "java(tenantID)")
    @Mapping(target = "idFuncionario", expression = "java(func)")
    @Mapping(target = "horaInicial", expression = "java(dto.obterHoraInicial().toDate())")
    @Mapping(target = "horaFinal", expression = "java(dto.obterHoraFinal().toDate())")
    FuncionarioAtendimento toEntity(FuncionarioAtendimentoDTO dto, Funcionario func, String tenantID);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenatID", expression = "java(tenantID)")
    @Mapping(target = "idFuncionario", expression = "java(func)")
    @Mapping(target = "idServico", expression = "java(new br.com.villefortconsulting.sgfinancas.entidades.Servico(dto.getId()))")
    FuncionarioServico toEntity(ServicoDTO dto, Funcionario func, String tenantID);

    @AfterMapping
    default void afterMapToDto(@MappingTarget FuncionarioDTO target) {
        if (target.getFuncionarioAtendimentoList() != null) {
            target.getFuncionarioAtendimentoList().sort((a, b) -> a.getDiaSemana().compareTo(b.getDiaSemana()));
        }
    }

    default String createThumb(byte[] foto) {
        return FileUtil.createThumb(foto);
    }

}
