/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecprogra2;

import com.mycompany.proyecprogra2.gt.edu.umg.bd.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Controlador JPA para la entidad Cliente.
 * Permite realizar operaciones CRUD con manejo de transacciones y rollback.
 */
public class ClienteJpaController {

    // Fábrica de EntityManager, configurada en persistence.xml
    private EntityManagerFactory emf;

    // Constructor que recibe la fábrica de EntityManager
    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Obtiene una instancia de EntityManager para interactuar con la base de datos
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Crea un nuevo cliente en la base de datos.
     * Se usa para registrar clientes desde el sistema.
     */
    public void create(Cliente cliente) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // Validaciones básicas
            if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
                throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
            }

            em.persist(cliente); // Guarda el cliente
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); // Rollback en caso de error
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Edita un cliente existente.
     * Se usa para actualizar datos como nombre, dirección o teléfono.
     */
    public void edit(Cliente cliente) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            if (cliente.getIdCliente() == null) {
                throw new IllegalArgumentException("El ID del cliente es obligatorio para editar.");
            }

            em.merge(cliente); // Actualiza el cliente
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un cliente por su ID.
     * Se usa para borrar registros obsoletos o incorrectos.
     */
    public void destroy(Integer idCliente) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, idCliente);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Busca un cliente por su ID.
     * Útil para cargar datos específicos de un cliente.
     */
    public Cliente findCliente(Integer idCliente) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, idCliente);
        } finally {
            em.close();
        }
    }

    /**
     * Lista todos los clientes registrados.
     * Se puede usar para mostrar clientes en una tabla o reporte.
     */
    public List<Cliente> findClienteEntities() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca clientes por nombre (búsqueda parcial).
     * Útil para filtros en formularios.
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(:nombre)", Cliente.class);
            query.setParameter("nombre", "%" + nombre + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

