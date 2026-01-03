package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.Main;
import com.anncode.amazonviewer.db.DataBase;
import com.anncode.amazonviewer.db.DataBase.TViewed;
import com.anncode.amazonviewer.db.DataBase.TMagazine;
import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.model.Magazine;

import java.sql.*;
import java.util.ArrayList;

public interface MagazineDAO extends IDBConnection {
    /**
     * Registra en la base de datos que una revista ha sido leída/vista.
     * @param magazine La revista seleccionada.
     * @return La revista con su estado actualizado.
     */
    default Magazine setMagazineRead(Magazine magazine) {
        String query = "INSERT INTO " + TViewed.NAME +
                " (" + TViewed.ID_MATERIAL + ", " + TViewed.ID_ELEMENT + ", " + TViewed.ID_USER + ", " + TViewed.DATE + ") " +
                " VALUES (?, ?, ?, ?)";

        try (Connection connection = connectToDB()) {
            int idMaterial = getMaterialIdByName("Magazine", connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, magazine.getId());
                pstmt.setInt(3, Main.activeUser.getId());
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                pstmt.executeUpdate();

                // magazine.setReaded(true); // Solo si tu modelo Magazine tiene este atributo
            }
        } catch (SQLException e) {
            System.err.println("Error al marcar la revista como leída: " + e.getMessage());
        }
        return magazine;
    }

    /**
     * Busca dinámicamente el ID de un material por su nombre en la tabla 'material'.
     * @return El ID del material.
     */
    default int getMaterialIdByName(String materialName, Connection connection) throws SQLException {
        int idMaterial = 0;
        String query = "SELECT " + DataBase.TMaterial.ID + " FROM " + DataBase.TMaterial.NAME +
                " WHERE " + DataBase.TMaterial.NAME_COL + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, materialName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idMaterial = rs.getInt(DataBase.TMaterial.ID);
                }
            }
        }
        return idMaterial;
    }

    /**
     * Método que saca de la base de datos un listado de todas las revistas.
     * @return Una lista de revistas.
     */
    default ArrayList<Magazine> read() {
        ArrayList<Magazine> magazines = new ArrayList<>();
        String query = "SELECT * FROM " + TMagazine.NAME;
        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString(TMagazine.TITLE);
                String editorial = rs.getString(TMagazine.EDITORIAL);
                java.util.Date editionDate = new java.util.Date(rs.getDate(TMagazine.EDITION_DATE).getTime());

                Magazine magazine = new Magazine(title, editionDate, editorial);
                magazine.setId(rs.getInt(TMagazine.ID));
                magazine.setAuthors(rs.getString("authors"));
                magazine.setReaded(getMagazineRead(connection, magazine.getId()));

                magazines.add(magazine);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer revistas: " + e.getMessage());
        }
        return magazines;
    }

    /**
     * Obtiene el estado de lectura de una revista.
     * @param connection Conexión a la base de datos.
     * @param idMagazine ID de la revista.
     * @return {@code true} si la revista ha sido leída, {@code false} en caso contrario.
     */
    private boolean getMagazineRead(Connection connection, int idMagazine) {
        boolean read = false;
        // La query es la misma para todos los materiales, lo que cambia es el ID_MATERIAL que enviamos
        String query = "SELECT * FROM " + TViewed.NAME +
                " WHERE " + TViewed.ID_MATERIAL + " = ?" +
                " AND " + TViewed.ID_ELEMENT + " = ?" +
                " AND " + TViewed.ID_USER + " = ?";
        try {
            // Obtenemos dinámicamente el ID del material "Magazine"
            int idMaterial = getMaterialIdByName("Magazine", connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, idMagazine);
                // Filtramos por el usuario que tiene la sesión iniciada en el Main
                pstmt.setInt(3, Main.activeUser.getId());

                try (ResultSet rs = pstmt.executeQuery()) {
                    // Si existe al menos un registro, rs.next() será true
                    read = rs.next();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar estado de lectura de la revista: " + e.getMessage());
        }
        return read;
    }
}
