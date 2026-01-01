package com.anncode.util;

import java.util.Scanner;

/**
 * Clase que contiene métodos de utilidad para la gestión de datos y validaciones
 * en toda la aplicación Amazon Viewer.
 * <p>
 * Proporciona herramientas para el manejo de entrada por consola, asegurando
 * que el flujo del programa no se interrumpa por errores de tipo de dato.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class AmazonUtil {

    /**
     * Valida la respuesta del usuario para asegurar que sea un entero y que se
     * encuentre dentro de un rango específico.
     * <p>
     * El método utiliza un {@link Scanner} para leer la entrada. Si el usuario ingresa
     * un valor no numérico o fuera de los límites, se solicita la entrada nuevamente
     * de forma recursiva (mediante bucles) hasta obtener una opción válida.
     * </p>
     * @param min El valor mínimo aceptable (inclusive).
     * @param max El valor máximo aceptable (inclusive).
     * @return {@code int} con la respuesta validada del usuario.
     */
    public static int validateUserResponseMenu(int min, int max) {
        //Leer la respuesta del usuario
        Scanner sc = new Scanner(System.in);

        //Validar respuesta int
        while(!sc.hasNextInt()) {
            sc.next();
            System.out.println("No ingresaste una opción válida");
            System.out.println("Intenta otra vez");
        }

        int response = sc.nextInt();

        //Validar rango de respuesta
        while(response < min || response > max) {
            //Solicitar de nuevo la respuesta
            System.out.println("No ingresaste una opción válida");
            System.out.println("Intenta otra vez");

            while(!sc.hasNextInt()) {
                sc.next();
                System.out.println("No ingresaste una opción válida");
                System.out.println("Intenta otra vez");
            }
            response = sc.nextInt();
        }
        System.out.println("Tu Respuesta fue: " + response + "\n");
        return response;
    }

}