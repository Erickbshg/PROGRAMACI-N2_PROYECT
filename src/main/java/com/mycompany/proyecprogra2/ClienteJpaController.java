package com.mycompany.proyecprogra2;

// Importa la entidad Cliente y las clases necesarias para trabajar con JPA
import com.mycompany.proyecprogra2.gt.edu.umg.bd.Cliente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Controlador JPA para la entidad Cliente.
 * Permite realizar operaciones CRUD (crear, leer, actualizar, eliminar)
 * con manejo explícito de transacciones y rollback en caso de error.
 */
public class ClienteJpaController {

    // Fábrica de EntityManager, se configura desde persistence.xml
    private EntityManagerFactory emf;

    // Constructor que recibe la fábrica de EntityManager
    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Método que devuelve una instancia de EntityManager para interactuar con la base de datos
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Crea un nuevo cliente en la base de datos.
     * Valida que el nombre no esté vacío antes de persistir.
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

    /**
     * Edita un cliente existente.
     * Valida que el ID esté presente antes de hacer el merge.
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

    /**
     * Elimina un cliente por su ID.
     * Si el cliente existe, lo elimina; si no, no hace nada.
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
            // Lanza una excepción con el mensaje de error
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage(), e);
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /**
     * Busca un cliente por su ID.
     * Retorna el cliente si existe, o null si no se encuentra.
     */
    public Cliente findCliente(Integer idCliente) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, idCliente); // Busca y retorna el cliente
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /**
     * Lista todos los clientes registrados en la base de datos.
     * Ordena los resultados por nombre.
     */
    public List<Cliente> findClienteEntities() {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL para obtener todos los clientes ordenados por nombre
            TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c ORDER BY c.nombre", Cliente.class);
            return query.getResultList(); // Retorna la lista de clientes
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /**
     * Busca clientes por nombre (búsqueda parcial).
     * Utiliza LIKE para encontrar coincidencias que contengan el texto ingresado.
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL con filtro por nombre (insensible a mayúsculas/minúsculas)
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(:nombre) ORDER BY c.nombre", Cliente.class);
            query.setParameter("nombre", "%" + nombre + "%"); // Parámetro con comodines
            return query.getResultList(); // Retorna la lista de coincidencias
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }
}
