package br.com.villefortconsulting.sgfinancas.util;

import br.com.villefortconsulting.sgfinancas.entidades.Boleto;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.servicos.exception.BoletoException;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RemessaUtil {

    public static Record createHeader(FlatFile<Record> ff, ContaBancaria contaBancaria, String nomeEmpresa, BancosSuportados banco) throws BoletoException {
        String codigoEmpresa;
        String agencia = contaBancaria.getAgencia().getCodigo().toString();
        String numeroConta = contaBancaria.getNumeroDaConta().getCodigoDaConta().toString();
        String digitoConta = contaBancaria.getNumeroDaConta().getDigitoDaConta();

        Record header = ff.createRecord("Header");

        switch (banco) {
            case BANCO_ITAU:
                codigoEmpresa = agencia + "00" + numeroConta + digitoConta;
                header.setValue("CodigoEmpresa", codigoEmpresa);
                header.setValue("NomeEmpresa", nomeEmpresa.substring(0, (nomeEmpresa.length() > 29) ? 29 : nomeEmpresa.length()));
                header.setValue("DataGravacao", new Date());
                header.setValue("DataFinal", new Date());
                header.setValue("NumeroSequencialRegistro", 1);
                break;
            case BANCO_SANTANDER:
                header.setValue("CodigoDoRegistro", 0);
                header.setValue("CodigoTransmissao", "39220905623801300314");
                header.setValue("NomeEmpresa", validarRegistroStringRemessa(nomeEmpresa, 29));
                header.setValue("DataGravacao", new Date());
                header.setValue("NumeroSequencialRegistro", 1);
                break;
            case CAIXA_ECONOMICA_FEDERAL:
                codigoEmpresa = agencia + "00" + numeroConta + digitoConta;
                header.setValue("CodigoDoRegistro", 0);
                header.setValue("CodigoEmpresa", codigoEmpresa);
                header.setValue("NomeEmpresa", validarRegistroStringRemessa(nomeEmpresa, 29));
                header.setValue("NumeroSequencialRemessa", 1);
                header.setValue("NumeroSequencialRegistro", 1);
                break;
            case BANCO_BRADESCO:
                header.setValue("CodigoDoRegistro", 0);
                header.setValue("CodigoRetorno", 1);
                header.setValue("CodigoBanco", 237);
                header.setValue("DataGravacao", new Date());
                break;
            case BANCO_DO_BRASIL:
                break;
            case HSBC:
                break;
            case BANCOOB:
                break;
            default:
                throw new BoletoException(" Banco não suportado para gerar remessa. ", null);
        }

        return header;
    }

    public static Record transacaoTitulo(FlatFile<Record> ff, BancosSuportados banco, Empresa empresa, Boleto boleto, br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria contaSistema, ContaBancaria contaBancaria, Integer numeroSequencialRegistro) throws BoletoException {
        Cliente cliente = boleto.getIdClienteSacado();

        if (cliente == null) {
            throw new BoletoException("Cliente não informado", null);
        }
        Cidade cidade = cliente.getEndereco().getIdCidade();

        Record transacaoTitulo = ff.createRecord("TransacaoTitulo");

        switch (banco) {
            case BANCO_ITAU:
                transacaoTitulo.setValue("InscricaoEmpresa", "2"); // 1- Pessoa fisica 2- Pessoa juridica

                // Registros conta bancaria
                transacaoTitulo.setValue("AgenciaCobradora", validarRegistroInteiroRemessa(contaBancaria.getAgencia().getCodigo(), 5));
                transacaoTitulo.setValue("Agencia", validarRegistroInteiroRemessa(contaBancaria.getAgencia().getCodigo(), 5));
                transacaoTitulo.setValue("Agencia", validarRegistroInteiroRemessa(contaBancaria.getAgencia().getCodigo(), 5));
                transacaoTitulo.setValue("Conta", validarRegistroInteiroRemessa(contaBancaria.getNumeroDaConta().getCodigoDaConta(), 5));
                transacaoTitulo.setValue("DigitoConta", validarRegistroStringRemessa(contaBancaria.getNumeroDaConta().getDigitoDaConta(), 1));
                transacaoTitulo.setValue("Carteira", validarRegistroInteiroRemessa(contaBancaria.getCarteira().getCodigo(), 3));

                // Registros da empresa
                transacaoTitulo.setValue("NumeroInscricao", validarRegistroStringRemessa(StringUtil.removerMascara(empresa.getCnpj()), 14));

                // Registros do boleto
                transacaoTitulo.setValue("NossoNumero", validarRegistroStringRemessa(boleto.getNossoNumero(), boleto.getNossoNumero().length()));
                transacaoTitulo.setValue("SeuNumero", validarRegistroInteiroRemessa(boleto.getId(), 25));
                transacaoTitulo.setValue("UsoEmpresa", validarRegistroInteiroRemessa(boleto.getId(), 10));
                transacaoTitulo.setValue("Vencimento", validarRegistroDateRemessa(boleto.getDataVencimento()));
                transacaoTitulo.setValue("DataEmissao", validarRegistroDateRemessa(boleto.getDataEmissao()));
                transacaoTitulo.setValue("DataDesconto", validarRegistroDateRemessa(boleto.getDataDesconto()));
                transacaoTitulo.setValue("DataMora", validarRegistroDateRemessa(boleto.getDataMora()));
                transacaoTitulo.setValue("ValorTitulo", validarRegistroDoubleRemessa(boleto.getValor(), 13));
                transacaoTitulo.setValue("ValorJuros", validarRegistroDoubleRemessa(boleto.getJuros(), 13));
                transacaoTitulo.setValue("ValorDesconto", validarRegistroDoubleRemessa(boleto.getValorDesconto(), 13));
                transacaoTitulo.setValue("ValorIOF", validarRegistroDoubleRemessa(boleto.getValorIof(), 13));
                transacaoTitulo.setValue("DiasProtesto", "00");
                transacaoTitulo.setValue("EspecieTitulo", "1");
                transacaoTitulo.setValue("Aceite", "N");

                // Registros do sacado
                transacaoTitulo.setValue("DocumentoSacado", validarRegistroStringRemessa(StringUtil.removerMascara(cliente.getCpfCNPJ()), 14));
                transacaoTitulo.setValue("TipoSacado", ("PF".equals(cliente.getTipo())) ? "2" : "1"); // 1- Pessoa fisica 2- Pessoa juridica
                transacaoTitulo.setValue("NomeSacado", validarRegistroStringRemessa(cliente.getNome(), 29));
                transacaoTitulo.setValue("EnderecoSacado", validarRegistroStringRemessa(cliente.getEndereco() + ", " + cliente.getEndereco().getNumero(), 40));
                transacaoTitulo.setValue("CepSacado", validarRegistroStringRemessa(StringUtil.removerMascara(cliente.getEndereco().getCep()), 8));
                transacaoTitulo.setValue("BairroSacado", validarRegistroStringRemessa(cliente.getEndereco().getBairro(), 12));
                if (cidade != null) {
                    transacaoTitulo.setValue("CidadeSacado", validarRegistroStringRemessa(cidade.getDescricao(), 15));
                    transacaoTitulo.setValue("EstadoSacado", validarRegistroStringRemessa(cidade.getIdUF().getDescricao(), 2));
                }

                transacaoTitulo.setValue("NumeroSequencialRegistro", numeroSequencialRegistro);
                break;
            case BANCO_SANTANDER:

                if (contaSistema.getConta().length() < 8) {
                    throw new BoletoException("Número da conta inválido para envio de remessa Santander, deve possuir no mínimo 8 caracteres", null);
                }

                transacaoTitulo.setValue("CodigoDoRegistro", "1");
                transacaoTitulo.setValue("InscricaoEmpresa", "2"); // 1- Pessoa fisica 2- Pessoa juridica
                transacaoTitulo.setValue("NumeroInscricao", validarRegistroStringRemessa(StringUtil.removerMascara(empresa.getCnpj()), 14));
                transacaoTitulo.setValue("Agencia", validarRegistroInteiroRemessa(contaBancaria.getAgencia().getCodigo(), 4));
                transacaoTitulo.setValue("Conta", validarRegistroInteiroRemessa(contaBancaria.getNumeroDaConta().getCodigoDaConta(), 8));
                transacaoTitulo.setValue("ContaCobranca", validarRegistroStringRemessa(contaSistema.getConta().substring(0, 8), 8));
                transacaoTitulo.setValue("UsoEmpresa", validarRegistroInteiroRemessa(boleto.getId(), 25));
                transacaoTitulo.setValue("NossoNumero", validarRegistroStringRemessa(boleto.getNossoNumero() + BoletoUtil.calcularDvNossoNumSantander(boleto.getNossoNumero()), 8));
                transacaoTitulo.setValue("DataSegundoDesconto", validarRegistroDateRemessa(boleto.getDataMora()));
                transacaoTitulo.setValue("Multa", boleto.getJuros() != null ? "4" : "0");
                transacaoTitulo.setValue("PercentualMulta", validarRegistroDoubleRemessaReturnZero(boleto.getPercentualJuros(), 4));
                transacaoTitulo.setValue("UnidadeValorMoeda", "00");
                transacaoTitulo.setValue("ValorTituloOutraUnidade", "0000000000000");
                transacaoTitulo.setValue("DataCobrancaMulta", "000000");
                transacaoTitulo.setValue("VariacaoCarteira", "5");
                transacaoTitulo.setValue("SeuNumero", validarRegistroInteiroRemessa(boleto.getId(), 10));
                transacaoTitulo.setValue("Vencimento", validarRegistroDateRemessa(boleto.getDataVencimento()));
                transacaoTitulo.setValue("ValorTitulo", validarRegistroDoubleRemessa(boleto.getValor(), 13));
                transacaoTitulo.setValue("AgenciaCobradora", validarRegistroInteiroRemessa(contaBancaria.getAgencia().getCodigo(), 5));
                transacaoTitulo.setValue("EspecieTitulo", "01");
                transacaoTitulo.setValue("Aceite", "N");
                transacaoTitulo.setValue("DataEmissao", validarRegistroDateRemessa(boleto.getDataEmissao()));
                transacaoTitulo.setValue("ValorJuros", validarRegistroDoubleRemessa(boleto.getJuros(), 13));
                transacaoTitulo.setValue("DataDesconto", validarRegistroDateRemessa(boleto.getDataDesconto()));
                transacaoTitulo.setValue("ValorDesconto", validarRegistroDoubleRemessa(boleto.getValorDesconto(), 13));
                transacaoTitulo.setValue("ValorIOF", validarRegistroDoubleRemessa(boleto.getValorIof(), 13));
                transacaoTitulo.setValue("ValorAbatimento", validarRegistroDoubleRemessa(boleto.getValorDesconto(), 13));
                transacaoTitulo.setValue("TipoSacado", ("PF".equals(cliente.getTipo())) ? 1 : 2); // 1- Pessoa fisica 2- Pessoa juridica
                transacaoTitulo.setValue("DocumentoSacado", validarRegistroStringRemessa(StringUtil.removerMascara(cliente.getCpfCNPJ()), 14));
                transacaoTitulo.setValue("NomeSacado", validarRegistroStringRemessa(cliente.getNome(), 40));
                transacaoTitulo.setValue("EnderecoSacado", validarRegistroStringRemessa(cliente.getEndereco() + ", " + cliente.getEndereco().getNumero(), 40));
                transacaoTitulo.setValue("CepSacado", validarRegistroStringRemessa(StringUtil.removerMascara(cliente.getEndereco().getCep()), 8));
                transacaoTitulo.setValue("BairroSacado", validarRegistroStringRemessa(cliente.getEndereco().getBairro(), 12));
                if (cidade != null) {
                    transacaoTitulo.setValue("CidadeSacado", validarRegistroStringRemessa(cidade.getDescricao(), 15));
                    transacaoTitulo.setValue("EstadoSacado", validarRegistroStringRemessa(cidade.getIdUF().getDescricao(), 2));
                }
                if (contaSistema.getConta().length() > 8 && contaSistema.getDigitoConta() != null) {
                    transacaoTitulo.setValue("IdentificadoComplemento", "I");
                    transacaoTitulo.setValue("Complemento", validarRegistroStringRemessa(contaSistema.getConta().charAt(contaSistema.getConta().length() - 1) + contaSistema.getDigitoConta(), 2));
                }
                transacaoTitulo.setValue("NumeroSequencialRegistro", numeroSequencialRegistro);

                break;
            case CAIXA_ECONOMICA_FEDERAL:

                if (contaSistema.getConta().length() < 8) {
                    throw new BoletoException("Número da conta inválido para envio de remessa Santander, deve possuir no mínimo 8 caracteres", null);
                }

                transacaoTitulo.setValue("CodigoDoRegistro", "1");
                transacaoTitulo.setValue("InscricaoEmpresa", "2"); // 1- Pessoa fisica 2- Pessoa juridica
                transacaoTitulo.setValue("NumeroInscricao", validarRegistroStringRemessa(StringUtil.removerMascara(empresa.getCnpj()), 14));
                transacaoTitulo.setValue("Agencia", validarRegistroInteiroRemessa(contaBancaria.getAgencia().getCodigo(), 4));
                transacaoTitulo.setValue("Operacao", validarRegistroStringRemessa(contaSistema.getOperacao(), 3));
                transacaoTitulo.setValue("Conta", validarRegistroStringRemessa(contaSistema.getConta(), 8));
                transacaoTitulo.setValue("DigitoConta", validarRegistroStringRemessa(contaSistema.getDigitoConta(), 1));
                transacaoTitulo.setValue("UsoEmpresa", validarRegistroInteiroRemessa(boleto.getId(), 25));
                transacaoTitulo.setValue("NossoNumero", validarRegistroStringRemessa(boleto.getNossoNumero(), 17));
                transacaoTitulo.setValue("PrimeiraMensagem", validarRegistroStringRemessa(boleto.getIdContaParcela().getObservacao(), 30));
                transacaoTitulo.setValue("Carteira", validarRegistroInteiroRemessa(contaBancaria.getCarteira().getCodigo(), 2));
                transacaoTitulo.setValue("CodigoOcorrencia", validarRegistroInteiroRemessa(1, 10));
                transacaoTitulo.setValue("SeuNumero", validarRegistroInteiroRemessa(boleto.getId(), 10));
                transacaoTitulo.setValue("Vencimento", validarRegistroDateRemessa(boleto.getDataVencimento()));
                transacaoTitulo.setValue("ValorTitulo", validarRegistroDoubleRemessa(boleto.getValor(), 13));
                transacaoTitulo.setValue("AgenciaCobradora", validarRegistroInteiroRemessa(contaBancaria.getAgencia().getCodigo(), 5));
                transacaoTitulo.setValue("EspecieTitulo", "01");
                transacaoTitulo.setValue("Aceite", "N");
                transacaoTitulo.setValue("DataEmissao", validarRegistroDateRemessa(boleto.getDataEmissao()));
                transacaoTitulo.setValue("ValorJuros", validarRegistroDoubleRemessa(boleto.getJuros(), 13));
                transacaoTitulo.setValue("DataDesconto", validarRegistroDateRemessa(boleto.getDataDesconto()));
                transacaoTitulo.setValue("ValorDesconto", validarRegistroDoubleRemessa(boleto.getValorDesconto(), 13));
                transacaoTitulo.setValue("ValorIOF", validarRegistroDoubleRemessa(boleto.getValorIof(), 13));
                transacaoTitulo.setValue("ValorAbatimento", validarRegistroDoubleRemessa(boleto.getValorDesconto(), 13));
                transacaoTitulo.setValue("TipoSacado", ("PF".equals(cliente.getTipo())) ? 1 : 2); // 1- Pessoa fisica 2- Pessoa juridica
                transacaoTitulo.setValue("DocumentoSacado", validarRegistroStringRemessa(StringUtil.removerMascara(cliente.getCpfCNPJ()), 14));
                transacaoTitulo.setValue("NomeSacado", validarRegistroStringRemessa(cliente.getNome(), 40));
                transacaoTitulo.setValue("EnderecoSacado", validarRegistroStringRemessa(cliente.getEndereco() + ", " + cliente.getEndereco().getNumero(), 40));
                transacaoTitulo.setValue("CepSacado", validarRegistroStringRemessa(StringUtil.removerMascara(cliente.getEndereco().getCep()), 8));
                transacaoTitulo.setValue("BairroSacado", validarRegistroStringRemessa(cliente.getEndereco().getBairro(), 12));
                if (cidade != null) {
                    transacaoTitulo.setValue("CidadeSacado", validarRegistroStringRemessa(cidade.getDescricao(), 15));
                    transacaoTitulo.setValue("EstadoSacado", validarRegistroStringRemessa(cidade.getIdUF().getDescricao(), 2));
                }

                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
                transacaoTitulo.setValue("DataCobrancaMulta", sdf.format(DataUtil.adicionarDias(boleto.getDataVencimento(), 1)));
                transacaoTitulo.setValue("ValorMulta", boleto.getJuros() != null ? "4" : "0");
                transacaoTitulo.setValue("NumeroSequencialRegistro", numeroSequencialRegistro);

                break;
            case BANCO_BRADESCO:
                transacaoTitulo.setValue("CodigoDoRegistro", "1");
                transacaoTitulo.setValue("AgenciaSacado", validarRegistroStringRemessa(contaSistema.getAgencia(), 5));
                transacaoTitulo.setValue("DigitoAgencia", validarRegistroStringRemessa(contaSistema.getDigitoAgencia(), 1));
                transacaoTitulo.setValue("ContaSacado", validarRegistroStringRemessa(contaSistema.getConta(), 7));
                transacaoTitulo.setValue("DigitoContaSacado", validarRegistroStringRemessa(contaSistema.getDigitoConta(), 1));
                transacaoTitulo.setValue("Carteira", validarRegistroInteiroRemessa(contaBancaria.getCarteira().getCodigo(), 3));
                transacaoTitulo.setValue("Agencia", validarRegistroInteiroRemessa(contaBancaria.getAgencia().getCodigo(), 5));
                transacaoTitulo.setValue("Conta", validarRegistroInteiroRemessa(contaBancaria.getNumeroDaConta().getCodigoDaConta(), 5));
                transacaoTitulo.setValue("DigitoConta", validarRegistroStringRemessa(contaBancaria.getNumeroDaConta().getDigitoDaConta(), 1));
                transacaoTitulo.setValue("UsoEmpresa", validarRegistroInteiroRemessa(boleto.getId(), 25));
                transacaoTitulo.setValue("Multa", boleto.getJuros() != null ? "2" : "0");
                transacaoTitulo.setValue("PercentualMulta", validarRegistroDoubleRemessaReturnZero(boleto.getPercentualJuros(), 4));
                transacaoTitulo.setValue("NossoNumero", validarRegistroStringRemessa(boleto.getNossoNumero(), 11));
                transacaoTitulo.setValue("DigitoAutoConferencia", "N");
                transacaoTitulo.setValue("DescontoBonificacao", validarRegistroDoubleRemessa(boleto.getValorDesconto(), 10));
                transacaoTitulo.setValue("CondicaoEmissao", "2");
                transacaoTitulo.setValue("IdentificacaoDebitoAutomatico", "N");
                transacaoTitulo.setValue("SeuNumero", validarRegistroInteiroRemessa(boleto.getId(), 10));
                transacaoTitulo.setValue("Vencimento", validarRegistroDateRemessa(boleto.getDataVencimento()));
                transacaoTitulo.setValue("ValorTitulo", validarRegistroDoubleRemessa(boleto.getValor(), 13));
                transacaoTitulo.setValue("EspecieTitulo", "01");
                transacaoTitulo.setValue("Aceite", "N");
                transacaoTitulo.setValue("DataEmissao", validarRegistroDateRemessa(boleto.getDataEmissao()));
                transacaoTitulo.setValue("ValorJuros", validarRegistroDoubleRemessa(boleto.getJuros(), 13));
                transacaoTitulo.setValue("DataDesconto", validarRegistroDateRemessa(boleto.getDataDesconto()));
                transacaoTitulo.setValue("ValorDesconto", validarRegistroDoubleRemessa(boleto.getValorDesconto(), 13));
                transacaoTitulo.setValue("ValorIOF", validarRegistroDoubleRemessa(boleto.getValorIof(), 13));
                transacaoTitulo.setValue("ValorAbatimento", validarRegistroDoubleRemessa(boleto.getValorDesconto(), 13));
                transacaoTitulo.setValue("TipoSacado", ("PF".equals(cliente.getTipo())) ? 1 : 2); // 1- Pessoa fisica 2- Pessoa juridica
                transacaoTitulo.setValue("DocumentoSacado", validarRegistroStringRemessa(StringUtil.removerMascara(cliente.getCpfCNPJ()), 14));
                transacaoTitulo.setValue("NomeSacado", validarRegistroStringRemessa(cliente.getNome(), 40));
                transacaoTitulo.setValue("EnderecoSacado", validarRegistroStringRemessa(cliente.getEndereco() + ", " + cliente.getEndereco().getNumero(), 40));
                transacaoTitulo.setValue("PrimeiraMensagem", validarRegistroStringRemessa(boleto.getIdContaParcela().getObservacao(), 30));
                transacaoTitulo.setValue("CepSacado", validarRegistroStringRemessa(StringUtil.removerMascara(cliente.getEndereco().getCep()), 8));
                transacaoTitulo.setValue("NumeroSequencialRegistro", numeroSequencialRegistro);

                break;
            case BANCO_DO_BRASIL:
                break;
            case HSBC:
                break;
            case BANCOOB:
                break;
            default:
                throw new BoletoException(" Banco não suportado para gerar remessa. ", null);
        }

        return transacaoTitulo;
    }

    public static Record createTrillerRecord(FlatFile<Record> ff, BancosSuportados banco, List<Boleto> boletos, Integer numeroRegistro) throws BoletoException {

        Record trailler = ff.createRecord("Trailler");
        switch (banco) {
            case BANCO_ITAU:
                trailler.setValue("NumeroSequencialRegistro", numeroRegistro);
                break;
            case BANCO_SANTANDER:
                trailler.setValue("QuantidadeTitulos", validarRegistroInteiroRemessa(boletos.size(), 6));
                trailler.setValue("ValorTotalTitulos", validarRegistroDoubleRemessa(boletos.stream()
                        .mapToDouble(Boleto::getValor)
                        .sum(), 13));
                trailler.setValue("NumeroSequencialRegistro", numeroRegistro);
                break;
            case CAIXA_ECONOMICA_FEDERAL:
                trailler.setValue("NumeroSequencialRegistro", numeroRegistro);
                break;
            case BANCO_BRADESCO:
                trailler.setValue("NumeroSequencialRegistro", numeroRegistro);
                break;
            case BANCO_DO_BRASIL:
                break;
            case HSBC:
                break;
            case BANCOOB:
                break;
            default:
                throw new BoletoException(" Banco não suportado para gerar remessa. ", null);
        }

        return trailler;
    }

    private static String validarRegistroDateRemessa(Date registro) {
        if (registro == null) {
            return "";
        }
        SimpleDateFormat spf = new SimpleDateFormat("ddMMyy");
        return spf.format(registro);
    }

    private static String validarRegistroDoubleRemessa(Double registro, int tamanho) {

        if (registro == null) {
            return "";
        }
        String retorno = String.format("%.2f", registro).replace(".", "").replace(",", "");

        if (retorno.length() > tamanho) {
            return retorno.substring(0, tamanho - 1);
        } else {
            return retorno;
        }
    }

    private static String validarRegistroDoubleRemessaReturnZero(Double registro, int tamanho) {
        StringBuilder retorno = new StringBuilder();
        if (registro == null) {
            for (int i = 0; i < tamanho; i++) {
                retorno.append("0");
            }
            return retorno.toString();
        }
        retorno.append(String.format("%.2f", registro).replace(".", "").replace(",", ""));

        if (retorno.length() > tamanho) {
            return retorno.substring(0, tamanho - 1);
        }
        return retorno.toString();
    }

    private static String validarRegistroInteiroRemessa(Integer registro, int tamanho) {
        if (registro == null) {
            return "";
        }
        String retorno = registro.toString();

        if (retorno.length() > tamanho) {
            return retorno.substring(0, tamanho - 1);
        }
        return retorno;
    }

    private static String validarRegistroStringRemessa(String registro, int tamanho) {
        if (StringUtils.isEmpty(registro)) {
            return "";
        }
        registro = StringUtil.removerAcentos(registro);

        if (registro.length() > tamanho) {
            return registro.substring(0, tamanho - 1);
        }
        return registro;
    }

}
