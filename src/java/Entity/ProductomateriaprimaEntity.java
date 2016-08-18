/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ponchis
 */
@Entity
@Table(name = "productomateriaprima")
@NamedQueries({
    @NamedQuery(name = "ProductomateriaprimaEntity.findAll", query = "SELECT p FROM ProductomateriaprimaEntity p"),
    @NamedQuery(name = "ProductomateriaprimaEntity.findByProductoId", query = "SELECT p FROM ProductomateriaprimaEntity p WHERE p.productomateriaprimaEntityPK.productoId = :productoId"),
    @NamedQuery(name = "ProductomateriaprimaEntity.findByMateriaPrimaId", query = "SELECT p FROM ProductomateriaprimaEntity p WHERE p.productomateriaprimaEntityPK.materiaPrimaId = :materiaPrimaId"),
    @NamedQuery(name = "ProductomateriaprimaEntity.findByCantidad", query = "SELECT p FROM ProductomateriaprimaEntity p WHERE p.cantidad = :cantidad")})
public class ProductomateriaprimaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductomateriaprimaEntityPK productomateriaprimaEntityPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "materiaPrimaId", referencedColumnName = "idMateriaPrima", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MateriaprimaEntity materiaprimaEntity;
    @JoinColumn(name = "productoId", referencedColumnName = "idProducto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductoEntity productoEntity;

    public ProductomateriaprimaEntity() {
    }

    public ProductomateriaprimaEntity(ProductomateriaprimaEntityPK productomateriaprimaEntityPK) {
        this.productomateriaprimaEntityPK = productomateriaprimaEntityPK;
    }

    public ProductomateriaprimaEntity(ProductomateriaprimaEntityPK productomateriaprimaEntityPK, int cantidad) {
        this.productomateriaprimaEntityPK = productomateriaprimaEntityPK;
        this.cantidad = cantidad;
    }

    public ProductomateriaprimaEntity(int productoId, int materiaPrimaId) {
        this.productomateriaprimaEntityPK = new ProductomateriaprimaEntityPK(productoId, materiaPrimaId);
    }

    public ProductomateriaprimaEntityPK getProductomateriaprimaEntityPK() {
        return productomateriaprimaEntityPK;
    }

    public void setProductomateriaprimaEntityPK(ProductomateriaprimaEntityPK productomateriaprimaEntityPK) {
        this.productomateriaprimaEntityPK = productomateriaprimaEntityPK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public MateriaprimaEntity getMateriaprimaEntity() {
        return materiaprimaEntity;
    }

    public void setMateriaprimaEntity(MateriaprimaEntity materiaprimaEntity) {
        this.materiaprimaEntity = materiaprimaEntity;
    }

    public ProductoEntity getProductoEntity() {
        return productoEntity;
    }

    public void setProductoEntity(ProductoEntity productoEntity) {
        this.productoEntity = productoEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productomateriaprimaEntityPK != null ? productomateriaprimaEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductomateriaprimaEntity)) {
            return false;
        }
        ProductomateriaprimaEntity other = (ProductomateriaprimaEntity) object;
        if ((this.productomateriaprimaEntityPK == null && other.productomateriaprimaEntityPK != null) || (this.productomateriaprimaEntityPK != null && !this.productomateriaprimaEntityPK.equals(other.productomateriaprimaEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ProductomateriaprimaEntity[ productomateriaprimaEntityPK=" + productomateriaprimaEntityPK + " ]";
    }
    
}
