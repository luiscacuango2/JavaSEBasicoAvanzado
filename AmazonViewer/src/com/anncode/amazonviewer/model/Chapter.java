package com.anncode.amazonviewer.model;

import com.anncode.amazonviewer.dao.ChapterDAO;
import com.anncode.amazonviewer.dao.SerieDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * <h2>Chapter</h2>
 * Representa los capítulos individuales de una serie.
 * <p>
 * Hereda de {@link Movie} y contiene información específica sobre el número
 * de sesión y la serie a la que pertenece.
 * </p>
 * @see Film
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Chapter extends Movie implements ChapterDAO {

	private int sessionNumber;
	private Serie serie;

    /**
     * Constructor para crear una instancia de Chapter con los detalles de la producción y su serie.
     * @param title Título del capítulo.
     * @param genre Género del capítulo.
     * @param creator Creador del capítulo.
     * @param duration Duración en minutos.
     * @param year Año de lanzamiento.
     * @param sessionNumber Número de temporada o sesión.
     * @param serie Objeto {@link Serie} al que pertenece este capítulo.
     */
	public Chapter(String title, String genre, String creator, int duration, short year, int sessionNumber, Serie serie) {
		super(title, genre, creator, duration, year);
		// TODO Auto-generated constructor stub
		this.setSessionNumber(sessionNumber);
		this.setSerie(serie);
	}

	public int getSessionNumber() {
		return sessionNumber;
	}

	public void setSessionNumber(int sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

    /**
     * Define la serie
     * @return {@link Serie}
     */
	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  "\n :: SERIE ::" + 
				"\n Title: " + getSerie().getTitle() +
				"\n :: CHAPTER ::" + 
				"\n Title: " + getTitle() +
				"\n Year: " + getYear() + 
				"\n Creator: " + getCreator() +
				"\n Duration: " + getDuration();
	}

    /**
     * Obtiene los capítulos de la DB.
     * @param serie Serie.
     * @return ArrayList<Chapter> con los capítulos de la serie dada.
     */
    public static ArrayList<Chapter> makeChaptersList(Serie serie) {
        ChapterDAO chapterDAO = new ChapterDAO() {};
        return chapterDAO.read(serie.getId()); // Lee los capítulos asociados al ID de la serie
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sobrescribe el comportamiento para marcar el capítulo como visto y
     * verifica si todos los capítulos de la serie han sido visualizados.
     * De ser así, marca automáticamente la {@link Serie} como vista.
     * </p>
     */
    @Override
    public void view() {
        super.view(); // Marca el capítulo como visto en DB

        ArrayList<Chapter> chapters = getSerie().getChapters();
        int chapterViewedCounter = 0;

        if (chapters != null) {
            for (Chapter chapter : chapters) {
                if (chapter.getIsViewed()) {
                    chapterViewedCounter++;
                }
            }

            if (chapterViewedCounter == chapters.size()) {
                // 1. Marcar en memoria
                getSerie().setViewed(true);
                // 2. Marcar en Base de Datos usando el DAO
                SerieDAO serieDAO = new SerieDAO() {};
                serieDAO.setSerieViewed(getSerie());
            }
        }
    }

    @Override
    public int getMaterialIdByName(String name, Connection conn) throws SQLException {
        // Le indicamos que use la implementación de ChapterDAO (o MovieDAO, son idénticas)
        return ChapterDAO.super.getMaterialIdByName(name, conn);
    }
}
