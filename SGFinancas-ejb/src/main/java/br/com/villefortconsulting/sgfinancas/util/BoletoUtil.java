package br.com.villefortconsulting.sgfinancas.util;

import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BoletoUtil {

    public static String calcularDVBanestes(String nn) {
        String[] chars = nn.split("");

        int soma = 0;
        int peso = 9;
        for (String s : chars) {
            if (!"".equals(s)) {
                int val = Integer.parseInt(s) * peso;

                soma += val;
                peso--;
            }
        }

        int resto = soma % 11;
        Integer digito1 = 0;
        String digitoStr1;
        if (resto > 1) {
            digito1 = 11 - resto;
        }

        digitoStr1 = digito1.toString();

        chars = (nn + digitoStr1).split("");

        peso = 10;
        soma = 0;

        for (String s : chars) {
            if (!"".equals(s)) {
                int val = Integer.parseInt(s) * peso;

                soma += val;
                peso--;
            }
        }

        resto = soma % 11;
        Integer digito2 = 0;
        if (resto > 1) {
            digito2 = 11 - resto;
        }

        return digitoStr1 + digito2.toString();
    }

    public static Integer calculeDVModulo11(String numero) {
        int dv = 0;
        int[] values = new int[numero.length()];
        for (int i = 0; i < numero.length(); i++) {
            values[i] = Integer.parseInt(numero.charAt(i) + "");
        }
        int soma = 0;
        int vetpos = numero.length() - 1;
        while (vetpos >= 0) {
            for (int i = 2; i < 10; i++) {
                soma += values[vetpos] * i;
                vetpos--;
                if (vetpos < 0) {
                    break;
                }
            }
        }

        if (soma < 11) {
            dv = soma - 11;
        } else {
            int resto = soma % 11;
            dv = 11 - resto;
        }

        if (dv > 9) {
            dv = 0;
        }

        return dv;
    }

    public static int calculeDVcedente(String numero) {
        int dv = 0;

        int[] values = new int[numero.length()];
        for (int i = 0; i < numero.length(); i++) {
            values[i] = Integer.parseInt(numero.charAt(i) + "");
        }
        int soma = 0;
        int posicaoVetor = numero.length() - 1;
        while (posicaoVetor >= 0) {
            for (int i = 2; i < 10; i++) {
                soma += values[posicaoVetor] * i;
                posicaoVetor--;
                if (posicaoVetor < 0) {
                    break;
                }
            }

        }

        if (soma < 11) {
            dv = soma - 11;
        } else {
            int resto = soma % 11;
            dv = 11 - resto;
        }

        if (dv > 9) {
            dv = 0;
        }

        return dv;
    }

    public static void concatenaObservacao(String[] msg, String[] instrucao) {
        int c = 0;

        for (String obs : instrucao) {
            if (c > 15) {
                continue;
            }
            msg[(c < 2) ? 0 : (c / 2)] += obs + " ";
            c++;
        }
    }

    public static String calcularDvNossoNumItau(String agencia, String conta, String carteira, String nossoNumero) {
        int modulo = 10;
        int sequencial = 1;
        String newString = agencia + conta + carteira + nossoNumero;
        int resultadoSoma = 0;
        for (int i = 0; i < newString.length(); i++) {
            String aux = String.valueOf(Integer.parseInt(newString.substring(i, i + 1)) * sequencial);
            if (aux.length() > 1) {
                for (int j = 0; j < aux.length(); j++) {
                    resultadoSoma += Integer.parseInt(aux.substring(j, j + 1));
                }
            } else {
                resultadoSoma += Integer.parseInt(newString.substring(i, i + 1)) * sequencial;
            }
            sequencial++;
            if (sequencial > 2) {
                sequencial = 1;
            }
        }
        return String.valueOf(modulo - (resultadoSoma % modulo));
    }

    public static String calcularDvNossoNumMercantil(String agencia, String nossoNumero) {
        Integer modulo = 11;
        Integer sequencial = 7;
        Double resultadoSoma = 0d;

        String newString = agencia + nossoNumero;
        String digito;

        for (int i = 0; i < newString.length(); i++) {
            Integer aux = Integer.parseInt(newString.substring(i, i + 1));

            resultadoSoma += aux * sequencial;

            sequencial--;
            if (sequencial == 1) {
                sequencial = 9;
            }
        }
        Integer resto = (int) (resultadoSoma % modulo);
        if (resto == 0 || resto == 1) {
            digito = "0";
        } else {
            digito = String.valueOf(modulo - resto);
        }
        return digito;
    }

    public static String calcularDvNossoNumSantander(String nossoNumero) {
        int j = 2;
        int digito = 0;
        int resto = 0;
        int soma = 0;

        for (int i = nossoNumero.length(); i > 0; i--) {
            String auxiliar = nossoNumero.substring(i - 1, i);
            Integer aux = Integer.parseInt(auxiliar);
            soma += aux * j;
            j++;
            if (j > 9) {
                j = 2;
            }
        }

        resto = soma % 11;
        digito = 11 - resto;

        if (digito == 1 || digito == 10) {
            digito = 0;
        }

        return String.valueOf(digito);
    }

    public static String completaCampoCaixa(String valor, Integer tamanho, String tipo) {
        return (tipo.equals("COMREG") ? "24" : "14") + StringUtil.adicionarCaracterEsquerda(valor, "0", tamanho - 2);
    }

    public static String completaCampoBB(String valor, Integer tamanho, String codigoCedente) {
        int tamanhoCodigo = valor.length();
        tamanho -= tamanhoCodigo;
        StringBuilder sb = new StringBuilder(codigoCedente);
        while (codigoCedente.length() < tamanho) {
            sb.append(codigoCedente).append("0");
        }
        sb.append(valor);
        return sb.toString();
    }

    public static String completaCampo(String nossoNumero, int tamanhoMaximo) {
        return StringUtil.adicionarCaracterEsquerda(nossoNumero, "0", tamanhoMaximo);
    }

    public static String calcularDv1NossoNumHSBC(String codigocedente) {
        int j = 9;
        int digito = 0;
        int soma = 0;

        for (int i = codigocedente.length(); i > 0; i--) {

            String auxiliar = codigocedente.substring(i - 1, i);
            Integer aux = Integer.parseInt(auxiliar);
            soma += aux * j;
            j--;
            if (j < 2) {
                j = 9;
            }
        }

        digito = soma % 11;

        if (digito > 9) {
            digito = 0;
        }

        return String.valueOf(digito);
    }

    public static String calcularDv2NossoNumHSBC(String codigoPagador, String codigocedente, String data) {
        int j = 9;
        int digito = 0;
        int soma = 0;
        int aux = 0;
        codigocedente = completaCampo(codigocedente, codigoPagador.length());
        data = completaCampo(data, codigoPagador.length());

        for (int i = codigoPagador.length(); i > 0; i--) {
            int somaColuna = 0;
            Integer auxCodCedente = Integer.parseInt(codigocedente.substring(i - 1, i));
            Integer auxCodPagador = Integer.parseInt(codigoPagador.substring(i - 1, i));
            Integer auxData = Integer.parseInt(data.substring(i - 1, i));

            somaColuna += auxCodCedente + auxCodPagador + auxData + aux;
            if (somaColuna > 9 && somaColuna < 20) {
                somaColuna %= 10;
                aux = 1;
            } else if (somaColuna > 19) {
                somaColuna %= 20;
                aux = 2;
            } else {
                aux = 0;
            }
            soma += somaColuna * j;
            j--;
            if (j < 2) {
                j = 9;
            }
        }

        digito = soma % 11;

        if (digito > 9) {
            digito = 0;
        }

        return String.valueOf(digito);
    }

    public static String retornaDigitoNossoNumero(String nossoNumero, String carteira) {
        Integer somaMultiplicacao = 0;
        Integer index = 2;
        Integer restoDivisao;
        nossoNumero = carteira + nossoNumero;

        Integer num;
        for (Integer i = nossoNumero.length() - 1; i > 0; i--) {
            num = Integer.parseInt("" + nossoNumero.charAt(i));
            somaMultiplicacao += num * index;
            index = (index == 7) ? 2 : index + 1;
        }

        restoDivisao = somaMultiplicacao / 11;

        if (11 - restoDivisao == 1) {
            return "P";
        } else if (restoDivisao == 0) {
            return "0";
        } else {
            return "" + (11 - restoDivisao);
        }
    }

    public static String codigoDeBarras(String entrada) {
        String[] barcodes = {"00110", "10001", "01001", "11000", "00101", "10100", "01100", "00011", "10010", "01010"};
        String[] generatedBarcodes = new String[100];

        for (Integer f1 = 9; f1 >= 0; f1--) {
            for (Integer f2 = 9; f2 >= 0; f2--) {
                Integer f = (f1 * 10) + f2;
                StringBuilder texto = new StringBuilder();

                for (Integer i = 1; i < 6; i++) {
                    texto.append(barcodes[f1].substring(i - 1, i))
                            .append(barcodes[f2].substring(i - 1, i));
                }
                generatedBarcodes[f] = texto.toString();
            }
        }
        return desenhoDaBarra(generatedBarcodes, entrada);
    }

    public static String desenhoDaBarra(String[] generatedBarcodes, String entrada) {
        StringBuilder html = new StringBuilder();

        String fino = "1";
        String largo = "3";
        String altura = "50";
        String texto = ((entrada.length() % 2) == 0) ? entrada : "0" + entrada;

        html.append("<img src=../img/cnab/p.png width=").append(fino).append(" height=").append(altura).append(" border=0>")
                .append("<img src=../img/cnab/b.png width=").append(fino).append(" height=").append(altura).append(" border=0>")
                .append("<img src=../img/cnab/p.png width=").append(fino).append(" height=").append(altura).append(" border=0>")
                .append("<img src=../img/cnab/b.png width=").append(fino).append(" height=").append(altura).append(" border=0><img ");

        while (texto.length() > 0) {
            Integer i = (int) Math.round(Double.parseDouble(esquerda(texto, 2)));
            texto = direita(texto, texto.length() - 2);

            String f = generatedBarcodes[i];
            for (i = 1; i < 11; i += 2) {
                html.append("src=../img/cnab/p.png width=")
                        .append(f.substring(i - 1, i).equals("0") ? fino : largo)
                        .append(" height=").append(altura).append(" border=0><img ")
                        .append("src=../img/cnab/b.png width=")
                        .append(f.substring(i, i + 1).equals("0") ? fino : largo)
                        .append(" height=").append(altura).append(" border=0><img ");
            }
        }

        html.append("src=../img/cnab/p.png width=").append(largo).append(" height=").append(altura).append(" border=0><img ")
                .append("src=../img/cnab/b.png width=").append(fino).append(" height=").append(altura).append(" border=0><img ")
                .append("src=../img/cnab/p.png width=").append(fino).append(" height=").append(altura).append(" border=0>");

        return html.toString();
    }

    public static String esquerda(String entrada, Integer comp) {
        return entrada.substring(0, comp);
    }

    public static String direita(String entrada, Integer comp) {
        return entrada.substring(entrada.length() - comp, entrada.length());
    }

    public static String apresentaLinhaDigitavel(String[] camposLinhaDigitavel, String[] digitos) {
        StringBuilder linhaDigitavelSTR = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            linhaDigitavelSTR
                    .append(camposLinhaDigitavel[i].substring(0, 5))
                    .append(".")
                    .append(camposLinhaDigitavel[i].substring(5, camposLinhaDigitavel[i].length()))
                    .append(digitos[i])
                    .append(" ");
        }
        linhaDigitavelSTR
                .append(camposLinhaDigitavel[3])
                .append(" ")
                .append(camposLinhaDigitavel[4]);

        return linhaDigitavelSTR.toString();
    }

    public static String[] adicionaDigitosVerificadores(String[] camposLinhaDigitavel) {
        // Separacao dos numeros
        String[] digitos = new String[camposLinhaDigitavel.length];
        Integer index = 0;

        for (String campo : camposLinhaDigitavel) {
            Integer fator = 2;
            Integer soma = 0;
            Integer mult;
            Integer modulo;

            for (Integer i = campo.length() - 1; i >= 0; i--) {
                Character numero = campo.charAt(i);
                mult = Integer.parseInt(numero.toString()) * fator;

                if (mult > 10) {
                    String multStr = mult.toString();
                    soma += Integer.parseInt("" + multStr.charAt(0)) + Integer.parseInt("" + multStr.charAt(1));
                } else {
                    soma += mult;
                }

                fator = (fator % 2) + 1;
            }
            digitos[index] = (modulo = (soma % 10)) == 0 ? "0" : ("" + (10 - modulo));
            index++;
        }
        return digitos;
    }

    public static String[] camposLinhaDigitavel(String codigoBarraComDigitoVerificador) {
        return new String[]{
            codigoBarraComDigitoVerificador.substring(0, 4) + codigoBarraComDigitoVerificador.substring(19, 20) + codigoBarraComDigitoVerificador.substring(20, 24),
            codigoBarraComDigitoVerificador.substring(24, 34), // 6 posicao até 15
            codigoBarraComDigitoVerificador.substring(34, 44), // 16 ate 25
            codigoBarraComDigitoVerificador.substring(4, 5), // digito verificador
            codigoBarraComDigitoVerificador.substring(5, 19) // fator de vencimento e valor sem separadores, 6 até 20
        };
    }

    public static String digitoVerificadorCodigoBarra(String codigoBarra) {
        Integer soma = 0;
        Integer fator = 2;
        Integer total;

        for (Integer i = codigoBarra.length() - 1; i >= 0; --i) {
            // pega cada numero isoladamente
            Character numero = codigoBarra.charAt(i);

            // Efetua multiplicacao do numero pelo falor
            soma += Integer.parseInt(numero.toString()) * fator;
            fator = (fator == 9) ? 2 : fator + 1;
        }

        total = 11 - (soma % 11);
        return (total == 0 || total == 1 || total > 9) ? "1" : total.toString();
    }

    public static String calculaFatorVencimento(Date date) {
        Long dataBase = 876193200000l; // 07/10/1997
        // Para testar -> return DateHelper.daysBetween(new Long("962593200000"), new Date(dataBase)).toString(); -> resultado 1000
        return DataUtil.diferencaEntreDias(date, new Date(dataBase)).toString();
    }

    public static String formatarContaBanestes(String codigoConta) {
        int sizeConta = 8;
        String randConta = "";
        int leftConta = sizeConta - codigoConta.length();

        for (int i = 0; i < leftConta; i++) {
            randConta = randConta.concat("0");
        }

        codigoConta = randConta + codigoConta;

        String bloco1 = codigoConta.substring(0, 2);
        String bloco2 = codigoConta.substring(2, 5);
        String bloco3 = codigoConta.substring(5, 8);

        return bloco1 + "." + bloco2 + "." + bloco3;

    }

    public static String formatarAgenciaBanestes(String codigoAgencia) {
        int sizeAgencia = 3;
        String randConta = "";
        int leftConta = sizeAgencia - codigoAgencia.length();

        for (int i = 0; i < leftConta; i++) {
            randConta = randConta.concat("0");
        }

        return randConta + codigoAgencia;
    }

}
