package com.anncode.amazonviewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.anncode.amazonviewer.dao.MagazineDAO;
import com.anncode.amazonviewer.dao.SerieDAO;
import com.anncode.amazonviewer.dao.UserDAO;
import com.anncode.amazonviewer.model.*;
import com.anncode.makereport.Report;
import com.anncode.util.AmazonUtil;

/**
 * <h2>AmazonViewer</h2>
 * AmazonViewer es el punto de entrada de la aplicación que permite gestionar la
 * visualización de contenido multimedia: Películas, Series, Libros y Revistas.
 * <p>
 * El programa ofrece una interfaz de consola para interactuar con el catálogo,
 * marcar contenidos como vistos o leídos, y generar reportes de consumo en archivos de texto.
 * </p>
 * <p>
 * Regla de negocio: Todos los elementos son visualizables o leíbles a excepción
 * de las Revistas (Magazines), las cuales solo se muestran en modo exposición.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Main implements UserDAO {
    // Sesión global del usuario
    public static User activeUser;
    static ArrayList<Movie> movies = new ArrayList<>();;
    static ArrayList<Serie> series = new ArrayList<>();
    static ArrayList<Chapter> chapters = new ArrayList<>();
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Magazine> magazines = new ArrayList<>();
    /**
     * Método principal que inicia la ejecución del programa.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
	public static void main(String[] args) {
        Main app = new Main();

        // LOGIN DINÁMICO:
        // Si "Luigi" existe, trae su ID. Si no, lo crea y nos da el nuevo ID.
        activeUser = app.login("Luigi");

        // Esto evitará que los mensajes de conexión se repitan infinitamente
        movies = Movie.makeMoviesList();
        series = Serie.makeSeriesList();
        books = Book.makeBookList();
        magazines = Magazine.makeMagazineList();

        showMenu();
	}

    /**
     * Despliega el menú principal en consola y gestiona la navegación
     * general del usuario mediante un switch-case.
     */
	public static void showMenu() {
		int exit = 0;
		do {

			System.out.println("BIENVENIDOS AMAZON VIEWER");
			System.out.println("");
			System.out.println("Selecciona el número de la opción deseada");
			System.out.println("1. Movies");
			System.out.println("2. Series");
			System.out.println("3. Books");
			System.out.println("4. Magazines");
			System.out.println("5. Report");
			System.out.println("6. Report Today");
			System.out.println("0. Exit");

			//Leer la respuesta del usuario
			int response = AmazonUtil.validateUserResponseMenu(0, 6);

			switch (response) {
				case 0:
					//salir
					exit = 0;
					break;
				case 1:
					showMovies();
					break;
				case 2:
					showSeries();
					break;
				case 3:
					showBooks();
					break;
				case 4:
					showMagazines();
					break;
				case 5:
					makeReport();
					exit = 1;
					break;
				case 6:
					//Date date = new Date();
					makeReport(new Date());
					exit = 1;
					break;

				default:
					System.out.println();
					System.out.println("....¡¡Selecciona una opción!!....");
					System.out.println();
					exit = 1;
					break;
			}
		} while(exit != 0);
	}

    /**
     * Gestiona el submenú de películas, permitiendo seleccionar una para su visualización.
     */
	public static void showMovies() {
        /** Lista persistente de películas cargadas en memoria */
//        movies = Movie.makeMoviesList();

		int exit = 1;

		do {
			System.out.println();
			System.out.println(":: MOVIES ::");
			System.out.println();

			for (int i = 0; i < movies.size(); i++) { //1. Movie 1
				System.out.println(i+1 + ". " + movies.get(i).getTitle() + " Visto: " + movies.get(i).isViewed());
			}

			System.out.println("0. Regresar al Menu");
			System.out.println();

			//Leer Respuesta usuario
			int response = AmazonUtil.validateUserResponseMenu(0, movies.size());

			if(response == 0) {
				exit = 0;
				showMenu();
			}
			if (response > 0) {
				Movie movieSelected = movies.get(response-1);
                movieSelected.view();
			}
		} while (exit !=0);

	}

    /**
     * Gestiona el submenú de series, permitiendo profundizar en los capítulos de cada una.
     */
	public static void showSeries() {
        /** Lista persistente de series cargadas en memoria */
//        series = Serie.makeSeriesList();

		int exit = 1;

		do {
			System.out.println();
			System.out.println(":: SERIES ::");
			System.out.println();

			for (int i = 0; i < series.size(); i++) { //1. Serie 1
				System.out.println(i+1 + ". " + series.get(i).getTitle() + " Visto: " + series.get(i).isViewed());
			}

			System.out.println("0. Regresar al Menu");
			System.out.println();

			//Leer Respuesta usuario
			int response = AmazonUtil.validateUserResponseMenu(0, series.size());

			if(response == 0) {
				exit = 0;
				showMenu();
			}

            if(response > 0) {
                Serie serieSeleccionada = series.get(response-1);

                // 1. Mostramos los capítulos
                showChapters(serieSeleccionada.getChapters());

                // 2. RE-VERIFICACIÓN MANUAL:
                // Al regresar de los capítulos, comprobamos si todos están vistos
                int contadorVistos = 0;
                for (Chapter c : serieSeleccionada.getChapters()) {
                    if (c.getIsViewed()) contadorVistos++;
                }

                // 3. Si todos están vistos, marcamos la serie en la lista de Main
                if (contadorVistos == serieSeleccionada.getChapters().size() && contadorVistos > 0) {
                    serieSeleccionada.setViewed(true);
                    // Opcional: Llamar al DAO aquí si quieres asegurar persistencia inmediata
                    new SerieDAO(){}.setSerieViewed(serieSeleccionada);
                }
            }
		} while(exit !=0);
	}

    /**
     * Despliega los capítulos de una serie seleccionada y permite su visualización individual.
     * @param chaptersOfSerieSelected Lista de capítulos pertenecientes a la serie elegida.
     */
	public static void showChapters(ArrayList<Chapter> chaptersOfSerieSelected) {
        /** Lista persistente de capítulos cargados en memoria */
        chapters = chaptersOfSerieSelected;

		int exit = 1;

		do {
			System.out.println();
			System.out.println(":: CHAPTERS ::");
			System.out.println();

			for (int i = 0; i < chaptersOfSerieSelected.size(); i++) { //1. Chapter 1
				System.out.println(i+1 + ". " + chaptersOfSerieSelected.get(i).getTitle() + " Visto: " + chaptersOfSerieSelected.get(i).isViewed());
			}

			System.out.println("0. Regresar al Menu");
			System.out.println();

			//Leer Respuesta usuario
			int response = AmazonUtil.validateUserResponseMenu(0, chaptersOfSerieSelected.size());

			if(response == 0) {
				exit = 0;
                showSeries();
			}

            if(response > 0) {
                Chapter chapterSelected = chaptersOfSerieSelected.get(response-1);
                chapterSelected.view();

                // Después de ver un capítulo, verificamos si la serie padre
                // ahora está vista y actualizamos nuestra lista global.
                if (Boolean.parseBoolean(chapterSelected.getSerie().isViewed())) {
                    // Buscamos la serie en nuestra lista estática y la actualizamos
                    for (Serie s : series) {
                        if (s.getId() == chapterSelected.getSerie().getId()) {
                            s.setViewed(true);
                        }
                    }
                }
            }
		} while (exit !=0);
	}

    /**
     * Gestiona el submenú de libros, permitiendo seleccionar uno para su lectura.
     */
	public static void showBooks() {
        /** Lista persistente de libros cargados en memoria */
        books = Book.makeBookList();

		int exit = 1;

		do {
			System.out.println();
			System.out.println(":: BOOKS ::");
			System.out.println();

			for (int i = 0; i < books.size(); i++) { //1. Book 1
				System.out.println(i+1 + ". " + books.get(i).getTitle() + " Leído: " + books.get(i).isReaded());
			}

			System.out.println("0. Regresar al Menu");
			System.out.println();

			//Leer Respuesta usuario
			int response = AmazonUtil.validateUserResponseMenu(0, books.size());

			if(response == 0) {
				exit = 0;
				showMenu();
			}

			if(response > 0) {
				Book bookSelected = books.get(response-1);
                bookSelected.view();

                System.out.println("\n----------------------------------------------");
                System.out.println(" Has leído el libro : " + bookSelected.getTitle());
                System.out.println("----------------------------------------------");
            }

		} while (exit !=0);
	}

    /**
     * Muestra el catálogo de revistas. No permite interacción de lectura
     * según las reglas de negocio establecidas.
     */
    public static void showMagazines() {
        int exit = 0;
        // Cargamos la lista real desde la DB una sola vez al entrar al menú
        magazines = Magazine.makeMagazineList();

        do {
            System.out.println("\n:: MAGAZINES ::");

            // Listado dinámico
            for (int i = 0; i < magazines.size(); i++) {
                System.out.println((i + 1) + ". " + magazines.get(i).getTitle() +
                        " [" + magazines.get(i).isReaded() + "]");
            }

            System.out.println("0. Regresar al Menu");
            System.out.println();

            // 1. CORRECCIÓN: El rango debe ser entre 0 y el tamaño de la lista
            int response = AmazonUtil.validateUserResponseMenu(0, magazines.size());

            if (response == 0) {
                exit = 0;
                showMenu();
            }

            if (response > 0) {
                Magazine magazineSelected = magazines.get(response - 1);
                magazineSelected.view();

                System.out.println("\n----------------------------------------------");
                System.out.println(" Has leído la revista: " + magazineSelected.getTitle());
                System.out.println("----------------------------------------------");

                exit = 1;
            }

        } while (exit != 0);
    }

    /**
     * Genera un reporte general de todos los elementos marcados como vistos o leídos.
     * Utiliza la clase {@link Report} para escribir el archivo en disco.
     */
	public static void makeReport() {

		Report report = new Report();
		report.setNameFile("reporte");
		report.setExtension("txt");
		report.setTitle(":: VISTOS ::");
		String contentReport = "REPORTE GENERAL\n";;

		for (Movie movie : movies) {
			if (movie.getIsViewed()) {
				contentReport += movie.toString() + "\n";
			}
		}

		for (Serie serie : series) {
			ArrayList<Chapter> chapters = serie.getChapters();
			for (Chapter chapter : chapters) {
				if (chapter.getIsViewed()) {
					contentReport += chapter.toString() + "\n";
				}
			}
		}

		for (Book book : books) {
			if (book.getIsReaded()) {
				contentReport += book.toString() + "\n";
			}
		}

		report.setContent(contentReport);
		report.makeReport();
		System.out.println("Reporte Generado");
		System.out.println();
	}

    /**
     * Genera un reporte cronológico detallado con la fecha y hora actual.
     * Sobrecarga el método {@link #makeReport()} para incluir una estampa de tiempo
     * en el nombre del archivo y en el encabezado.
     * @param date Objeto {@link Date} con la fecha para el reporte.
     */
	public static void makeReport(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-h-m-s");
		String dateString = df.format(date);
		Report report = new Report();

		report.setNameFile("reporte" + dateString);
		report.setExtension("txt");
		report.setTitle(":: VISTOS ::");

        Locale spanishLocale = new Locale("es", "ES");
        SimpleDateFormat dfNameDays = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", spanishLocale);

//		SimpleDateFormat dfNameDays = new SimpleDateFormat("day dd 'de' MMMM 'de' yyyy");
		dateString = dfNameDays.format(date);
		String contentReport = "Fecha: " + dateString + "\n\n";

		for (Movie movie : movies) {
			if (movie.getIsViewed()) {
				contentReport += movie.toString() + "\n";
			}
		}

		for (Serie serie : series) {
			ArrayList<Chapter> chapters = serie.getChapters();
			for (Chapter chapter : chapters) {
				if (chapter.getIsViewed()) {
					contentReport += chapter.toString() + "\n";
				}
			}
		}

		for (Book book : books) {
			if (book.getIsReaded()) {
				contentReport += book.toString() + "\n";
			}
		}
		report.setContent(contentReport);
		report.makeReport();

		System.out.println("Reporte Generado");
		System.out.println();
	}
}