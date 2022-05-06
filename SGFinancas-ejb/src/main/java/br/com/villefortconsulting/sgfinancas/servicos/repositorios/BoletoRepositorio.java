package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Boleto;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import java.util.List;
import javax.persistence.EntityManager;

public class BoletoRepositorio extends BasicRepository<Boleto> {

    public BoletoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public BoletoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Boleto> listar() {
        String jpql = "select b from Boleto b order by b.descricao";
        return getPureList(jpql);
    }

    public Long buscarNumeroViaBoleto(ContaParcela parcela) {
        String jpql = "select count(b.id) from Boleto b where b.idContaParcela = ?1 ";
        return getPurePojo(Long.class, jpql, parcela);
    }

    public Boleto buscarBoletoPorNossoNumero(String nossoNumero, ContaBancaria contaBancaria) {
        String jpql = "select b from Boleto b where b.nossoNumero like ?1 and b.idContaBancaria = ?2 ";
        return getPurePojo(jpql, "%" + nossoNumero, contaBancaria);
    }

}
