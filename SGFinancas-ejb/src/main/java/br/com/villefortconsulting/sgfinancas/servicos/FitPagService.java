package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoSistemaDTO;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import br.com.villefortconsulting.util.fitpag.request.Payment;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FitPagService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Random rand;

    private Integer gerarCodigoNumerico() {
        if (rand == null) {
            try {
                rand = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException ex) {
                return null;
            }
        }
        return Math.abs(rand.nextInt() * 100_000_001);
    }

    public Payment createPayment(final PagamentoSistemaDTO dados, final Empresa empresa, final List<Modulo> modulos, final Double qteDiasRestantes) {
        String notificationUrl = SystemPreferencesUtil.getPropOrThrow("sec.fitpag.notificationUrl", () -> new IllegalStateException("URL de notificação da fitpag não configurada"));
        Payment pagamento = new Payment();

        pagamento.setMode("default");

        pagamento.setMethod(dados.getTipoPagamentoFitPag());

        pagamento.getSender().setName(empresa.getNomeFantasia());
        pagamento.getSender().setEmail(empresa.getEmail());
        pagamento.getSender().getPhone().setPhone(empresa.getFone());
        pagamento.getSender().getDocuments().addDocument(dados.getFatura().getCpfCnpj());
        pagamento.getSender().setHash(dados.getHash());

        pagamento.setCurrency("BRL");

        pagamento.setNotificationURL(notificationUrl + "/RESTful/v1/public/transacao/fitpag/notificacao/sistema");

        pagamento.getItems().getItem().addAll(modulos.stream()
                .map(mod -> new Payment.Items.Item(mod.getId(), mod.getNome(), 1, mod.getValorFinal(qteDiasRestantes)))
                .collect(Collectors.toList()));

        pagamento.setReference(empresa.getTenatID() + empresa.getTipoConta() + DataUtil.dateToString(new Date(), "ddMM") + gerarCodigoNumerico());

        pagamento.getShipping().setAddressRequired(false);

        pagamento.getShipping().setCost(0d);

        if (dados.getCartao() != null) {
            pagamento.setCreditCard(new Payment.CreditCard());
            pagamento.getCreditCard().setToken(dados.getCartao().getToken());

            pagamento.getCreditCard().getInstallment().setNoInterestInstallmentQuantity(3);
            pagamento.getCreditCard().getInstallment().setQuantity(1);
            pagamento.getCreditCard().getInstallment().setValue(dados.getCartao().getInstallment());

            pagamento.getCreditCard().getHolder().setName(dados.getCartao().getNome());
            pagamento.getCreditCard().getHolder().getDocuments().addDocument(dados.getFatura().getCpfCnpj());
            if (pagamento.getCreditCard().getHolder().getDocuments().getDocument().stream().noneMatch(doc -> doc.getType().equals("CPF"))) {
                pagamento.getCreditCard().getHolder().getDocuments().addDocument(dados.getCartao().getCpfTitular());
            }
            pagamento.getCreditCard().getHolder().setBirthDate(DataUtil.dateToString(dados.getCartao().getNascimentoTitular()));
            pagamento.getCreditCard().getHolder().getPhone().setPhone(empresa.getFone());

            pagamento.getCreditCard().getBillingAddress().setStreet(dados.getFatura().getEndereco().getRua());
            pagamento.getCreditCard().getBillingAddress().setNumber(dados.getFatura().getEndereco().getNumero());
            pagamento.getCreditCard().getBillingAddress().setComplement(dados.getFatura().getEndereco().getComplemento());
            pagamento.getCreditCard().getBillingAddress().setDistrict(dados.getFatura().getEndereco().getBairro());
            pagamento.getCreditCard().getBillingAddress().setCity(dados.getFatura().getEndereco().getCidade());
            pagamento.getCreditCard().getBillingAddress().setState(dados.getFatura().getEndereco().getUf());
            pagamento.getCreditCard().getBillingAddress().setCountry("BRA");
            pagamento.getCreditCard().getBillingAddress().setPostalCode(dados.getFatura().getEndereco().getCep().replaceAll("\\D", ""));
        }

        return pagamento;
    }

}
