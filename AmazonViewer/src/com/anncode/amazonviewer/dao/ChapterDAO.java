package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.*;
import com.anncode.amazonviewer.model.Chapter;
import com.anncode.amazonviewer.Main;
import com.anncode.amazonviewer.model.Serie;

import java.sql.*;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de persistencia para el objeto {@link Chapter}.
 * <p>
 * Esta interfaz extiende de {@link IDBConnection} para facilitar la comunicación
 * con la base de datos MySQL. Su responsabilidad principal es gestionar la carga
 * de episodios individuales y registrar su estado de visualización, permitiendo
 * además la vinculación lógica con su {@link Serie} correspondiente.
 * </p>
 * @author Luigi
 * @version 1.3
 * @since 2026-01-03
 */
public interface ChapterDAO extends IDBConnection {

    /**
     * Registra en la base de datos que un capítulo ha sido visualizado.
     * <p>
     * Este método inserta un nuevo registro en la tabla {@code viewed}, asociando
     * el {@code id} del capítulo, el {@code id_material} correspondiente a "Chapter"
     * y el {@code id} del usuario que tiene la sesión activa.
     * </p>
     * @param chapter El objeto {@link Chapter} que el usuario ha terminado de ver.
     * @return El objeto {@link Chapter} procesado, permitiendo el encadenamiento de
     * métodos o la actualización de la interfaz de usuario.
     */
    default Chapter setChapterViewed(Chapter chapter) {
        String query = "INSERT INTO " + TViewed.NAME + " (" + TViewed.ID_MATERIAL + ", " +
                TViewed.ID_ELEMENT + ", " + TViewed.ID_USER + ", " + TViewed.DATE + ") VALUES (?, ?, ?, ?)";
        try (Connection connection = connectToDB()) {
            int idMaterial = getMaterialIdByName(MaterialNames.CHAPTER, connection);
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, idMaterial);
                pstmt.setInt(2, chapter.getId());
                pstmt.setInt(3, Main.activeUser.getId());
                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                pstmt.executeUpdate();
                chapter.setViewed(true);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return chapter;
    }

    /**
     * Recupera de la base de datos la lista de capítulos pertenecientes a una serie específica.
     * <p>
     * Este método ejecuta una consulta con un filtro {@code WHERE id_serie = ?} para
     * asegurar que solo se carguen los episodios vinculados a la serie seleccionada.
     * Por cada registro encontrado, se verifica su estado de visualización en la tabla
     * {@code viewed} para el usuario activo.
     * </p>
     *
     * @param idSerie El identificador único de la {@link Serie} cuyos capítulos se desean obtener.
     * @return Una {@link ArrayList} de objetos {@link Chapter} con sus datos y estado de
     * visualización sincronizados con la base de datos.
     */
    default ArrayList<Chapter> read(int idSerie) {
        ArrayList<Chapter> chapters = new ArrayList<>();

        // QUERY COMPLETA: Traemos todos los datos necesarios para construir Serie y Chapter
        String query = "SELECT c.*, s." + TSerie.TITLE + ", s." + TSerie.GENRE + ", s." + TSerie.CREATOR +
                ", s." + TSerie.DURATION + ", s." + TSerie.SESSION_QUANTITY +
                " FROM " + TChapter.NAME + " AS c " +
                " INNER JOIN " + TSerie.NAME + " AS s " +
                " ON c." + TChapter.ID_SERIE + " = s." + TSerie.ID +
                " WHERE c." + TChapter.ID_SERIE + " = ?";

        try (Connection connection = connectToDB();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, idSerie);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // 1. Construir el objeto Serie con los datos del JOIN (Constructor de 5 parámetros)
                Serie seriePadre = new Serie(
                        rs.getString("s." + TSerie.TITLE),
                        rs.getString("s." + TSerie.GENRE),
                        rs.getString("s." + TSerie.CREATOR),
                        rs.getInt("s." + TSerie.DURATION),
                        rs.getInt("s." + TSerie.SESSION_QUANTITY)
                );
                seriePadre.setId(idSerie);

                // 2. Extraer datos del Capítulo
                String title = rs.getString("c." + TChapter.TITLE);
                int duration = rs.getInt("c." + TChapter.DURATION);
                short year = rs.getShort("c." + TChapter.YEAR);
                int sessionNumber = rs.getInt("c." + TChapter.SESSION_NUMBER);

                // 3. Crear Chapter usando los datos de la serie y el capítulo (Constructor de 7 parámetros)
                Chapter chapter = new Chapter(
                        title,
                        seriePadre.getGenre(),
                        seriePadre.getCreator(),
                        duration,
                        year,
                        sessionNumber,
                        seriePadre
                );

                chapter.setId(rs.getInt("c." + TChapter.ID));
                chapter.setViewed(getChapterViewed(connection, chapter.getId()));

                chapters.add(chapter);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return chapters;
    }

    /**
     * Consulta la base de datos para verificar si un capítulo específico ya fue visto por el usuario.
     * <p>
     * Este método realiza una búsqueda en la tabla {@code viewed} filtrando por el identificador
     * del material (tipo Capítulo), el ID del elemento y el ID del usuario activo.
     * Es fundamental para mantener la persistencia visual entre diferentes sesiones de ejecución.
     * </p>
     * @param connection La conexión activa a la base de datos necesaria para la consulta.
     * @param idChapter  El identificador único del {@link Chapter} que se desea verificar.
     * @return {@code true} si existe al menos un registro de visualización para este capítulo
     * y usuario; {@code false} en caso contrario o si no hay un usuario activo.
     * @throws SQLException Si ocurre un error durante la ejecución de la consulta SQL.
     */
    private boolean getChapterViewed(Connection connection, int idChapter) throws SQLException {
        String query = "SELECT * FROM " + TViewed.NAME + " WHERE " + TViewed.ID_MATERIAL + " = ? AND " +
                TViewed.ID_ELEMENT + " = ? AND " + TViewed.ID_USER + " = ?";

        int idMaterial = getMaterialIdByName(MaterialNames.CHAPTER, connection);

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idMaterial);
            pstmt.setInt(2, idChapter);
            pstmt.setInt(3, Main.activeUser.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

}