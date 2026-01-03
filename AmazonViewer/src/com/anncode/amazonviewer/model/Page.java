package com.anncode.amazonviewer.model;

/**
 * Representa la unidad mínima de contenido dentro de un {@link Book}.
 * <p>
 * Esta clase encapsula la información de una página individual, permitiendo
 * la navegación estructurada a través de un número de página y su contenido textual.
 * </p>
 * @author Luigi
 * @version 1.3
 * @since 2026-01-03
 */
public class Page {
    private int id;
    private int number;
    private String content;

    /**
     * Constructor para crear una página con su numeración y contenido.
     * @param number El número secuencial de la página dentro del libro.
     * @param content El texto o información que contiene la página.
     */
    public Page(int number, String content) {
        this.number = number;
        this.content = content;
    }

    /**
     * Obtiene el identificador único de la página en la base de datos.
     * @return {@code int} con el ID de persistencia.
     */
    public int getId() {
        return id;
    }

    /**
     * Define el identificador único de la página. Utilizado principalmente
     * por la capa de datos al sincronizar con la DB.
     * @param id El identificador numérico proveniente de la base de datos.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el número secuencial de la página dentro del libro.
     * @return {@code int} que indica la posición de la página (ej. Página 1, 2, etc.).
     */
    public int getNumber() {
        return number;
    }

    /**
     * Asigna el número de orden de la página.
     * @param number El número de página para la navegación del usuario.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Obtiene el cuerpo de texto o contenido de la página.
     * @return {@code String} con la información textual de la página.
     */
    public String getContent() {
        return content;
    }

    /**
     * Define el contenido textual de la página.
     * @param content El texto que el usuario visualizará al leer.
     */
    public void setContent(String content) {
        this.content = content;
    }
}