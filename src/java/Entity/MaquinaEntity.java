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
@Table(name = "maquinas")
@NamedQueries({
    @NamedQuery(name = "MaquinaEntity.findAll", query = "SELECT m FROM MaquinaEntity m"),
    @NamedQuery(name = "MaquinaEntity.findByIdMaquina", query = "SELECT m FROM MaquinaEntity m WHERE m.idMaquina = :idMaquina"),
    @NamedQuery(name = "MaquinaEntity.findByNombreMaquina", query = "SELECT m FROM MaquinaEntity m WHERE m.nombreMaquina = :nombreMaquina"),
    @NamedQuery(name = "MaquinaEntity.findByTipoMaquina", query = "SELECT m FROM MaquinaEntity m WHERE m.tipoMaquina = :tipoMaquina"),
    @NamedQuery(name = "MaquinaEntity.findByTipoProceso", query = "SELECT m FROM MaquinaEntity m WHERE m.tipoProceso = :tipoProceso")})
public class MaquinaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMaquina")
    private Integer idMaquina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nombreMaquina")
    private String nombreMaquina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipoMaquina")
    private String tipoMaquina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipoProceso")
    private String tipoProceso;
    @ManyToMany(mappedBy = "maquinaEntityCollection")
    private Collection<ProductoEntity> productoEntityCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maquinasidMaquina")
    private Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection;

    public MaquinaEntity() {
    }

    public MaquinaEntity(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }

    public MaquinaEntity(Integer idMaquina, String nombreMaquina, String tipoMaquina, String tipoProceso) {
        this.idMaquina = idMaquina;
        this.nombreMaquina = nombreMaquina;
        this.tipoMaquina = tipoMaquina;
        this.tipoProceso = tipoProceso;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getNombreMaquina() {
        return nombreMaquina;
    }

    public void setNombreMaquina(String nombreMaquina) {
        this.nombreMaquina = nombreMaquina;
    }

    public String getTipoMaquina() {
        return tipoMaquina;
    }

    public void setTipoMaquina(String tipoMaquina) {
        this.tipoMaquina = tipoMaquina;
    }

    public String getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(String tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public Collection<ProductoEntity> getProductoEntityCollection() {
        return productoEntityCollection;
    }

    public void setProductoEntityCollection(Collection<ProductoEntity> productoEntityCollection) {
        this.productoEntityCollection = productoEntityCollection;
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
        hash += (idMaquina != null ? idMaquina.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaquinaEntity)) {
            return false;
        }
        MaquinaEntity other = (MaquinaEntity) object;
        if ((this.idMaquina == null && other.idMaquina != null) || (this.idMaquina != null && !this.idMaquina.equals(other.idMaquina))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.MaquinaEntity[ idMaquina=" + idMaquina + " ]";
    }
    
}
