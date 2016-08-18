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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "defectuosos")
@NamedQueries({
    @NamedQuery(name = "DefectuosoEntity.findAll", query = "SELECT d FROM DefectuosoEntity d"),
    @NamedQuery(name = "DefectuosoEntity.findByIdDefectuoso", query = "SELECT d FROM DefectuosoEntity d WHERE d.idDefectuoso = :idDefectuoso"),
    @NamedQuery(name = "DefectuosoEntity.findByCausa", query = "SELECT d FROM DefectuosoEntity d WHERE d.causa = :causa"),
    @NamedQuery(name = "DefectuosoEntity.findByDescripcion", query = "SELECT d FROM DefectuosoEntity d WHERE d.descripcion = :descripcion")})
public class DefectuosoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDefectuoso")
    private Integer idDefectuoso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "causa")
    private String causa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "defectuosos")
    private Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection;

    public DefectuosoEntity() {
    }

    public DefectuosoEntity(Integer idDefectuoso) {
        this.idDefectuoso = idDefectuoso;
    }

    public DefectuosoEntity(Integer idDefectuoso, String causa, String descripcion) {
        this.idDefectuoso = idDefectuoso;
        this.causa = causa;
        this.descripcion = descripcion;
    }

    public Integer getIdDefectuoso() {
        return idDefectuoso;
    }

    public void setIdDefectuoso(Integer idDefectuoso) {
        this.idDefectuoso = idDefectuoso;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<OrdenesproduccionEntity> getOrdenesproduccionEntityCollection() {
        return ordenesproduccionEntityCollection;
    }

    public void setOrdenesproduccionEntityCollection(Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection) {
        this.ordenesproduccionEntityCollection = ordenesproduccionEntityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDefectuoso != null ? idDefectuoso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DefectuosoEntity)) {
            return false;
        }
        DefectuosoEntity other = (DefectuosoEntity) object;
        if ((this.idDefectuoso == null && other.idDefectuoso != null) || (this.idDefectuoso != null && !this.idDefectuoso.equals(other.idDefectuoso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DefectuosoEntity[ idDefectuoso=" + idDefectuoso + " ]";
    }
    
}
