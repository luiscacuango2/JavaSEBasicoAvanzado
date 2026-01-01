package com.anncode.amazonviewer.model;

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
public class Chapter extends Movie {

	private int id;
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

    /**
     * Obtiene el identificador único del capítulo.
     * <p>
     * Sobrescribe el método de {@link Movie} para devolver el ID específico
     * asignado durante la instancia de la serie.
     * </p>
     * @return {@code int} con el identificador único.
     */
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return this.id;
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
	
	
	public static ArrayList<Chapter> makeChaptersList(Serie serie) {
		ArrayList<Chapter> chapters = new ArrayList();
		
		for (int i = 1; i <= 5; i++) {
			chapters.add(new Chapter("Capituo "+i, "genero "+i, "creator" +i, 45, (short)(2017+i), i, serie));
		}
		
		return chapters;
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
        super.view();
        ArrayList<Chapter> chapters = getSerie().getChapters();
        int chapterViewedCounter = 0;
        for (Chapter chapter : chapters) {
            if (chapter.getIsViewed()) {
                chapterViewedCounter++;
            }
        }

        if (chapterViewedCounter == chapters.size()) {
            getSerie().view();
        }
    }
}
