package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioCadastroDTO;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class HomologService {

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ServicosWebService service;

    @EJB
    private AdministracaoService administracaoService;

    @EJB
    private CategoriaEmpresaService categoriaEmpresaService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private UsuarioService usuarioService;

    @PostConstruct
    public void init() {
        if (!"RELEASE".equalsIgnoreCase(SystemPreferencesUtil.getProp("ambiente", "TESTE"))) {
            return;
        }
        try (final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("carregaTabelasBasicas.sql");
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String s;
            do {
                StringBuilder sb = new StringBuilder();
                s = bufferedReader.readLine();
                if (s != null) {
                    sb.append(s);
                    if (s.endsWith(";")) {
                        administracaoService.executaAtualizaoByNativeQuery(sb.toString());
                    }
                }
            } while (s != null);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }

        cadastrarEmpresa("33491592000140", "(31) 98765-4321", "30.140-910", 109, "Thiago");
        cadastrarUsuario("Suporte", "suporte", "e7d80ffeefa212b7c5c55700e4f7193e");
        cadastrarUsuario("Dev", "humberto.carvalho", "dbab4df3e5935d8be13a11327b15ccde");
        cadastrarUsuario("Dev", "emanuel.ribeiro", "68751f899e5fd4262113032957e20766");
        cadastrarUsuario("Dev", "caio.cassemiro", "0192023a7bbd73250516f069df18b500");
        cadastrarUsuario("Dev", "christopher.sarmento", "0192023a7bbd73250516f069df18b500");
        cadastrarEmpresa("12463506000116", "(31) 98765-4321", "05.882-050", 500, "Christopher");
        cadastrarEmpresa("11462614000101", "(31) 98765-4321", "28.470-000", 100, "Wilson");
        cadastrarEmpresa("13246424000182", "(31) 98765-4321", "06.696-110", 666, "Alberto");
        cadastrarEmpresa("33136637000169", "(31) 98765-4321", "30.720-430", 304, "Adriane");
        Empresa empresa = empresaService.getEmpresa();
        empresa.setIdCategoriaEmpresa(categoriaEmpresaService.buscarPorNome("Oficina mecânica"));
        empresaService.salvar(empresa);

        adHocTenant.setTenantID(null);
        usuarioService.listar().stream()
                .filter(user -> user.getNome().equals("Dev"))
                .map(user -> {
                    user.setMenuMode("dev");
                    user.setTema("light");
                    user.setAdministrator(true);
                    return user;
                })
                .forEach(usuarioService::salvar);
    }

    private EmpresaDTO cadastrarEmpresa(String cnpj, String telefone, String cep, int numero, String responsavel) {
        EmpresaCadastroDTO dto = EmpresaCadastroDTO.builder()
                .cpfCnpj(cnpj).nomeResponsavel(responsavel)
                .telefone(telefone).cep(cep).numero(String.valueOf(numero))
                .build();
        dto.setEmailResponsavel(dto.getNomeResponsavel().toLowerCase() + "@villefortconsulting.com");
        dto.setSenhaResponsavel("senha123");
        CepDTO cepDTO = cidadeService.getEnderecoPorCep(dto.getCep());
        dto.setCodCidade(cepDTO.getCidade().getCodigoIBGE());
        dto.setEndereco(cepDTO.getEndereco());
        dto.setBairro(cepDTO.getBairro());
        EmpresaDTO empresa = (EmpresaDTO) service.cadastrarEmpresa(dto);
        logarComTenant(empresa.getId().toString());
        return empresa;
    }

    private Integer cadastrarUsuario(String nome, String login, String senha) {
        return (Integer) service.cadastrarUsuario(new UsuarioCadastroDTO(nome, login, senha, "AD"));
    }

    private void logarComTenant(String tenant) {
        usuarioService.logarComTenant(tenant);
        adHocTenant.setTenantID(tenant);
    }

}
