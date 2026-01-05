package com.anncode.amazonviewer.model;

import java.util.Date;

/**
 * Representa la entidad transaccional que registra la visualización o lectura
 * de un material dentro de Amazon Viewer.
 * <p>
 * Esta clase actúa como un objeto de transferencia de datos (DTO) que vincula
 * un elemento específico (Película, Capítulo, Libro, etc.) con un usuario
 * y la fecha en que se realizó la acción.
 * </p>
 * @author Luigi
 * @version 1.3
 * @since 2026-01-04
 */
public class Viewed {
    /** Identificador único del registro en la base de datos */
    private int id;

    /** Identificador del elemento visualizado (ID de Movie, Chapter, Book, etc.) */
    private int idItem;

    /** Identificador del usuario que realizó la acción de visualizar o leer */
    private int idUser;

    /** * Identificador del tipo de material:
     * 1 para Movie, 2 para Serie, 3 para Chapter, 4 para Book
     */
    private int idType;

    /** Fecha y hora exacta en la que se realizó la visualización */
    private Date date;

    /**
     * Constructor para crear una instancia de registro de visualización.
     * @param idItem Identificador del objeto visualizado.
     * @param idUser Identificador del usuario activo.
     * @param idType Tipo de material (1: Movie, 3: Chapter, etc.).
     * @param date   Fecha de la acción.
     */
    public Viewed(int idItem, int idUser, int idType, Date date) {
        this.idItem = idItem;
        this.idUser = idUser;
        this.idType = idType;
        this.date = date;
    }

    /**
     * Obtiene el identificador único del registro.
     * @return Un {@code int} con el ID de la transacción.
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el identificador único del registro (usado por el DAO).
     * @param id El ID recuperado de la base de datos.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID del elemento visualizado.
     * @return El identificador del material específico.
     */
    public int getIdItem() {
        return idItem;
    }

    /**
     * Define el ID del elemento visualizado.
     * @param idItem El ID del material a asignar.
     */
    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    /**
     * Obtiene el ID del usuario asociado al registro.
     * @return El identificador del usuario.
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     * Define el ID del usuario que realizó la acción.
     * @param idUser El ID del usuario a asignar.
     */
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    /**
     * Obtiene el tipo de material visualizado.
     * @return El ID que representa el tipo (Movie, Chapter, etc.).
     */
    public int getIdType() {
        return idType;
    }

    /**
     * Define el tipo de material visualizado.
     * @param idType El identificador de tipo a asignar.
     */
    public void setIdType(int idType) {
        this.idType = idType;
    }

    /**
     * Obtiene la fecha de visualización.
     * @return Objeto {@link Date} con la marca de tiempo.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Asigna la fecha de visualización.
     * @param date La fecha a establecer.
     */
    public void setDate(Date date) {
        this.date = date;
    }
}