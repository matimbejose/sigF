package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InutilizacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date ano;

    private String cnpj;

    private String uf;

    private String modelo;

    private String serie;

    private String numeroInicial;

    private String numeroFinal;

    private String motivo;

    public InutilizacaoDTO(Date ano, String cnpj, String uf) {
        this.ano = ano;
        this.cnpj = cnpj;
        this.uf = uf;
    }

    /**
     * Retorna o ID de inutilização no padrão da SEFAZ
     *
     * @return "ID" + uf + ano[2 dígitos] + cnpj + modelo + serie + numeroInicial + numeroFinal
     */
    public String getId() {
        return "ID" + uf + DataUtil.dateToString(ano, "yy") + cnpj.replaceAll("[\\./-]", "") + modelo
                + StringUtils.leftPad(serie, 3, "0")
                + StringUtils.leftPad(numeroInicial, 9, "0")
                + StringUtils.leftPad(numeroFinal, 9, "0");
    }

}
