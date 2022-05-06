package br.com.villefortconsulting.sgfinancas.cnab;

import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import java.util.List;
import org.jrimum.bopepo.BancosSuportados;
import static org.jrimum.utilix.Collections.checkNotEmpty;

public class ArquivoLayout {

    public String retornarArquivoLayout(List<String> lines) {
        String codCompensacao = "";
        try {
            checkNotEmpty(lines, "Linhas ausentes!");
            codCompensacao = lines.get(0).substring(76, 79);
            Integer tamnhoArquivo = lines.get(0).length();
            switch (tamnhoArquivo) {
                case 400:
                    return arquivoLayoutCnab400(codCompensacao);
                case 240:
                    return arquivoLayoutCnab240(codCompensacao);
                default:
                    throw new Exception("Arquivo de retorno inválido");
            }
        } catch (Exception e) {
        }
        return codCompensacao;
    }

    private String arquivoLayoutCnab240(String codCompensacao) {
        String layout = "";

        if (codCompensacao.equals(BancosSuportados.BANCO_ITAU.getCodigoDeCompensacao())) {
            layout = "LayoutItauCNAB400Retorno.txg.xml";
        } else if (codCompensacao
                .equals(BancosSuportados.CAIXA_ECONOMICA_FEDERAL.getCodigoDeCompensacao())) {
            layout = "leiaute_retorno_caixa_240.xml";
        } else if (codCompensacao.equals(BancosSuportados.BANCO_SANTANDER.getCodigoDeCompensacao())) {
            layout = "leiaute_retorno_santander_240.xml";
        } else {
            throw new CadastroException("Banco não suportado!", null);
        }
        return layout;
    }

    private String arquivoLayoutCnab400(String codCompensacao) {
        String layout = "";
        if (codCompensacao.equals(BancosSuportados.BANCO_ITAU.getCodigoDeCompensacao())) {
            layout = "LayoutItauCNAB400Retorno.txg.xml";
        } else if (codCompensacao
                .equals(BancosSuportados.CAIXA_ECONOMICA_FEDERAL.getCodigoDeCompensacao())) {
            layout = "leiaute_retorno_caixa_400.xml";
        } else if (codCompensacao.equals(BancosSuportados.BANCO_SANTANDER.getCodigoDeCompensacao())) {
            layout = "leiaute_retorno_santander_400.xml";
        } else if (codCompensacao.equals(BancosSuportados.HSBC.getCodigoDeCompensacao())) {
            layout = "leiaute_retorno_hsbc_400.xml";
        } else if (codCompensacao
                .equals(BancosSuportados.BANCO_DO_ESTADO_DO_ESPIRITO_SANTO.getCodigoDeCompensacao())) {
            layout = "leiaute_retorno_banestes_400.xml";
        } else {
            throw new CadastroException("Banco não suportado!", null);
        }
        return layout;
    }

}
