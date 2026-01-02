package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.Main;
import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.*;
import com.anncode.amazonviewer.model.Book;
import com.anncode.amazonviewer.model.Page;

import java.sql.*;
import java.util.ArrayList;

public interface BookDAO extends IDBConnection {

    /**
     * Registra en la base de datos que un libro ha sido leído.
     * @param book El libro leído.
     * @return El libro con su estado actualizado.
     */
    default void setBookRead(Book book) {
        String query = "INSERT INTO " + TViewed.NAME +
                " (" + TViewed.ID_MATERIAL + ", " + TViewed.ID_ELEMENT + ", " + TViewed.ID_USER + ", " + TViewed.DATE + ") " +
                " VALUES (?, ?, ?, ?)";
        try (Connection connection = connectToDB()) {
            int idMaterial = getMaterialIdByName("Book", connection);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, book.getId());
                pstmt.setInt(3, Main.activeUser.getId());
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                java.util.Date editionDate = new java.util.Date(rs.getDate(TBook.EDITION_DATE).getTime());

                // CORRECCIÓN AQUÍ: Usamos Page, no Book.Page
                ArrayList<Page> pages = new ArrayList<>();

                Book book = new Book(
                        rs.getString(TBook.TITLE),
                        editionDate,
                        rs.getString(TBook.EDITORIAL),
                        rs.getString(TBook.AUTHORS),
                        pages
                );

                book.setId(rs.getInt(TBook.ID));
                book.setIsbn(rs.getString(TBook.ISBN));
                book.setReaded(getIsBookRead(connection, book.getId()));

                books.add(book);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }

    /**
     * Lee las páginas de la tabla 'page' para un libro específico.
     */
    default ArrayList<Page> readPages(int idBook) {
        ArrayList<Page> pages = new ArrayList<>();
        String query = "SELECT * FROM page WHERE id_book = ?";

        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, idBook);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Usamos el constructor de la nueva clase Page.java
                    Page page = new Page(
                            rs.getInt("number"),
                            rs.getString("content")
                    );
                    page.setId(rs.getInt("id"));
                    pages.add(page);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pages;
    }

    /**
     * Verifica si el libro existe en la tabla viewed para el usuario actual.
     */
    private boolean getIsBookRead(Connection connection, int idBook) {
        boolean read = false;
        String query = "SELECT * FROM " + TViewed.NAME +
                " WHERE " + TViewed.ID_MATERIAL + " = ?" +
                " AND " + TViewed.ID_ELEMENT + " = ?" +
                " AND " + TViewed.ID_USER + " = ?";
        try {
            int idMaterial = getMaterialIdByName("Book", connection);
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, idBook);
                pstmt.setInt(3, Main.activeUser.getId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    read = rs.next();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar estado de lectura del libro:" + e.getMessage());
        }
        return read;
    }
}