package com.anncode.amazonviewer.dao;

import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.db.DataBase.*;
import com.anncode.amazonviewer.model.Chapter;
import com.anncode.amazonviewer.Main;
import com.anncode.amazonviewer.model.Serie;

import java.sql.*;
import java.util.ArrayList;

public interface ChapterDAO extends IDBConnection {

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