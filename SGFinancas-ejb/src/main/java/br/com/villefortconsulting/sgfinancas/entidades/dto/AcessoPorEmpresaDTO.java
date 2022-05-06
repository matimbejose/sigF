package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcessoPorEmpresaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEmpresa;

    private int idUsuario;

    private String empresa;

    private String usuario;

    private String perfil;

    private int mes;

    private int ano;

    private double valor;

    private long quantidade;

    private boolean fix = false;

    public AcessoPorEmpresaDTO(PagamentoMensalidade pm) {
        this.idEmpresa = pm.getIdEmpresa().getId();
        this.idUsuario = pm.getIdUsuarioPagamento().getId();
        this.empresa = pm.getIdEmpresa().getDescricao();
        this.usuario = pm.getIdUsuarioPagamento().getNome();
        this.perfil = pm.getIdUsuarioPagamento().getIdPerfil().getDescricao();
        this.mes = DataUtil.getMes(pm.getDataPagamento());
        this.ano = DataUtil.getAno(pm.getDataPagamento());
        this.valor = pm.getValorPago();
        this.quantidade = 0;
        this.fix = true;
    }

    public String getData() {
        return StringUtil.adicionarCaracterEsquerda("" + mes, "0", 2) + "/" + ano;
    }

    public void add(AcessoPorEmpresaDTO dto) {
        this.valor += dto.valor;
        this.quantidade += dto.quantidade;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.idEmpresa;
        hash = 59 * hash + this.idUsuario;
        hash = 59 * hash + this.mes;
        hash = 59 * hash + this.ano;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AcessoPorEmpresaDTO other = (AcessoPorEmpresaDTO) obj;
        if (this.idEmpresa != other.idEmpresa) {
            return false;
        }
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        return this.ano == other.ano;
    }

}
