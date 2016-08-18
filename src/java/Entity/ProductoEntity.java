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
@Table(name = "productos")
@NamedQueries({
    @NamedQuery(name = "ProductoEntity.findAll", query = "SELECT p FROM ProductoEntity p"),
    @NamedQuery(name = "ProductoEntity.findByIdProducto", query = "SELECT p FROM ProductoEntity p WHERE p.idProducto = :idProducto"),
    @NamedQuery(name = "ProductoEntity.findByNombreProducto", query = "SELECT p FROM ProductoEntity p WHERE p.nombreProducto = :nombreProducto"),
    @NamedQuery(name = "ProductoEntity.findByTipoProducto", query = "SELECT p FROM ProductoEntity p WHERE p.tipoProducto = :tipoProducto"),
    @NamedQuery(name = "ProductoEntity.findByCaracteristicas", query = "SELECT p FROM ProductoEntity p WHERE p.caracteristicas = :caracteristicas")})
public class ProductoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idProducto")
    private Integer idProducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombreProducto")
    private String nombreProducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "tipoProducto")
    private String tipoProducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "caracteristicas")
    private String caracteristicas;
    @ManyToMany(mappedBy = "productoEntityCollection")
    private Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection;
    @JoinTable(name = "maquinaproducto", joinColumns = {
        @JoinColumn(name = "maquinaId", referencedColumnName = "idProducto")}, inverseJoinColumns = {
        @JoinColumn(name = "productoId", referencedColumnName = "idMaquina")})
    @ManyToMany
    private Collection<MaquinaEntity> maquinaEntityCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoEntity")
    private Collection<ProductomateriaprimaEntity> productomateriaprimaEntityCollection;

    public ProductoEntity() {
    }

    public ProductoEntity(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public ProductoEntity(Integer idProducto, String nombreProducto, String tipoProducto, String caracteristicas) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.tipoProducto = tipoProducto;
        this.caracteristicas = caracteristicas;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public Collection<OrdenesproduccionEntity> getOrdenesproduccionEntityCollection() {
        return ordenesproduccionEntityCollection;
    }

    public void setOrdenesproduccionEntityCollection(Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection) {
        this.ordenesproduccionEntityCollection = ordenesproduccionEntityCollection;
    }

    public Collection<MaquinaEntity> getMaquinaEntityCollection() {
        return maquinaEntityCollection;
    }

    public void setMaquinaEntityCollection(Collection<MaquinaEntity> maquinaEntityCollection) {
        this.maquinaEntityCollection = maquinaEntityCollection;
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
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoEntity)) {
            return false;
        }
        ProductoEntity other = (ProductoEntity) object;
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ProductoEntity[ idProducto=" + idProducto + " ]";
    }
    
}
