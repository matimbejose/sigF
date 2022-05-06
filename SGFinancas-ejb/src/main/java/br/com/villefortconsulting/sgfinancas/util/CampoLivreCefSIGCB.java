package br.com.villefortconsulting.sgfinancas.util;

import org.jrimum.bopepo.campolivre.CampoLivre;
import org.jrimum.utilix.text.Field;
import org.jrimum.utilix.text.Filler;

public class CampoLivreCefSIGCB implements CampoLivre {

    private static final long serialVersionUID = -8102800826867486985L;

    String campoLivreFormatado = null;

    public CampoLivreCefSIGCB(String numeroConta, String dvNumConta, String nossoNumero) {
        String campo1 = formataCampo1(new Field<>(numeroConta.substring(0, 5), 5, Filler.ZERO_LEFT).write());
        String campo2 = formataCampo2(numeroConta, dvNumConta, nossoNumero);
        String campo3 = formataCampo3(nossoNumero, campo1, campo2);

        campoLivreFormatado = campo1 + campo2 + campo3;
    }

    @Override
    public void read(String arg0) {
        // Só é necessária a função de escrever
    }

    @Override
    public String write() {
        return campoLivreFormatado;
    }

    private static String formataCampo1(String numero) {
        return numero;
    }

    public static String formataCampo2(String numeroCedente, String dvNumCedente, String nossoNumero) {
        numeroCedente = numeroCedente.substring(5, 6);
        String modalidade = nossoNumero.substring(0, 1);
        String identificador = nossoNumero.substring(1, 2);
        String nossoNumero1 = nossoNumero.substring(2, 5);
        String nossoNumero2 = nossoNumero.substring(5, 8);

        return numeroCedente + dvNumCedente + nossoNumero1 + modalidade + nossoNumero2 + identificador;
    }

    public static String formataCampo3(String nossoNumero, String campo1, String campo2) {
        nossoNumero = nossoNumero.substring(8, 17);
        Integer dv = BoletoUtil.calculeDVModulo11(campo1 + campo2 + nossoNumero);

        return nossoNumero + dv;
    }

}
