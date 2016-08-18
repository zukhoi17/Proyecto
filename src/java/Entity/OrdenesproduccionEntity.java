/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ponchis
 */
@Entity
@Table(name = "ordenesproduccion")
@NamedQueries({
    @NamedQuery(name = "OrdenesproduccionEntity.findAll", query = "SELECT o FROM OrdenesproduccionEntity o"),
    @NamedQuery(name = "OrdenesproduccionEntity.findByIdOrdenProduccion", query = "SELECT o FROM OrdenesproduccionEntity o WHERE o.idOrdenProduccion = :idOrdenProduccion"),
    @NamedQuery(name = "OrdenesproduccionEntity.findByCantidadGenerar", query = "SELECT o FROM OrdenesproduccionEntity o WHERE o.cantidadGenerar = :cantidadGenerar"),
    @NamedQuery(name = "OrdenesproduccionEntity.findByFecHoraInicio", query = "SELECT o FROM OrdenesproduccionEntity o WHERE o.fecHoraInicio = :fecHoraInicio"),
    @NamedQuery(name = "OrdenesproduccionEntity.findByFecHoraFin", query = "SELECT o FROM OrdenesproduccionEntity o WHERE o.fecHoraFin = :fecHoraFin"),
    @NamedQuery(name = "OrdenesproduccionEntity.findByCantResiduo", query = "SELECT o FROM OrdenesproduccionEntity o WHERE o.cantResiduo = :cantResiduo"),
    @NamedQuery(name = "OrdenesproduccionEntity.findByCantDefecto", query = "SELECT o FROM OrdenesproduccionEntity o WHERE o.cantDefecto = :cantDefecto")})
public class OrdenesproduccionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idOrdenProduccion")
    private Integer idOrdenProduccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidadGenerar")
    private int cantidadGenerar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecHoraInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecHoraInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecHoraFin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecHoraFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantResiduo")
    private int cantResiduo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantDefecto")
    private int cantDefecto;
    @JoinTable(name = "ordenprodproductos", joinColumns = {
        @JoinColumn(name = "ordenProId", referencedColumnName = "idOrdenProduccion")}, inverseJoinColumns = {
        @JoinColumn(name = "productoId", referencedColumnName = "idProducto")})
    @ManyToMany
    private Collection<ProductoEntity> productoEntityCollection;
    @JoinColumn(name = "defectuosos", referencedColumnName = "idDefectuoso")
    @ManyToOne(optional = false)
    private DefectuosoEntity defectuosos;
    @JoinColumn(name = "residuo", referencedColumnName = "idResiduo")
    @ManyToOne(optional = false)
    private ResiduoEntity residuo;
    @JoinColumn(name = "usuarioProduccion", referencedColumnName = "nroDoc")
    @ManyToOne(optional = false)
    private UsuarioEntity usuarioProduccion;
    @JoinColumn(name = "maquinas_idMaquina", referencedColumnName = "idMaquina")
    @ManyToOne(optional = false)
    private MaquinaEntity maquinasidMaquina;

    public OrdenesproduccionEntity() {
    }

    public OrdenesproduccionEntity(Integer idOrdenProduccion) {
        this.idOrdenProduccion = idOrdenProduccion;
    }

    public OrdenesproduccionEntity(Integer idOrdenProduccion, int cantidadGenerar, Date fecHoraInicio, Date fecHoraFin, int cantResiduo, int cantDefecto) {
        this.idOrdenProduccion = idOrdenProduccion;
        this.cantidadGenerar = cantidadGenerar;
        this.fecHoraInicio = fecHoraInicio;
        this.fecHoraFin = fecHoraFin;
        this.cantResiduo = cantResiduo;
        this.cantDefecto = cantDefecto;
    }

    public Integer getIdOrdenProduccion() {
        return idOrdenProduccion;
    }

    public void setIdOrdenProduccion(Integer idOrdenProduccion) {
        this.idOrdenProduccion = idOrdenProduccion;
    }

    public int getCantidadGenerar() {
        return cantidadGenerar;
    }

    public void setCantidadGenerar(int cantidadGenerar) {
        this.cantidadGenerar = cantidadGenerar;
    }

    public Date getFecHoraInicio() {
        return fecHoraInicio;
    }

    public void setFecHoraInicio(Date fecHoraInicio) {
        this.fecHoraInicio = fecHoraInicio;
    }

    public Date getFecHoraFin() {
        return fecHoraFin;
    }

    public void setFecHoraFin(Date fecHoraFin) {
        this.fecHoraFin = fecHoraFin;
    }

    public int getCantResiduo() {
        return cantResiduo;
    }

    public void setCantResiduo(int cantResiduo) {
        this.cantResiduo = cantResiduo;
    }

    public int getCantDefecto() {
        return cantDefecto;
    }

    public void setCantDefecto(int cantDefecto) {
        this.cantDefecto = cantDefecto;
    }

    public Collection<ProductoEntity> getProductoEntityCollection() {
        return productoEntityCollection;
    }

    public void setProductoEntityCollection(Collection<ProductoEntity> productoEntityCollection) {
        this.productoEntityCollection = productoEntityCollection;
    }

    public DefectuosoEntity getDefectuosos() {
        return defectuosos;
    }

    public void setDefectuosos(DefectuosoEntity defectuosos) {
        this.defectuosos = defectuosos;
    }

    public ResiduoEntity getResiduo() {
        return residuo;
    }

    public void setResiduo(ResiduoEntity residuo) {
        this.residuo = residuo;
    }

    public UsuarioEntity getUsuarioProduccion() {
        return usuarioProduccion;
    }

    public void setUsuarioProduccion(UsuarioEntity usuarioProduccion) {
        this.usuarioProduccion = usuarioProduccion;
    }

    public MaquinaEntity getMaquinasidMaquina() {
        return maquinasidMaquina;
    }

    public void setMaquinasidMaquina(MaquinaEntity maquinasidMaquina) {
        this.maquinasidMaquina = maquinasidMaquina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrdenProduccion != null ? idOrdenProduccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenesproduccionEntity)) {
            return false;
        }
        OrdenesproduccionEntity other = (OrdenesproduccionEntity) object;
        if ((this.idOrdenProduccion == null && other.idOrdenProduccion != null) || (this.idOrdenProduccion != null && !this.idOrdenProduccion.equals(other.idOrdenProduccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.OrdenesproduccionEntity[ idOrdenProduccion=" + idOrdenProduccion + " ]";
    }
    
}
