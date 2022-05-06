package br.com.villefortconsulting.sgfinancas.cnab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import lombok.Getter;
import org.jrimum.texgit.Record;
import static org.jrimum.utilix.Collections.checkNotEmpty;
import static org.jrimum.utilix.Collections.hasElement;

@Getter
public class ArquivoRetorno extends AbstractFlatFile {

    private Protocolo protocolo;

    private List<TransacaoDeTitulo> transacoes;

    private Sumario sumario;

    private static String layout = "";

    private ArquivoRetorno() {
        super(layout);
    }

    private ArquivoRetorno(String cfgFile) {
        super(cfgFile);
    }

    public static ArquivoRetorno newInstance(List<String> lines) throws IOException {

        checkNotEmpty(lines, "Linhas ausentes!");
        layout = new ArquivoLayout().retornarArquivoLayout(lines);
        ArquivoRetorno ff = new ArquivoRetorno();

        ff.read(lines);

        ff.loadInfo();

        ff.write("UTF-8");

        return ff;

    }

    private void loadInfo() {

        this.protocolo = new Protocolo(getFlatFile().getRecord("Header"));
        this.sumario = new Sumario(getFlatFile().getRecord("Trailler"));

        Collection<Record> registrosDeTransacoes = getFlatFile().getRecords("TransacaoTitulo");

        if (hasElement(registrosDeTransacoes)) {
            ArrayList<TransacaoDeTitulo> transacoesAux = new ArrayList<>(
                    registrosDeTransacoes.size());
            for (Record registro : registrosDeTransacoes) {
                try {
                    transacoesAux.add(new TransacaoDeTitulo(registro));
                } catch (Exception e) {
                }
            }
            transacoes = transacoesAux;
        }
    }

    public Map<Integer, Collection<TransacaoDeTitulo>> getTransacoesPorCodigoDeOcorrencia() {

        Map<Integer, Collection<TransacaoDeTitulo>> transacoesPorOcorrencias = new TreeMap<>();

        if (hasElement(getTransacoes())) {

            for (TransacaoDeTitulo transacao : getTransacoes()) {
                try {
                    if (!transacoesPorOcorrencias.containsKey(transacao
                            .getCodigoDeOcorrencia())) {
                        ArrayList<TransacaoDeTitulo> trans = new ArrayList<>();
                        trans.add(transacao);
                        transacoesPorOcorrencias.put(transacao
                                .getCodigoDeOcorrencia(), trans);
                    } else {
                        transacoesPorOcorrencias.get(
                                transacao.getCodigoDeOcorrencia()).add(
                                transacao);
                    }
                } catch (Exception e) {
                }
            }
            return transacoesPorOcorrencias;
        }

        return Collections.emptyMap();
    }

    public Collection<String> getColecaoDeNossoNumero() {
        Set<String> numeros = new HashSet<>();

        if (hasElement(getTransacoes())) {
            getTransacoes().stream()
                    .map(TransacaoDeTitulo::getNossoNumeroComDigito)
                    .forEach(numeros::add);
        }

        return numeros;
    }

}
