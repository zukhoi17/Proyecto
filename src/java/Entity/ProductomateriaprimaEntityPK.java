/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ponchis
 */
@Embeddable
public class ProductomateriaprimaEntityPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "productoId")
    private int productoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "materiaPrimaId")
    private int materiaPrimaId;

    public ProductomateriaprimaEntityPK() {
    }

    public ProductomateriaprimaEntityPK(int productoId, int materiaPrimaId) {
        this.productoId = productoId;
        this.materiaPrimaId = materiaPrimaId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getMateriaPrimaId() {
        return materiaPrimaId;
    }

    public void setMateriaPrimaId(int materiaPrimaId) {
        this.materiaPrimaId = materiaPrimaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) productoId;
        hash += (int) materiaPrimaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductomateriaprimaEntityPK)) {
            return false;
        }
        ProductomateriaprimaEntityPK other = (ProductomateriaprimaEntityPK) object;
        if (this.productoId != other.productoId) {
            return false;
        }
        if (this.materiaPrimaId != other.materiaPrimaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ProductomateriaprimaEntityPK[ productoId=" + productoId + ", materiaPrimaId=" + materiaPrimaId + " ]";
    }
    
}
