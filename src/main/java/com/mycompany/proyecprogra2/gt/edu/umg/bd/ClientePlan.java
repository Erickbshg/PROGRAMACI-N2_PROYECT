/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecprogra2.gt.edu.umg.bd;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author erici
 */
@Entity
@Table(name = "Cliente_Plan", catalog = "EmpresaSeguridad_1", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "ClientePlan.findAll", query = "SELECT c FROM ClientePlan c")})
public class ClientePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClientePlanPK clientePlanPK;
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fechaFin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cliente cliente;
    @JoinColumn(name = "idPlan", referencedColumnName = "idPlan", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanSeguridad planSeguridad;

    public ClientePlan() {
    }

    public ClientePlan(ClientePlanPK clientePlanPK) {
        this.clientePlanPK = clientePlanPK;
    }

    public ClientePlan(int idCliente, int idPlan) {
        this.clientePlanPK = new ClientePlanPK(idCliente, idPlan);
    }

    public ClientePlanPK getClientePlanPK() {
        return clientePlanPK;
    }

    public void setClientePlanPK(ClientePlanPK clientePlanPK) {
        this.clientePlanPK = clientePlanPK;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public PlanSeguridad getPlanSeguridad() {
        return planSeguridad;
    }

    public void setPlanSeguridad(PlanSeguridad planSeguridad) {
        this.planSeguridad = planSeguridad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientePlanPK != null ? clientePlanPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientePlan)) {
            return false;
        }
        ClientePlan other = (ClientePlan) object;
        if ((this.clientePlanPK == null && other.clientePlanPK != null) || (this.clientePlanPK != null && !this.clientePlanPK.equals(other.clientePlanPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.proyecprogra2.gt.edu.umg.bd.ClientePlan[ clientePlanPK=" + clientePlanPK + " ]";
    }
    
}
