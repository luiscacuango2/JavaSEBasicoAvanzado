package com.anncode.amazonviewer;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import com.anncode.amazonviewer.dao.SerieDAO;
import com.anncode.amazonviewer.dao.UserDAO;
import com.anncode.amazonviewer.dao.ViewedDAO;
import com.anncode.amazonviewer.model.*;
import com.anncode.makereport.Report;
import com.anncode.util.AmazonUtil;

/**
 * La aplicación permite gestionar la
 * visualización de contenido multimedia: Películas, Series, Libros y Revistas.
 * <p>
 * El programa ofrece una interfaz de consola para interactuar con el catálogo,
 * marcar contenidos como vistos o leídos, y generar reportes en archivos de texto.
 * </p>
 * @author Luigi
 * @version 1.3
 * @since 2026-01-03
 */
public class Main implements UserDAO {
    /** Lista persistente de usuarios cargados en memoria */
    public static User activeUser;
    static ArrayList<Movie> movies = new ArrayList<>();;
    static ArrayList<Serie> series = new ArrayList<>();
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Magazine> magazines = new ArrayList<>();

    /**
     * Constructor por defecto de la clase Main.
     * <p>
     * En el contexto de AmazonViewer, este constructor inicializa los componentes
     * básicos de la aplicación de consola. Se encarga de preparar la lógica de
     * arranque antes de que el método {@code main} tome el control para desplegar
     * la interfaz de usuario y gestionar la sesión del usuario activo.
     * </p>
     */
    public Main() {
    }

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

			System.out.println("\nBIENVENIDOS A AMAZON VIEWER");
			System.out.println("Seleccione una Opción:");
			System.out.println("1. Películas");
			System.out.println("2. Series");
			System.out.println("3. Libros");
			System.out.println("4. Revistas");
			System.out.println("5. Reporte Consolidado");
			System.out.println("6. Reporte de Hoy");
			System.out.println("0. Salir");

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
					makeReport(new Date());
					exit = 1;
					break;

