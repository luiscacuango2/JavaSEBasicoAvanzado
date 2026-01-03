package com.anncode.amazonviewer.db;

import java.sql.*;

import static com.anncode.amazonviewer.db.DBConfig.*;

/**
 * Interface para la gestión de ciclo de vida de conexiones JDBC.
 */
public interface IDBConnection {

    /**
     * Establece una conexión con MySQL.
     * Utiliza las constantes de {@link DBConfig}.
     * @return Una {@link Connection} activa.
     * @throws RuntimeException si la conexión no se puede establecer.
     */
    default Connection connectToDB() {
        try {
            // Ya no es necesario Class.forName(DRIVER) en JDBC 4.0+
//            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(FULL_URL, USER, PASSWORD);

            if (connection != null && !connection.isClosed()) {
                // Usamos un mensaje más discreto o logs
//                System.out.println(">>> Conexión exitosa a: " + DB_NAME);
            }
            return connection;

        } catch (SQLException e) {
            // Lanzamos una excepción en lugar de retornar null para fallar rápido
            throw new RuntimeException("Fallo crítico: No se pudo conectar a la base de datos " + DB_NAME, e);
        }
    }

    /**
     * Método de utilidad para cerrar la conexión de forma segura.
     * @param connection La conexión a cerrar.
     */
    default void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println(">>> Conexión cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("Error al intentar cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene el identificador numérico de un tipo de material a partir de su nombre descriptivo.
     * <p>
     * Este método consulta la tabla {@code material} para encontrar el {@code id} asociado
     * a una categoría específica (ej. "Movie", "Chapter", "Book"). Es una pieza auxiliar
     * crítica para realizar inserciones y consultas precisas en la tabla de transacciones
     * {@code viewed}, asegurando la integridad referencial.
     * </p>
     *
     * @param name El nombre del material tal como está definido en {@link DataBase.MaterialNames}.
     * @param conn La conexión activa a la base de datos para realizar la búsqueda.
     * @return El {@code id} numérico correspondiente al material encontrado en la base de datos.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    default int getMaterialIdByName(String name, Connection conn) throws SQLException {
        String query = "SELECT id FROM material WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt("id") : 0;
            }
        }
    }
}