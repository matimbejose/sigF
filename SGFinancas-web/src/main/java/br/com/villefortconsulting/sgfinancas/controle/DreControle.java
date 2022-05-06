package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaPagarReceberDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DreDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.DreFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.AnalisesService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Named
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
public class DreControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private AnalisesService analisesService;

    private DreFiltro filtro = new DreFiltro();

    private transient Map<Integer, TreeNode> mapDRE;

    private List<ContaPagarReceberDTO> listaNecessidadeCaixa;

    private CentroCusto centroCustoSelecionado;

    public DreControle() {
        filtro = new DreFiltro();
        filtro.setAno(Calendar.getInstance().get(Calendar.YEAR));
    }

    public TreeNode obterTreeNodeDre() {
        if (mapDRE == null) {
            mapDRE = new HashMap<>();
        }
        if (mapDRE.get(filtro.getAno()) == null) {
            TreeNode tn = new DefaultTreeNode();
            if (filtro.getAno() != null) {
                recursiveAddAll(analisesService.obterDre(filtro.getAno(), centroCustoSelecionado), tn);
                mapDRE.put(filtro.getAno(), recursiveCleanup(tn));
            }
        }

        return mapDRE.get(filtro.getAno());
    }

    private static void recursiveAddAll(List<DreDTO> dre, TreeNode tn) {
        dre.forEach(dto -> {
            TreeNode ntn = new DefaultTreeNode(dto);
            if (!dto.getChildren().isEmpty()) {
                recursiveAddAll(dto.getChildren(), ntn);
            }
            tn.getChildren().add(ntn);
        });
    }

    private static TreeNode recursiveCleanup(TreeNode tn) {
        if (!tn.isLeaf()) {
            for (int i = tn.getChildCount() - 1; i >= 0; i--) {
                TreeNode child = recursiveCleanup(tn.getChildren().get(i));
                DreDTO dto = (DreDTO) child.getData();
                if (dto != null && dto.canDelete()) {
                    tn.getChildren().remove(child);
                }
            }
        }
        return tn;
    }

    public String formataNumero(Double num) {
        if (num == null || num == 0d) {
            return "";
        }
        return NumeroUtil.converterValorParaMonetario(num, 2);
    }

    public String getValorMensal(DreDTO dreDTO, Integer mes) {
        ValorLancamentoDTO valorLancamentoDTO = dreDTO.getListaLancamento().stream()
                .filter(p -> p.getMes().equals(mes))
                .findFirst().orElse(null);

        if (valorLancamentoDTO == null || (valorLancamentoDTO.getValor() == null || valorLancamentoDTO.getValor() == 0)) {
            return "";
        }

        return "R$ " + NumeroUtil.converterValorParaMonetario(valorLancamentoDTO.getValor(), 2);
    }

    public String obtervalorCaixa(String tipo, String mes) {
        ContaPagarReceberDTO contaPagarReceberDTO = null;

        if (listaNecessidadeCaixa != null && !listaNecessidadeCaixa.isEmpty()) {
            contaPagarReceberDTO = listaNecessidadeCaixa.stream()
                    .filter(p -> p.getMes().equals(mes))
                    .findFirst().orElse(null);
        }
        if (contaPagarReceberDTO == null) {
            return "";
        }

        String retorno = "";
        Double valor = null;

        switch (tipo) {
            case "r":
                valor = contaPagarReceberDTO.getValorReceber() != null ? contaPagarReceberDTO.getValorReceber() : null;
                break;
            case "d":
                valor = contaPagarReceberDTO.getValorPagar() != null ? contaPagarReceberDTO.getValorPagar() : null;
                break;
            case "c":
                valor = contaPagarReceberDTO.getNecessidadeCaixa() != null ? contaPagarReceberDTO.getNecessidadeCaixa() : null;
                break;
            default:
                break;
        }

        if (valor != null) {
            retorno = NumeroUtil.converterValorParaMonetario(valor, 2);
        }
        return retorno;
    }

    public void gerarRelatorioDre() {
        try {
            gerarPdf(relatorioService.gerarRelatorioDre(filtro.getAno(), centroCustoSelecionado), "DRE " + filtro.getAno() + " - " + empresaService.getEmpresa().getDescricao());
        } catch (RelatorioException | IOException | JRException ex) {
            Logger.getLogger(DreControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer[] getMesesArray() {
        return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    }

    public String getStyleClass(Double d) {
        String style = "money";
        if (d == null || d == 0d) {
            style += " empty";
        } else if (d < 0) {
            style += " text-danger";
        } else {
            style += " text-success";
        }
        return style;
    }

}
