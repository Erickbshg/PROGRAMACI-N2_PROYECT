package com.mycompany.proyecprogra2;

import com.mycompany.proyecprogra2.gt.edu.umg.bd.PlanSeguridad;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * ------------------------------------------------------------
 *  CONTROLADOR JPA: PlanSeguridadJpaController
 * ------------------------------------------------------------
 *  Esta clase gestiona todas las operaciones relacionadas con
 *  la entidad PlanSeguridad.
 *  
 *  FUNCIONALIDADES:
 *  - Registrar nuevos planes de seguridad.
 *  - Consultar planes registrados.
 *  - Buscar planes por ID.
 *  
 *  Usa JPA (Java Persistence API) para interactuar con la base de datos.
 * ------------------------------------------------------------
 */
public class PlanSeguridadJpaController {

    // Factoría de EntityManagers para crear conexiones a la base de datos
    private EntityManagerFactory emf;

    /**
     * Constructor que inicializa el controlador con la fábrica de entidades.
     * @param emf EntityManagerFactory proporcionado por la configuración de persistencia.
     */
    public PlanSeguridadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Obtiene una nueva instancia de EntityManager.
     * @return EntityManager para realizar operaciones en la base de datos.
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ------------------------------------------------------------
    //  MÉTODO: CREATE (Registrar nuevo plan de seguridad)
    // ------------------------------------------------------------
    /**
     * Registra un nuevo plan de seguridad en la base de datos.
     * @param plan Objeto PlanSeguridad que contiene los datos del nuevo plan.
     */
    public void create(PlanSeguridad plan) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // Validaciones básicas antes de guardar
            if (plan.getTipoPlan() == null || plan.getTipoPlan().isBlank()) {
                throw new IllegalArgumentException("El tipo de plan es obligatorio.");
            }
            if (plan.getCostoMensual() == null) {
                throw new IllegalArgumentException("El costo mensual es obligatorio.");
            }

            // Inserta el nuevo registro en la base de datos
            em.persist(plan);
            em.getTransaction().commit();

        } catch (Exception e) {
            // Si ocurre un error, revierte la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            // Lanza una excepción más clara para el programador
            throw new RuntimeException("Error al registrar plan de seguridad: " + e.getMessage(), e);

        } finally {
            // Cierra el EntityManager para liberar recursos
            em.close();
        }
    }

    // ------------------------------------------------------------
    //  MÉTODO: READ (Consultar todos los planes)
    // ------------------------------------------------------------
    /**
     * Consulta todos los planes de seguridad registrados.
     * @return Lista de objetos PlanSeguridad ordenados por tipo de plan.
     */
    public List<PlanSeguridad> findPlanSeguridadEntities() {
        EntityManager em = getEntityManager();
        try {
            // Consulta con JPQL para obtener todos los registros
            TypedQuery<PlanSeguridad> query = em.createQuery(
                "SELECT p FROM PlanSeguridad p ORDER BY p.tipoPlan", 
                PlanSeguridad.class
            );
            return query.getResultList();

        } finally {
            // Cierra el EntityManager tras la consulta
            em.close();
        }
    }

    // ------------------------------------------------------------
    //  MÉTODO: READ (Buscar plan por ID)
    // ------------------------------------------------------------
    /**
     * Busca un plan de seguridad por su identificador único.
     * @param idPlan Identificador del plan a buscar.
     * @return Objeto PlanSeguridad encontrado o null si no existe.
     */
    public PlanSeguridad findPlanSeguridad(Integer idPlan) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlanSeguridad.class, idPlan);
        } finally {
            em.close();
        }
    }
}
