package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.TokenApp;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TokenAppFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.TokenAppRepositorio;
import br.com.villefortconsulting.util.AESUtil;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Service;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Service
public class TokenAppService extends BasicService<TokenApp, TokenAppRepositorio, TokenAppFiltro> {

    private static final long serialVersionUID = 1L;

    private static final String SECRET_KEY = "5986-623-693-7823";

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new TokenAppRepositorio(em);
    }

    public TokenApp buscar(String token) {
        return repositorio.buscar(token);
    }

    public TokenApp buscar(Usuario usuario, Empresa empresa, String uuid) {
        return repositorio.buscar(usuario, empresa, uuid);
    }

    public TokenApp buscar(Usuario usuario, String uuid) {
        return repositorio.buscar(usuario, uuid);
    }

    public List<TokenApp> listar(Usuario usuario) {
        return repositorio.listar(usuario);
    }

    public List<TokenApp> listar(Empresa empresa) {
        return repositorio.listar(empresa);
    }

    public List<TokenApp> listar(Usuario usuario, Empresa empresa) {
        return repositorio.listar(usuario, empresa);
    }

    @Override
    public Criteria getModel(TokenAppFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public String generateToken(Usuario usuario, Empresa empresa, String deviceId) {
        TokenApp token = buscar(usuario, deviceId);

        if (token != null) {
            remover(token);
        }

        token = new TokenApp();
        token.setIdUsuario(usuario);
        token.setIdEmpresa(empresa);
        token.setDeviceUuid(deviceId);
        try {
            if (empresa == null) {
                empresa = new Empresa();
            }
            token.setToken(AESUtil.encrypt(usuario.getId() + ":" + empresa.getId() + ":" + deviceId, SECRET_KEY));
        } catch (Exception ex) {
            Logger.getLogger(TokenAppService.class.getName()).log(Level.SEVERE, null, ex);
        }
        token.setData(new Date());
        salvar(token);

        return token.getToken();
    }

    public String updateToken(Usuario usuario, Empresa newEmpresa) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        TokenApp token = buscar(usuario, usuario.getUuid());
        token.setIdEmpresa(newEmpresa);
        token.setToken(AESUtil.encrypt(usuario.getId() + ":" + newEmpresa.getId() + ":" + usuario.getUuid(), SECRET_KEY));
        token.setData(new Date());
        salvar(token);
        return token.getToken();
    }

    public String[] getTokenData(String token) {
        try {
            return AESUtil.decrypt(token, SECRET_KEY).split(":");
        } catch (Exception ex) {
            Logger.getLogger(TokenAppService.class.getName()).log(Level.SEVERE, null, ex);
            return new String[]{};
        }
    }

}
