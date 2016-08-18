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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "permisos")
@NamedQueries({
    @NamedQuery(name = "PermisoEntity.findAll", query = "SELECT p FROM PermisoEntity p"),
    @NamedQuery(name = "PermisoEntity.findByIdMod", query = "SELECT p FROM PermisoEntity p WHERE p.idMod = :idMod"),
    @NamedQuery(name = "PermisoEntity.findByNombre", query = "SELECT p FROM PermisoEntity p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "PermisoEntity.findByDescripcion", query = "SELECT p FROM PermisoEntity p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PermisoEntity.findByUrl", query = "SELECT p FROM PermisoEntity p WHERE p.url = :url")})
public class PermisoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idMod")
    private Integer idMod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 64)
    @Column(name = "url")
    private String url;
    @JoinTable(name = "rolespermiso", joinColumns = {
        @JoinColumn(name = "permisos_idMod", referencedColumnName = "idMod")}, inverseJoinColumns = {
        @JoinColumn(name = "roles_idRol", referencedColumnName = "idRol")})
    @ManyToMany
    private Collection<RolEntity> rolEntityCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permisosidMod")
    private Collection<PermisoEntity> permisoEntityCollection;
    @JoinColumn(name = "permisos_idMod", referencedColumnName = "idMod")
    @ManyToOne(optional = false)
    private PermisoEntity permisosidMod;

    public PermisoEntity() {
    }

    public PermisoEntity(Integer idMod) {
        this.idMod = idMod;
    }

    public PermisoEntity(Integer idMod, String nombre) {
        this.idMod = idMod;
        this.nombre = nombre;
    }

    public Integer getIdMod() {
        return idMod;
    }

    public void setIdMod(Integer idMod) {
        this.idMod = idMod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Collection<RolEntity> getRolEntityCollection() {
        return rolEntityCollection;
    }

    public void setRolEntityCollection(Collection<RolEntity> rolEntityCollection) {
        this.rolEntityCollection = rolEntityCollection;
    }

    public Collection<PermisoEntity> getPermisoEntityCollection() {
        return permisoEntityCollection;
    }

    public void setPermisoEntityCollection(Collection<PermisoEntity> permisoEntityCollection) {
        this.permisoEntityCollection = permisoEntityCollection;
    }

    public PermisoEntity getPermisosidMod() {
        return permisosidMod;
    }

    public void setPermisosidMod(PermisoEntity permisosidMod) {
        this.permisosidMod = permisosidMod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMod != null ? idMod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoEntity)) {
            return false;
        }
        PermisoEntity other = (PermisoEntity) object;
        if ((this.idMod == null && other.idMod != null) || (this.idMod != null && !this.idMod.equals(other.idMod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.PermisoEntity[ idMod=" + idMod + " ]";
    }
    
}
