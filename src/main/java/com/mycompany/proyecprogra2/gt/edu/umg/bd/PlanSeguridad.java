/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecprogra2.gt.edu.umg.bd;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author erici
 */
@Entity
@Table(name = "PlanSeguridad", catalog = "EmpresaSeguridad_1", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "PlanSeguridad.findAll", query = "SELECT p FROM PlanSeguridad p")})
public class PlanSeguridad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPlan", nullable = false)
    private Integer idPlan;
    @Basic(optional = false)
    @Column(name = "tipoPlan", nullable = false, length = 50)
    private String tipoPlan;
    @Column(name = "descripcion", length = 2147483647)
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "costoMensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoMensual;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planSeguridad", fetch = FetchType.LAZY)
    private Collection<ClientePlan> clientePlanCollection;
    @OneToMany(mappedBy = "idPlan", fetch = FetchType.LAZY)
    private Collection<Factura> facturaCollection;

    public PlanSeguridad() {
    }

    public PlanSeguridad(Integer idPlan) {
        this.idPlan = idPlan;
    }

    public PlanSeguridad(Integer idPlan, String tipoPlan, BigDecimal costoMensual) {
        this.idPlan = idPlan;
        this.tipoPlan = tipoPlan;
        this.costoMensual = costoMensual;
    }

    public Integer getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Integer idPlan) {
        this.idPlan = idPlan;
    }

    public String getTipoPlan() {
        return tipoPlan;
    }

    public void setTipoPlan(String tipoPlan) {
        this.tipoPlan = tipoPlan;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCostoMensual() {
        return costoMensual;
    }

    public void setCostoMensual(BigDecimal costoMensual) {
        this.costoMensual = costoMensual;
    }

    public Collection<ClientePlan> getClientePlanCollection() {
        return clientePlanCollection;
    }

    public void setClientePlanCollection(Collection<ClientePlan> clientePlanCollection) {
        this.clientePlanCollection = clientePlanCollection;
    }

    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlan != null ? idPlan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanSeguridad)) {
            return false;
        }
        PlanSeguridad other = (PlanSeguridad) object;
        if ((this.idPlan == null && other.idPlan != null) || (this.idPlan != null && !this.idPlan.equals(other.idPlan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.proyecprogra2.gt.edu.umg.bd.PlanSeguridad[ idPlan=" + idPlan + " ]";
    }
    
}
