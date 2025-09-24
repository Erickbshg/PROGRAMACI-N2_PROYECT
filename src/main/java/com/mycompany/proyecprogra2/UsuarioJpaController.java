package com.mycompany.proyecprogra2;


import com.mycompany.proyecprogra2.gt.edu.umg.bd.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Controlador JPA para la entidad Usuario.
 * Permite realizar operaciones CRUD y validar credenciales de acceso.
 */
public class UsuarioJpaController {

    // Fábrica de EntityManager, se configura desde persistence.xml
    private EntityManagerFactory emf;

    // Constructor que recibe la fábrica de EntityManager
    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Obtiene una instancia de EntityManager para interactuar con la base de datos
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     * Se usa para registrar usuarios desde el sistema.
     */
    public void create(Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin(); // Inicia la transacción
            em.persist(usuario);         // Guarda el usuario
            em.getTransaction().commit(); // Finaliza la transacción
        } finally {
            em.close(); // Cierra el EntityManager
        }
    }

    /**
     * Edita un usuario existente.
     * Se usa para actualizar datos como nombre, correo o rol.
     */
    public void edit(Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario); // Actualiza el usuario
            em.getTransaction().commit();
        } finally {
            em.close();
        }   
    }

    /**
     * Elimina un usuario por su ID.
     * Se usa para borrar registros obsoletos o incorrectos.
     */
    public void destroy(Integer idUsuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, idUsuario); // Busca el usuario
            if (usuario != null) {
                em.remove(usuario); // Elimina si existe
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Busca un usuario por su ID.
     * Útil para cargar datos específicos de un usuario.
     */
    public Usuario findUsuario(Integer idUsuario) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, idUsuario);
        } finally {
            em.close();
        }
    }

    /**
     * Lista todos los usuarios registrados.
     * Se puede usar para mostrar usuarios en una tabla o reporte.
     */
    public List<Usuario> findUsuarioEntities() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Valida las credenciales de acceso (login).
     * Se usa en el menú principal para iniciar sesión.
     * Retorna el usuario si las credenciales son correctas, o null si no lo son.
     */
    public Usuario validarCredenciales(String correo, String contraseña) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.correo = :correo AND u.contraseña = :contraseña", Usuario.class);
            query.setParameter("correo", correo);
            query.setParameter("contraseña", contraseña);

            List<Usuario> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }
}
