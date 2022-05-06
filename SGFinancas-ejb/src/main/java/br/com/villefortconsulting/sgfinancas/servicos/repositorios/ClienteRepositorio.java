package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteContato;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;

public class ClienteRepositorio extends BasicRepository<Cliente> {

    public ClienteRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ClienteRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Cliente buscarPorCpfCnpj(String cpfCnpj) {
        String jpql = "select c from Cliente c where c.cpfCNPJ = ?1 and c.ativo = 'S' ";
        return getPurePojoTop1(jpql, cpfCnpj);
    }

    public List<Cliente> buscarPor(Usuario usuario) {
        String jpql = "select c from Cliente c where c.idUsuario = ?1 and c.tenatID is not null";
        return getPureList(jpql, usuario);
    }

    @Override
    public List<Cliente> list() {
        String jpql = "select c from Cliente c "
                + " left join fetch c.idPlanoConta c1 "
                + " left join fetch c1.idContaPai  "
                + " left join fetch c1.idContaContrapartida  "
                + " left join fetch c.endereco.idCidade c2 "
                + " where ativo = 'S' "
                + " order by c.nome";
        return getPureList(jpql);
    }

    public List<Cliente> listar(String nome) {
        String jpql = "select c from Cliente c where c.nome = ?1 order by c.nome";
        return getPureList(jpql, nome);
    }

    public Cliente buscarCliente(String nome) {
        String jpql = "select c from Cliente c where c.nome = ?1 ";
        return getPurePojoTop1(jpql, nome);
    }

    public List<Cliente> listarSeguradores() {
        String jpql = "select c from Cliente c where c.seguradora = 'S' order by c.nome";
        return getPureList(jpql);
    }

    public List<Cliente> listarPessoa(String nome) {
        nome = "%" + nome + "%";
        String jpql = "select c from Cliente c where c.nome like ?1 order by c.nome";
        return getPureList(jpql, nome);
    }

    public List<ClienteContato> listarClienteContato() {
        String jpql = "select cc from ClienteContato cc order by cc.nome";
        return getPureList(jpql);
    }

    public List<Cliente> listarClientePorEmpresa(Empresa empresa) {
        String jpql = "select c from Cliente c where c.tenatID =?1 order by c.nome";
        return getPureList(jpql, empresa.getTenatID());
    }

    public List<Cliente> listarClienteAtivoPorEmpresa(Empresa empresa) {
        String jpql = "select c from Cliente c where c.tenatID =?1 and c.ativo='S' order by c.nome";
        return getPureList(jpql, empresa.getTenatID());
    }

    public List<ClienteContato> listarClienteContato(Cliente cliente) {
        String jpql = " select cc from ClienteContato cc where cc.idCliente = ?1 order by cc.nome ";
        return getPureList(jpql, cliente);
    }

    public List<Cliente> findByCpfCnpjNome(String cpfCNPJ, String nome) {
        String jpql = "select c from Cliente c where c.ativo = 'S' and (c.cpfCNPJ =?1 or upper(c.nome) = ?2)";
        return getPureList(jpql, cpfCNPJ, nome.toUpperCase());
    }

    public Cliente findByIdIntegracao(Long idIntegracao) {
        String jpql = "select c from Cliente c where c.ativo = 'S' and c.idIntegracao =?1";
        return getPurePojo(jpql, idIntegracao);
    }

    public List<Cliente> listarComIdIntegracao() {
        String jpql = "select c from Cliente c where c.ativo = 'S' and c.idIntegracao is not null";
        return getPureList(jpql);
    }

}
