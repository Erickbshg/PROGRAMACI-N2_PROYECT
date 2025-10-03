package com.mycompany.proyecprogra2;

import com.mycompany.proyecprogra2.gt.edu.umg.bd.Usuario;
import com.mycompany.proyecprogra2.gt.edu.umg.bd.Cliente;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
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
    }

    private static void iniciarSesion(Scanner scanner) {
        System.out.println("----------------[ Login ]----------------------");
        System.out.print("-Correo: ");
        String correo = scanner.nextLine();
        System.out.print("-Contraseña: ");
        String contrasena = scanner.nextLine();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyecProgra2PU");
        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
        LogSistemaJpaController logController = new LogSistemaJpaController(emf);

        Usuario usuario = usuarioController.validarCredenciales(correo, contrasena);

        if (usuario == null) {
            System.out.println("Credenciales inválidas.");
            return;
        }

        logController.registrarLog("Login", "Inicio de sesión exitoso", usuario);
        System.out.println("Bienvenido, " + usuario.getNombre() + " [" + usuario.getRol() + "]");

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
                    System.out.println("\n--- Registros del Sistema ---");
                    List<com.mycompany.proyecprogra2.gt.edu.umg.bd.LogSistema> logs = logController.listarLogs();
                    if (logs.isEmpty()) {
                        System.out.println("No hay registros disponibles.");
                    } else {
                        for (com.mycompany.proyecprogra2.gt.edu.umg.bd.LogSistema log : logs) {
                            System.out.println("[" + log.getFecha() + "] " +
                                               log.getAccion() + " - " +
                                               log.getDescripcion() + " (Usuario: " +
                                               (log.getIdUsuario() != null ? log.getIdUsuario().getNombre() : "Desconocido") + ")");
                        }
                    }
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

    private static void menuCliente(Scanner scanner, LogSistemaJpaController logController, Usuario usuario) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMenú Cliente:");
            System.out.println("1. Ver mis datos");
            System.out.println("2. Ver mis facturas");
            System.out.println("3. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "3":
                    logController.registrarLog("Logout", "Cierre de sesión del cliente", usuario);
                    salir = true;
                    break;
                default:
                    System.out.println("Funcionalidad aún no implementada.");
            }
        }
    }

    private static void registrarUsuario(Scanner scanner, Usuario usuarioActual) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyecProgra2PU");
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

    private static void registrarCliente(Scanner scanner, Usuario usuarioActual) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyecProgra2PU");
        ClienteJpaController clienteController = new Cliente