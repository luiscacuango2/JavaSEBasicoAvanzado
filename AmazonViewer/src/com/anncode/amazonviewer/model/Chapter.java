package com.anncode.amazonviewer.model;

import com.anncode.amazonviewer.dao.ChapterDAO;
import com.anncode.amazonviewer.dao.SerieDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Es una clase que representa los capítulos individuales de una serie.
 * <p>
 * Hereda de {@link Movie} y contiene información específica sobre el número
 * de capítulos y la serie a la que pertenece.
 * </p>
 * <p>
 *     Chapter es una extensión de {@link ChapterDAO} que se caracteriza
 *     por poseer los detalles de la producción y su serie.
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
		this.setSessionNumber(sessionNumber);
		this.setSerie(serie);
	}

    /**
     * Obtiene el número de temporada o episodio del capítulo.
     * @return Un {@code int} que representa la posición del capítulo
     * dentro de la cronología de la serie.
     */
	public int getSessionNumber() {
		return sessionNumber;
	}

    /**
     * Asigna o actualiza el número de temporada o episodio del capítulo.
     * <p>
     * Este método es utilizado por la capa de datos (DAO) al momento de
     * instanciar los capítulos recuperados de la base de datos.
     * </p>
     * @param sessionNumber El número secuencial que identifica al capítulo.
     */
	public void setSessionNumber(int sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

    /**
     * Obtiene la serie a la que pertenece el capítulo.
     * @return {@link Serie} asociado al capítulo.
     */
	public Serie getSerie() {
		return serie;
	}

    /**
     * Define la serie a la que pertenece el capítulo.
     * @param serie Objeto {@link Serie} a asignar.
     */
	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	@Override
	public String toString() {
		return  "\n :: SERIE ::" + 
				"\n Título: " + getSerie().getTitle() +
				"\n :: CAPÍTULO ::" +
				"\n Título: " + getTitle() +
				"\n Año: " + getYear() +
				"\n Creador: " + getCreator() +
				"\n Duración: " + getDuration();
	}

    /**
     * Obtiene los capítulos de la DB.
     * @param serie Serie.
     * @return {@code ArrayList<Chapter>} con los capítulos de la serie dada.
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

    /**
     * Obtiene el identificador del material dado el nombre del material.
     * {@inheritDoc}
     */
    @Override
    public int getMaterialIdByName(String name, Connection conn) throws SQLException {
        // Le indicamos que use la implementación de ChapterDAO (o MovieDAO, son idénticas)
        return ChapterDAO.super.getMaterialIdByName(name, conn);
    }
}