				default:
					System.out.println();
					System.out.println("....¡Seleccione una opción!....");
					System.out.println();
					exit = 1;
					break;
			}
		} while (exit != 0);
	}

    /**
     * Gestiona el submenú de películas, permitiendo seleccionar una para su visualización.
     */
	public static void showMovies() {
		int exit = 1;

		do {
			System.out.println(":: LISTADO DE PELÍCULAS ::");

            AtomicInteger counter = new AtomicInteger(1);
            movies.forEach(m -> System.out.println(counter.getAndIncrement() + ". " + m.getTitle() + ". Visto: " + m.isViewed()));

//			for (int i = 0; i < movies.size(); i++) { //1. Movie 1
//				System.out.println(i+1 + ". " + movies.get(i).getTitle() + " Visto: " + movies.get(i).isViewed());
//			}

			System.out.println("0. REGRESAR AL MENU PRINCIPAL");
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

		int exit = 1;

		do {
			System.out.println(":: LISTADO DE SERIES ::");

            AtomicInteger counter = new AtomicInteger(1);
            series.forEach(s -> System.out.println(counter.getAndIncrement() + ". " + s.getTitle() + ". Visto: " + s.isViewed()));

			System.out.println("0. REGRESAR AL MENU PRINCIPAL");
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

                // 2. Al regresar de los capítulos, comprobamos si todos están vistos
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
		} while (exit !=0);
	}

    /**
     * Despliega los capítulos de una serie seleccionada y permite su visualización individual.
     * @param chaptersOfSerieSelected Lista de capítulos pertenecientes a la serie elegida.
     */
	public static void showChapters(ArrayList<Chapter> chaptersOfSerieSelected) {
		int exit = 1;

		do {
			System.out.println("SERIE: " + chaptersOfSerieSelected.get(0).getSerie().getTitle());
			System.out.println(":: CAPÍTULOS ::");

            AtomicInteger counter = new AtomicInteger(1);
            chaptersOfSerieSelected.forEach(c -> System.out.println(counter.getAndIncrement() + ". " + c.getTitle() + ". Visto: " + c.isViewed()));

			System.out.println("0. REGRESAR A SERIES");
			System.out.println();

			//Leer Respuesta usuario
			int response = AmazonUtil.validateUserResponseMenu(0, chaptersOfSerieSelected.size());

			if(response == 0) {
				exit = 0;
			}

            if(response > 0) {
                Chapter chapterSelected = chaptersOfSerieSelected.get(response-1);
                chapterSelected.view();

                // Después de ver un capítulo, actualizamos nuestra lista global.
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

		int exit = 1;

		do {
            System.out.println();
			System.out.println(":: LISTADO DE LIBROS ::");

            AtomicInteger counter = new AtomicInteger(1);
            books.forEach(b -> System.out.println(counter.getAndIncrement() + ". " + b.getTitle() + ". Leído: " + b.isReaded()));

			System.out.println("0. REGRESAR AL MENU PRINCIPAL");
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
        int exit = 1;

        do {
            System.out.println(":: LISTADO DE REVISTAS ::");

            AtomicInteger counter = new AtomicInteger(1);
            magazines.forEach(ma -> System.out.println(counter.getAndIncrement() + ". " + ma.getTitle() + ". Leida: " + ma.isReaded()));

            System.out.println("0. REGRESAR AL MENU PRINCIPAL");
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

                System.out.println("----------------------------------------------");
                System.out.println(" Has leído la revista: " + magazineSelected.getTitle());
                System.out.println("----------------------------------------------\n");
            }

        } while (exit != 0);
    }

    /**
     * Genera un reporte consolidado de todos los elementos marcados como
     * vistos o leídos en formato de texto plano.
     * <p>
     * Realiza un proceso de filtrado y aplanamiento (flattening) de las colecciones
     * de {@link Movie}, {@link Serie} (incluyendo sus {@link Chapter}) y {@link Book}
     * utilizando la API de <b>Java Streams</b> para identificar exclusivamente los
     * elementos con estado "visto" o "leído".
     * </p>
     * <p><b>Flujo de ejecución:</b></p>
     * <ol>
     * <li>Filtra los elementos de contenido mediante predicados de estado.</li>
     * <li>Concatena la representación {@code toString()} de cada objeto.</li>
     * <li>Utiliza el método {@code makeReport()} de la clase {@link Report}
     * para guardar el archivo en disco.</li>
     * </ol>
     * @see Report
     * @see Movie
     * @see Serie
     * @see Book
     */
    public static void makeReport() {

        // 1. Obtener la actividad real desde la base de datos
        ViewedDAO viewedDAO = new ViewedDAO() {};
        ArrayList<Viewed> viewedList = viewedDAO.read(); // Traemos TODO lo visto

        // 2. Configurar el reporte
        Report report = new Report();
        report.setNameFile("Reporte-Consolidado");
        report.setExtension("txt");
        report.setTitle(":: REPORTE CONSOLIDADO VISTOS/LEIDOS ::");

        // Verificamos cuantos registros trae la lista
        System.out.println("Registros encontrados para el reporte consolidado: " + viewedList.size());

        // 3. Cruzar los IDs de la DB con los objetos en memoria (Movies, Chapters, etc.)
        String contentReport = viewedList.stream()
                .map(v -> findContentById(v.getIdItem(), v.getIdType())) // Buscamos el objeto real
                .filter(Objects::nonNull)                               // Evitamos nulos
                .map(item -> item.toString() + "\n")
                .collect(Collectors.joining("", ":: REPORTE CONSOLIDADO VISTOS/LEIDOS ::\n", ""));

        // 4. Asignar y generar
        report.setContent(contentReport);
        report.makeReport();

        System.out.println("Reporte Consolidado Generado con Éxito");
        System.out.println();
    }

    /**
     * Genera un reporte cronológico filtrado por una fecha específica (hoy).
     * <p>
     * Este método unifica el historial de {@link Movie}, {@link Chapter} y {@link Book}
     * que coincidan con el parámetro {@code date}. Utiliza la API de Streams para
     * optimizar la búsqueda en colecciones grandes y formatea la salida en español.
     * </p>
     * @param date Fecha para la cual se desea filtrar los elementos vistos o leídos.
     */
    public static void makeReport(Date date) {
        // 1. Configuración de formatos
        SimpleDateFormat dfFile = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        SimpleDateFormat dfContent = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

        Report report = new Report();
        report.setNameFile("reporte_diario_" + dfFile.format(date));
        report.setExtension("txt");
        report.setTitle(":: REPORTE DE ACTIVIDAD DE VISTOS/LEIDOS DE HOY::");

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        Date searchDate = cal.getTime();

        // 2. Obtener registros de la DB filtrados por la fecha recibida
        // Aseguramos que solo procesamos lo que ocurrió el día "date"
        ViewedDAO viewedDAO = new ViewedDAO() {};
        ArrayList<Viewed> viewedList = viewedDAO.readByDate(searchDate);

        // Verificar cuantos registros trae la lista
        System.out.println("Registros encontrados para " + dfContent.format(searchDate) + ": " + viewedList.size());

        // 3. Flujo Ultra-Optimizado: Unificamos Movies, Chapters y Books en un solo String
        String bodyContent = viewedList.stream()
                .map(v -> findContentById(v.getIdItem(), v.getIdType())) // Cruce de datos
                .filter(Objects::nonNull)                               // Seguridad ante IDs inexistentes
                .map(item -> item.toString() + "\n")
                .collect(Collectors.joining("", "REPORTE DE ACTIVIDAD DE VISTOS/LEIDOS\nFecha: " + dfContent.format(date) + "\n", ""));

        // 4. Validación: Si no hubo actividad, informamos al usuario
        if (bodyContent.isEmpty()) {
            System.out.println("No se encontró actividad de lectura o visualización para hoy: " + dfContent.format(date));
            return;
        }

        // 5. Ensamblaje y creación del archivo
        report.setContent(bodyContent);
        report.makeReport();

        System.out.println("Reporte del día generado exitosamente.");
    }

    /**
     * Busca el objeto real (Movie, Chapter o Book) basándose en los metadatos de la tabla viewed.
     */
    private static Object findContentById(int id, int idType) {
        // idType: 1=Movie, 2=Serie, 4=Book, 5=Magazine (Asegúrate que coincidan con tus constantes)
        return switch (idType) {
            case 1 -> movies.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
            case 2 -> series.stream()
                    .flatMap(s -> s.getChapters().stream())
                    .filter(c -> c.getId() == id).findFirst().orElse(null);
            case 4 -> books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
            case 5 -> magazines.stream().filter(ma -> ma.getId() == id).findFirst().orElse(null);
            default -> null;
        };
    }
}