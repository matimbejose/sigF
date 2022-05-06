package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.TokenApp;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;

public class TokenAppRepositorio extends BasicRepository<TokenApp> {

    public TokenAppRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public TokenApp buscar(String token) {
        return getPurePojoTop1(getNamedQuery("findByToken"), token);
    }

    public TokenApp buscar(Usuario usuario, Empresa empresa, String uuid) {
        if (empresa == null) {
            return getPurePojoTop1(getNamedQuery("findByUsuarioAndUuidAndEmpresaIsNull"), usuario, uuid);
        }
        return getPurePojoTop1(getNamedQuery("findByUsuarioAndEmpresaAndUuid"), usuario, empresa, uuid);
    }

    public TokenApp buscar(Usuario usuario, String uuid) {
        if (uuid == null) {
            return getPurePojoTop1(getNamedQuery("findByUsuario"), usuario);
        }
        return getPurePojoTop1(getNamedQuery("findByUsuarioAndUuid"), usuario, uuid);
    }

    public List<TokenApp> listar(Usuario usuario) {
        return getPureList(getNamedQuery("listByUsuario"), usuario);
    }

    public List<TokenApp> listar(Empresa empresa) {
        return getPureList(getNamedQuery("listByEmpresa"), empresa);
    }

    public List<TokenApp> listar(Usuario usuario, Empresa empresa) {
        return getPureList(getNamedQuery("listByUsuarioAndEmpresa"), usuario, empresa);
    }

}
