package com.mycompany.proyecprogra2;


import com.mycompany.proyecprogra2.gt.edu.umg.bd.Factura;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * -------------------------------------------------------------
 * CONTROLADOR JPA: FacturaJpaController
 * -------------------------------------------------------------
 * Controlador responsable de manejar las operaciones sobre la entidad
 * Factura, la cual representa las facturas emitidas en el sistema.
 * 
 * Permite:
 *  - Registrar nuevas facturas (método create).
 *  - Listar todas las facturas registradas.
 *  - Buscar facturas por su identificador único.
 * 
 * -------------------------------------------------------------
 */
public class FacturaJpaController {

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
    public FacturaJpaController(EntityManagerFactory emf) {
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
    //  MÉTODO CREATE (REGISTRAR FACTURA)
    // ---------------------------------------------
    /**
     * Registra una nueva factura en la base de datos.
     * 
     * Realiza validaciones para asegurar que:
     * - La fecha de emisión esté presente.
     * - El monto total no sea nulo.
     * - Exista un cliente y un plan asociados.
     * 
     * Si ocurre un error durante la transacción, esta se revierte automáticamente.
     * 
     * @param factura Objeto Factura que contiene los datos a registrar
     */
    public void create(Factura factura) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin(); // Inicia la transacción

            // Validaciones de campos obligatorios
            if (factura.getFecha() == null || factura.getMontoTotal() == null ||
                factura.getIdCliente() == null || factura.getIdPlan() == null) {
                throw new IllegalArgumentException("Todos los campos de la factura son obligatorios.");
            }

            // Guarda la factura en la base de datos
            em.persist(factura);
            em.getTransaction().commit(); // Finaliza la transacción correctamente
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanza una excepción con mensaje detallado
            throw new RuntimeException("Error al registrar factura: " + e.getMessage(), e);
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO FIND ALL (LISTAR TODAS LAS FACTURAS)
    // ---------------------------------------------
    /**
     * Obtiene una lista de todas las facturas emitidas en el sistema.
     * 
     * La consulta se realiza en orden descendente por fecha (las más recientes primero).
     * 
     * @return Lista de objetos Factura registrados en la base de datos
     */
    public List<Factura> findFacturaEntities() {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL para obtener todas las facturas ordenadas por fecha descendente
            TypedQuery<Factura> query = em.createQuery(
                "SELECT f FROM Factura f ORDER BY f.fecha DESC", Factura.class);
            return query.getResultList();
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO FIND (BUSCAR FACTURA POR ID)
    // ---------------------------------------------
    /**
     * Busca una factura específica utilizando su identificador único.
     * 
     * @param idFactura ID de la factura a buscar
     * @return Objeto Factura si se encuentra, o null si no existe
     */
    public Factura findFactura(Integer idFactura) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, idFactura); // Busca la factura por ID
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }
}
