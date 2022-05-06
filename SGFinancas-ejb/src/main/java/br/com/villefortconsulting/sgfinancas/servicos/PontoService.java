package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.Ponto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PontoEntradaSaidaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.RegistroPontoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PontoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContratacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoHora;
import br.com.villefortconsulting.util.DataUtil;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PontoService extends BasicService<Ponto, PontoRepositorio, RegistroPontoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private FuncionarioService funcionarioService;

    @EJB
    private PontoObservacaoService pontoObservacaoService;

    @EJB
    private ParametroSistemaService parametroService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new PontoRepositorio(em, adHocTenant);
    }

    public List<PontoEntradaSaidaDTO> listarRegistroEntradaSaidaFuncionario(Funcionario funcionario, Date competencia) {
        if (competencia == null) {
            throw new IllegalArgumentException("Favor informar a competência.");
        }

        List<PontoEntradaSaidaDTO> lpes = new LinkedList<>();
        if (funcionario != null) {
            lpes = preenchePontoEntradaSaidaDTO(funcionario, competencia);
        } else {
            lpes.addAll(funcionarioService.listarFuncionariosAtivos(competencia).stream()
                    .map(func -> preenchePontoEntradaSaidaDTO(func, competencia))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));
        }

        return lpes;
    }

    public List<PontoEntradaSaidaDTO> preenchePontoEntradaSaidaDTO(Funcionario funcionario, Date competencia) {
        List<PontoEntradaSaidaDTO> lpes = new LinkedList<>();
        ParametroSistema parametro = parametroService.getParametro();

        GregorianCalendar gc = new GregorianCalendar();
        for (int i = 1; i <= DataUtil.quantidadeDiasMes(competencia); i++) {
            gc.setTime(competencia);
            gc.set(GregorianCalendar.DAY_OF_MONTH, i);

            String diaSemana = DataUtil.obterNomeDiaSemana(i, competencia);

            PontoEntradaSaidaDTO pes = new PontoEntradaSaidaDTO();

            pes.setDia(i);
            pes.setDiaSemana(diaSemana);
            pes.setEntradaManha(repositorio.buscarHoraEntradaSaida(funcionario, i, competencia, EnumTipoHora.ENTRADA_MANHA.getTipo()));
            pes.setSaidaManha(repositorio.buscarHoraEntradaSaida(funcionario, i, competencia, EnumTipoHora.SAIDA_MANHA.getTipo()));
            pes.setEntradaTarde(repositorio.buscarHoraEntradaSaida(funcionario, i, competencia, EnumTipoHora.ENTRADA_TARDE.getTipo()));
            pes.setSaidaTarde(repositorio.buscarHoraEntradaSaida(funcionario, i, competencia, EnumTipoHora.SAIDA_TARDE.getTipo()));
            pes.setEntradaExtra(repositorio.buscarHoraEntradaSaida(funcionario, i, competencia, EnumTipoHora.ENTRADA_EXTRA.getTipo()));
            pes.setSaidaExtra(repositorio.buscarHoraEntradaSaida(funcionario, i, competencia, EnumTipoHora.SAIDA_EXTRA.getTipo()));
            pes.setObservacao(pontoObservacaoService.buscarObservacao(competencia, funcionario));
            pes.setFuncionario(funcionario.getNome());
            pes.setTipoContratacao(EnumTipoContratacao.getDescricao(funcionario.getTipoContratacao()));
            pes.setIdFuncionario(funcionario.getId());

            Long tempoObrigatorio = 0l;

            if (!diaSemana.equals("Domingo") && !diaSemana.equals("Sábado") // Verifica fds
                    && !DataUtil.ehFeriado(gc.getTime()) // Verifica feriado
                    && !funcionarioService.verificaFuncionarioEstaFerias(funcionario, gc.getTime())) { // Verifica ferias
                if ("S".equals(funcionario.getTemHorarioEspecial())) {

                    Integer hora = Integer.parseInt(funcionario.getHoraEspecial().substring(0, 2));
                    Integer minuto = Integer.parseInt(funcionario.getHoraEspecial().substring(3, 5));

                    Calendar hrInicio = Calendar.getInstance();
                    hrInicio.setTime(new Date());
                    hrInicio.set(Calendar.HOUR_OF_DAY, hora);
                    hrInicio.set(Calendar.MINUTE, minuto);
                    hrInicio.set(Calendar.SECOND, 0);

                    hora = Integer.parseInt(funcionario.getHoraEspecialFinal().substring(0, 2));
                    minuto = Integer.parseInt(funcionario.getHoraEspecialFinal().substring(3, 5));

                    Calendar hrFim = Calendar.getInstance();
                    hrFim.setTime(new Date());
                    hrFim.set(Calendar.HOUR_OF_DAY, hora);
                    hrFim.set(Calendar.MINUTE, minuto);
                    hrFim.set(Calendar.SECOND, 0);

                    Duration duration = Duration.between(hrInicio.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                            hrFim.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

                    tempoObrigatorio = duration.toMinutes();
                } else if (EnumTipoContratacao.FUNCIONARIO.getTipo().equals(funcionario.getTipoContratacao()) || EnumTipoContratacao.CONTRATADO.getTipo().equals(funcionario.getTipoContratacao())) {
                    tempoObrigatorio = LocalTime.parse(parametro.getHoraTrabalhadaFuncionario()).getLong(ChronoField.MILLI_OF_DAY);
                } else if (EnumTipoContratacao.ESTAGIARIO.getTipo().equals(funcionario.getTipoContratacao())) {
                    tempoObrigatorio = LocalTime.parse(parametro.getHoraTrabalhadaEstagiario()).getLong(ChronoField.MILLI_OF_DAY);
                } else {
                    tempoObrigatorio = LocalTime.parse(parametro.getHoraTrabalhadaMenorAprendiz()).getLong(ChronoField.MILLI_OF_DAY);
                }
            }

            pes.setHorasObrigatorias(tempoObrigatorio);

            lpes.add(pes);
        }
        return lpes;
    }

    public List<PontoEntradaSaidaDTO> buscarRegistroEntradaSaidaFuncionario(Funcionario funcionario) {
        List<PontoEntradaSaidaDTO> lpes = new LinkedList<>();

        PontoEntradaSaidaDTO pes = new PontoEntradaSaidaDTO();
        pes.setEntradaManha(repositorio.buscarHoraEntradaSaida(funcionario, EnumTipoHora.ENTRADA_MANHA.getTipo()));
        pes.setSaidaManha(repositorio.buscarHoraEntradaSaida(funcionario, EnumTipoHora.SAIDA_MANHA.getTipo()));
        pes.setEntradaTarde(repositorio.buscarHoraEntradaSaida(funcionario, EnumTipoHora.ENTRADA_TARDE.getTipo()));
        pes.setSaidaTarde(repositorio.buscarHoraEntradaSaida(funcionario, EnumTipoHora.SAIDA_TARDE.getTipo()));
        pes.setEntradaExtra(repositorio.buscarHoraEntradaSaida(funcionario, EnumTipoHora.ENTRADA_EXTRA.getTipo()));
        pes.setSaidaExtra(repositorio.buscarHoraEntradaSaida(funcionario, EnumTipoHora.SAIDA_EXTRA.getTipo()));

        lpes.add(pes);

        return lpes;
    }

    public Ponto salvar(Funcionario funcionario) {
        String tipoHora = calcularTipoHora(funcionario);
        ParametroSistema parametro = parametroService.getParametro();
        Integer hora;
        Integer minuto;

        if (tipoHora.equals(EnumTipoHora.ENTRADA_MANHA.getTipo())) {
            if (StringUtils.isNotEmpty(funcionario.getHoraEspecial())) {
                hora = Integer.parseInt(funcionario.getHoraEspecial().substring(0, 2));
                minuto = Integer.parseInt(funcionario.getHoraEspecial().substring(3, 5));
            } else {
                hora = Integer.parseInt(parametro.getHoraInicioJornada().substring(0, 2));
                minuto = Integer.parseInt(parametro.getHoraInicioJornada().substring(3, 5));
            }

            Calendar horaMaxima = Calendar.getInstance();
            horaMaxima.setTime(new Date());
            horaMaxima.set(Calendar.HOUR_OF_DAY, hora);
            horaMaxima.set(Calendar.MINUTE, minuto);
            horaMaxima.set(Calendar.SECOND, 0);
        } else if (tipoHora.equals(EnumTipoHora.SAIDA_TARDE.getTipo())) {
            if (StringUtils.isNotEmpty(funcionario.getHoraEspecialFinal())) {
                hora = Integer.parseInt(funcionario.getHoraEspecialFinal().substring(0, 2));
                minuto = Integer.parseInt(funcionario.getHoraEspecialFinal().substring(3, 5));
            } else {
                hora = Integer.parseInt(parametro.getHoraFimJornada().substring(0, 2));
                minuto = Integer.parseInt(parametro.getHoraFimJornada().substring(3, 5));
            }

            Calendar horaMaxima = Calendar.getInstance();
            horaMaxima.setTime(new Date());
            horaMaxima.set(Calendar.HOUR_OF_DAY, hora);
            horaMaxima.set(Calendar.MINUTE, minuto);
            horaMaxima.set(Calendar.SECOND, 0);
        }

        Ponto ponto = new Ponto();
        ponto.setIdFuncionario(funcionario);
        ponto.setDataPonto(DataUtil.getHoje());
        ponto.setTipoHora(tipoHora);

        return salvar(ponto);
    }

    private String calcularTipoHora(Funcionario funcionario) {
        switch (quantidadeRegistrosDia(funcionario)) {
            case 1:
                return EnumTipoHora.SAIDA_MANHA.getTipo();
            case 2:
                return EnumTipoHora.ENTRADA_TARDE.getTipo();
            case 3:
                return EnumTipoHora.SAIDA_TARDE.getTipo();
            case 4:
                return EnumTipoHora.ENTRADA_EXTRA.getTipo();
            case 5:
                return EnumTipoHora.SAIDA_EXTRA.getTipo();
            default:
                return EnumTipoHora.ENTRADA_MANHA.getTipo();
        }
    }

    public boolean podeBaterPonto(Funcionario funcionario) {
        Date dataSaidaManha = buscarHoraEntradaSaida(funcionario, DataUtil.getHoje(), EnumTipoHora.SAIDA_MANHA.getTipo());
        Date dataRetornoManha = buscarHoraEntradaSaida(funcionario, DataUtil.getHoje(), EnumTipoHora.ENTRADA_TARDE.getTipo());

        LocalDateTime saidaAlmoco;

        if (dataSaidaManha != null && dataRetornoManha == null) {
            saidaAlmoco = dataSaidaManha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            return true;
        }

        LocalDateTime local = LocalDateTime.now();

        Duration duration = Duration.between(saidaAlmoco, local);
        Long minutos = duration.toMinutes();

        Integer minutosDesdeASaida = Integer.valueOf(minutos.toString());

        int registroPonto = quantidadeRegistrosDia(funcionario);

        int periodoPadrao = retornaPeriodoAcordoFuncionario(funcionario);
        return (registroPonto == 2 && minutosDesdeASaida >= periodoPadrao) || (registroPonto == 1 || registroPonto > 3);
    }

    public int retornaPeriodoAcordoFuncionario(Funcionario funcionario) {

        ParametroSistema parametro = parametroService.getParametro();

        if (EnumTipoContratacao.ESTAGIARIO.getTipo().equals(funcionario.getTipoContratacao())) {
            return parametro.getPeriodoAlmocoEstagiario();
        }
        if (EnumTipoContratacao.MENOR_APRENDIZ.getTipo().equals(funcionario.getTipoContratacao())) {
            return parametro.getPeriodoAlmocoMenorAprendiz();
        }
        if (EnumTipoContratacao.GESTAO_PESSOA.getTipo().equals(funcionario.getTipoContratacao())) {
            return parametro.getPeriodoAlmocoMenorAprendiz();
        }
        return parametro.getPeriodoAlmoco();
    }

    public String tempoRestanteRetornoAlmoco(Funcionario funcionario) {

        int periodoPadrao = retornaPeriodoAcordoFuncionario(funcionario);

        Date dataSaidaManha = buscarHoraEntradaSaida(funcionario, DataUtil.getHoje(), EnumTipoHora.SAIDA_MANHA.getTipo());
        Date dataRetornoManha = buscarHoraEntradaSaida(funcionario, DataUtil.getHoje(), EnumTipoHora.ENTRADA_TARDE.getTipo());

        LocalDateTime saidaAlmoco;

        if (dataSaidaManha != null && dataRetornoManha == null) {
            saidaAlmoco = dataSaidaManha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            saidaAlmoco = LocalDateTime.now();
        }

        LocalDateTime local = LocalDateTime.now();

        Duration duration = Duration.between(saidaAlmoco, local);
        Long minutos = duration.toMinutes();

        Integer minutosDesdeASaida = Integer.valueOf(minutos.toString());

        return Integer.toString(periodoPadrao - minutosDesdeASaida);
    }

    public PontoEntradaSaidaDTO buscarPontoDTOPorTipo(Funcionario funcionario, Date competencia, String tipo) {

        Integer dia = DataUtil.getDia(competencia);

        Ponto ponto = repositorio.buscarPeriodoDia(funcionario, dia, tipo);

        PontoEntradaSaidaDTO pontoRetorno = new PontoEntradaSaidaDTO();

        if (ponto != null) {
            pontoRetorno.setSaidaManha(ponto.getDataPonto());
        }

        return pontoRetorno;
    }

    public Integer quantidadeRegistrosDia(Funcionario funcionario) {
        return repositorio.quantidadeRegistrosDia(funcionario).intValue();
    }

    @Override
    public Criteria getModel(RegistroPontoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addEqRestrictionTo(criteria, "idFuncionario", filter.getFuncionario());

        criteria.add(Restrictions.between("dataPonto", DataUtil.getPrimeiroDiaDoMes(filter.getCompetencia()), DataUtil.getUltimoDiaDoMes(filter.getCompetencia())));
        return criteria;
    }

    public String calculaHoraDiariaPonto(PontoEntradaSaidaDTO funcionario) {
        Date entradaManha = funcionario.getEntradaManha();
        Date saidaManha = funcionario.getSaidaManha();
        Date entradaTarde = funcionario.getEntradaTarde();
        Date saidaTarde = funcionario.getSaidaTarde();
        Date entradaExtra = funcionario.getEntradaExtra();
        Date saidaExtra = funcionario.getSaidaExtra();

        long diferencaSegundosManha = 0;
        long diferencaSegundosTarde = 0;
        long diferencaSegundosExtra = 0;

        if (saidaManha != null && entradaManha != null) {
            diferencaSegundosManha = (saidaManha.getTime() - entradaManha.getTime()) / 1000;
        }

        if (saidaTarde != null && entradaTarde != null) {
            diferencaSegundosTarde = (saidaTarde.getTime() - entradaTarde.getTime()) / 1000;
        }

        if (entradaExtra != null && saidaExtra != null) {
            diferencaSegundosExtra = (saidaExtra.getTime() - entradaExtra.getTime()) / 1000;
        }

        Long segundos = diferencaSegundosManha + diferencaSegundosTarde + diferencaSegundosExtra;

        return formataTempo(Integer.valueOf(segundos.toString()));
    }

    public Ponto buscarPeriodoDia(Funcionario funcionario, Integer competencia, String tipoHora) {
        return repositorio.buscarPeriodoDia(funcionario, competencia, tipoHora);
    }

    public Ponto buscarPontoPorTipo(Funcionario funcionario, Date competencia, String tipoHora) {
        return repositorio.buscarPontoPorTipo(funcionario, competencia, tipoHora);
    }

    public Ponto buscarPonto(Funcionario funcionario, Date competencia, String tipoHora, Integer dia) {
        return repositorio.buscarPonto(funcionario, competencia, tipoHora, dia);
    }

    public void atualizarRegistroPonto(Funcionario funcionario, Date competencia, List<PontoEntradaSaidaDTO> lista) {
        lista.forEach(pontoDTO -> atualizaPonto(funcionario, competencia, pontoDTO));
    }

    private void atualizaPonto(Funcionario funcionario, Date competencia, PontoEntradaSaidaDTO pontoDTO) {
        Date dataAnterior = null;
        for (int i = 0; i < 6; i++) {
            // Faz merge da competencia com a hora informada pois o componente do primefaces pega somente a hora
            // Verifica se horarios foram atualizados corretamente (Ex. se a saida para o almoço foi posterior a entrada pela manha)
            Date dataHoraPonto = pontoDTO.getDataHoraPonto(i);
            if (dataHoraPonto != null) {
                Calendar dataCompetencia = Calendar.getInstance();
                dataCompetencia.setTime(competencia);

                Calendar dataAtualizada = Calendar.getInstance();
                dataAtualizada.setTime(dataHoraPonto);
                dataAtualizada.set(dataCompetencia.get(Calendar.YEAR), dataCompetencia.get(Calendar.MONTH), pontoDTO.getDia());

                dataHoraPonto = dataAtualizada.getTime();

                if (dataAnterior != null && dataHoraPonto.before(dataAnterior)) {
                    throw new CadastroException("inconsistência no valor das horas, favor verificar lançamentos. ", null);
                }
            }

            // Insere novo horario ou atualiza ponto
            String tipoHora = EnumTipoHora.retornaEnumSelecionado(i).getTipo();
            Ponto ponto = repositorio.buscarPonto(funcionario, competencia, tipoHora, pontoDTO.getDia());
            if (ponto == null) {
                if (dataHoraPonto != null) {
                    ponto = new Ponto();
                    ponto.setIdFuncionario(funcionario);
                    ponto.setTipoHora(tipoHora);
                    ponto.setDataPonto(dataHoraPonto);
                    adicionar(ponto);
                }
            } else if (!ponto.getDataPonto().equals(dataHoraPonto)) {
                ponto.setDataPonto(dataHoraPonto);
                alterar(ponto);
            }

            dataAnterior = dataHoraPonto;
        }
    }

    public String somaTotalHora(PontoEntradaSaidaDTO func, PontoEntradaSaidaDTO funcAnt) {
        //Verifica se teve algum ponto anterior
        if (funcAnt == null) {
            //Soma os dias e os converte para string para retorna-lo.
            Integer segundosDia = converterHorasParaSegundos(func.getSomaHoras());
            return formataTempo(segundosDia);

        } else { //Caso tenha anterior, soma o total de horas anterior com o total de horas do loop para ter um novo total para aquele funcionário.
            Integer segundosTotalDiaAnterior = converterHorasParaSegundos(funcAnt.getTotalHoras());
            Integer segundosDoDia = converterHorasParaSegundos(func.getSomaHoras());

            return formataTempo(segundosTotalDiaAnterior + segundosDoDia);
        }
    }

    public String formataTempo(Integer tempo) {
        Integer ss = tempo % 60;
        tempo /= 60;
        Integer min = tempo % 60;
        tempo /= 60;
        Integer hh = tempo;
        return strzero(hh) + ":" + strzero(min) + ":" + strzero(ss);
    }

    private static String strzero(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return String.valueOf(n);
    }

    public Integer converterHorasParaSegundos(String data) {
        String formatacao = data;
        String hora = StringUtils.substringBefore(data, ":");
        formatacao = StringUtils.substringAfter(formatacao, ":");
        String minuto = StringUtils.substring(formatacao, 0, 2);
        String segundo = StringUtils.substring(formatacao, 3, 5);

        Integer horaSoma = Integer.parseInt(hora) * 3600;
        Integer minutoSoma = Integer.parseInt(minuto) * 60;
        Integer segundoSoma = Integer.parseInt(segundo);

        return horaSoma + minutoSoma + segundoSoma;
    }

    public String retornaMes(int mes) {
        if (mes > 0 && mes < 13) {
            return DataUtil.getMesesCompletos().get(mes - 1);
        }
        return null;
    }

    public Date buscarHoraEntradaSaida(Funcionario funcionario, Date competencia, String tipoHora) {
        return repositorio.buscarHoraEntradaSaida(funcionario, DataUtil.getDia(competencia), competencia, tipoHora);
    }

    public void removePontoPorTipo(Funcionario funcionario, Date competencia) {
        repositorio.removePontoPorTipo(funcionario, competencia);
    }

}
