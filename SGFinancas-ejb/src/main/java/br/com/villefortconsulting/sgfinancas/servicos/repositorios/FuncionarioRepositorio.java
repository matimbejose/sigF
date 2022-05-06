package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioAtendimento;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioFerias;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class FuncionarioRepositorio extends BasicRepository<Funcionario> {

    public FuncionarioRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public FuncionarioRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Funcionario> listar() {
        String jpql = "select c from Funcionario c order by c.nome";
        return getPureList(jpql);
    }

    public List<Funcionario> listar(Usuario usuario) {
        String jpql = "select b from Usuario a join a.idFuncionario b where a = ?1 and b.tenatID = ?2 order by b.nome";
        return getPureList(jpql, usuario, adHocTenant.getTenantID());
    }

    public List<Funcionario> listarSemUsuario() {
        String jpql = "select c from Funcionario c where c not in (select distinct tenatID.idFuncionario from Usuario tenatID where tenatID.idFuncionario is not null) and c.tenatID = ?1 order by c.nome";
        return getPureList(jpql, adHocTenant.getTenantID());
    }

    public List<Funcionario> listarFuncionariosAtivos(Date competencia) {
        String jpql = " select c from Funcionario c ";
        jpql += " where c.tenatID = ?4 ";
        jpql += " and ( c.dataDemissao is null or ( ";
        jpql += " month(c.dataDemissao) = ?1 and year(c.dataDemissao) = ?2 ) or  ";
        jpql += " c.dataDemissao > ?3 ) ";
        jpql += " order by c.nome ";
        return getPureList(jpql, DataUtil.getMes(competencia), DataUtil.getAno(competencia), competencia, adHocTenant.getTenantID());
    }

    public List<Funcionario> listarFuncionariosAtivosVendedores() {
        String jpql = " select f from Funcionario f ";
        jpql += " where f.tenatID = ?1 ";
        jpql += " and f.dataDemissao is null ";
        jpql += " and f.ehOrcamentista = 'S' ";
        jpql += " order by c.nome ";
        return getPureList(jpql, adHocTenant.getTenantID());
    }

    public Funcionario buscarFuncionarioPorMatricula(String matricula) {
        String jpql = "select f from Funcionario f where f.matricula = ?1 and tenatID = ?2 ";
        return getPurePojo(jpql, matricula, adHocTenant.getTenantID());
    }

    public boolean possuiFuncionarioComMatricula(String matricula) {
        String jpql = "select c.id from Funcionario c where c.matricula = ?1";
        return getPurePojo(jpql, matricula) != null;
    }

    public boolean possuiFuncionarioComCpf(String cpf) {
        String jpql = "select c.id from Funcionario c where c.cpf = ?1 and c.ativo = 'S'";
        return getPurePojo(jpql, cpf) != null;
    }

    public boolean possuiFuncionarioComEmail(String email) {
        String jpql = "select c.id from Funcionario c where c.email = ?1 and c.ativo = 'S'";
        return getPurePojoTop1(jpql, email) != null;
    }

    public boolean verificaFuncionatioEstaFerias(Funcionario funcionario, Date data) {
        String jpql = "select c.id from FuncionarioFerias c where c.idFuncionario = ?1 and ?2 >= c.dataInicio and ?2 <= c.dataFim ";
        return getPurePojoTop1(jpql, funcionario, data) != null;
    }

    public FuncionarioFerias adicionarFuncionarioFerias(FuncionarioFerias funcionario) {
        addEntity(funcionario);
        return funcionario;
    }

    public FuncionarioFerias alterarFuncionarioFerias(FuncionarioFerias funcionario) {
        return setEntity(funcionario);
    }

    public void removerFuncionarioFerias(FuncionarioFerias funcionario) {
        removeEntity(funcionario);
    }

    public FuncionarioFerias buscarFuncionarioFerias(int id) {
        return getEntity(FuncionarioFerias.class, id);
    }

    public List<FuncionarioFerias> listarFuncionarioFerias() {
        String jpql = "select ff from FuncionarioFerias ff";
        return getPureList(jpql);
    }

    public List<FuncionarioServico> listarFuncionarioServicoByIdFuncionario(Funcionario funcionario) {
        String jpql = "select fs from FuncionarioServico fs where fs.idFuncionario = ?1";
        return getPureList(jpql, funcionario);
    }

    public List<FuncionarioAtendimento> listarFuncionarioAtendimentoByIdFuncionario(Funcionario funcionario) {
        String jpql = "select fa from FuncionarioAtendimento fa where fa.idFuncionario = ?1";
        return getPureList(jpql, funcionario);
    }

}
