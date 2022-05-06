package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import java.util.List;
import javax.persistence.EntityManager;

public class ModuloRepositorio extends BasicRepository<Modulo> {

    public ModuloRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Modulo> list() {
        return getPureList("select m from Modulo m where m.ativo = 'S' order by m.nome");
    }

    public Double valorAdesaoObrigatorio() {
        return getPurePojo("select sum(m.valorAdesao) from Modulo m where m.obrigatorio = 'S'");
    }

    public Double valorMensalidadeObrigatorio() {
        return getPurePojo("select sum(m.valorMensalidade) from Modulo m where m.obrigatorio = 'S'");
    }

    public List<Permissao> permissoesPor(Modulo modulo) {
        return getPureList("select mp.idPermissao from ModuloPermissao mp where mp.idModulo = ?1", modulo);
    }

}
