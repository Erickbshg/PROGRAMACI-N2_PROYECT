/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecprogra2;

/**
 *
 * @author erici
 */
import com.mycompany.proyecprogra2.gt.edu.umg.bd.LogSistema;
import com.mycompany.proyecprogra2.gt.edu.umg.bd.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Controlador JPA para la entidad LogSistema.
 * Permite registrar y consultar eventos del sistema como logins, errores, etc.
 */
public class LogSistemaJpaController {

    private EntityManagerFactory emf;

    // Constructor que recibe la fábrica de EntityManager
    public LogSistemaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Obtiene una instancia de EntityManager para interactuar con la base de datos
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Registra un nuevo log en la base de datos.
     * Se usa para guardar eventos como inicio de sesión, errores, acciones administrativas, etc.
     *
     * @param accion      Nombre de la acción (ej. "Login", "Registro", "Error")
     * @param descripcion Detalle adicional del evento
     * @param usuario     Usuario que realizó la acción
     */
    public void registrarLog(String accion, String descripcion, Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            LogSistema log = new LogSistema();
            log.setAccion(accion);
            log.setDescripcion(descripcion);
            log.setFecha(new Date()); // Fecha actual
            log.setIdUsuario(usuario); // Usuario relacionado

            em.persist(log); // Guarda el log
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error al registrar log: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Lista todos los logs registrados en el sistema.
     * Útil para mostrar en reportes o auditorías.
     */
    public List<LogSistema> listarLogs() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<LogSistema> query = em.createQuery("SELECT l FROM LogSistema l", LogSistema.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}