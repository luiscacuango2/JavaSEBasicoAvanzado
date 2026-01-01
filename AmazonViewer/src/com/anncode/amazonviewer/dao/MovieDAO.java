package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.db.DataBase;
import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.*; // Importamos nuestras constantes
import com.anncode.amazonviewer.model.Movie;

import java.sql.*;
import java.util.ArrayList;

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

        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, 1); // Supongamos que 1 es el ID para el material 'Movie'
            pstmt.setInt(2, movie.getId());
            pstmt.setInt(3, 1); // ID del usuario actual (por ahora estático)
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            pstmt.executeUpdate();
            movie.setViewed(true);

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
     * Consulta interna para verificar el estado de visualización en la DB.
     * @return {@code true} si la película ha sido vista.
     */
    private boolean getMovieViewed(Connection connection, int idMovie) {
        boolean viewed = false;
        // Query mucho más robusta y legible
        String query = "SELECT * FROM " + TViewed.NAME +
                " WHERE " + TViewed.ID_MATERIAL + " = ?" +
                " AND " + TViewed.ID_ELEMENT + " = ?" +
                " AND " + TViewed.ID_USER + " = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, idMovie);
            preparedStatement.setInt(3, 1);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                viewed = rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar estado 'visto': " + e.getMessage());
        }
        return viewed;
    }
}