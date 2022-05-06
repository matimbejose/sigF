package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.sgfinancas.util.EnumAnexosSolicitacaoCadastroCliente;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
import java.util.Arrays;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCadastroClienteVeiculoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String tipo;

    private String marca;

    private DescricaoDTO modelo;

    private Integer anoModelo;

    private DocumentoDTO anexos;

    private DescricaoDTO corVeiculo;

    private DescricaoDTO combustivel;

    private Integer anoFabricacao;

    private String placa;

    private String cambio;

    private Integer nroPortas;

    private Integer nroPassageiros;

    private String renavam;

    private String chassi;

    private Double valorFipe;

    private String status;

    private Date dataModificacao;

    private String observacao;

    public boolean isAnexosOk() {
        EnumTipoVeiculoFipe tipoVeiculo = EnumTipoVeiculoFipe.retornaEnumSelecionado(tipo);
        if (tipo == null) {
            return false;
        }
        return anexos != null && anexos.getListaAnexo() != null && !anexos.getListaAnexo().isEmpty() && Arrays.stream(EnumAnexosSolicitacaoCadastroCliente.values())
                .filter(eascc -> eascc.getTipo() == tipoVeiculo || eascc.getTipo() == null)
                .filter(EnumAnexosSolicitacaoCadastroCliente::isObrigatorio)
                .allMatch(e -> anexos.getListaAnexo().stream().anyMatch(anexo -> anexo.getNome().startsWith(e.name()) || anexo.getNome().startsWith(e.getDescricao())));
    }

}
