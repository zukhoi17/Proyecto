/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ponchis
 */
@Entity
@Table(name = "turnos")
@NamedQueries({
    @NamedQuery(name = "TurnoEntity.findAll", query = "SELECT t FROM TurnoEntity t"),
    @NamedQuery(name = "TurnoEntity.findByIdTurno", query = "SELECT t FROM TurnoEntity t WHERE t.idTurno = :idTurno"),
    @NamedQuery(name = "TurnoEntity.findByDescripcion", query = "SELECT t FROM TurnoEntity t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TurnoEntity.findByHoraInicio", query = "SELECT t FROM TurnoEntity t WHERE t.horaInicio = :horaInicio"),
    @NamedQuery(name = "TurnoEntity.findByHoraFin", query = "SELECT t FROM TurnoEntity t WHERE t.horaFin = :horaFin")})
public class TurnoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTurno")
    private Integer idTurno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HoraInicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HoraFin")
    @Temporal(TemporalType.TIME)
    private Date horaFin;

    public TurnoEntity() {
    }

    public TurnoEntity(Integer idTurno) {
        this.idTurno = idTurno;
    }

    public TurnoEntity(Integer idTurno, String descripcion, Date horaInicio, Date horaFin) {
        this.idTurno = idTurno;
        this.descripcion = descripcion;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Integer getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Integer idTurno) {
        this.idTurno = idTurno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTurno != null ? idTurno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurnoEntity)) {
            return false;
        }
        TurnoEntity other = (TurnoEntity) object;
        if ((this.idTurno == null && other.idTurno != null) || (this.idTurno != null && !this.idTurno.equals(other.idTurno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.TurnoEntity[ idTurno=" + idTurno + " ]";
    }
    
}
