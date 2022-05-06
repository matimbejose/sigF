package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Ctiss;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CtissDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NfsDTO;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.CtissService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class NFSeMapper {

    @Inject
    protected CidadeService cidadeService;

    @Inject
    protected ClienteService clienteService;

    @Inject
    protected CtissService ctissService;

    @Inject
    protected VendaService vendaService;

    @Mapping(target = "descricaoCtiss", source = "idCtiss.descricao")
    public abstract NfsDTO toDTO(NFS nfs);

    @Mapping(target = "idCliente", expression = "java(clienteService.buscar(dto.getIdCliente()))")
    @Mapping(target = "idVenda", expression = "java(dto.getIdVenda() != null ? vendaService.buscar(dto.getIdVenda()) : null)")
    @Mapping(target = "contaParcelaList", ignore = true)
    @Mapping(target = "idCtiss", expression = "java(ctissService.buscar(dto.getIdCtiss()))")
    @Mapping(target = "idMunicipioISSQN", expression = "java(cidadeService.buscar(dto.getIdMunicipioISSQN()))")
    @Mapping(target = "idCidadeCliente", expression = "java(cidadeService.buscar(dto.getIdCidadeCliente()))")
    @Mapping(target = "idUFCliente", expression = "java(nFS.getIdCidadeCliente().getIdUF())", dependsOn = "idCidadeCliente")
    @Mapping(target = "erro", ignore = true)
    public abstract NFS toEntity(NfsDTO dto);

    public abstract CtissDTO toDTO(Ctiss ctiss);

}
