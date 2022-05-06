package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "DOCUMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Inheritance
public class Documento extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NonNull
    @Column(name = "DESCRICAO")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDocumento", orphanRemoval = true)
    private List<DocumentoAnexo> documentoAnexoList = new ArrayList<>();

    public List<DocumentoAnexo> updateList() {
        for (int i = documentoAnexoList.size() - 1; i >= 0; i--) {
            DocumentoAnexo da = documentoAnexoList.get(i);
            boolean keep = da.getAtivo().equals("S");
            if (!keep) {
                da.setIdDocumento(null);
                documentoAnexoList.remove(da);
            }
        }
        return documentoAnexoList;
    }

    @Override
    public String toString() {
        return "Documento{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
