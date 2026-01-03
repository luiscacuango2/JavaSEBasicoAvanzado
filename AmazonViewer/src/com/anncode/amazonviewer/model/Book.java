package com.anncode.amazonviewer.model;

import com.anncode.amazonviewer.dao.BookDAO;
import com.anncode.util.AmazonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Es una clase que representa los libros en la aplicación.
 * <p>
 *  Hereda de {@link Publication} e implementa {@link IVisualizable} para
 *  gestionar el tiempo de lectura y el estado de leído.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Book extends Publication implements IVisualizable, BookDAO {
    private int id;
    private String isbn;
    private int timeReaded;
    private ArrayList<Page> pages;

    /**
     * Constructor para crear una instancia de la clase {@code Book}
     *
     * @param title         Título del libro.
     * @param edititionDate Fecha de edición del libro.
     * @param editorial     Nombre del editorial del libro.
     * @param authors       Arreglo de autores del libro.
     * @param pages         Lista de páginas del libro.
     */
    public Book(String title, Date edititionDate, String editorial, String authors, ArrayList<Page> pages) {
        super(title, edititionDate, editorial);
        setAuthors(authors);
        this.pages = pages;
    }

    /**
     * Obtiene el identificador del libro.
     * @return El identificador del libro.
     */
    public int getId() {
        return id;
    }

    /**
     * Define el identificador del libro.
     * @param id El identificador a establecer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el código ISBN (International Standard Book Number) del libro.
     * <p>
     * El ISBN es un identificador único de 10 o 13 dígitos que permite catalogar
     * de manera precisa el título y la edición del material bibliográfico
     * dentro de la plataforma.
     * </p>
     * @return Un {@code String} que contiene el código ISBN del libro.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Define el ISBN del libro.
     * @param isbn El ISBN a establecer.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Obtiene el tiempo de lectura del libro.
     * @return El tiempo de lectura en milisegundos.
     */
    public int getTimeReaded() {
        return timeReaded;
    }

    /**
     * Define el tiempo de lectura del libro.
     * @param timeReaded El tiempo de lectura a establecer.
     */
    public void setTimeReaded(int timeReaded) {
        this.timeReaded = timeReaded;
    }

    /**
     * Obtiene la lista de páginas del libro.
     * @return Un {@code ArrayList} de objetos {@link Page}.
     */
    public ArrayList<Page> getPages() {
        return pages;
    }

    /**
     * Asigna o actualiza la colección de páginas que conforman el libro.
     * <p>
     * Este método permite establecer la estructura de lectura del libro mediante
     * una lista de objetos {@link Page}. Es utilizado principalmente durante
     * el proceso de construcción del objeto o al recuperar el contenido
     * detallado desde la fuente de datos.
     * </p>
     *
     * @param pages Una {@link ArrayList} de objetos {@link Page} que representan
     * el contenido textual y numerado del libro.
     */
    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    /**
     * <p>
     * Construye una representación en cadena de texto detallada del libro.
     * Incluye el título, la editorial, la fecha de edición y recorre el arreglo
     * de autores para listarlos de forma tabular.
     * </p>
     * @return Una cadena de texto ({@code String}) formateada con la ficha técnica del libro.
     */
    @Override
    public String toString() {
        // Creamos el formato deseado: día/mes/año
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormated = sdf.format(getEditionDate());

        String detailBook = "\n :: DETALLES DEL LIBRO ::" +
                "\n Título: " + getTitle() +
                "\n Editorial: " + getEditorial() +
                "\n Fecha Edición: " + dateFormated + // Usamos la fecha formateada
                "\n Autores: ";
        // Obtenemos el arreglo de autores
        String[] authors = getAuthors();

        if (authors != null && authors.length > 0) {
            for (int i = 0; i < authors.length; i++) {
                // Añadimos el autor y una coma si no es el último
                detailBook += authors[i] + (i < authors.length - 1 ? ", " : "");
            }
        } else {
            detailBook += "Sin autores registrados";
        }
        return detailBook;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date startToSee(Date dateI) {
        return dateI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopToSee(Date dateI, Date dateF) {
        if (dateF.getTime() > dateI.getTime()) {
            setTimeReaded((int) (dateF.getTime() - dateI.getTime()));
        } else {
            setTimeReaded(0);
        }
    }

    /**
     * Método que simula la lectura de un libro, calcula el tiempo transcurrido
     * y marca el libro como leído.
     */
    public void view() {
        // 1. Iniciamos en false para asegurar que solo la lectura completa lo cambie
        setReaded(false);
        Date dateI = startToSee(new Date());

        int i = 0;
        int response = 0;
        boolean finished = false;

        System.out.println(this.toString());

        do {
            System.out.println("==============================================");
            System.out.println(" LEYENDO: " + getTitle().toUpperCase());
            System.out.println(" Página: " + getPages().get(i).getNumber() + " de " + getPages().size());
            System.out.println("----------------------------------------------");
            System.out.println(getPages().get(i).getContent());
            System.out.println("==============================================\n");

            if (i > 0) System.out.println("1. Regresar Página");
            if (i < getPages().size() - 1) System.out.println("2. Siguiente Página");
            System.out.println("0. Cerrar Libro");

            response = AmazonUtil.validateUserResponseMenu(0, 2);

            if (response == 2 && i < getPages().size() - 1) {
                i++;
                // Detectamos si el usuario llegó a la última página
                if (i == getPages().size() - 1) {
                    finished = true;
                }
            } else if (response == 1 && i > 0) {
                i--;
            }

        } while (response != 0);

        // 2. Lógica de Persistencia: Solo si leyó hasta el final
        if (finished || getPages().size() == 1) {
            setReaded(true); // Actualizamos objeto en memoria

            // INSERT en la base de datos (Tabla viewed)
            this.setBookRead(this);

            System.out.println("**********************************************");
            System.out.println(" ¡LIBRO COMPLETADO! Registrado en tu historial.");
            System.out.println("**********************************************");
        }

        stopToSee(dateI, new Date());
        System.out.println(" Tiempo total de lectura: " + getTimeReaded() + " ms");
    }

    /**
     * Genera datos de prueba para los libros.
     * @return Un {@code ArrayList} de objetos {@link Book}.
     */
    public static ArrayList<Book> makeBookList() {
        BookDAO bookDAO = new BookDAO() {};
        ArrayList<Book> books = bookDAO.read();

        for (Book book : books) {
            ArrayList<Page> pagesFromDB = bookDAO.readPages(book.getId());

            if (pagesFromDB.isEmpty()) {
                pagesFromDB.add(new Page(1, "Sin contenido en DB."));
            }
            book.setPages(pagesFromDB);
        }
        return books;
    }
}
