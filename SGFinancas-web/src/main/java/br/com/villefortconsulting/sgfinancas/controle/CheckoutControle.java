package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoSistemaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.BoletoService;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.TransacaoGetnetService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.BoletoException;
import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BoletoService boletoService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private TransacaoGetnetService transacaoService;

    private PagamentoSistemaDTO dadosPagamento;

    private TransacaoGetnet transacao;

    private String recorrencia;

    private String indexPlano;

    public void iniciar(String recorrencia, String indexPlano) {
        this.recorrencia = recorrencia;
        this.indexPlano = indexPlano;
        iniciar();
    }

    public String iniciar() {
        if (recorrencia != null && !recorrencia.isEmpty() && indexPlano != null && !indexPlano.isEmpty()) {
            dadosPagamento = new PagamentoSistemaDTO(recorrencia, indexPlano);
            dadosPagamento.getFatura().setEndereco(empresaService.getEmpresa().getEnderecoGetnet());
            dadosPagamento.getFatura().setNome(empresaService.getEmpresa().getDescricao());
            dadosPagamento.getFatura().setCpfCnpj(empresaService.getEmpresa().getCnpj());
        }
        return "efetuarPagamento.xhtml";
    }

    public void buscarEnderecoPorCep() {
        if (dadosPagamento.getFatura().getEndereco().getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(dadosPagamento.getFatura().getEndereco().getCep());

            dadosPagamento.getFatura().getEndereco().setUf(cepDTO.getUf().getDescricao());
            dadosPagamento.getFatura().getEndereco().setCidade(cepDTO.getCidade().getDescricao());
            dadosPagamento.getFatura().getEndereco().setRua(cepDTO.getEndereco());
            dadosPagamento.getFatura().getEndereco().setBairro(cepDTO.getBairro());
        }
    }

    public String confirmar() {
        try {
            transacao = null;
            transacao = transacaoService.adicionarEnviarTransacaoPendente(dadosPagamento);
            return "success.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(CheckoutControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao realizar a cobrança.");
        }
        return "";
    }

    public void baixarBoleto() {
        try {
            boletoService.gerarBoletoPara(transacao);
        } catch (BoletoException ex) {
            Logger.getLogger(PagamentoControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível gerar o download do boleto.");
        }
    }

    public String retornarDataHora() {
        return DataUtil.dataHoraToString(transacao.getDataSolicitacao());
    }

}
