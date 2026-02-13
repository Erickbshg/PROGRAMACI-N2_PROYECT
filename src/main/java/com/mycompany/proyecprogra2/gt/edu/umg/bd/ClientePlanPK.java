/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecprogra2.gt.edu.umg.bd;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author erici
 */
@Embeddable
public class ClientePlanPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idCliente", nullable = false)
    private int idCliente;
    @Basic(optional = false)
    @Column(name = "idPlan", nullable = false)
    private int idPlan;

    public ClientePlanPK() {
    }

    public ClientePlanPK(int idCliente, int idPlan) {
        this.idCliente = idCliente;
        this.idPlan = idPlan;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCliente;
        hash += (int) idPlan;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientePlanPK)) {
            return false;
        }
        ClientePlanPK other = (ClientePlanPK) object;
        if (this.idCliente != other.idCliente) {
            return false;
        }
        if (this.idPlan != other.idPlan) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.proyecprogra2.gt.edu.umg.bd.ClientePlanPK[ idCliente=" + idCliente + ", idPlan=" + idPlan + " ]";
    }
    
}
