/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "estadomaq")
@NamedQueries({
    @NamedQuery(name = "EstadomaqEntity.findAll", query = "SELECT e FROM EstadomaqEntity e"),
    @NamedQuery(name = "EstadomaqEntity.findByIdEstado", query = "SELECT e FROM EstadomaqEntity e WHERE e.idEstado = :idEstado"),
    @NamedQuery(name = "EstadomaqEntity.findByAbreviatura", query = "SELECT e FROM EstadomaqEntity e WHERE e.abreviatura = :abreviatura"),
    @NamedQuery(name = "EstadomaqEntity.findByDescripcion", query = "SELECT e FROM EstadomaqEntity e WHERE e.descripcion = :descripcion")})
public class EstadomaqEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEstado")
    private Integer idEstado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;

    public EstadomaqEntity() {
    }

    public EstadomaqEntity(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public EstadomaqEntity(Integer idEstado, String abreviatura, String descripcion) {
        this.idEstado = idEstado;
        this.abreviatura = abreviatura;
        this.descripcion = descripcion;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadomaqEntity)) {
            return false;
        }
        EstadomaqEntity other = (EstadomaqEntity) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.EstadomaqEntity[ idEstado=" + idEstado + " ]";
    }
    
}
