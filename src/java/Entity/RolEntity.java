/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ponchis
 */
@Entity
@Table(name = "roles")
@NamedQueries({
    @NamedQuery(name = "RolEntity.findAll", query = "SELECT r FROM RolEntity r"),
    @NamedQuery(name = "RolEntity.findByIdRol", query = "SELECT r FROM RolEntity r WHERE r.idRol = :idRol"),
    @NamedQuery(name = "RolEntity.findByNombreRol", query = "SELECT r FROM RolEntity r WHERE r.nombreRol = :nombreRol"),
    @NamedQuery(name = "RolEntity.findByDescripcion", query = "SELECT r FROM RolEntity r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "RolEntity.findByIdSubmodulo", query = "SELECT r FROM RolEntity r WHERE r.idSubmodulo = :idSubmodulo")})
public class RolEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRol")
    private Integer idRol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = " nombreRol")
    private String nombreRol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idSubmodulo")
    private int idSubmodulo;
    @ManyToMany(mappedBy = "rolEntityCollection")
    private Collection<PermisoEntity> permisoEntityCollection;
    @ManyToMany(mappedBy = "rolEntityCollection")
    private Collection<UsuarioEntity> usuarioEntityCollection;

    public RolEntity() {
    }

    public RolEntity(Integer idRol) {
        this.idRol = idRol;
    }

    public RolEntity(Integer idRol, String nombreRol, String descripcion, int idSubmodulo) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.idSubmodulo = idSubmodulo;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdSubmodulo() {
        return idSubmodulo;
    }

    public void setIdSubmodulo(int idSubmodulo) {
        this.idSubmodulo = idSubmodulo;
    }

    public Collection<PermisoEntity> getPermisoEntityCollection() {
        return permisoEntityCollection;
    }

    public void setPermisoEntityCollection(Collection<PermisoEntity> permisoEntityCollection) {
        this.permisoEntityCollection = permisoEntityCollection;
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
        hash += (idRol != null ? idRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolEntity)) {
            return false;
        }
        RolEntity other = (RolEntity) object;
        if ((this.idRol == null && other.idRol != null) || (this.idRol != null && !this.idRol.equals(other.idRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.RolEntity[ idRol=" + idRol + " ]";
    }
    
}
