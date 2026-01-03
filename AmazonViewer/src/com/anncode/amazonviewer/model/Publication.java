package com.anncode.amazonviewer.model;

import java.util.Date;

/**
 * Clase base para todas las publicaciones impresas o digitales.
 * <p>
 * Agrupa atributos comunes como título, fecha de edición y editorial.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Publication {

    /** Título de la publicación */
    private String title;
    /** Fecha en la que se realizó la edición */
    private Date editionDate;
    /** Nombre de la empresa editorial */
    private String editorial;
    /** Listado de nombres de los autores de la publicación */
    private String authors;
    /** Estado de visualización de la publicación */
    private boolean readed;

    /**
     * Constructor para inicializar los datos generales de una publicación.
     * @param title Título de la obra.
     * @param editionDate Fecha de publicación o edición.
     * @param editorial Nombre de la editorial encargada.
     */
    public Publication(String title, Date editionDate, String editorial) {
        super();
        this.title = title;
        this.editionDate = editionDate;
        this.editorial = editorial;
    }

    /**
     * Recupera el título de la obra.
     * @return El título de la publicación.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Define el título de la obra.
     * @param title El título a asignar a la obra.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Recupera la fecha de edición de la obra.
     * @return El objeto {@code Date} con la fecha de edición.
     */
    public Date getEditionDate() {
        return editionDate;
    }

    /**
     * Define la fecha de edición de la obra.
     * @param editionDate La fecha de edición a establecer.
     */
    public void setEditionDate(Date editionDate) {
        this.editionDate = editionDate;
    }

    /**
     * Recupera el nombre de la editorial.
     * @return El nombre de la editorial.
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Define el nombre de la editorial.
     * @param editorial La editorial a asignar.
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * Obtiene el listado de autores asociados a la publicación.
     * @return Un arreglo de {@code String} con los nombres de los autores.
     */
    public String[] getAuthors() {
        if (authors != null && !authors.isEmpty()) {
            return authors.split(","); // Si en la DB están separados por comas, los divide
        }
        return new String[0];
    }

    /**
     * Establece los autores de la publicación.
     * @param authors Un arreglo de {@code String} que contiene los autores.
     */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /**
     * Devuelve una cadena de texto indicando si la publicación fue leído.
     * @return "Sí" si fue leído, "No" en caso contrario.
     */
    public String isReaded() {
        return readed ? "Sí" : "No";
    }

    /**
     * Recupera si la publicación ha sido leído.
     * @return {@code true} si la publicación ha sido leído.
     * */
    public boolean getIsReaded() {
        return readed;
    }

    /**
     * Define el estado de visualización de la publicación.
     * @param readed El estado a establecer.
     */
    public void setReaded(boolean readed) {
        this.readed = readed;
    }
}