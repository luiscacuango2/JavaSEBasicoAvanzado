package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.Main;
import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.*; // Importamos nuestras constantes
import com.anncode.amazonviewer.model.Movie;

import java.sql.*;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de persistencia para el objeto {@link Movie}.
 * <p>
 * Hereda de {@link IDBConnection} para facilitar el acceso a la base de datos MySQL.
 * Su responsabilidad principal es gestionar la carga del catálogo de películas y
 * coordinar el registro de visualización (estado "visto") mediante la interacción
 * con la tabla de transacciones {@code viewed}.
 * </p>
 * @author Luigi
 * @version 1.3
 * @since 2026-01-03
 */
public interface MovieDAO extends IDBConnection {

    /**
     * Registra en la base de datos que una película ha sido vista.
     * @param movie La película seleccionada.
     * @return La película con su estado actualizado.
     */
    default Movie setMovieViewed(Movie movie) {
        String query = "INSERT INTO " + TViewed.NAME +
                " (" + TViewed.ID_MATERIAL + ", " +
                TViewed.ID_ELEMENT + ", " +
                TViewed.ID_USER + ", " +
                TViewed.DATE + ") " +
                " VALUES (?, ?, ?, ?)";

        try (Connection connection = connectToDB()) {
            // OBTENCIÓN DINÁMICA DEL ID DE MATERIAL
            int idMaterial = getMaterialIdByName(MaterialNames.MOVIE, connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);            // ID Material Dinámico
                pstmt.setInt(2, movie.getId());         // ID Elemento Dinámico
                pstmt.setInt(3, Main.activeUser.getId()); // ID Usuario Dinámico
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

                pstmt.executeUpdate();
                movie.setViewed(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al marcar película como vista: " + e.getMessage());
        }
        return movie;
    }


    /**
     * Lee todas las películas de la base de datos y verifica si han sido vistas.
     * @return Lista de objetos Movie.
     */
    default ArrayList<Movie> read() {
        ArrayList<Movie> movies = new ArrayList<>();
        // Query utilizando constantes
        String query = "SELECT * FROM " + TMovie.NAME;

        try (Connection connection = connectToDB();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getString(TMovie.TITLE),
                        rs.getString(TMovie.GENRE),
                        rs.getString(TMovie.CREATOR),
                        rs.getInt(TMovie.DURATION),
                        rs.getShort(TMovie.YEAR)
                );

                movie.setId(rs.getInt(TMovie.ID));
                movie.setViewed(getMovieViewed(connection, movie.getId()));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer películas: " + e.getMessage());
        }
        return movies;
    }

    /**
     * Consulta interna para verificar el estado de visualización de una película en la base de datos.
     * <p>
     * Este método privado realiza una búsqueda en la tabla {@code viewed} filtrando por el
     * identificador del material (tipo Película), el ID de la película específica y el ID
     * del usuario activo. Permite determinar si existe una coincidencia que valide que
     * el contenido ya fue consumido por el perfil actual.
     * </p>
     *
     * @param connection La conexión activa a la base de datos proporcionada por el método {@code read()}.
     * @param idMovie    El identificador único de la {@link Movie} que se desea verificar.
     * @return {@code true} si existe un registro de visualización para este usuario y película;
     * {@code false} en caso contrario o ante cualquier error de SQL.
     */
    private boolean getMovieViewed(Connection connection, int idMovie) {
        boolean viewed = false;
        String query = "SELECT * FROM " + TViewed.NAME +
                " WHERE " + TViewed.ID_MATERIAL + " = ?" + // Dinámico
                " AND " + TViewed.ID_ELEMENT + " = ?" +
                " AND " + TViewed.ID_USER + " = ?";        // Dinámico

        try {
            int idMaterial = getMaterialIdByName(MaterialNames.MOVIE, connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, idMovie);
                pstmt.setInt(3, Main.activeUser.getId()); // Solo lo que el usuario actual vio

                try (ResultSet rs = pstmt.executeQuery()) {
                    viewed = rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return viewed;
    }
}