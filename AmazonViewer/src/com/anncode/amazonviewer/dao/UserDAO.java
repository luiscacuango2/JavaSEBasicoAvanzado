package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.TUser;
import com.anncode.amazonviewer.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface UserDAO extends IDBConnection {

    /**
     * Busca un usuario por nombre. Si no existe, lo crea.
     * @param name Nombre del usuario (ej. "Luigi")
     * @return Objeto User con su ID asignado por la DB.
     */
    default User login(String name) {
        User user = new User(name);
        String querySelect = "SELECT * FROM " + TUser.NAME + " WHERE " + TUser.USERNAME + " = ?";

        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(querySelect)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Usuario encontrado
                user.setId(rs.getInt(TUser.ID));
            } else {
                // Usuario no existe, procedemos a insertarlo dinámicamente
                user = insertUser(user, connection);
            }

        } catch (SQLException e) {
            System.err.println("Error al gestionar sesión de usuario: " + e.getMessage());
        }
        return user;
    }

    /**
     * Inserta un nuevo usuario y recupera el ID generado.
     */
    private User insertUser(User user, Connection connection) throws SQLException {
        String queryInsert = "INSERT INTO " + TUser.NAME + " (" + TUser.USERNAME + ") VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.executeUpdate();

            // Recuperamos el ID que MySQL generó automáticamente
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                System.out.println(">>> Nuevo usuario creado: " + user.getName() + " con ID: " + user.getId());
            }
        }
        return user;
    }
}