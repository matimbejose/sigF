package br.com.villefortconsulting.sgfinancas.aud;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@RevisionEntity(AuditoriaListener.class)
@Table(name = "REVINFO")
public class CustomRevisionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVTYPE")
    private int id;

    @Column(name = "USUARIO")
    private String usuario;

    @Column(name = "DATA_ATUALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @Column(name = "IP")
    private String ip;

    @RevisionTimestamp
    @Column(name = "TIMESTAMP")
    private long timestamp;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
