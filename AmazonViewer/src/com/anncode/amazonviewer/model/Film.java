package com.anncode.amazonviewer.model;

/**
 * Clase padre abstracta de la familia Films.
 * <p>
 * Esta clase es la base para todas las producciones. Como es abstracta
 * no puede crearse instancias. Contiene el método {@code view()}
 * que es obligatorio implementar.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public abstract class Film {

    /** Título de la producción */
    private String title;
    /** Género cinematográfico o literario */
    private String genre;
    /** Nombre del creador o director */
    private String creator;
    /** Duración en minutos (para películas/capítulos) o páginas (para libros) */
    private int duration;
    /** Año de lanzamiento o publicación */
    private short year;
    /** Estado de visualización del contenido */
    private boolean viewed;

    public Film() {}

    /**
     * Constructor para inicializar los atributos base de un Film.
     * @param title Título de la obra.
     * @param genre Género de la obra.
     * @param creator Creador de la obra.
     * @param duration Tiempo de duración o extensión.
     */
	public Film(String title, String genre, String creator, int duration) {
		super();
		this.title = title;
		this.genre = genre;
		this.creator = creator;
		this.duration = duration;
	}

    /**
     * Obtiene el título.
     * @return El título del film.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Asigna el título.
     * @param title El título a asignar.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Recupera el género.
     * @return El género del film.
     * */
    public String getGenre() {
        return genre;
    }

    /**
     * Asigna el género.
     * @param genre El género a asignar.
     * */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Recupera el nombre del creador.
     * @return El nombre del creador.
     * */
    public String getCreator() {
        return creator;
    }

    /**
     * Asigna el creador a asignar
     * @param creator El creador a asignar.
     * */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Recupera la duración.
     * @return La duración en números enteros.
     * */
    public int getDuration() {
        return duration;
    }

    /**
     * Asigna la duración.
     * @param duration La duración a asignar.
     * */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Recupera el año de estreno.
     * @return El año de estreno.
     * */
    public short getYear() {
        return year;
    }

    /**
     * Asigna el año de estreno.
     * @param year El año a asignar.
     * */
    public void setYear(short year) {
        this.year = year;
    }

    /**
     * Devuelve una respuesta legible sobre el estado de visualización.
     * @return {@code "Sí"} si el contenido fue visto, {@code "No"} en caso contrario.
     */
    public String isViewed() {
        String visto = "";
        if(viewed == true) {
            visto = "Sí";
        }else {
            visto = "No";
        }

        return visto;
    }

    /**
     * Recupera si el film ha sido visto.
     * @return {@code true} si el film ha sido visto.
     * */
    public boolean getIsViewed() {
        return viewed;
    }

    /**
     * Asigna el estado de visualización.
     * @param viewed El estado booleano de visualización a asignar. */
    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    /**
     * {@code view()} es un método abstracto obligatorio de implementar.
     * Define la lógica de negocio para marcar un contenido como visualizado.
     */
    public abstract void view();

}
