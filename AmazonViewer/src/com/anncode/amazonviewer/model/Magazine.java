package com.anncode.amazonviewer.model;

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
public class Magazine extends Publication {

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
        String detailMagazine = "\n :: MAGAZINE ::" +
                "\n Title: " + getTitle() +
                "\n Editorial: " + getEditorial() +
                "\n Edition Date: " + getEdititionDate() +
                "\n Authors: ";
        for (int i = 0; i < getAuthors().length; i++) {
            detailMagazine += "\t" + getAuthors()[i] + " ";
        }
        return  detailMagazine;
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
        ArrayList<Magazine> magazines = new ArrayList();
        String[] authors = new String[3];
        for (int i = 0; i < 3; i++) {
            authors[i] = "author "+i;
        }
        for (int i = 1; i <= 5; i++) {
            magazines.add(new Magazine("Magazine " + i, new Date(), "Editorial " + i));
        }

        return magazines;
    }

}