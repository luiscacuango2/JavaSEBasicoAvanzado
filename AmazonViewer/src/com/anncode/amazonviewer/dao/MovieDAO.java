package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.Main;
import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.*; // Importamos nuestras constantes
import com.anncode.amazonviewer.model.Movie;
import com.anncode.amazonviewer.model.User;

import java.sql.*;
import java.util.ArrayList;

public interface MovieDAO extends IDBConnection {

    /**
     * Busca un usuario en la base de datos por su nombre.
     * @param name Nombre del usuario a buscar.
     * @return Objeto User con su ID de la base de datos.
     */
    default User getUserByName(String name) {
        User user = new User(name);
        String query = "SELECT * FROM " + TUser.NAME +
                " WHERE " + TUser.USERNAME + " = ?";

        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt(TUser.ID));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return user;
    }

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
            int idMaterial = getMaterialIdByName("Movie", connection);

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
     * Busca dinámicamente el ID de un material por su nombre en la tabla 'material'.
     */
    default int getMaterialIdByName(String materialName, Connection connection) throws SQLException {
        int idMaterial = 0;
        String query = "SELECT " + TMaterial.ID + " FROM " + TMaterial.NAME +
                " WHERE " + TMaterial.NAME_COL + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, materialName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idMaterial = rs.getInt(TMaterial.ID);
                }
            }
        }
        return idMaterial;
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
        String query = "SELECT * FROM " + TViewed.NAME +
                " WHERE " + TViewed.ID_MATERIAL + " = ?" + // Dinámico
                " AND " + TViewed.ID_ELEMENT + " = ?" +
                " AND " + TViewed.ID_USER + " = ?";        // Dinámico

        try {
            int idMaterial = getMaterialIdByName("Movie", connection);

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