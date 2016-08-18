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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "usuarios")
@NamedQueries({
    @NamedQuery(name = "UsuarioEntity.findAll", query = "SELECT u FROM UsuarioEntity u"),
    @NamedQuery(name = "UsuarioEntity.findByNroDoc", query = "SELECT u FROM UsuarioEntity u WHERE u.nroDoc = :nroDoc"),
    @NamedQuery(name = "UsuarioEntity.findByNombres", query = "SELECT u FROM UsuarioEntity u WHERE u.nombres = :nombres"),
    @NamedQuery(name = "UsuarioEntity.findByApellidos", query = "SELECT u FROM UsuarioEntity u WHERE u.apellidos = :apellidos"),
    @NamedQuery(name = "UsuarioEntity.findByUsuario", query = "SELECT u FROM UsuarioEntity u WHERE u.usuario = :usuario"),
    @NamedQuery(name = "UsuarioEntity.findByPassword", query = "SELECT u FROM UsuarioEntity u WHERE u.password = :password"),
    @NamedQuery(name = "UsuarioEntity.findByFechNac", query = "SELECT u FROM UsuarioEntity u WHERE u.fechNac = :fechNac"),
    @NamedQuery(name = "UsuarioEntity.findByEMail", query = "SELECT u FROM UsuarioEntity u WHERE u.eMail = :eMail"),
    @NamedQuery(name = "UsuarioEntity.findByDireccion", query = "SELECT u FROM UsuarioEntity u WHERE u.direccion = :direccion"),
    @NamedQuery(name = "UsuarioEntity.findByTelefono", query = "SELECT u FROM UsuarioEntity u WHERE u.telefono = :telefono"),
    @NamedQuery(name = "UsuarioEntity.findByCelular", query = "SELECT u FROM UsuarioEntity u WHERE u.celular = :celular")})
public class UsuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "nroDoc")
    private String nroDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "nombres")
    private String nombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechNac")
    @Temporal(TemporalType.DATE)
    private Date fechNac;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "eMail")
    private String eMail;
    @Size(max = 45)
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "telefono")
    private Integer telefono;
    @Column(name = "celular")
    private Integer celular;
    @JoinTable(name = "rolesusuario", joinColumns = {
        @JoinColumn(name = "Usuarios_nroDoc", referencedColumnName = "nroDoc")}, inverseJoinColumns = {
        @JoinColumn(name = "roles_idRol", referencedColumnName = "idRol")})
    @ManyToMany
    private Collection<RolEntity> rolEntityCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioProduccion")
    private Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection;
    @JoinColumn(name = "idTipoDoc", referencedColumnName = "idTipoDoc")
    @ManyToOne(optional = false)
    private TipodocEntity idTipoDoc;

    public UsuarioEntity() {
    }

    public UsuarioEntity(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public UsuarioEntity(String nroDoc, String nombres, String apellidos, String usuario, String password, Date fechNac, String eMail) {
        this.nroDoc = nroDoc;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.password = password;
        this.fechNac = fechNac;
        this.eMail = eMail;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechNac() {
        return fechNac;
    }

    public void setFechNac(Date fechNac) {
        this.fechNac = fechNac;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public Integer getCelular() {
        return celular;
    }

    public void setCelular(Integer celular) {
        this.celular = celular;
    }

    public Collection<RolEntity> getRolEntityCollection() {
        return rolEntityCollection;
    }

    public void setRolEntityCollection(Collection<RolEntity> rolEntityCollection) {
        this.rolEntityCollection = rolEntityCollection;
    }

    public Collection<OrdenesproduccionEntity> getOrdenesproduccionEntityCollection() {
        return ordenesproduccionEntityCollection;
    }

    public void setOrdenesproduccionEntityCollection(Collection<OrdenesproduccionEntity> ordenesproduccionEntityCollection) {
        this.ordenesproduccionEntityCollection = ordenesproduccionEntityCollection;
    }

    public TipodocEntity getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(TipodocEntity idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroDoc != null ? nroDoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioEntity)) {
            return false;
        }
        UsuarioEntity other = (UsuarioEntity) object;
        if ((this.nroDoc == null && other.nroDoc != null) || (this.nroDoc != null && !this.nroDoc.equals(other.nroDoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.UsuarioEntity[ nroDoc=" + nroDoc + " ]";
    }
    
}
