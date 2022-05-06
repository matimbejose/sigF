package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DocumentoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import java.util.List;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class DocumentoMapper {

    @Inject
    private DocumentoAnexoService documentoAnexoService;

    @Inject
    private DocumentoService documentoService;

    @Mapping(target = "nome", source = "nomeArquivo")
    @Mapping(target = "mimeType", source = "contentType")
    @Mapping(target = "conteudo", source = "conteudoArquivo64")
    @Mapping(target = "nomeUsuarioEnvio", source = "idUsuarioEnvio.nome")
    public abstract AnexoDTO toDTO(DocumentoAnexo documentoAnexo);

    @Mapping(target = "listaAnexo", source = "documentoAnexoList")
    public abstract DocumentoDTO toDTO(Documento documento);

    public Documento toEntity(Documento doc, Usuario usuario, String nome, List<AnexoDTO> listaAnexos) {
        if (nome == null) {
            nome = "";
        }
        if (doc == null) {
            doc = documentoService.criarDocumento(usuario, nome);
        }
        return documentoAnexoService.persistirAnexoDTO(doc, usuario, listaAnexos);
    }

}
