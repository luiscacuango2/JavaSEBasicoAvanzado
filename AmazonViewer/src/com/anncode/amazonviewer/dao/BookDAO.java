package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.Main;
import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.*;
import com.anncode.amazonviewer.model.Book;
import java.sql.*;
import java.util.ArrayList;

public interface BookDAO extends IDBConnection {

    /**
     * Registra en la base de datos que un libro ha sido leído.
     * @param book El libro leído.
     * @return El libro con su estado actualizado.
     */
    default Book setBookRead(Book book) {
        String query = "INSERT INTO " + TViewed.NAME +
                " (" + TViewed.ID_MATERIAL + ", " +
                TViewed.ID_ELEMENT + ", " +
                TViewed.ID_USER + ", " +
                TViewed.DATE + ") " +
                " VALUES (?, ?, ?, ?)";

        try (Connection connection = connectToDB()) {
            int idMaterial = getMaterialIdByName("Book", connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, book.getId());
                pstmt.setInt(3, Main.activeUser.getId()); // ID Usuario Dinámico
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

                pstmt.executeUpdate();
                book.setReaded(true);

            }
        } catch (SQLException e) {
            System.err.println("Error al marcar libro como leído: " + e.getMessage());
        }
        return book;
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

    default ArrayList<Book> read() {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT * FROM " + TBook.NAME;
        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // 1. Convertimos la fecha de SQL a java.util.Date
                java.util.Date editionDate = new java.util.Date(rs.getDate(TBook.EDITION_DATE).getTime());

                // 2. Preparamos los campos que el constructor requiere (aunque estén vacíos por ahora)
                String[] authors = new String[0]; // O podrías traerlos de otra tabla
                ArrayList<Book.Page> pages = new ArrayList<>();

                // 3. Llamamos al constructor con los tipos correctos
                Book book = new Book(
                        rs.getString(TBook.TITLE),
                        editionDate,
                        rs.getString(TBook.EDITORIAL),
                        authors, // Requiere String[]
                        pages    // Requiere ArrayList<Page>
                );

                book.setId(rs.getInt(TBook.ID));
                book.setIsbn(rs.getString(TBook.ISBN));
                book.setReaded(getBookRead(connection, book.getId()));

                books.add(book);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }

    private boolean getBookRead(Connection connection, int idBook) throws SQLException {
        boolean readed = false;
        String query = "SELECT * FROM " + TViewed.NAME +
                " WHERE " + TViewed.ID_MATERIAL + " = ?" + // Dinámico
                " AND " + TViewed.ID_ELEMENT + " = ?" +
                " AND " + TViewed.ID_USER + " = ?";

        try {
            int idMaterial = getMaterialIdByName("Book", connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, idBook);
                pstmt.setInt(3, Main.activeUser.getId()); // Solo lo que el usuario actual vio
                try (ResultSet rs = pstmt.executeQuery()) {
                    readed = rs.next();
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return readed;
    }
}