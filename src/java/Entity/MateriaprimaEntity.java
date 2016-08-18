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
@Table(name = "materiaprima")
@NamedQueries({
    @NamedQuery(name = "MateriaprimaEntity.findAll", query = "SELECT m FROM MateriaprimaEntity m"),
    @NamedQuery(name = "MateriaprimaEntity.findByIdMateriaPrima", query = "SELECT m FROM MateriaprimaEntity m WHERE m.idMateriaPrima = :idMateriaPrima"),
    @NamedQuery(name = "MateriaprimaEntity.findByNombreMateriaPrima", query = "SELECT m FROM MateriaprimaEntity m WHERE m.nombreMateriaPrima = :nombreMateriaPrima")})
public class MateriaprimaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMateriaPrima")
    private Integer idMateriaPrima;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreMateriaPrima")
    private String nombreMateriaPrima;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materiaprimaEntity")
    private Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollection;

    public MateriaprimaEntity() {
    }

    public MateriaprimaEntity(Integer idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
    }

    public MateriaprimaEntity(Integer idMateriaPrima, String nombreMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
        this.nombreMateriaPrima = nombreMateriaPrima;
    }

    public Integer getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public void setIdMateriaPrima(Integer idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
    }

    public String getNombreMateriaPrima() {
        return nombreMateriaPrima;
    }

    public void setNombreMateriaPrima(String nombreMateriaPrima) {
        this.nombreMateriaPrima = nombreMateriaPrima;
    }

    public Collection<ProductomateriaprimaEntity> getProductomateriaprimaEntityCollection() {
        return productomateriaprimaEntityCollection;
    }

    public void setProductomateriaprimaEntityCollection(Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollection) {
        this.productomateriaprimaEntityCollection = productomateriaprimaEntityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMateriaPrima != null ? idMateriaPrima.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriaprimaEntity)) {
            return false;
        }
        MateriaprimaEntity other = (MateriaprimaEntity) object;
        if ((this.idMateriaPrima == null && other.idMateriaPrima != null) || (this.idMateriaPrima != null && !this.idMateriaPrima.equals(other.idMateriaPrima))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.MateriaprimaEntity[ idMateriaPrima=" + idMateriaPrima + " ]";
    }
    
}
