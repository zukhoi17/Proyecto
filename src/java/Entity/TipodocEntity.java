/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ponchis
 */
@Entity
@Table(name = "tipodoc")
@NamedQueries({
    @NamedQuery(name = "TipodocEntity.findAll", query = "SELECT t FROM TipodocEntity t"),
    @NamedQuery(name = "TipodocEntity.findByIdTipoDoc", query = "SELECT t FROM TipodocEntity t WHERE t.idTipoDoc = :idTipoDoc"),
    @NamedQuery(name = "TipodocEntity.findByDescripcion", query = "SELECT t FROM TipodocEntity t WHERE t.descripcion = :descripcion")})
public class TipodocEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "idTipoDoc")
    private String idTipoDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoDoc")
    private Collection<UsuarioEntity> usuarioEntityCollection;

    public TipodocEntity() {
    }

    public TipodocEntity(String idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public TipodocEntity(String idTipoDoc, String descripcion) {
        this.idTipoDoc = idTipoDoc;
        this.descripcion = descripcion;
    }

    public String getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(String idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<UsuarioEntity> getUsuarioEntityCollection() {
        return usuarioEntityCollection;
    }

    public void setUsuarioEntityCollection(Collection<UsuarioEntity> usuarioEntityCollection) {
        this.usuarioEntityCollection = usuarioEntityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoDoc != null ? idTipoDoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipodocEntity)) {
            return false;
        }
        TipodocEntity other = (TipodocEntity) object;
        if ((this.idTipoDoc == null && other.idTipoDoc != null) || (this.idTipoDoc != null && !this.idTipoDoc.equals(other.idTipoDoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.TipodocEntity[ idTipoDoc=" + idTipoDoc + " ]";
    }
    
}
