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
@Table(name = "residuo")
@NamedQueries({
    @NamedQuery(name = "ResiduoEntity.findAll", query = "SELECT r FROM ResiduoEntity r"),
    @NamedQuery(name = "ResiduoEntity.findByIdResiduo", query = "SELECT r FROM ResiduoEntity r WHERE r.idResiduo = :idResiduo"),
    @NamedQuery(name = "ResiduoEntity.findByTipo", query = "SELECT r FROM ResiduoEntity r WHERE r.tipo = :tipo"),
    @NamedQuery(name = "ResiduoEntity.findByObservacion", query = "SELECT r FROM ResiduoEntity r WHERE r.observacion = :observacion")})
public class ResiduoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idResiduo")
    private Integer idResiduo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "observacion")
    private String observacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "residuo")
    private Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection;

    public ResiduoEntity() {
    }

    public ResiduoEntity(Integer idResiduo) {
        this.idResiduo = idResiduo;
    }

    public ResiduoEntity(Integer idResiduo, String tipo, String observacion) {
        this.idResiduo = idResiduo;
        this.tipo = tipo;
        this.observacion = observacion;
    }

    public Integer getIdResiduo() {
        return idResiduo;
    }

    public void setIdResiduo(Integer idResiduo) {
        this.idResiduo = idResiduo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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
        hash += (idResiduo != null ? idResiduo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResiduoEntity)) {
            return false;
        }
        ResiduoEntity other = (ResiduoEntity) object;
        if ((this.idResiduo == null && other.idResiduo != null) || (this.idResiduo != null && !this.idResiduo.equals(other.idResiduo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ResiduoEntity[ idResiduo=" + idResiduo + " ]";
    }
    
}
