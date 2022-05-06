package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.CategoriaEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacote;
import java.util.List;
import javax.persistence.EntityManager;

public class ModuloPacoteRepositorio extends BasicRepository<ModuloPacote> {

    public ModuloPacoteRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<ModuloPacote> list() {
        return getPureList("select mp from ModuloPacote mp where mp.ativo = 'S' order by mp.nome");
    }

    public ModuloPacote buscarPor(CategoriaEmpresa categoriaEmpresa) {
        return getPurePojoTop1("select mp from ModuloPacote mp where mp.idCategoriaEmpresa = ?1 and mp.dataVencimento <= current_date()", categoriaEmpresa);
    }

}
