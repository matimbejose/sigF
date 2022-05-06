package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.dto.*;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProduto;
import br.com.villefortconsulting.sgfinancas.util.TreeNodeItem;
import br.com.villefortconsulting.sgfinancas.util.TreeNodeList;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumCodigoReceitaDespesa;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import com.google.gson.GsonBuilder;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.ejb.*;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AnalisesService extends BasicService<EntityId, BasicRepository<EntityId>, BasicFilter<EntityId>> {

    private static final long serialVersionUID = 1L;

    @EJB
    private ContaService contaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private VendaService vendaService;

    private List<DreDTO> listaDre;

    public String obterFluxoCaixaJSon(Date dataInicial, Date dataFinal) {
        return new GsonBuilder().create().toJson(obterFluxoCaixa(dataInicial, dataFinal, true, null));
    }

    public String obterAnaliseNecessidadeCaixaJson(Date dataInicial, Date dataFinal) {
        return new GsonBuilder().create().toJson(obterAnaliseNecessidadeCaixa(dataInicial, dataFinal, true));
    }

    public String obterVendaJson(Date dataInicial, Date dataFinal) {
        return new GsonBuilder().create().toJson(obterVenda(dataInicial, dataFinal, true));
    }

    public List<ContaPagarReceberDTO> obterAnaliseNecessidadeCaixa(Date dataInicial, Date dataFinal, boolean preecherDadosIniciais) {
        List<ContaPagarReceberDTO> listaContaPagarReceber = preecherDadosVazioNecessidadeCaixa(preecherDadosIniciais);
        int mesSaldoInicial = DataUtil.getMes(dataInicial);

        for (ValorLancamentoDTO valorLancamentoDTO : contaService.obterValorReceitaMensal(dataInicial, dataFinal)) {
            listaContaPagarReceber.stream()
                    .filter(p -> p.getMes().equals(valorLancamentoDTO.getMes() + ""))
                    .forEach(g -> g.setValorReceber(valorLancamentoDTO.getValor()));
        }

        for (ValorLancamentoDTO valorLancamentoDTO : contaService.obterValorDespesaMensal(dataInicial, dataFinal)) {
            listaContaPagarReceber.stream()
                    .filter(p -> p.getMes().equals(valorLancamentoDTO.getMes() + ""))
                    .forEach(g -> g.setValorPagar(valorLancamentoDTO.getValor()));
        }

        double valorCaixa;
        double valorReceitaAcumulado = 0d;
        double valorDespesaAcumulado = 0d;

        for (ContaPagarReceberDTO contaPagarReceberDTO : listaContaPagarReceber) {
            if (contaPagarReceberDTO.getMes().equals("tot") || Integer.parseInt(contaPagarReceberDTO.getMes()) >= mesSaldoInicial) {
                valorDespesaAcumulado += NumeroUtil.somar(contaPagarReceberDTO.getValorPagar());
                valorReceitaAcumulado += NumeroUtil.somar(contaPagarReceberDTO.getValorReceber());

                valorCaixa = NumeroUtil.somar(contaPagarReceberDTO.getValorPagar()) - NumeroUtil.somar(contaPagarReceberDTO.getValorReceber());

                if (valorCaixa > 0) {
                    contaPagarReceberDTO.setNecessidadeCaixa(valorCaixa);
                }
            }
        }

        // acumulado
        final double valorReceitaAcumuladoFinal = valorReceitaAcumulado;
        final double valorDespesaAcumuladoFinal = valorDespesaAcumulado;
        Double valorCaixaAcumulado = null;

        if (valorDespesaAcumuladoFinal > valorReceitaAcumuladoFinal) {
            valorCaixaAcumulado = valorDespesaAcumuladoFinal - valorReceitaAcumuladoFinal;
        }
        final Double valorCaixaAcumuladoFinal = valorCaixaAcumulado;

        listaContaPagarReceber.stream()
                .filter(p -> p.getMes().equals("tot"))
                .forEach(g -> {
                    g.setValorPagar(valorDespesaAcumuladoFinal);
                    g.setValorReceber(valorReceitaAcumuladoFinal);
                    g.setNecessidadeCaixa(valorCaixaAcumuladoFinal);
                });

        return listaContaPagarReceber;
    }

    public List<FluxoCaixaDTO> obterFluxoCaixa(Date dataInicial, Date dataFinal, boolean preecherDadosIniciais, CentroCusto centro) {
        Date dataAnterior = DataUtil.converterStringParaDate((DataUtil.getAno(dataInicial) - 1) + "-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");

        List<String> planosTransferenciaCredito = new ArrayList<>();
        List<String> planosTransferenciaDebito = new ArrayList<>();
        contaBancariaService.listarContasByCentroCusto(centro).stream()
                .map(ContaBancaria::getIdPlanoConta)
                .forEach(planoConta -> {
                    planosTransferenciaDebito.add(planoContaService.buscarPlanoContaTransferencia("D", planoConta).getCodigo());
                    planosTransferenciaCredito.add(planoContaService.buscarPlanoContaTransferencia("C", planoConta).getCodigo());
                });

        Double saldo
                = /*contaBancariaService.listar().stream()
                .filter(cb -> centro == null || centro.equals(cb.getIdCentroCusto()))
                .filter(cb -> cb.getDataSaldo().before(dataInicial))
                .map(ContaBancaria::getSaldoInicial)
                .filter(Objects::nonNull)
                .reduce(0d, Double::sum)
                + */ contaService.obterValorReceitaFluxoCaixa(dataAnterior, centro, planosTransferenciaCredito)
                - contaService.obterValorDespesaFluxoCaixa(dataAnterior, centro, planosTransferenciaDebito);

        List<FluxoCaixaDTO> listaFluxoCaixa = preecherDadosVazio(preecherDadosIniciais);
        contaService.obterValorReceitaFluxoCaixa(dataInicial, dataFinal, centro, planosTransferenciaCredito).forEach(valorLancamentoDTO -> listaFluxoCaixa.stream()
                .filter(p -> p.getMes().equals(valorLancamentoDTO.getMes() + ""))
                .forEach(g -> g.setReceita(valorLancamentoDTO.getValor())));
        contaService.obterValorDespesaFluxoCaixa(dataInicial, dataFinal, centro, planosTransferenciaDebito).forEach(valorLancamentoDTO -> listaFluxoCaixa.stream()
                .filter(p -> p.getMes().equals(valorLancamentoDTO.getMes() + ""))
                .forEach(g -> g.setDespesa(valorLancamentoDTO.getValor())));

        // atualiza o saldo inicial do primeiro registro
        double valorAcumuladoAnterior = saldo;
        double receitaAcumulada = 0d;
        double despesaAcumulada = 0d;
        double lucroPrejizoAcumulado = 0d;

        // valores calculados
        for (FluxoCaixaDTO fluxoCaixaDTO : listaFluxoCaixa) {
            fluxoCaixaDTO.setSaldoInicial(valorAcumuladoAnterior);

            // lucro / prejuizo
            if (fluxoCaixaDTO.getReceita() != null && fluxoCaixaDTO.getDespesa() != null) {
                fluxoCaixaDTO.setLucroPrejuizo(fluxoCaixaDTO.getReceita() - fluxoCaixaDTO.getDespesa());
            } else if (fluxoCaixaDTO.getReceita() != null) {
                fluxoCaixaDTO.setLucroPrejuizo(fluxoCaixaDTO.getReceita());
            } else if (fluxoCaixaDTO.getDespesa() != null) {
                fluxoCaixaDTO.setLucroPrejuizo(-fluxoCaixaDTO.getDespesa());
            }

            // lucratividade
            if (fluxoCaixaDTO.getLucroPrejuizo() != null && fluxoCaixaDTO.getReceita() != null && fluxoCaixaDTO.getReceita() > 0) {
                fluxoCaixaDTO.setLucratividade(NumeroUtil.formatarCasasDecimais(fluxoCaixaDTO.getLucroPrejuizo() / fluxoCaixaDTO.getReceita() * 100, 2));
            }

            // acumulado
            fluxoCaixaDTO.setAcumulado(NumeroUtil.somar(fluxoCaixaDTO.getSaldoInicial(), fluxoCaixaDTO.getLucroPrejuizo()));

            receitaAcumulada += NumeroUtil.somar(fluxoCaixaDTO.getReceita());
            despesaAcumulada += NumeroUtil.somar(fluxoCaixaDTO.getDespesa());
            lucroPrejizoAcumulado += NumeroUtil.somar(fluxoCaixaDTO.getLucroPrejuizo());

            valorAcumuladoAnterior = fluxoCaixaDTO.getAcumulado();
        }

        // acumulado
        final double receitaAcumuladaFinal = receitaAcumulada;
        final double despesaAcumuladaFinal = despesaAcumulada;
        final double lucroPrejizoAcumuladoFinal = lucroPrejizoAcumulado;
        final double lucratividadeFinal = NumeroUtil.formatarCasasDecimais(receitaAcumulada > 0 ? lucroPrejizoAcumulado / receitaAcumulada * 100 : 0d, 2);
        listaFluxoCaixa.stream()
                .filter(p -> p.getMes().equals("tot"))
                .forEach(g -> {
                    g.setReceita(receitaAcumuladaFinal);
                    g.setDespesa(despesaAcumuladaFinal);
                    g.setLucroPrejuizo(lucroPrejizoAcumuladoFinal);
                    g.setLucratividade(lucratividadeFinal);
                    g.setAcumulado(g.getSaldoInicial());
                    g.setSaldoInicial(null);
                });

        return listaFluxoCaixa;
    }

    private static List<DreDTO> flattenList(List<DreDTO> list) {
        return flattenList(list, new ArrayList<>());
    }

    private static List<DreDTO> flattenList(List<DreDTO> list, List<DreDTO> flat) {
        list.stream()
                .map(dreDTO -> {
                    flat.add(dreDTO);
                    return dreDTO;
                })
                .forEachOrdered(dreDTO -> flattenList(dreDTO.getChildren(), flat));
        return flat;
    }

    public List<DreDTO> obterDre(Integer ano, CentroCusto centro) {
        Date dataInicial = DataUtil.converterStringParaDate("01/01/" + ano);
        Date dataFinal = DataUtil.converterStringParaDate("31/12/" + ano);
        List<ValorLancamentoDTO> listaReceita = contaService.obterValorReceitaDreMensal(dataInicial, dataFinal, centro, false);
        List<ValorLancamentoDTO> listaDespesa = contaService.obterValorDespesaDreMensal(dataInicial, dataFinal, centro, false);

        listaDespesa.forEach(despesa -> despesa.setValor(-despesa.getValor()));
        List<ValorLancamentoDTO> listaLancamento = new ArrayList<>();
        listaLancamento.addAll(listaReceita);
        listaLancamento.addAll(listaDespesa);
        listaDre = preecherDadosVazioDre();

        List<DreDTO> listaDreCalculo = new ArrayList<>();

        flattenList(listaDre, listaDreCalculo);

        listaDreCalculo.stream()
                .forEach(dto -> {
                    Double valorSomaTotal = 0d;

                    // atualiza os valores em cada mes
                    for (int i = 1; i <= 12; i++) {
                        final int posicao = i;

                        Double valorSoma = listaLancamento.stream()
                                .filter(p -> p.getMes().equals(posicao) && p.getCodigo().startsWith(dto.getCodigo()))
                                .findFirst().map(ValorLancamentoDTO::getValor).orElse(0d);

                        // atualiza a lista
                        dto.getListaLancamento().stream()
                                .filter(p -> p.getMes().equals(posicao))
                                .forEach(g -> g.setValor(valorSoma));

                        valorSomaTotal += valorSoma;
                    }

                    // total da conta
                    final Double somaTotalFinal = valorSomaTotal;
                    dto.getListaLancamento().stream()
                            .filter(p -> p.getMes().equals(13))
                            .forEach(g -> g.setValor(somaTotalFinal));
                });

        // calcula valores consolidados, mes a mes
        // Receita Líquida
        listaDreCalculo.stream()
                .forEach(dreDTO -> {
                    switch (dreDTO.getConta()) {
                        case "Receita Líquida":
                            // lucro liquido
                            for (int i = 1; i <= 13; i++) {
                                dreDTO.getListaLancamento().get(i).setValor(
                                        getValorLancamentoPorConta("4.1.1", i)
                                                - getValorLancamentoPorConta("4.1", i));
                            }
                            break;
                        case "Resultado Bruto":
                            for (int i = 1; i <= 13; i++) {
                                dreDTO.getListaLancamento().get(i).setValor(
                                        getValorLancamentoPorConta("4.1.1", i)
                                                - getValorLancamentoPorConta("4.1", i)
                                                - getValorLancamentoPorConta("5.1.1", i)
                                                - getValorLancamentoPorConta("5.1.2", i)
                                                - getValorLancamentoPorConta("5.1.3", i)
                                );
                            }
                            break;
                        case "Resultado Operacional":
                            for (int i = 1; i <= 13; i++) {
                                dreDTO.getListaLancamento().get(i).setValor(
                                        getValorLancamentoPorConta("4.1.1", i)
                                                - getValorLancamentoPorConta("4.1", i)
                                                - getValorLancamentoPorConta("3.1.1", i)
                                                - getValorLancamentoPorConta("3.1.2", i)
                                                - getValorLancamentoPorConta("3.1.3", i)
                                                - getValorLancamentoPorConta("3.1.4", i)
                                                - getValorLancamentoPorConta("3.2.1", i)
                                                - getValorLancamentoPorConta("3.2.2", i)
                                );
                            }
                            break;
                        case "Resultado Líquido":
                            // Resultado Não Operacional
                            for (int i = 1; i <= 13; i++) {
                                dreDTO.getListaLancamento().get(i).setValor(
                                        getValorLancamentoPorConta("3.2.3", i)
                                                - getValorLancamentoPorConta("3.3", i)
                                                + getValorLancamentoPorConta("4.2", i)
                                );
                            }
                            break;
                        case "Lucro / Prejuízo":
                            for (int i = 1; i <= 13; i++) {
                                dreDTO.getListaLancamento().get(i).setValor(
                                        getValorLancamentoPorConta("4.2.5.1", i)
                                                + getValorLancamentoPorConta("3.1.5", i) - getValorLancamentoPorConta("4.2.5", i)
                                );
                            }
                            break;
                        default:
                            break;
                    }
                });

        Collections.sort(listaDre, (p1, p2) -> p1.getCodigoOrdenacao().compareTo(p2.getCodigoOrdenacao()));
        return listaDre;
    }

    /**
     * NÃO MEXER NESSA FUNÇÃO A NÃO SER QUE VOCÊ TENHA A CERTEZA DO QUE ESTÁ FAZENDO
     *
     * @param ano
     * @param centro
     * @return
     */
    public List<DreDTO> obterDreFull(Integer ano, CentroCusto centro) {
        Date dataInicial = DataUtil.converterStringParaDate("01/01/" + ano);
        Date dataFinal = DataUtil.converterStringParaDate("31/12/" + ano);

        List<ContaBancaria> contasBancarias = contaBancariaService.listar().stream()
                .filter(cb -> centro == null || centro.equals(cb.getIdCentroCusto()))
                .filter(cb -> DataUtil.getAno(cb.getDataSaldo()).equals(ano))
                .collect(Collectors.toList());

        List<ValorLancamentoDTO> listaReceita = contaService.obterValorReceitaDreMensal(dataInicial, dataFinal, centro, true);
        List<ValorLancamentoDTO> listaDespesa = contaService.obterValorDespesaDreMensal(dataInicial, dataFinal, centro, true);

        List<ValorLancamentoDTO> listaTransferencias = new ArrayList<>();

        listaDespesa.forEach(despesa -> despesa.setValor(-despesa.getValor()));
        List<ValorLancamentoDTO> listaLancamento = new ArrayList<>();
        listaLancamento.addAll(listaReceita);
        listaLancamento.addAll(listaDespesa);

        for (int i = listaLancamento.size() - 1; i >= 0; i--) {
            PlanoConta planoConta = planoContaService.buscarPlanoDeContasByCodigo(listaLancamento.get(i).getCodigo());
            if (!planoConta.getTipo().equals(planoConta.getIdContaPai().getTipo())) {
                listaTransferencias.add(listaLancamento.get(i));
                listaLancamento.remove(i);
            }
        }

        Double[] totais43 = new Double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d};
        Double[] totais44 = new Double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d};
        List<DreDTO> lista = preecherDadosVazioDreFull();
        if (!contasBancarias.isEmpty()) {
            DreDTO auxDreDto = new DreDTO("Saldo inicial");
            auxDreDto.setCodigo("4.4");
            auxDreDto.setCor("color-32C5D2");
            auxDreDto.setCodigoOrdenacao("23.2.34.3");
            auxDreDto.setTipo("C");
            for (int j = 0; j < 14; j++) {
                auxDreDto.getListaLancamento().add(new ValorLancamentoDTO(0d, j));
            }
            lista.stream()
                    .filter(dto -> "C".equals(dto.getTipo()) && "4".equals(dto.getCodigo()))
                    .findAny()
                    .ifPresent(dto -> dto.getChildren().add(auxDreDto));
            contasBancarias.stream().map(cb -> {
                int i = DataUtil.getMes(cb.getDataSaldo());
                DreDTO aux = new DreDTO();
                aux.setCodigo("4.4.1");
                aux.setCor("color-32C5D2");
                aux.setTipo("C");
                for (int j = 0; j < 14; j++) {
                    aux.getListaLancamento().add(new ValorLancamentoDTO(i == j || j == 13 ? cb.getSaldoInicial() : 0d, j));
                }
                aux.setCodigoOrdenacao("1" + aux.getCodigo());
                aux.setConta(cb.getDescricao());
                auxDreDto.getChildren().add(aux);
                totais44[i - 1] += cb.getSaldoInicial();
                return cb;
            }).forEachOrdered(cb -> totais44[12] += cb.getSaldoInicial());
        }
        if (!listaTransferencias.isEmpty()) {
            DreDTO auxDreDto = new DreDTO("Transferências");
            auxDreDto.setCodigo("4.3");
            auxDreDto.setCor("color-32C5D2");
            auxDreDto.setCodigoOrdenacao("23.2.34.3");
            auxDreDto.setTipo("C");
            for (int j = 0; j < 14; j++) {
                auxDreDto.getListaLancamento().add(new ValorLancamentoDTO(0d, j));
            }
            lista.stream()
                    .filter(dto -> "C".equals(dto.getTipo()) && "4".equals(dto.getCodigo()))
                    .findAny()
                    .ifPresent(dto -> dto.getChildren().add(auxDreDto));
            for (int i = 0; i < listaTransferencias.size(); i++) {
                PlanoConta plano = planoContaService.buscarPlanoDeContasByCodigo(listaTransferencias.get(i).getCodigo());
                final int _i = i;
                final String nome = plano.getIdContaPai().getDescricao() + " " + plano.getDescricao();
                DreDTO aux = auxDreDto.getChildren().stream()
                        .filter(dre -> dre.getConta().equals(nome))
                        .findAny()
                        .orElseGet(() -> {
                            DreDTO aux2 = new DreDTO();
                            aux2.setCodigo("4.3." + _i);
                            aux2.setCor("color-32C5D2");
                            aux2.setTipo("C");
                            for (int j = 0; j < 14; j++) {
                                aux2.getListaLancamento().add(new ValorLancamentoDTO(0d, j));
                            }
                            aux2.setCodigoOrdenacao("1" + aux2.getCodigo());
                            aux2.setConta(nome);

                            auxDreDto.getChildren().add(aux2);
                            return aux2;
                        });

                Double sum = 0d;
                for (int j = 1; j < 13; j++) {
                    ValorLancamentoDTO valor = aux.getListaLancamento().get(j);
                    if (j == listaTransferencias.get(i).getMes()) {
                        valor.setValor(valor.getValor() + listaTransferencias.get(i).getValor());
                        totais43[j - 1] += listaTransferencias.get(i).getValor();
                        totais43[12] += listaTransferencias.get(i).getValor();
                        sum += listaTransferencias.get(i).getValor();
                    }
                }
                ValorLancamentoDTO aux3 = aux.getListaLancamento().stream()
                        .filter(ll -> ll.getMes() == 13)
                        .findAny()
                        .orElseGet(ValorLancamentoDTO::new);
                aux3.setValor(sum + aux3.getValor());
            }
        }
        lista.stream()
                .filter(a1 -> "4".equals(a1.getCodigo()))
                .forEach(a1 -> {
                    for (int k = 0; k < 13; k++) {
                        a1.getListaLancamento().get(k + 1).setValor(totais43[k] + totais44[k]);
                    }
                    a1.getChildren().stream()
                            .filter(a2 -> "4.3".equals(a2.getCodigo()))
                            .forEach(a2 -> {
                                for (int l = 0; l < 13; l++) {
                                    if (a2.getListaLancamento().size() > l) {
                                        a2.getListaLancamento().get(l + 1).setValor(totais43[l]);
                                    }
                                }
                            });
                    a1.getChildren().stream()
                            .filter(a2 -> "4.4".equals(a2.getCodigo()))
                            .forEach(a2 -> {
                                for (int l = 0; l < 13; l++) {
                                    if (a2.getListaLancamento().size() > l) {
                                        a2.getListaLancamento().get(l + 1).setValor(totais44[l]);
                                    }
                                }
                            });
                });

        flattenList(lista).forEach(dto -> listaLancamento.stream()
                .filter(lancamento -> lancamento.getCodigo().startsWith(dto.getCodigo()))
                .forEach(lancamento -> {
                    dto.setValor(lancamento.getMes(), dto.getValorAsDouble(lancamento.getMes()) + lancamento.getValor());
                    dto.setValor(13, dto.getValorAsDouble(13) + lancamento.getValor());
                }));

        return lista;
    }

    private Double getValorLancamentoPorConta(String codigoConta, int mes) {
        ValorLancamentoDTO valorLancamentoDTO = null;
        DreDTO dreDTO = listaDre
                .stream()
                .filter(p -> p.getCodigo().equals(codigoConta))
                .findFirst().orElse(null);

        if (dreDTO != null) {
            valorLancamentoDTO = dreDTO.getListaLancamento()
                    .stream()
                    .filter(p -> p.getMes().equals(mes))
                    .findFirst().orElse(null);

        }

        if (valorLancamentoDTO != null) {
            return valorLancamentoDTO.getValor();
        }

        return 0d;
    }

    private List<DreDTO> preecherDadosVazioDreFull() {
        List<PlanoContaDreDTO> listaFull = planoContaService.listarPlanoContaDreDto(null).stream()
                .collect(Collectors.toList());

        return planoContaService.listarPlanoContaPai().stream()
                .map(PlanoContaDreDTO::new)
                .map(pc -> criaDreDTO(listaFull, pc))
                .collect(Collectors.toList());
    }

    private List<DreDTO> preecherDadosVazioDre() {
        List<DreDTO> listaDreDto = new ArrayList<>();

        listaDreDto.addAll(planoContaService.obterPlanoContaDre().stream()
                .map(PlanoContaDreDTO::new)
                .map(AnalisesService::criaDreDTO)
                .collect(Collectors.toList()));

        // contas calculadas
        listaDreDto.add(criaDreDTO("Receita Líquida", "CA", "4.1.2.1"));
        listaDreDto.add(criaDreDTO("Resultado Bruto", "CA", "4.3.0"));
        listaDreDto.add(criaDreDTO("Resultado Operacional", "CA", "3.2.3"));
        listaDreDto.add(criaDreDTO("Resultado Líquido", "CA", "3.2.5.2"));

        return listaDreDto;
    }

    private static DreDTO criaDreDTO(List<PlanoContaDreDTO> listaFull, PlanoContaDreDTO dto) {
        DreDTO dre = criaDreDTO(dto.getDescricao(), dto.getTipo(), dto.getCodigo());
        dre.setChildren(listaFull.stream()
                .filter(pc -> dto.getCodigo().equals(pc.getCodigoPai()))
                .map(pc -> criaDreDTO(listaFull, pc))
                .collect(Collectors.toList()));
        return dre;
    }

    private static DreDTO criaDreDTO(PlanoContaDreDTO dto) {
        return criaDreDTO(dto.getDescricao(), dto.getTipo(), dto.getCodigo());
    }

    private static DreDTO criaDreDTO(String descricao, String tipo, String codigo) {
        DreDTO dreDTO = new DreDTO();
        ValorLancamentoDTO valorLancamentoDTO;

        dreDTO.setConta(descricao);
        dreDTO.setTipo(tipo);
        dreDTO.setCodigo(codigo);

        switch (tipo) {
            case "C":
                dreDTO.setCor("color-32C5D2");
                break;
            case "D":
                dreDTO.setCor("color-E7505A");
                break;
            default:
                dreDTO.setCor("color-3598DC");
                break;
        }

        for (int i = 0; i <= 12; i++) {
            valorLancamentoDTO = new ValorLancamentoDTO();
            valorLancamentoDTO.setMes(i);
            dreDTO.getListaLancamento().add(valorLancamentoDTO);
        }

        // total
        valorLancamentoDTO = new ValorLancamentoDTO();
        valorLancamentoDTO.setMes(13);
        dreDTO.getListaLancamento().add(valorLancamentoDTO);

        // codigo de ordenacao tela
        if (dreDTO.getCodigo().equals("4.1.3")) {
            dreDTO.setCodigoOrdenacao("23.1.0");
        } else if (dreDTO.getCodigo().equals("4.1.4")) {
            dreDTO.setCodigoOrdenacao("23.1.0");
        } else if (dreDTO.getCodigo().equals("4.1.5")) {
            dreDTO.setCodigoOrdenacao("23.1.0");
        } else if (dreDTO.getCodigo().startsWith(EnumCodigoReceitaDespesa.CODIGO_RECEITA.getTipo()) && dreDTO.getCodigo().length() > 3) {
            dreDTO.setCodigoOrdenacao("1" + dreDTO.getCodigo());
        } else if (dreDTO.getCodigo().startsWith(EnumCodigoReceitaDespesa.CODIGO_DESPESA.getTipo()) && dreDTO.getCodigo().length() > 3) {
            dreDTO.setCodigoOrdenacao("2" + dreDTO.getCodigo());
        } else if (dreDTO.getCodigo().length() > 3) {
            // contas de custo de mercadoria vendida
            dreDTO.setCodigoOrdenacao("14.2" + dreDTO.getCodigo());
        } else {
            // contas não operacionais
            dreDTO.setCodigoOrdenacao("23.2.3" + dreDTO.getCodigo());
        }

        return dreDTO;
    }

    private static List<ContaPagarReceberDTO> preecherDadosVazioNecessidadeCaixa(boolean preecherDadosIniciais) {
        List<ContaPagarReceberDTO> listaContaPagarReceber = new ArrayList<>();
        ContaPagarReceberDTO contaPagarReceberDTO;
        for (int i = 1; i <= 12; i++) {
            contaPagarReceberDTO = new ContaPagarReceberDTO();
            contaPagarReceberDTO.setMes(i + "");
            contaPagarReceberDTO.setNomeMes(DataUtil.obterNomeMesResumido(i + ""));
            if (preecherDadosIniciais) {
                contaPagarReceberDTO.setValorPagar(0d);
                contaPagarReceberDTO.setValorReceber(0d);
                contaPagarReceberDTO.setNecessidadeCaixa(0d);
            }
            listaContaPagarReceber.add(contaPagarReceberDTO);
        }

        if (!preecherDadosIniciais) {
            contaPagarReceberDTO = new ContaPagarReceberDTO();
            contaPagarReceberDTO.setMes("tot");
            listaContaPagarReceber.add(contaPagarReceberDTO);
        }

        return listaContaPagarReceber;
    }

    private static List<FluxoCaixaDTO> preecherDadosVazio(boolean preecherDadosIniciais) {
        List<FluxoCaixaDTO> listaFluxoCaixa = new ArrayList<>();
        FluxoCaixaDTO fluxoCaixaDTO;

        for (int i = 1; i <= 12; i++) {
            fluxoCaixaDTO = new FluxoCaixaDTO();
            fluxoCaixaDTO.setMes(i + "");
            fluxoCaixaDTO.setNomeMes(DataUtil.obterNomeMesResumido(i + ""));
            if (preecherDadosIniciais) {
                fluxoCaixaDTO.setDespesa(0d);
                fluxoCaixaDTO.setReceita(0d);
                fluxoCaixaDTO.setLucroPrejuizo(0d);
                fluxoCaixaDTO.setSaldoInicial(0d);
                fluxoCaixaDTO.setLucratividade(0d);
            }
            listaFluxoCaixa.add(fluxoCaixaDTO);
        }

        if (!preecherDadosIniciais) {
            fluxoCaixaDTO = new FluxoCaixaDTO();
            fluxoCaixaDTO.setMes("tot");
            listaFluxoCaixa.add(fluxoCaixaDTO);
        }

        return listaFluxoCaixa;
    }

    private static List<VendaDTO> preecherDadosVazioVenda(boolean preecherDadosIniciais) {
        List<VendaDTO> listaVenda = new ArrayList<>();
        VendaDTO vendaDTO;

        for (int i = 1; i <= 12; i++) {
            vendaDTO = new VendaDTO();
            vendaDTO.setMes(i + "");
            vendaDTO.setNomeMes(DataUtil.obterNomeMesResumido(i + ""));
            if (preecherDadosIniciais) {
                vendaDTO.setValorProduto(0d);
                vendaDTO.setValorServico(0d);
            }
            listaVenda.add(vendaDTO);
        }

        if (!preecherDadosIniciais) {
            vendaDTO = new VendaDTO();
            vendaDTO.setMes("tot");
            listaVenda.add(vendaDTO);
        }

        return listaVenda;
    }

    public List<VendaDTO> obterVenda(Date dataInicial, Date dataFinal, boolean preecherDadosIniciais) {
        List<VendaDTO> listaVenda = preecherDadosVazioVenda(preecherDadosIniciais);

        vendaService.obterValorPorProduto(dataInicial, dataFinal, EnumTipoProduto.PRODUTO.getTipo()).forEach(valorLancamentoDTO -> listaVenda.stream()
                .filter(p -> p.getMes().equals(valorLancamentoDTO.getMes() + ""))
                .forEach(g -> g.setValorProduto(valorLancamentoDTO.getValor())));

        vendaService.obterValorPorServico(dataInicial, dataFinal).forEach(valorLancamentoDTO -> listaVenda.stream()
                .filter(p -> p.getMes().equals(valorLancamentoDTO.getMes() + ""))
                .forEach(g -> g.setValorServico(valorLancamentoDTO.getValor())));

        return listaVenda;
    }

    private static String[][] getConfigFluxoDeCaixa() {
        return new String[][]{
                new String[]{"Saldo inicial (R$)", "si"},
                new String[]{"Receita (R$)", "r"},
                new String[]{"Despesa (R$)", "d"},
                new String[]{"Lucro/Prejuízo (R$)", "lp"},
                new String[]{"Acumulado (R$)", "a"},
                new String[]{"Resultado do fluxo de caixa", "l"}
        };
    }

    private static TreeNodeItem getItem(TreeNode tn, int pos) {
        return ((TreeNodeList) tn.getData()).getItems().get(pos);
    }

    private static String calcValue(String a, String b) {
        Double c = "-".equals(a) ? 0d : NumeroUtil.converterStringParaDouble(a);
        Double d = "-".equals(b) ? 0d : NumeroUtil.converterStringParaDouble(b);
        return NumeroUtil.converterValorParaMonetario(NumeroUtil.somar(c, d), 2);
    }

    private static TreeNodeItem obterTreeNodeItem(List<FluxoCaixaDTO> listaFluxoCaixa, final String tipo, final String mes) {
        if (listaFluxoCaixa == null || listaFluxoCaixa.isEmpty()) {
            return new TreeNodeItem("-", "empty");
        }
        return listaFluxoCaixa.stream()
                .filter(p -> p.getMes().equals(mes)).findAny()
                .map(dto -> dto.getValorByTipo(tipo))
                .filter(Objects::nonNull)
                .map(valor -> new TreeNodeItem(NumeroUtil.converterValorParaMonetario(valor, 2) + (tipo.equals("l") ? "%" : ""), "number"))
                .orElse(new TreeNodeItem("-", "empty"));
    }

    private static void recursiveAddAll(List<DreDTO> dre, TreeNode tn, String type) {
        Collections.sort(dre, (a, b) -> a.getCodigo().compareTo(b.getCodigo()));
        dre.stream()
                .filter(ValorDTO::hasValue)
                .forEach(dto -> {
                    TreeNodeList tnl = new TreeNodeList(dto.getConta(), dto.getCodigo(), type);
                    for (int i = 1; i < 14; i++) {
                        tnl.addItem(dto.getValor(i));
                    }

                    TreeNode ntn = new DefaultTreeNode(tnl);
                    if (!dto.getChildren().isEmpty()) {
                        recursiveAddAll(dto.getChildren(), ntn, type);
                    }
                    tn.getChildren().add(ntn);
                });
    }

    public TreeNode getFluxoCaixa(Integer ano, List<FluxoCaixaDTO> listaFluxoCaixa, CentroCusto centro) {
        List<String> meses = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "tot");
        List<DreDTO> listaDreFull = obterDreFull(ano, centro);
        List<DreDTO> listaReceber = listaDreFull.stream()
                .filter(item -> "C".equals(item.getTipo()))
                .collect(Collectors.toList());
        List<DreDTO> listaPagar = listaDreFull.stream()
                .filter(item -> "D".equals(item.getTipo()))
                .collect(Collectors.toList());

        TreeNode fluxoCaixa = new DefaultTreeNode();
        Arrays.asList(getConfigFluxoDeCaixa()).forEach(config -> {
            String type = config[1];
            TreeNodeList tnl = new TreeNodeList(config[0], "", type);
            meses.forEach(mes -> tnl.addItem(obterTreeNodeItem(listaFluxoCaixa, type, mes)));
            TreeNode ntn = new DefaultTreeNode(tnl);
            if ("r".equals(type) || "d".equals(type)) {
                List<DreDTO> aux = "r".equals(type) ? listaReceber : listaPagar;
                recursiveAddAll(aux, ntn, type);
            }
            fluxoCaixa.getChildren().add(ntn);
        });
        String novoAcumulado = getItem(fluxoCaixa.getChildren().get(0), 1).getValue();
        TreeNode lucro = fluxoCaixa.getChildren().get(3);
        TreeNode acumulado = fluxoCaixa.getChildren().get(4);
        for (int i = 1; i < 13; i++) {
            novoAcumulado = calcValue(novoAcumulado, getItem(lucro, i).getValue());
            getItem(acumulado, i).setValue(novoAcumulado);
        }
        return fluxoCaixa;
    }

}
