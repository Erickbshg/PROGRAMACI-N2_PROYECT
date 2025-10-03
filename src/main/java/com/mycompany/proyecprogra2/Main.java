package com.mycompany.proyecprogra2;

import com.mycompany.proyecprogra2.gt.edu.umg.bd.Usuario;
import com.mycompany.proyecprogra2.gt.edu.umg.bd.Cliente;
import com.mycompany.proyecprogra2.gt.edu.umg.bd.LogSistema;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase principal del Sistema de Seguridad
 * Gestiona el flujo principal del sistema (login, menús por rol y operaciones CRUD)
 * 
 * @author 
 */
public class Main {
    

    // Se centraliza el manejo de EntityManagerFactory para todo el sistema
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyecProgra2PU");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("######################################");
            System.out.println("# Bienvenido al Sistema de Seguridad #");
            System.out.println("#1. Iniciar Sesión                   #");
            System.out.println("#2. Salir                            #");
            System.out.println("######################################");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    iniciarSesion(scanner);
                    break;
                case "2":
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
        scanner.close();
        emf.close();
    }

    /**
     * Proceso de inicio de sesión
     */
    private static void iniciarSesion(Scanner scanner) {
        System.out.println("----------------[ Login ]----------------------");
        System.out.print("-Correo: ");
        String correo = scanner.nextLine();
        System.out.print("-Contraseña: ");
        String contrasena = scanner.nextLine();

        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
        LogSistemaJpaController logController = new LogSistemaJpaController(emf);

        // Validar credenciales
        Usuario usuario = usuarioController.validarCredenciales(correo, contrasena);

        if (usuario == null) {
            System.out.println("Credenciales inválidas.");
            logController.registrarLog("Login", "Intento de inicio de sesión fallido", null);
            return;
        }

        logController.registrarLog("Login", "Inicio de sesión exitoso", usuario);
        System.out.println("Bienvenido, " + usuario.getNombre() + " [" + usuario.getRol() + "]");

        // Menú según el rol del usuario
        switch (usuario.getRol()) {
            case "Administrador":
                menuAdministrador(scanner, logController, usuario);
                break;
            case "Empleado":
                menuEmpleado(scanner, logController, usuario);
                break;
            case "Cliente":
                menuCliente(scanner, logController, usuario);
                break;
            default:
                System.out.println("Rol desconocido.");
        }
    }

    /**
     * Menú para usuarios con rol Administrador
     */
    private static void menuAdministrador(Scanner scanner, LogSistemaJpaController logController, Usuario usuario) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMenú Administrador:");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Administración de Clientes");
            System.out.println("3. Registrar Plan de Seguridad");
            System.out.println("4. Asignar Plan a Cliente");
            System.out.println("5. Generar Factura");
            System.out.println("6. Ver Logs");
            System.out.println("7. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarUsuario(scanner, usuario);
                    break;
                case "2":
                    menuAdministracionClientes(scanner, logController, usuario, true);
                    break;
                case "6":
                    mostrarLogs(logController);
                    break;
                case "7":
                    logController.registrarLog("Logout", "Cierre de sesión del administrador", usuario);
                    salir = true;
                    break;
                default:
                    System.out.println("Funcionalidad aún no implementada.");
            }
        }
    }

    /**
     * Menú para usuarios con rol Empleado
     */
    private static void menuEmpleado(Scanner scanner, LogSistemaJpaController logController, Usuario usuario) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMenú Empleado:");
            System.out.println("1. Administración de Clientes");
            System.out.println("2. Asignar Plan a Cliente");
            System.out.println("3. Generar Factura");
            System.out.println("4. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    menuAdministracionClientes(scanner, logController, usuario, false);
                    break;
                case "4":
                    logController.registrarLog("Logout", "Cierre de sesión del empleado", usuario);
                    salir = true;
                    break;
                default:
                    System.out.println("Funcionalidad aún no implementada.");
            }
        }
    }

    /**
     * Menú para usuarios con rol Cliente
     */
    //
  private static void menuCliente(Scanner scanner, LogSistemaJpaController logController, Usuario usuario) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyecProgra2PU");
    ClienteJpaController clienteController = new ClienteJpaController(emf);

    boolean salir = false;
    while (!salir) {
        System.out.println("\nMenú Cliente:");
        System.out.println("1. Ver mis datos");
        System.out.println("2. Ver mis facturas");
        System.out.println("3. Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                // Buscar cliente asociado al usuario actual
                List<Cliente> clientes = clienteController.findClienteEntities();
                Cliente clienteEncontrado = null;
                for (Cliente c : clientes) {
                    if (c.getIdUsuario() != null && c.getIdUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                        clienteEncontrado = c;
                        break;
                    }
                }

                if (clienteEncontrado != null) {
                    System.out.println("\n--- Mis Datos ---");
                    System.out.println("ID: " + clienteEncontrado.getIdCliente());
                    System.out.println("Nombre: " + clienteEncontrado.getNombre());
                    System.out.println("Dirección: " + clienteEncontrado.getDireccion());
                    System.out.println("Teléfono: " + clienteEncontrado.getTelefono());
                    System.out.println("Correo: " + clienteEncontrado.getCorreo());
                    logController.registrarLog("Consulta de Datos", "El cliente visualizó sus datos personales", usuario);
                } else {
                    System.out.println("No se encontraron datos asociados a este usuario.");
                }
                break;

            case "3":
                logController.registrarLog("Logout", "Cierre de sesión del cliente", usuario);
                salir = true;
                break;

            default:
                System.out.println("Funcionalidad aún no implementada.");
        }
    }
}


    /**
     * Registrar un nuevo usuario en el sistema
     */
    private static void registrarUsuario(Scanner scanner, Usuario usuarioActual) {
        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
        LogSistemaJpaController logController = new LogSistemaJpaController(emf);

        System.out.println("\n--- Registro de Nuevo Usuario ---");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contraseña = scanner.nextLine();
        System.out.print("Rol (Administrador / Empleado / Cliente): ");
        String rol = scanner.nextLine();

        if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || rol.isEmpty()) {
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        Usuario existente = usuarioController.validarCredenciales(correo, contraseña);
        if (existente != null) {
            System.out.println("Ya existe un usuario con ese correo.");
            logController.registrarLog("Registro de Usuario", "Intento de registrar usuario duplicado: " + correo, usuarioActual);
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setContraseña(contraseña);
        nuevoUsuario.setRol(rol);

        try {
            usuarioController.create(nuevoUsuario);
            System.out.println("Usuario registrado exitosamente.");
            logController.registrarLog("Registro de Usuario", "Se registró el usuario: " + nombre, usuarioActual);
        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    /**
     * Mostrar los logs del sistema
     */
    private static void mostrarLogs(LogSistemaJpaController logController) {
        System.out.println("\n--- Registros del Sistema ---");
        List<LogSistema> logs = logController.listarLogs();
        if (logs.isEmpty()) {
            System.out.println("No hay registros disponibles.");
        } else {
            for (LogSistema log : logs) {
                System.out.println("[" + log.getFecha() + "] " +
                        log.getAccion() + " - " +
                        log.getDescripcion() + " (Usuario: " +
                        (log.getIdUsuario() != null ? log.getIdUsuario().getNombre() : "Desconocido") + ")");
            }
        }
    }

    // ---------------------------------------------
    //  MÉTODOS CRUD CLIENTES (Completo)
    // ---------------------------------------------

    private static void menuAdministracionClientes(Scanner scanner, LogSistemaJpaController logController, Usuario usuario, boolean esAdministrador) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Administración de Clientes ---");
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Ver Clientes");
            if (esAdministrador) {
                System.out.println("3. Modificar Cliente");
                System.out.println("4. Eliminar Cliente");
                System.out.println("5. Volver");
            } else {
                System.out.println("3. Volver");
            }
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarCliente(scanner, usuario);
                    break;
                case "2":
                    verClientes(usuario);
                    logController.registrarLog("Consulta de Clientes", "El usuario visualizó la lista de clientes", usuario);
                    break;
                case "3":
                    if (esAdministrador) {
                        modificarCliente(scanner, usuario);
                    } else {
                        salir = true;
                    }
                    break;
                case "4":
                    if (esAdministrador) {
                        eliminarCliente(scanner, usuario);
                    } else {
                        System.out.println("Opción inválida.");
                    }
                    break;
                case "5":
                    if (esAdministrador) salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    //update seleccionar el id a crear:
    
    private static void registrarCliente(Scanner scanner, Usuario usuarioActual) {
    ClienteJpaController clienteController = new ClienteJpaController(emf);
    UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
    LogSistemaJpaController logController = new LogSistemaJpaController(emf);

    // Obtener todos los usuarios con rol "Cliente"
    List<Usuario> usuariosCliente = usuarioController.findUsuarioEntities();
    List<Cliente> clientesRegistrados = clienteController.findClienteEntities();

    // Filtrar usuarios que no estén en la tabla Cliente
    List<Usuario> disponibles = new ArrayList<>();
    for (Usuario u : usuariosCliente) {
        if ("Cliente".equalsIgnoreCase(u.getRol())) {
            boolean yaRegistrado = false;
            for (Cliente c : clientesRegistrados) {
                if (c.getIdUsuario() != null && c.getIdUsuario().getIdUsuario().equals(u.getIdUsuario())) {
                    yaRegistrado = true;
                    break;
                }
            }
            if (!yaRegistrado) {
                disponibles.add(u);
            }
        }
    }

    // Mostrar usuarios disponibles
    if (disponibles.isEmpty()) {
        System.out.println("No hay usuarios con rol 'Cliente' disponibles para registrar.");
        return;
    }

    System.out.println("\n--- Usuarios disponibles para registrar como Cliente ---");
    for (Usuario u : disponibles) {
        System.out.println("ID: " + u.getIdUsuario() + " | Nombre: " + u.getNombre() + " | Correo: " + u.getCorreo());
    }

    System.out.print("Ingrese el ID del usuario que desea registrar como cliente: ");
    int idSeleccionado = Integer.parseInt(scanner.nextLine());

    Usuario usuarioSeleccionado = null;
    for (Usuario u : disponibles) {
        if (u.getIdUsuario() == idSeleccionado) {
            usuarioSeleccionado = u;
            break;
        }
    }

    if (usuarioSeleccionado == null) {
        System.out.println("ID inválido o usuario ya registrado como cliente.");
        return;
    }

    // Capturar datos del cliente
    System.out.println("\n--- Registro de Cliente ---");
    System.out.print("Nombre: ");
    String nombre = scanner.nextLine();
    System.out.print("Dirección: ");
    String direccion = scanner.nextLine();
    System.out.print("Teléfono: ");
    String telefono = scanner.nextLine();
    System.out.print("Correo: ");
    String correo = scanner.nextLine();

    if (nombre.isBlank()) {
        System.out.println("El nombre es obligatorio.");
        return;
    }

    Cliente cliente = new Cliente();
    cliente.setNombre(nombre);
    cliente.setDireccion(direccion);
    cliente.setTelefono(telefono);
    cliente.setCorreo(correo);
    cliente.setIdUsuario(usuarioSeleccionado);

    try {
        clienteController.create(cliente);
        System.out.println("Cliente registrado exitosamente.");
        logController.registrarLog("Registro de Cliente", "Se registró el cliente: " + nombre, usuarioActual);
    } catch (Exception e) {
        System.out.println("Error al registrar cliente: " + e.getMessage());
    }
}

    private static void verClientes(Usuario usuarioActual) {
        ClienteJpaController clienteController = new ClienteJpaController(emf);

        List<Cliente> clientes = clienteController.findClienteEntities();
        System.out.println("\n--- Lista de Clientes ---");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            for (Cliente c : clientes) {
                System.out.println("ID: " + c.getIdCliente() +
                        " | Nombre: " + c.getNombre() +
                        " | Dirección: " + c.getDireccion() +
                        " | Teléfono: " + c.getTelefono() +
                        " | Correo: " + c.getCorreo());
            }
        }
    }

    private static void modificarCliente(Scanner scanner, Usuario usuarioActual) {
        ClienteJpaController clienteController = new ClienteJpaController(emf);
        LogSistemaJpaController logController = new LogSistemaJpaController(emf);

        System.out.print("ID del cliente a modificar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Cliente cliente = clienteController.findCliente(id);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre (" + cliente.getNombre() + "): ");
        String nombre = scanner.nextLine();
        System.out.print("Nueva dirección (" + cliente.getDireccion() + "): ");
        String direccion = scanner.nextLine();
        System.out.print("Nuevo teléfono (" + cliente.getTelefono() + "): ");
        String telefono = scanner.nextLine();
        System.out.print("Nuevo correo (" + cliente.getCorreo() + "): ");
        String correo = scanner.nextLine();

        if (!nombre.isBlank()) cliente.setNombre(nombre);
        if (!direccion.isBlank()) cliente.setDireccion(direccion);
        if (!telefono.isBlank()) cliente.setTelefono(telefono);
        if (!correo.isBlank()) cliente.setCorreo(correo);

        try {
            clienteController.edit(cliente);
            System.out.println("Cliente modificado correctamente.");
            logController.registrarLog("Modificación de Cliente", "Se modificó el cliente ID: " + id, usuarioActual);
        } catch (Exception e) {
            System.out.println("Error al modificar cliente: " + e.getMessage());
        }
    }

    private static void eliminarCliente(Scanner scanner, Usuario usuarioActual) {
        ClienteJpaController clienteController = new ClienteJpaController(emf);
        LogSistemaJpaController logController = new LogSistemaJpaController(emf);

        System.out.print("ID del cliente a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Cliente cliente = clienteController.findCliente(id);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("¿Está seguro que desea eliminar al cliente " + cliente.getNombre() + "? (s/n): ");
        String confirmacion = scanner.nextLine();
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        try {
            clienteController.destroy(id);
            System.out.println("Cliente eliminado correctamente.");
            logController.registrarLog("Eliminación de Cliente", "Se eliminó el cliente ID: " + id, usuarioActual);
        } catch (Exception e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
    }
}
