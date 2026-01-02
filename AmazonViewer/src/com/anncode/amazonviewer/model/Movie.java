package com.anncode.amazonviewer.model;

import com.anncode.amazonviewer.dao.MovieDAO;
import java.util.ArrayList;
import java.util.Date;

/**
 * <h2>Movie</h2>
 * Representa una película dentro de la aplicación.
 * <p>
 * Esta clase extiende o hereda de {@link Film} las propiedades básicas de una producción
 * e implementa {@link IVisualizable} para gestionar el control de tiempo de visualización.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Movie extends Film implements IVisualizable, MovieDAO {

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

    public Movie() { }

    /**
     * Obtiene el identificador único de la película.
     * @return El identificador único de la película.
     */
    public int getId() {
        return id;
    }

    /**
     * Define el identificador único de la película.
     * @param id El identificador a asignar.
     */
    public void setId(int id) {
        this.id = id;
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
        MovieDAO movieDAO = new MovieDAO() {};
        return movieDAO.read();
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
        // 1. Marcamos como visto en memoria
        setViewed(true);

        // 2. INSERT DINÁMICO: Llamamos al método de la interfaz MovieDAO
        // pasándole 'this' (esta película específica con su ID de la DB)
        this.setMovieViewed(this);

        Date dateI = startToSee(new Date());

        System.out.println("\nReproduciendo: " + getTitle());

        // Simulación de barra de progreso profesional
        int totalPasos = 20;
        for (int i = 0; i <= totalPasos; i++) {
            StringBuilder bar = new StringBuilder("[");
            for (int j = 0; j < totalPasos; j++) {
                bar.append((j < i) ? "=" : " ");
            }
            bar.append("] ").append(i * 50 / totalPasos).append("%");
            System.out.print("\r" + bar.toString());

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        stopToSee(dateI, new Date());
        System.out.println("\nVisualización finalizada.");
        System.out.println(toString());
        System.out.println("Tiempo total: " + getTimeViewed() + " ms");
    }
}