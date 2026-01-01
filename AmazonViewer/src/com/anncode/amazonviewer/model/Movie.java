package com.anncode.amazonviewer.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Representa una película dentro de la aplicación.
 * <p>
 * Esta clase extiende de {@link Film} para heredar las propiedades básicas de una producción
 * e implementa {@link IVisualizable} para gestionar el control de tiempo de visualización.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Movie extends Film implements IVisualizable {

    /** Identificador único de la película */
    private int id;
    /** Tiempo total en milisegundos que el usuario ha visto la película */
    private int timeViewed;

    /**
     * Constructor para crear una instancia de Movie.
     * @param title Título de la película.
     * @param genre Género cinematográfico.
     * @param creator Nombre del director o estudio creador.
     * @param duration Duración total en minutos.
     * @param year Año de estreno.
     */
    public Movie(String title, String genre, String creator, int duration, short year) {
        super(title, genre, creator, duration);
        setYear(year);
    }

    /**
     * Obtiene el identificador único de la película.
     * @return El identificador único de la película.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el tiempo visualizado en milisegundos.
     * @return El tiempo visualizado en milisegundos.
     */
    public int getTimeViewed() {
        return timeViewed;
    }

    /**
     * Define el tiempo visualizado en milisegundos.
     * @param timeViewed El tiempo en milisegundos a asignar como visto.
     */
    public void setTimeViewed(int timeViewed) {
        this.timeViewed = timeViewed;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retorna una ficha técnica con el título, género, año, creador y duración.
     * </p>
     */
    @Override
    public String toString() {
        return  "\n :: MOVIE ::" +
                "\n Title: " + getTitle() +
                "\n Genero: " + getGenre() +
                "\n Year: " + getYear() +
                "\n Creator: " + getCreator() +
                "\n Duration: " + getDuration();
    }

    /**
     * {@inheritDoc}
     * @param dateI Fecha y hora de inicio de visualización.
     * @return La misma fecha de inicio capturada.
     */
    @Override
    public Date startToSee(Date dateI) {
        return dateI;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Calcula la diferencia de tiempo entre el inicio y el fin.
     * Si la fecha final es válida, la duración se guarda en {@code timeViewed}.
     * </p>
     */
    @Override
    public void stopToSee(Date dateI, Date dateF) {
        if (dateF.getTime() > dateI.getTime()) {
            setTimeViewed((int)(dateF.getTime() - dateI.getTime()));
        } else {
            setTimeViewed(0);
        }
    }

    /**
     * Método estático que genera una lista de películas de prueba.
     * @return Un {@code ArrayList} con objetos {@link Movie} para inicializar la aplicación.
     */
    public static ArrayList<Movie> makeMoviesList() {
        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            movies.add(new Movie("Movie " + i, "Genero " + i, "Creador " + i, 120+i, (short)(2017+i)));
        }

        return movies;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Ejecuta el proceso de visualización: marca la película como vista,
     * inicia el cronómetro, simula una carga y detiene el cronómetro al finalizar.
     * </p>
     */
    @Override
    public void view() {
        setViewed(true);
        Date dateI = startToSee(new Date());

        for (int i = 0; i < 100000; i++) {
            System.out.println("..........");
        }

        stopToSee(dateI, new Date());
        System.out.println();
        System.out.println("Viste: " + toString());
        System.out.println("Por: " + getTimeViewed() + " milisegundos");
    }
}