package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NcmDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.NcmFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.NcmRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoNcm;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.LocalBean;
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
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NcmService extends BasicService<Ncm, NcmRepositorio, NcmFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new NcmRepositorio(em);
    }

    public Ncm buscarPorNcm(String codigo) {
        return repositorio.buscarPorCodigo(codigo);
    }

    public Ncm buscarPorCest(String cest) {
        return repositorio.buscarPorCest(cest);
    }

    public List<Ncm> listar(String descricao) {
        return repositorio.listar(descricao);
    }

    public List<Ncm> listar(Integer codigo) {
        return repositorio.listar(codigo);
    }

    public List<Ncm> listarPorCodigo(String codigo) {
        return repositorio.listarPorCodigo(codigo);
    }

    public List<Ncm> conferirCodigo(String descricao) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(descricao);

        if (matcher.find()) {
            return listar(Integer.parseInt(descricao));
        }
        return listar(descricao);
    }

    @Override
    public Criteria getModel(NcmFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "codigo", filter.getCodigo(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "codigoPai", filter.getCodigoPai(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "tipo", filter.getTipo());

        return criteria;
    }

    public List<Ncm> listaCapitulos() {
        return repositorio.listarCapitulos();
    }

    public List<Ncm> listarNcmPorCodigoDoPai(String codigo, String tipo) {
        return repositorio.listarNcmPorCodigoDoPai(codigo, tipo);
    }

    public NcmDTO selecionarCapitulo(NcmDTO ncmDTO) {
        // remover todos ncm selecionados de uma categoria abaixo do capitulo
        ncmDTO.setPosicao(null);
        ncmDTO.setSubposicaoUm(null);
        ncmDTO.setSubposicaoDois(null);
        ncmDTO.setItem(null);
        ncmDTO.setSubitem(null);

        // remover todas as listas disponiveis de uma categoria abaixo do capitulo
        ncmDTO.setPosicoes(null);
        ncmDTO.setSubposicoesUm(null);
        ncmDTO.setSubposicoesDois(null);
        ncmDTO.setItens(null);
        ncmDTO.setSubitens(null);

        // disponibilizar todas as listas de uma categoria abaixo do capitulo
        Ncm capitulo = ncmDTO.getCapitulo();
        if (capitulo != null) {
            ncmDTO.setPosicoes(repositorio.listarNcmPorCodigoDoPai(capitulo.getCodigo(), EnumTipoNcm.POSICAO.getTipo()));
            ncmDTO.setSubposicoesUm(repositorio.listarNcmPorCodigoDoPai(capitulo.getCodigo(), EnumTipoNcm.SUBPOSICAO_UM.getTipo()));
            ncmDTO.setSubposicoesDois(repositorio.listarNcmPorCodigoDoPai(capitulo.getCodigo(), EnumTipoNcm.SUBPOSICAO_DOIS.getTipo()));
            ncmDTO.setItens(repositorio.listarNcmPorCodigoDoPai(capitulo.getCodigo(), EnumTipoNcm.ITEM.getTipo()));
            ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(capitulo.getCodigo(), EnumTipoNcm.NCM.getTipo()));
        }

        return ncmDTO;
    }

    public NcmDTO selecionarPosicao(NcmDTO ncmDTO) {
        // remover todos ncm selecionados de uma categoria abaixo da posicao
        ncmDTO.setSubposicaoUm(null);
        ncmDTO.setSubposicaoDois(null);
        ncmDTO.setItem(null);
        ncmDTO.setSubitem(null);

        // remover todas as listas disponiveis de uma categoria abaixo da posicao
        ncmDTO.setSubposicoesUm(null);
        ncmDTO.setSubposicoesDois(null);
        ncmDTO.setItens(null);
        ncmDTO.setSubitens(null);

        // disponibilizar todas as listas de uma categoria abaixo da posicao
        Ncm posicao = ncmDTO.getPosicao();
        if (posicao != null) {
            // limpa lista de elementos nao selecionados

            ncmDTO.setSubposicoesUm(repositorio.listarNcmPorCodigoDoPai(posicao.getCodigo(), EnumTipoNcm.SUBPOSICAO_UM.getTipo()));
            ncmDTO.setSubposicoesDois(repositorio.listarNcmPorCodigoDoPai(posicao.getCodigo(), EnumTipoNcm.SUBPOSICAO_DOIS.getTipo()));
            ncmDTO.setItens(repositorio.listarNcmPorCodigoDoPai(posicao.getCodigo(), EnumTipoNcm.ITEM.getTipo()));
            ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(posicao.getCodigo(), EnumTipoNcm.NCM.getTipo()));
        }

        return ncmDTO;
    }

    public NcmDTO selecionarSubposicaoUm(NcmDTO ncmDTO) {
        // remover todos ncm selecionados de uma categoria abaixo da subposicao 1
        ncmDTO.setSubposicaoDois(null);
        ncmDTO.setItem(null);
        ncmDTO.setSubitem(null);

        // remover todas as listas disponiveis de uma categoria abaixo da subposicao 1
        ncmDTO.setSubposicoesDois(null);
        ncmDTO.setItens(null);
        ncmDTO.setSubitens(null);

        // disponibilizar todas as listas de uma categoria abaixo da subposicao 1
        Ncm subposicaoUm = ncmDTO.getSubposicaoUm();
        if (subposicaoUm != null) {
            // limpa lista de elementos nao selecionados
            ncmDTO = removerListasCategoriaNaoSelecionada(ncmDTO, EnumTipoNcm.SUBPOSICAO_UM.getTipo());

            ncmDTO.setSubposicoesDois(repositorio.listarNcmPorCodigoDoPai(subposicaoUm.getCodigo(), EnumTipoNcm.SUBPOSICAO_DOIS.getTipo()));
            ncmDTO.setItens(repositorio.listarNcmPorCodigoDoPai(subposicaoUm.getCodigo(), EnumTipoNcm.ITEM.getTipo()));
            ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(subposicaoUm.getCodigo(), EnumTipoNcm.NCM.getTipo()));
        }

        return ncmDTO;
    }

    public NcmDTO selecionarSubposicaoDois(NcmDTO ncmDTO) {
        // remover todos ncm selecionados de uma categoria abaixo da subposicao 2
        ncmDTO.setItem(null);
        ncmDTO.setSubitem(null);

        // remover todas as listas disponiveis de uma categoria abaixo da subposicao 2
        ncmDTO.setItens(null);
        ncmDTO.setSubitens(null);

        // disponibilizar todas as listas de uma categoria abaixo da subposicao 2
        Ncm subposicaoDois = ncmDTO.getSubposicaoDois();
        if (subposicaoDois != null) {
            // limpa lista de elementos nao selecionados
            ncmDTO = removerListasCategoriaNaoSelecionada(ncmDTO, EnumTipoNcm.SUBPOSICAO_DOIS.getTipo());

            ncmDTO.setItens(repositorio.listarNcmPorCodigoDoPai(subposicaoDois.getCodigo(), EnumTipoNcm.ITEM.getTipo()));
            ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(subposicaoDois.getCodigo(), EnumTipoNcm.NCM.getTipo()));
        }

        return ncmDTO;
    }

    public NcmDTO selecionarItem(NcmDTO ncmDTO) {
        // remover todos ncm selecionados de uma categoria abaixo do item
        ncmDTO.setSubitem(null);

        // remover todas as listas disponiveis de uma categoria abaixo do item
        ncmDTO.setSubitens(null);

        // disponibilizar todas as listas de uma categoria abaixo do item
        Ncm item = ncmDTO.getItem();
        if (item != null) {
            // limpa lista de elementos nao selecionados
            ncmDTO = removerListasCategoriaNaoSelecionada(ncmDTO, EnumTipoNcm.ITEM.getTipo());

            ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(item.getCodigo(), EnumTipoNcm.NCM.getTipo()));
        }

        return ncmDTO;
    }

    public NcmDTO removerListasCategoriaNaoSelecionada(NcmDTO ncmDTO, String tipo) {

        if (tipo.equals(EnumTipoNcm.SUBPOSICAO_UM.getTipo())) {
            removerListaPosicao(ncmDTO);
        } else if (tipo.equals(EnumTipoNcm.SUBPOSICAO_DOIS.getTipo())) {
            removerListaSubposicaoUm(ncmDTO);
            removerListaPosicao(ncmDTO);
        } else if (tipo.equals(EnumTipoNcm.ITEM.getTipo())) {
            removerListaSubposicaoDois(ncmDTO);
            removerListaSubposicaoUm(ncmDTO);
            removerListaPosicao(ncmDTO);
        } else if (tipo.equals(EnumTipoNcm.SUBITEM.getTipo())) {
            removerListaItem(ncmDTO);
            removerListaSubposicaoDois(ncmDTO);
            removerListaSubposicaoUm(ncmDTO);
            removerListaPosicao(ncmDTO);
        }

        return ncmDTO;
    }

    private static NcmDTO removerListaPosicao(NcmDTO ncmDTO) {
        if (ncmDTO.getPosicao() == null) {
            ncmDTO.setPosicoes(null);
        }
        return ncmDTO;
    }

    private static NcmDTO removerListaSubposicaoUm(NcmDTO ncmDTO) {
        if (ncmDTO.getSubposicaoUm() == null) {
            ncmDTO.setSubposicoesUm(null);
        }
        return ncmDTO;
    }

    private static NcmDTO removerListaSubposicaoDois(NcmDTO ncmDTO) {
        if (ncmDTO.getSubposicaoDois() == null) {
            ncmDTO.setSubposicoesDois(null);
        }
        return ncmDTO;
    }

    private static NcmDTO removerListaItem(NcmDTO ncmDTO) {
        if (ncmDTO.getItem() == null) {
            ncmDTO.setItens(null);
        }
        return ncmDTO;
    }

    public NcmDTO carregarNcmDTO(Ncm ncm) {
        NcmDTO ncmDTO = new NcmDTO();
        if (ncm != null && ncm.getId() != null) {
            ncmDTO.setSubitem(ncm);

            // Carregar Ncm e suas categorias correspondentes selecionados cno cadastro
            while (!ncm.getTipo().equals(EnumTipoNcm.CAPITULO.getTipo())) {
                ncm = carregarNcmPai(ncmDTO, ncm);
            }

            // Carregar listas disponiveis de acordo com as selecoes na hora do cadastro
            return carregarListasNcmPai(ncmDTO, ncm);
        }

        return ncmDTO;
    }

    private Ncm carregarNcmPai(NcmDTO ncmDTO, Ncm ncm) {
        Ncm ncmPai = buscarPorNcm(ncm.getCodigoPai());
        String tipo = ncmPai.getTipo();

        if (tipo.equals(EnumTipoNcm.ITEM.getTipo())) {
            ncmDTO.setItem(ncmPai);
        } else if (tipo.equals(EnumTipoNcm.SUBPOSICAO_DOIS.getTipo())) {
            ncmDTO.setSubposicaoDois(ncmPai);
        } else if (tipo.equals(EnumTipoNcm.SUBPOSICAO_UM.getTipo())) {
            ncmDTO.setSubposicaoUm(ncmPai);
        } else if (tipo.equals(EnumTipoNcm.POSICAO.getTipo())) {
            ncmDTO.setPosicao(ncmPai);
        } else if (tipo.equals(EnumTipoNcm.CAPITULO.getTipo())) {
            ncmDTO.setCapitulo(ncmPai);
        }

        return ncmPai;
    }

    private NcmDTO carregarListasNcmPai(NcmDTO ncmDTO, Ncm ncm) {

        if (ncmDTO.getPosicao() != null) {
            ncmDTO.setPosicoes(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getCapitulo().getCodigo(), EnumTipoNcm.POSICAO.getTipo()));
        }

        if (ncmDTO.getSubposicaoUm() != null) {
            if (ncmDTO.getPosicao() != null) {
                ncmDTO.setSubposicoesUm(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getPosicao().getCodigo(), EnumTipoNcm.SUBPOSICAO_UM.getTipo()));
            } else {
                ncmDTO.setSubposicoesUm(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getCapitulo().getCodigo(), EnumTipoNcm.SUBPOSICAO_UM.getTipo()));
            }
        }

        if (ncmDTO.getSubposicaoDois() != null) {
            if (ncmDTO.getSubposicaoUm() != null) {
                ncmDTO.setSubposicoesDois(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getSubposicaoUm().getCodigo(), EnumTipoNcm.SUBPOSICAO_DOIS.getTipo()));
            } else {
                if (ncmDTO.getPosicao() != null) {
                    ncmDTO.setSubposicoesDois(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getPosicao().getCodigo(), EnumTipoNcm.SUBPOSICAO_DOIS.getTipo()));
                } else {
                    ncmDTO.setSubposicoesDois(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getCapitulo().getCodigo(), EnumTipoNcm.SUBPOSICAO_DOIS.getTipo()));
                }
            }
        }

        if (ncmDTO.getItem() != null) {
            if (ncmDTO.getSubposicaoDois() != null) {
                ncmDTO.setItens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getSubposicaoDois().getCodigo(), EnumTipoNcm.ITEM.getTipo()));
            } else {
                if (ncmDTO.getSubposicaoUm() != null) {
                    ncmDTO.setItens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getSubposicaoUm().getCodigo(), EnumTipoNcm.ITEM.getTipo()));
                } else {
                    if (ncmDTO.getPosicao() != null) {
                        ncmDTO.setItens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getPosicao().getCodigo(), EnumTipoNcm.ITEM.getTipo()));
                    } else {
                        ncmDTO.setItens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getCapitulo().getCodigo(), EnumTipoNcm.ITEM.getTipo()));
                    }
                }
            }
        }

        if (ncmDTO.getSubitem() != null) {
            if (ncmDTO.getItem() != null) {
                ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getItem().getCodigo(), EnumTipoNcm.NCM.getTipo()));
            } else {
                if (ncmDTO.getSubposicaoDois() != null) {
                    ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getSubposicaoDois().getCodigo(), EnumTipoNcm.NCM.getTipo()));
                } else {
                    if (ncmDTO.getSubposicaoUm() != null) {
                        ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getSubposicaoUm().getCodigo(), EnumTipoNcm.NCM.getTipo()));
                    } else {
                        if (ncmDTO.getPosicao() != null) {
                            ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getPosicao().getCodigo(), EnumTipoNcm.NCM.getTipo()));
                        } else {
                            ncmDTO.setSubitens(repositorio.listarNcmPorCodigoDoPai(ncmDTO.getCapitulo().getCodigo(), EnumTipoNcm.NCM.getTipo()));
                        }
                    }
                }
            }
        }
        return ncmDTO;
    }

    public static String buscarCest(Ncm ncm) {
        if (ncm.getIdNcmPai() != null && StringUtils.isEmpty(ncm.getCest())) {
            return buscarCest(ncm.getIdNcmPai());
        }

        return ncm.getCest();
    }

}
