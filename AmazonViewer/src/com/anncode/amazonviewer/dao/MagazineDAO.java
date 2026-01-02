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
                " (" + TViewed.ID_MATERIAL + ", " +
                TViewed.ID_ELEMENT + ", " +
                TViewed.ID_USER + ", " +
                TViewed.DATE + ") " +
                " VALUES (?, ?, ?, ?)";

        try (Connection connection = connectToDB()) {
            // Obtenemos el ID del material "Magazine" dinámicamente
            int idMaterial = getMaterialIdByName("Magazine", connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);            // ID Material Dinámico
                pstmt.setInt(2, magazine.getId());      // ID Revista Dinámico
                pstmt.setInt(3, Main.activeUser.getId()); // ID Usuario de la sesión actual
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

                pstmt.executeUpdate();

                // Actualizamos el estado en el objeto (usando el método del modelo)
                magazine.setReaded(true);

            }
        } catch (SQLException e) {
            System.err.println("Error al marcar la revista como leída: " + e.getMessage());
        }
        return magazine;
    }

    /**
     * Busca dinámicamente el ID de un material por su nombre en la tabla 'material'.
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

    default ArrayList<Magazine> read() {
        ArrayList<Magazine> magazines = new ArrayList<>();
        String query = "SELECT * FROM " + TMagazine.NAME;
        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // 1. Extraer datos de la base de datos
                String title = rs.getString(TMagazine.TITLE);
                String editorial = rs.getString(TMagazine.EDITORIAL);

                // 2. CORRECCIÓN DE TIPO: Convertir java.sql.Date a java.util.Date
                // El constructor requiere java.util.Date
                java.util.Date editionDate = new java.util.Date(rs.getDate(TMagazine.EDITION_DATE).getTime());

                // 3. LLAMADA AL CONSTRUCTOR (Solo 3 parámetros requeridos)
                Magazine magazine = new Magazine(
                        title,
                        editionDate,
                        editorial
                );

                // 4. SETTERS para el resto de la información dinámica
                magazine.setId(rs.getInt(TMagazine.ID));
                magazine.setReaded(getMagazineRead(connection, magazine.getId()));
                // Si necesitas guardar el ISBN o similar que no está en el constructor:
                // magazine.setIsbn(rs.getString(TMagazine.ISBN));

                magazines.add(magazine);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return magazines;
    }

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
