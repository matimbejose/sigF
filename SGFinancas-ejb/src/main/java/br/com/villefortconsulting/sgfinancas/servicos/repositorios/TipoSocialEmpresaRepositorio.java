package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.TipoSocialEmpresa;
import java.util.List;
import javax.persistence.EntityManager;

public class TipoSocialEmpresaRepositorio extends BasicRepository<TipoSocialEmpresa> {

    public TipoSocialEmpresaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<TipoSocialEmpresa> getTipoSocialEmpresa() {
        return getPureList("select te from TipoSocialEmpresa te");
    }

}
