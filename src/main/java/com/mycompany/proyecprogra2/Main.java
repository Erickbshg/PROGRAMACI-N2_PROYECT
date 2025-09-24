package com.mycompany.proyecprogra2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("######################################");
            System.out.println("# Bienvenido al Sistema de Seguridad #");
            System.out.println("#1. Iniciar Sesión                   #");
            System.out.println("#2. Salir                            #");
            System.out.println("#3. Ver Datos Demo                   #");
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
                case "3":
                    System.out.println("Es una demo jeje");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private static void iniciarSesion(Scanner scanner) {
        System.out.println("----------------[ Login ]----------------------");
        System.out.println("-Correo: ");
        String correo = scanner.nextLine();
        System.out.println("-Contraseña: ");
       
        
        String contraseña = scanner.nextLine();

        String rol = obtenerRolDesdeBD(correo, contraseña); // Simulado por ahora

        if (rol == null) {
            System.out.println("Credenciales inválidas.");
            return;
        }

        switch (rol) {
            case "Administrador":
                menuAdministrador(scanner);
                break;
            case "Empleado":
                menuEmpleado(scanner);
                break;
            case "Cliente":
                menuCliente(scanner);
                break;
            default:
                System.out.println("Rol desconocido.");
        }
    }

    private static String obtenerRolDesdeBD(String correo, String contraseña) {
        if (correo.equals("admin@seguridad.com") && contraseña.equals("admin123")) return "Administrador";
        if (correo.equals("empleado@seguridad.com") && contraseña.equals("empleado123")) return "Empleado";
        if (correo.equals("cliente@seguridad.com") && contraseña.equals("cliente123")) return "Cliente";
        return null;
    }

    private static void menuAdministrador(Scanner scanner) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMenú Administrador:");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Registrar Cliente");
            System.out.println("3. Registrar Plan de Seguridad");
            System.out.println("4. Asignar Plan a Cliente");
            System.out.println("5. Generar Factura");
            System.out.println("6. Ver Logs");
            System.out.println("7. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "7":
                    salir = true;
                    break;
                default:
                    System.out.println("Funcionalidad aún no implementada.");
            }
        }
    }

    private static void menuEmpleado(Scanner scanner) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMenú Empleado:");
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Asignar Plan a Cliente");
            System.out.println("3. Generar Factura");
            System.out.println("4. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "4":
                    salir = true;
                    break;
                default:
                    System.out.println("Funcionalidad aún no implementada.");
            }
        }
    }

    private static void menuCliente(Scanner scanner) {
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
                    salir = true;
                    break;
                default:
                    System.out.println("Funcionalidad aún no implementada.");
            }
        }
    }
}
