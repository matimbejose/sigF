package br.com.villefortconsulting.sgfinancas.cnab;

import java.lang.reflect.Field;

public class LayoutCnab {

    public static String completaCampo(String valor, Integer tamanho, String tipo) {

        if (tipo.equals("numerico")) {
            while (valor.length() < tamanho) {
                valor = "0" + valor;
            }
        } else {
            while (valor.length() < tamanho) {
                valor += " ";
            }
        }

        return valor;
    }

    public String completaCampoCNAB(Field field) throws IllegalAccessException {

        CampoDelimitado campoCNAB = field.getAnnotation(CampoDelimitado.class);

        Integer digitos = Integer.parseInt(campoCNAB.digitos());
        String valorCampo = (String) field.get(this);
        valorCampo = valorCampo == null ? "" : valorCampo;

        if (campoCNAB.completar()) { // Apenas campos com auto completar ativo

            if (campoCNAB.tipo().equals("numerico") && (valorCampo.length() < digitos)) {

                while (valorCampo.length() < digitos) {
                    valorCampo = "0" + valorCampo;
                }
            } else if (campoCNAB.tipo().equals("alfanumerico") && (valorCampo.length() < digitos)) {

                while (valorCampo.length() < digitos) {
                    valorCampo += " ";
                }
            } else if (valorCampo.length() > digitos) {
                valorCampo = valorCampo.substring(0, digitos);
            }

            field.set(this, valorCampo);
        }

        return valorCampo;
    }

    public String toCNABString() {

        Class<?> layout = this.getClass();
        Field[] fields = layout.getDeclaredFields();

        String retorno = "";

        for (Field field : fields) {

            try {
                retorno += completaCampoCNAB(field);
            } catch (IllegalAccessException e) {
            }
        }

        return retorno;
    }

}
