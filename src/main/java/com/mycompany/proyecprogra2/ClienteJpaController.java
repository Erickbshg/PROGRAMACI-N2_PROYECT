
package com.mycompany.proyecprogra2;

import com.mycompany.proyecprogra2.gt.edu.umg.bd.Cliente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * -------------------------------------------------------------
 * CONTROLADOR JPA: ClienteJpaController
 * -------------------------------------------------------------
 * Controlador encargado de gestionar las operaciones CRUD
 * (Crear, Leer, Actualizar, Eliminar) para la entidad Cliente.
 * 
 * Utiliza JPA con manejo explícito de transacciones y rollback
 * en caso de errores.
 * -------------------------------------------------------------
 * Métodos principales:
 * - create(Cliente cliente)
 * - edit(Cliente cliente)
 * - destroy(Integer idCliente)
 * - findCliente(Integer idCliente)
 * - findClienteEntities()
 * - buscarPorNombre(String nombre)
 * -------------------------------------------------------------
 */
public class ClienteJpaController {

    /// ---------------------------------------------
    //  ATRIBUTOS
    // ---------------------------------------------
    // Fábrica de EntityManager, se configura desde persistence.xml
    private EntityManagerFactory emf;

    /// ---------------------------------------------
    //  CONSTRUCTOR
    // ---------------------------------------------
    /**
     * Constructor que recibe la fábrica de EntityManager.
     * @param emf Instancia de EntityManagerFactory
     */
    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /// ---------------------------------------------
    //  MÉTODOS DE APOYO
    // ---------------------------------------------
    /**
     * Devuelve una instancia de EntityManager para interactuar con la base de datos.
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /// ---------------------------------------------
    //  MÉTODO CREATE (INSERTAR CLIENTE)
    // ---------------------------------------------
    /**
     * Crea un nuevo cliente en la base de datos.
     * 
     * Validaciones:
     * - El nombre no puede estar vacío.
     *
     * @param cliente Objeto Cliente a registrar
     */
    public void create(Cliente cliente) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin(); // Inicia la transacción

            // Validación básica: el nombre no puede estar vacío
            if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
                throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
            }

            em.persist(cliente); // Guarda el cliente en la base de datos
            em.getTransaction().commit(); // Finaliza la transacción
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanza una excepción con el mensaje de error
            throw new RuntimeException("Error al crear cliente: " + e.getMessage(), e);
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO EDIT (ACTUALIZAR CLIENTE)
    // ---------------------------------------------
    /**
     * Edita un cliente existente.
     * 
     * Validaciones:
     * - El cliente debe tener un ID válido para poder editarlo.
     *
     * @param cliente Objeto Cliente a actualizar
     */
    public void edit(Cliente cliente) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin(); // Inicia la transacción

            // Validación: el cliente debe tener un ID para poder editarlo
            if (cliente.getIdCliente() == null) {
                throw new IllegalArgumentException("El ID del cliente es obligatorio para editar.");
            }

            em.merge(cliente); // Actualiza el cliente en la base de datos
            em.getTransaction().commit(); // Finaliza la transacción
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanza una excepción con el mensaje de error
            throw new RuntimeException("Error al editar cliente: " + e.getMessage(), e);
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO DESTROY (ELIMINAR CLIENTE)
    // ---------------------------------------------
    /**
     * Elimina un cliente de la base de datos por su ID.
     * 
     * Si el cliente no existe, no realiza ninguna acción.
     *
     * @param idCliente ID del cliente a eliminar
     */
    public void destroy(Integer idCliente) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin(); // Inicia la transacción

            Cliente cliente = em.find(Cliente.class, idCliente); // Busca el cliente por ID
            if (cliente != null) {
                em.remove(cliente); // Elimina el cliente si existe
            }

            em.getTransaction().commit(); // Finaliza la transacción
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage(), e);
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO FIND (BUSCAR POR ID)
    // ---------------------------------------------
    /**
     * Busca un cliente por su ID.
     * 
     * @param idCliente ID del cliente a buscar
     * @return Cliente encontrado o null si no existe
     */
    public Cliente findCliente(Integer idCliente) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, idCliente); // Busca y retorna el cliente
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO FIND ALL (LISTAR TODOS)
    // ---------------------------------------------
    /**
     * Lista todos los clientes registrados en la base de datos.
     * 
     * Ordena los resultados por nombre.
     * 
     * @return Lista de todos los clientes
     */
    public List<Cliente> findClienteEntities() {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL para obtener todos los clientes ordenados por nombre
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c ORDER BY c.nombre", Cliente.class);
            return query.getResultList(); // Retorna la lista de clientes
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /// ---------------------------------------------
    //  MÉTODO BUSCAR POR NOMBRE
    // ---------------------------------------------
    /**
     * Busca clientes por nombre (búsqueda parcial).
     * 
     * Utiliza la cláusula LIKE para encontrar coincidencias
     * que contengan el texto ingresado.
     *
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de clientes coincidentes
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL con filtro por nombre (insensible a mayúsculas/minúsculas)
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(:nombre) ORDER BY c.nombre",
                Cliente.class);
            query.setParameter("nombre", "%" + nombre + "%"); // Parámetro con comodines
            return query.getResultList(); // Retorna la lista de coincidencias
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }
}
