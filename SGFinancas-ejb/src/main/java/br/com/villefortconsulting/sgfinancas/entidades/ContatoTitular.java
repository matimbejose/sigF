package br.com.villefortconsulting.sgfinancas.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONTATO_TITULAR")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContatoTitular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CELULAR")
    private String celular;

    @Column(name = "TEL_FIXO")
    private String telefoneFixo;

    @Column(name = "RAMAL")
    private String ramal;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "SITE_TITULAR")
    private String site;

    //-- Endereco
    @Basic(optional = false)
    @Column(name = "RUA")
    private String ruaEndereco; //

    @Basic(optional = false)
    @Column(name = "NUMERO")
    private Integer numeroEndereco;//

    @Basic(optional = false)
    @Column(name = "CEP")
    private String cepEndereco;//

    @Basic(optional = false)
    @Column(name = "BAIRRO")
    private String bairroEndereco;//

    @Basic(optional = false)
    @Column(name = "CIDADE")
    private String cidadeEndereco;//

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getRamal() {
        return ramal;
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getRuaEndereco() {
        return ruaEndereco;
    }

    public void setRuaEndereco(String ruaEndereco) {
        this.ruaEndereco = ruaEndereco;
    }

    public Integer getNumeroEndereco() {
        return numeroEndereco;
    }

    public void setNumeroEndereco(Integer numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public String getCepEndereco() {
        return cepEndereco;
    }

    public void setCepEndereco(String cepEndereco) {
        this.cepEndereco = cepEndereco;
    }

    public String getBairroEndereco() {
        return bairroEndereco;
    }

    public void setBairroEndereco(String bairroEndereco) {
        this.bairroEndereco = bairroEndereco;
    }

    public String getCidadeEndereco() {
        return cidadeEndereco;
    }

    public void setCidadeEndereco(String cidadeEndereco) {
        this.cidadeEndereco = cidadeEndereco;
    }

}
