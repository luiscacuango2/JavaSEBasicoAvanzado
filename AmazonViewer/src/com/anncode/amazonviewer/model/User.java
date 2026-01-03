package com.anncode.amazonviewer.model;

/**
 * Representa al usuario activo dentro de la aplicación.
 * <p>
 * Esta clase se encarga de gestionar la identidad del perfil que navega
 * por el catálogo. Su identificador único ({@code id}) es esencial para
 * realizar el seguimiento de visualización en la tabla {@code viewed}
 * de la base de datos MySQL.
 * </p>
 * @author Luigi
 * @version 1.3
 * @since 2026-01-03
 */
public class User {
    private int id;
    private String name;

    /**
     * Constructor para inicializar un usuario con su nombre.
     * @param name Nombre del perfil de usuario (ej: "Invitado" o un nombre real).
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * Obtiene el ID único del usuario asignado por la base de datos.
     * @return {@code int} con el identificador de persistencia.
     */
    public int getId() {
        return id;
    }

    /**
     * Define el ID del usuario. Generalmente invocado por {@code UserDAO}
     * tras el registro o inicio de sesión.
     * @param id El identificador numérico único.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre descriptivo del usuario.
     * @return {@code String} con el nombre del perfil.
     */
    public String getName() {
        return name;
    }

    /**
     * Actualiza el nombre del perfil del usuario.
     * @param name El nuevo nombre a asignar.
     */
    public void setName(String name) {
        this.name = name;
    }
}