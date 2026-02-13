package com.mycompany.proyecprogra2;

import com.mycompany.proyecprogra2.gt.edu.umg.bd.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * ------------------------------------------------------------
 *  CONTROLADOR JPA: UsuarioJpaController
 * ------------------------------------------------------------
 *  Esta clase gestiona todas las operaciones relacionadas con
 *  la entidad Usuario.
 *  
 *  FUNCIONALIDADES:
 *  - CRUD completo (crear, leer, actualizar, eliminar)
 *  - Validación de credenciales de inicio de sesión
 *  
 *  Usa JPA (Java Persistence API) para interactuar con la base
 *  de datos definida en persistence.xml.
 * ------------------------------------------------------------
 */
public class UsuarioJpaController {

    // ------------------------------------------------------------
    //  ATRIBUTO PRINCIPAL
    // ------------------------------------------------------------
    /** Fábrica de EntityManager configurada desde persistence.xml */
    private EntityManagerFactory emf;

    // ------------------------------------------------------------
    //  CONSTRUCTOR
    // ------------------------------------------------------------
    /**
     * Constructor que inicializa el controlador con la fábrica de entidades.
     * @param emf EntityManagerFactory proporcionada por la configuración de persistencia.
     */
    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Crea y devuelve una nueva instancia de EntityManager.
     * @return EntityManager para realizar operaciones JPA.
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ------------------------------------------------------------
    //  MÉTODO CREATE (Registrar nuevo usuario)
    // ------------------------------------------------------------
    /**
     * Crea un nuevo usuario en la base de datos.
     * Se utiliza para registrar nuevos usuarios en el sistema.
     * @param usuario Objeto Usuario con los datos a guardar.
     */
    public void create(Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();  // Inicia la transacción
            em.persist(usuario);          // Inserta el nuevo usuario
            em.getTransaction().commit(); // Confirma la transacción
        } finally {
            em.close(); // Libera los recursos
        }
    }

    // ------------------------------------------------------------
    //  MÉTODO EDIT (Actualizar usuario)
    // ------------------------------------------------------------
    /**
     * Edita un usuario existente en la base de datos.
     * Se usa para actualizar datos como nombre, correo o rol.
     * @param usuario Objeto Usuario con los nuevos datos.
     */
    public void edit(Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario); // Actualiza el registro existente
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // ------------------------------------------------------------
    //  MÉTODO DELETE (Eliminar usuario)
    // ------------------------------------------------------------
    /**
     * Elimina un usuario de la base de datos mediante su ID.
     * Se usa para eliminar registros obsoletos o incorrectos.
     * @param idUsuario Identificador único del usuario.
     */
    public void destroy(Integer idUsuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // Busca el usuario antes de eliminar
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                em.remove(usuario); // Elimina si existe
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // ------------------------------------------------------------
    //  MÉTODO READ (Buscar por ID)
    // ------------------------------------------------------------
    /**
     * Busca un usuario por su identificador único.
     * @param idUsuario ID del usuario a buscar.
     * @return Objeto Usuario si se encuentra, o null si no existe.
     */
    public Usuario findUsuario(Integer idUsuario) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, idUsuario);
        } finally {
            em.close();
        }
    }

    // ------------------------------------------------------------
    //  MÉTODO READ (Listar todos los usuarios)
    // ------------------------------------------------------------
    /**
     * Obtiene todos los usuarios registrados en la base de datos.
     * @return Lista de objetos Usuario.
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

    // ------------------------------------------------------------
    //  MÉTODO LOGIN (Validar credenciales de acceso)
    // ------------------------------------------------------------
    /**
     * Valida las credenciales de acceso de un usuario.
     * Se utiliza para iniciar sesión desde el menú principal.
     * 
     * @param correo Correo electrónico del usuario.
     * @param contraseña Contraseña del usuario.
     * @return Objeto Usuario si las credenciales son correctas, o null si no coinciden.
     */
    public Usuario validarCredenciales(String correo, String contraseña) {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL para verificar usuario y contraseña
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.correo = :correo AND u.contraseña = :contraseña",
                Usuario.class
            );
            query.setParameter("correo", correo);
            query.setParameter("contraseña", contraseña);

            // Retorna el primer resultado o null si no hay coincidencia
            List<Usuario> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }
}
