package com.anncode.amazonviewer.model;

import com.anncode.amazonviewer.dao.MagazineDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * <h2>Magazine</h2>
 * Representa las publicaciones periódicas o revistas dentro de la aplicación.
 * <p>
 * Al heredar de {@link Publication}, comparte atributos como título, fecha de edición
 * y editorial. A diferencia de otros elementos de lectura, las revistas no registran
 * métricas de tiempo de visualización.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Magazine extends Publication implements MagazineDAO {

    /** Identificador único de la revista */
    private int id;

    /**
     * Constructor para inicializar una revista con sus datos básicos.
     * @param title Título de la revista.
     * @param edititionDate Fecha en la que se editó la revista.
     * @param editorial Nombre de la casa editorial encargada.
     */
    public Magazine(String title, Date edititionDate, String editorial) {
        super(title, edititionDate, editorial);
    }

    /**
     * Obtiene el identificador único de la revista.
     * @return El identificador único del ejemplar.
     */
    public int getId() {
        return id;
    }

    /**
     * Define el identificador único de la revista.
     * @param id El identificador a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Construye una ficha técnica detallada de la revista, incluyendo su
     * título, editorial y la lista de autores asociados.
     * </p>
     */
    @Override
    public String toString() {
        // Creamos el formato deseado: día/mes/año
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormated = sdf.format(getEditionDate());

        String detailMagazine = "\n :: MAGAZINE ::" +
                    "\n Title: " + getTitle() +
                    "\n Editorial: " + getEditorial() +
                    "\n Edition Date: " + dateFormated + // Usamos la fecha formateada
                    "\n Authors: ";
        // Obtenemos el arreglo de autores
        String[] authors = getAuthors();

        if (authors != null && authors.length > 0) {
            for (int i = 0; i < authors.length; i++) {
                // Añadimos el autor y una coma si no es el último
                detailMagazine += authors[i] + (i < authors.length - 1 ? ", " : "");
            }
        } else {
            detailMagazine += "Sin autores registrados";
        }
        return detailMagazine;
    }

    /**
     * Método de utilidad que genera una lista predefinida de revistas.
     * <p>
     * Se utiliza para poblar la interfaz de usuario con datos de prueba
     * durante la ejecución inicial del programa.
     * </p>
     * @return Un {@code ArrayList} con 5 ejemplares de {@link Magazine} inicializados.
     */
    public static ArrayList<Magazine> makeMagazineList() {
        MagazineDAO magazineDAO = new MagazineDAO() {};
        return magazineDAO.read();
    }

    /**
     * Método que simula la visualización de una revista.
     * Al ejecutarse, muestra la ficha técnica y marca automáticamente
     * la revista como leída en la Base de Datos.
     */
    public void view() {
        // 1. Marcamos como leído en el objeto (Memoria)
        setReaded(true);

        // 2. Persistencia: Guardar en la tabla 'viewed' de MySQL
        // Como Magazine implementa MagazineDAO, podemos llamar al método default
        this.setMagazineRead(this);

        // 3. Feedback visual para el usuario
        System.out.println("\n==============================================");
        System.out.println(" ABRIENDO REVISTA: " + getTitle().toUpperCase());
        System.out.println("==============================================");

        // Simulación de carga rápida
        try {
            System.out.print("Cargando contenido");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
            System.out.println("\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 4. Mostrar la información técnica (toString corregido anteriormente)
        System.out.println(this.toString());

        System.out.println("\n----------------------------------------------");
        System.out.println(" La revista ha sido marcada como leída.");
        System.out.println("----------------------------------------------\n");
    }
}