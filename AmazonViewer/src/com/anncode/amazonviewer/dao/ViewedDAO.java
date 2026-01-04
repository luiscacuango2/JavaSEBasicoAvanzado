package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.*;
import com.anncode.amazonviewer.model.Viewed;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Interfaz para la gestión de persistencia de la tabla transaccional {@code viewed}.
 * <p>
 * Proporciona métodos para recuperar el historial de visualización filtrado por fechas,
 * permitiendo la generación de reportes diarios optimizados.
 * </p>
 */
public interface ViewedDAO extends IDBConnection {

    /**
     * Recupera todos los registros de visualización que coinciden con una fecha específica.
     * @param date Fecha para filtrar los registros.
     * @return Lista de objetos {@link Viewed} encontrados.
     */
    default ArrayList<Viewed> readByDate(Date date) {
        ArrayList<Viewed> viewedList = new ArrayList<>();
        String query = "SELECT * FROM " + TViewed.NAME +
                " WHERE DATE(" + TViewed.DATE + ") = ?";

        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(date.getTime()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Viewed viewed = new Viewed(
                            rs.getInt(TViewed.ID_ELEMENT),
                            rs.getInt(TViewed.ID_USER),
                            rs.getInt(TViewed.ID_MATERIAL),
                            rs.getTimestamp(TViewed.DATE)
                    );
                    viewed.setId(rs.getInt(TViewed.ID));
                    viewedList.add(viewed);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar registros por fecha: " + e.getMessage());
        }
        return viewedList;
    }

    /**
     * Recupera todos los registros de la tabla viewed sin filtros.
     * @return Lista completa de objetos {@link Viewed}.
     */
    default ArrayList<Viewed> read() {
        ArrayList<Viewed> viewedList = new ArrayList<>();
        // Consulta simple para traer todo
        String query = "SELECT * FROM " + TViewed.NAME;

        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Mapeo de columnas basándonos en tu constructor y constantes
                Viewed viewed = new Viewed(
                        rs.getInt(TViewed.ID_ELEMENT),  // idItem
                        rs.getInt(TViewed.ID_USER),     // idUser
                        rs.getInt(TViewed.ID_MATERIAL), // idType
                        rs.getTimestamp(TViewed.DATE)   // date
                );
                viewed.setId(rs.getInt(TViewed.ID));
                viewedList.add(viewed);
            }

        } catch (SQLException e) {
            System.err.println("Error al recuperar el histórico total de registros: " + e.getMessage());
        }
        return viewedList;
    }
}