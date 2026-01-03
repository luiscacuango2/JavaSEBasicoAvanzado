package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.Main;
import com.anncode.amazonviewer.db.DataBase;
import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.TSerie;
import com.anncode.amazonviewer.db.DataBase.TViewed;
import com.anncode.amazonviewer.db.DataBase.MaterialNames;
import com.anncode.amazonviewer.model.Serie;
import java.sql.*;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de persistencia para el objeto {@link Serie}.
 * <p>
 * Hereda de {@link IDBConnection} para la gestión de la conexión con la base de datos MySQL.
 * Su función principal es recuperar la estructura de las series de televisión y
 * coordinar con {@link ChapterDAO} la carga de sus episodios correspondientes,
 * permitiendo una visualización íntegra del catálogo.
 * </p>
 *
 * @author Luigi
 * @version 1.3
 * @since 2026-01-03
 */
public interface SerieDAO extends IDBConnection {

    /**
     * Lee todas las series de la base de datos.
     * @return Lista de objetos Serie.
     */
    default ArrayList<Serie> read() {
        ArrayList<Serie> series = new ArrayList<>();
        String query = "SELECT * FROM " + TSerie.NAME;
        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            // Dentro del método read() de SerieDAO
            while (rs.next()) {
                // Obtenemos los datos de la base de datos
                String title = rs.getString(TSerie.TITLE);
                String genre = rs.getString(TSerie.GENRE);
                String creator = rs.getString(TSerie.CREATOR);
                int duration = rs.getInt(TSerie.DURATION);

                // Suponiendo que el 5to parámetro es la cantidad de temporadas
                // y que tienes esa columna en tu DB (por ejemplo TSerie.SESSION_QUANTITY)
                int year = rs.getInt(TSerie.YEAR);

                // LLAMADA CORRECTA CON LOS 5 PARÁMETROS
                Serie serie = new Serie(
                        title,
                        genre,
                        creator,
                        duration,
                        year
                );

                serie.setId(rs.getInt(TSerie.ID));
                serie.setViewed(getSerieViewed(connection, serie.getId()));

                series.add(serie);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return series;
    }

    private boolean getSerieViewed(Connection connection, int idSerie) {
        boolean viewed = false;
        String query = "SELECT * FROM " + TViewed.NAME +
                " WHERE " + TViewed.ID_MATERIAL + " = ?" +
                " AND " + TViewed.ID_ELEMENT + " = ?" +
                " AND " + TViewed.ID_USER + " = ?";
        try {
            int idMaterial = getMaterialIdByName(MaterialNames.SERIE, connection);
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, idSerie);
                pstmt.setInt(3, Main.activeUser.getId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    viewed = rs.next();
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return viewed;
    }

    default void setSerieViewed(Serie serie) {
        String query = "INSERT INTO " + TViewed.NAME +
                " (" + TViewed.ID_MATERIAL + ", " + TViewed.ID_ELEMENT + ", " + TViewed.ID_USER + ", " + TViewed.DATE + ") " +
                " VALUES (?, ?, ?, ?)";

        try (Connection connection = connectToDB()) {
            // Obtenemos el ID del material "Serie"
            int idMaterial = getMaterialIdByName(MaterialNames.SERIE, connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, serie.getId());
                pstmt.setInt(3, Main.activeUser.getId());
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}