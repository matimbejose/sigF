package br.com.villefortconsulting.servicos.getnet;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoSistema;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.dto.BoletoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CartaoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CreditoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DebitoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EnderecoGetnetDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoSistemaDTO;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ServiceApi extends GetnetApi {

    private static final long serialVersionUID = 1L;

    public CreditoGetnetDTO preencherCredito(CartaoGetnetDTO cartao, String descricao, Double parcela) {
        CreditoGetnetDTO credito = new CreditoGetnetDTO();
        credito.setCartao(cartao);
        credito.setDescricao(descricao);
        credito.setParcela(parcela);

        return credito;
    }

    public DebitoGetnetDTO preencherDebito(CartaoGetnetDTO cartao) {
        DebitoGetnetDTO debito = new DebitoGetnetDTO();
        debito.setCartao(cartao);
        return debito;
    }

    public ClienteGetnetDTO preencherCliente(Cliente cliente) {
        ClienteGetnetDTO clienteDTO = new ClienteGetnetDTO();

        clienteDTO.setIdCliente(cliente.getId().toString());
        clienteDTO.setNomeCompleto(cliente.getNome());
        clienteDTO.setNumeroDocumento(CpfCnpjUtil.removerMascaraCnpj(cliente.getCpfCNPJ()));
        clienteDTO.setTipoDocumento(CpfCnpjUtil.removerMascaraCnpj(cliente.getCpfCNPJ()).length() == 11 ? "CPF" : "CNPJ");
        clienteDTO.setEndereco(new EnderecoGetnetDTO(cliente.getEndereco()));

        return clienteDTO;
    }

    public ClienteGetnetDTO preencherCliente(Empresa empresa, PagamentoSistemaDTO dto) {
        ClienteGetnetDTO clienteDTO = new ClienteGetnetDTO();

        clienteDTO.setIdCliente("customer-" + empresa.getTenatID());
        if (dto != null) {
            clienteDTO.setNomeCompleto(dto.getFatura().getNome());
            clienteDTO.setNumeroDocumento(CpfCnpjUtil.removerMascaraCnpj(dto.getFatura().getCpfCnpj()));
            clienteDTO.setTipoDocumento(CpfCnpjUtil.removerMascaraCnpj(dto.getFatura().getCpfCnpj()).length() == 11 ? "CPF" : "CNPJ");
            clienteDTO.setEndereco(dto.getFatura().getEndereco());
        } else {
            clienteDTO.setNomeCompleto(empresa.getDescricao());
            clienteDTO.setNumeroDocumento(CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()));
            clienteDTO.setTipoDocumento(CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()).length() == 11 ? "CPF" : "CNPJ");
            clienteDTO.setEndereco(empresa.getEnderecoGetnet());
        }

        String cep = clienteDTO.getEndereco().getCep();
        if (cep == null) {
            cep = "";
        }
        clienteDTO.getEndereco().setCep(cep.replaceAll("\\D", ""));

        return clienteDTO;
    }

    public CompraGetnetDTO preencherCompra(Venda venda) {
        CompraGetnetDTO compra = new CompraGetnetDTO();

        compra.setIdCompra(venda.getId().toString());
        compra.setImposto(0.0);
        compra.setTipoProduto("service");

        return compra;
    }

    public CompraGetnetDTO preencherCompra(Integer numPagamento) {
        CompraGetnetDTO compra = new CompraGetnetDTO();

        compra.setIdCompra(numPagamento.toString());
        compra.setImposto(0.0);
        compra.setTipoProduto("service");

        return compra;
    }

    public BoletoGetnetDTO preencherBoleto(Venda venda) {
        BoletoGetnetDTO boleto = new BoletoGetnetDTO();

        boleto.setInstrucao("Compra pelo SG Finanças.");

        return boleto;
    }

    public BoletoGetnetDTO preencherBoleto(PagamentoSistema pagamentoSistema) {
        BoletoGetnetDTO boleto = new BoletoGetnetDTO();

        boleto.setInstrucao("Pagamento da licença do SG Finanças.");

        return boleto;
    }

}
