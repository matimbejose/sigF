package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumAnexosSolicitacaoCadastroCliente {

    // Geral
    CNH_FRENTE(true, true, "CNH frente", null),
    CNH_VERSO(true, true, "CNH verso", null),
    COMPROVANTE_ENDERECO(true, true, "Comprovante de endereço", null),
    DUT(true, true, "DUT veículo", null),
    //
    // Carros
    KM_VEICULO_LIGADO(false, true, "KM veículo ligado", EnumTipoVeiculoFipe.CARROS),
    CAPO_ABERTO(false, true, "Capô aberto", EnumTipoVeiculoFipe.CARROS),
    INTERIOR_VEICULO(false, true, "Interior do veículo", EnumTipoVeiculoFipe.CARROS),
    BATERIA(false, true, "Bateria com a marca", EnumTipoVeiculoFipe.CARROS),
    PARABRISA(false, true, "Parabrisa", EnumTipoVeiculoFipe.CARROS),
    VIDRO_PORTA_DIANTEIRA_DIREITA(false, true, "Vidro da porta dianteira direita", EnumTipoVeiculoFipe.CARROS),
    VIDRO_PORTA_DIANTEIRA_ESQUERDA(false, true, "Vidro da porta dianteira esquerda", EnumTipoVeiculoFipe.CARROS),
    VIDRO_PORTA_TRASEIRA_DIREITA(false, false, "Vidro da porta traseira direita", EnumTipoVeiculoFipe.CARROS),
    VIDRO_PORTA_TRASEIRA_ESQUERDA(false, false, "Vidro da porta traseira esquerda", EnumTipoVeiculoFipe.CARROS),
    VIDRO_RETROVISOR_DIREITO(false, false, "Vidro retrovisor direito", EnumTipoVeiculoFipe.CARROS),
    VIDRO_RETROVISOR_ESQUERDO(false, false, "Vidro retrovisor esquerdo", EnumTipoVeiculoFipe.CARROS),
    VIDRO_TRASEIRO(false, true, "Vidro traseiro", EnumTipoVeiculoFipe.CARROS),
    TAMPA_TRASEIRA_ABERTA(false, true, "Tampa traseira aberta", EnumTipoVeiculoFipe.CARROS),
    PNEU_DIANTEIRO_DIREITA(false, true, "Pneu dianteiro direito com marca", EnumTipoVeiculoFipe.CARROS),
    PNEU_DIANTEIRO_ESQUERDA(false, true, "Pneu dianteiro esquerdo com marca", EnumTipoVeiculoFipe.CARROS),
    PNEU_TRASEIRO_DIREITA(false, true, "Pneu traseiro direito com marca", EnumTipoVeiculoFipe.CARROS),
    PNEU_TRASEIRO_ESQUERDA(false, true, "Pneu traseiro esquerdo com marca", EnumTipoVeiculoFipe.CARROS),
    //
    // Motos
    ANGULO_90_DIANTEIRO_DIREITO(false, true, "Ângulos de 90° dianteiro lado direito", EnumTipoVeiculoFipe.MOTOS),
    ANGULO_90_TRASEIRO_DIREITO(false, true, "Ângulos de 90° traseiro lado direito", EnumTipoVeiculoFipe.MOTOS),
    ANGULO_90_DIANTEIRO_ESQUERDO(false, true, "Ângulos de 90° dianteiro lado esquerdo", EnumTipoVeiculoFipe.MOTOS),
    ANGULO_90_TRASEIRO_ESQUERDO(false, true, "Ângulos de 90° traseiro lado esquerdo", EnumTipoVeiculoFipe.MOTOS),
    PAINEL(false, true, "Painel com veículo ligado", EnumTipoVeiculoFipe.MOTOS),
    CHASSI(false, true, "Chassi", EnumTipoVeiculoFipe.MOTOS),
    LATERAL_DIREITA(false, true, "Lateral direita", EnumTipoVeiculoFipe.MOTOS),
    LATERAL_ESQUERDA(false, true, "Lateral esquerda", EnumTipoVeiculoFipe.MOTOS),
    RODA_DIANTEIRA(false, true, "Roda dianteira", EnumTipoVeiculoFipe.MOTOS),
    RODA_TRASEIRA(false, true, "Roda traseira", EnumTipoVeiculoFipe.MOTOS),
    //
    // Caminhões
    _90_GRAUS_DIANTEIRO(false, true, "90° dianteiro", EnumTipoVeiculoFipe.CAMINHOES),
    _90_GRAUS_TRASEIRO(false, true, "90° traseiro", EnumTipoVeiculoFipe.CAMINHOES),
    INTERIOR_CABINE(false, true, "Interior da cabine", EnumTipoVeiculoFipe.CAMINHOES),
    CAPO_ABERTO_CAMINHAO(false, true, "Capo aberto com motor", EnumTipoVeiculoFipe.CAMINHOES),
    PAINEL_CAMINHAO(false, true, "Painel Km com veículo ligado", EnumTipoVeiculoFipe.CAMINHOES),
    CHASSI_CAMINHAO(false, true, "Chassi", EnumTipoVeiculoFipe.CAMINHOES),
    PNEU_DIANTEIRO_DIREITA_CAMINHAO(false, true, "Pneus dianteiro", EnumTipoVeiculoFipe.CAMINHOES),
    PNEU_DIANTEIRO_ESQUERDA_CAMINHAO(false, true, "Pneus traseiro", EnumTipoVeiculoFipe.CAMINHOES),
    PNEU_TRASEIRO_DIREITA_CAMINHAO(false, true, "Lateral direita", EnumTipoVeiculoFipe.CAMINHOES),
    PNEU_TRASEIRO_ESQUERDA_CAMINHAO(false, true, "Lateral esquerda", EnumTipoVeiculoFipe.CAMINHOES);

    private final boolean documento;

    private final boolean obrigatorio;

    private final String descricao;

    private final EnumTipoVeiculoFipe tipo;

}
