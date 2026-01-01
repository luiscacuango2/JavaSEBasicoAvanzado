package com.anncode.amazonviewer.model;

import java.util.ArrayList;

/**
 * Representa una serie de televisión dentro de Amazon Viewer.
 * <p>
 * Una serie es una extensión de {@link Film} que se caracteriza por poseer
 * una cantidad de temporadas (sesiones) y una colección de objetos {@link Chapter}.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Serie extends Film {

    /** Identificador único de la serie */
    private int id;
    /** Cantidad de temporadas o sesiones disponibles */
    private int sessionQuantity;
    /** Listado de capítulos que componen la serie */
    private ArrayList<Chapter> chapters;


    /**
     * Constructor para inicializar una Serie con sus datos básicos.
     * @param title Título de la serie.
     * @param genre Género principal.
     * @param creator Nombre del creador o productora.
     * @param duration Duración total estimada (suma de capítulos).
     * @param sessionQuantity Número total de temporadas.
     */
    public Serie(String title, String genre, String creator, int duration, int sessionQuantity) {
        super(title, genre, creator, duration);
        this.sessionQuantity = sessionQuantity;
    }

    /**
     * Obtiene el identificador único de la serie.
     * @return El identificador único de la serie.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene la cantidad de temporadas disponibles.
     * @return La cantidad de temporadas disponibles.
     */
    public int getSessionQuantity() {
        return sessionQuantity;
    }

    /**
     * @param sessionQuantity La cantidad de temporadas a asignar.
     */
    public void setSessionQuantity(int sessionQuantity) {
        this.sessionQuantity = sessionQuantity;
    }

    /**
     * Obtiene la lista de capítulos asociados a la serie.
     * @return {@code ArrayList} con objetos de tipo {@link Chapter}.
     */
    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    /**
     * Asigna una lista de capítulos a la serie.
     * @param chapters Colección de capítulos a establecer.
     */
    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retorna una ficha técnica que incluye el título, género, año de estreno,
     * creador y la duración acumulada.
     * </p>
     */
    @Override
    public String toString() {
        return  "\n :: SERIE ::" +
                "\n Title: " + getTitle() +
                "\n Genero: " + getGenre() +
                "\n Year: " + getYear() +
                "\n Creator: " + getCreator() +
                "\n Duration: " + getDuration();
    }

    /**
     * Genera una lista de series predefinidas para pruebas.
     * <p>
     * Este método también se encarga de disparar la creación de capítulos
     * para cada serie generada mediante {@link Chapter#makeChaptersList(Serie)}.
     * </p>
     * @return Un {@code ArrayList} de objetos {@link Serie} con sus respectivos capítulos cargados.
     */
    public static ArrayList<Serie> makeSeriesList() {
        ArrayList<Serie> series = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Serie serie = new Serie("Serie "+i, "genero "+i, "creador "+i, 1200, 5);
            serie.setChapters(Chapter.makeChaptersList(serie));
            series.add(serie);
        }

        return series;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Marca la serie como visualizada. Este método suele ser invocado
     * automáticamente cuando todos los capítulos de la serie han sido vistos.
     * </p>
     */
    @Override
    public void view() {
        setViewed(true);
    }
}