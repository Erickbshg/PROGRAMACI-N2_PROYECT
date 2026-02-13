package com.mycompany.proyecprogra2;


import com.mycompany.proyecprogra2.gt.edu.umg.bd.ClientePlan;
import com.mycompany.proyecprogra2.gt.edu.umg.bd.ClientePlanPK;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * -------------------------------------------------------------
 * CONTROLADOR JPA: ClientePlanJpaController
 * -------------------------------------------------------------
 * Controlador responsable de manejar las operaciones sobre la entidad
 * ClientePlan, la cual representa la relación entre los clientes y
 * los planes de seguridad asignados.
 * 
 * Permite:
 *  - Crear nuevas asignaciones de plan a cliente.
 *  - Consultar todas las asignaciones registradas.
 *  - Buscar una asignación específica por su clave compuesta.
 * 
 * -------------------------------------------------------------
 * 
 */
public class ClientePlanJpaController {

    /// ---------------------------------------------
    //  ATRIBUTOS
    // ---------------------------------------------
    // Fábrica de EntityManager utilizada para crear las instancias de conexión con la base de datos
    private EntityManagerFactory emf;

    /// ---------------------------------------------
    //  CONSTRUCTOR
    // ---------------------------------------------
    /**
     * Constructor que recibe la fábrica de EntityManager.
     * @param emf Fábrica de EntityManager (configurada en persistence.xml)
     */
    public ClientePlanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /// ---------------------------------------------
    //  MÉTODO DE APOYO
    // ---------------------------------------------
    /**
     * Devuelve una nueva instancia de EntityManager para interactuar con la base de datos.
     * @return EntityManager
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /// ---------------------------------------------
    //  MÉTODO CREATE (ASIGNAR PLAN A CLIENTE)
    // ---------------------------------------------
    /**
     * Crea una nueva asignación de un plan de seguridad a un cliente.
     * 
     * Realiza validaciones para asegurar que:
     * - El cliente no sea nulo.
     * - El plan de seguridad no sea nulo.
     * 
     * Además, genera automáticamente la clave primaria compuesta
     * (ClientePlanPK) a partir de los IDs del cliente y el plan.
     * 
     * @param clientePlan Objeto ClientePlan que contiene la información de la asignación
     */
    public void create(ClientePlan clientePlan) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin(); // Inicia la transacción

            // Validaciones: Cliente y Plan no deben ser nulos
            if (clientePlan.getCliente() == null || clientePlan.getPlanSeguridad() == null) {
                throw new IllegalArgumentException("Cliente y Plan son obligatorios.");
            }

            // Crea la clave compuesta utilizando los IDs del cliente y plan
            ClientePlanPK pk = new ClientePlanPK(
                clientePlan.getCliente().getIdCliente(),
                clientePlan.getPlanSeguridad().getIdPlan()
            );
            clientePlan.setClientePlanPK(pk); // Asigna la clave compuesta

            // Guarda la asignación en la base de datos
            em.persist(clientePlan);
            em.getTransaction().commit(); // Finaliza la transacción correctamente
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al asignar plan al cliente: " + e.getMessage(), e);
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO FIND ALL (LISTAR TODAS LAS ASIGNACIONES)
    // ---------------------------------------------
    /**
     * Obtiene una lista con todas las asignaciones registradas
     * entre clientes y planes de seguridad.
     * 
     * @return Lista de objetos ClientePlan existentes en la base de datos
     */
    public List<ClientePlan> findClientePlanEntities() {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL para obtener todas las asignaciones
            TypedQuery<ClientePlan> query = em.createQuery(
                "SELECT cp FROM ClientePlan cp", ClientePlan.class);
            return query.getResultList();
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO FIND (BUSCAR ASIGNACIÓN POR CLAVE COMPUESTA)
    // ---------------------------------------------
    /**
     * Busca una asignación específica de cliente-plan utilizando
     * su clave compuesta (idCliente + idPlan).
     * 
     * @param idCliente ID del cliente
     * @param idPlan ID del plan de seguridad
     * @return Objeto ClientePlan si se encuentra, o null si no existe
     */
    public ClientePlan findClientePlan(int idCliente, int idPlan) {
        EntityManager em = getEntityManager();
        try {
            // Crea la clave compuesta para realizar la búsqueda
            ClientePlanPK pk = new ClientePlanPK(idCliente, idPlan);
            return em.find(ClientePlan.class, pk);
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }
}
