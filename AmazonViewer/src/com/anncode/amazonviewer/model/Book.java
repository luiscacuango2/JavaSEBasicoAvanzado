package com.anncode.amazonviewer.model;

import com.anncode.amazonviewer.dao.BookDAO;
import com.anncode.amazonviewer.model.Page;
import com.anncode.util.AmazonUtil;
import java.util.ArrayList;
import java.util.Date;

/**
 * <h2>Book</h2>
 * Book es una clase que representa los libros en la aplicación.
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
     */
    public Book(String title, Date edititionDate, String editorial, String authors, ArrayList<Page> pages) {
        super(title, edititionDate, editorial);
        // TODO Auto-generated constructor stub
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getTimeReaded() {
        return timeReaded;
    }

    public void setTimeReaded(int timeReaded) {
        this.timeReaded = timeReaded;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Construye una representación en cadena de texto detallada del libro.
     * Incluye el título, la editorial, la fecha de edición y recorre el arreglo
     * de autores para listarlos de forma tabular.
     * </p>
     *
     * @return Una cadena de texto ({@code String}) formateada con la ficha técnica del libro.
     */
    @Override
    public String toString() {
        return "\n :: BOOK ::" +
                "\n Title: " + getTitle() +
                "\n Editorial: " + getEditorial() +
                "\n Edition Date: " + getEditionDate() +
                "\n Authors: " + getAuthors();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date startToSee(Date dateI) {
        // TODO Auto-generated method stub
        return dateI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopToSee(Date dateI, Date dateF) {
        // TODO Auto-generated method stub
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
        setReaded(true);
        Date dateI = startToSee(new Date());

        int i = 0;
        do {
            // Limpiamos la vista con una cabecera clara en lugar de miles de puntos
            System.out.println("\n==============================================");
            System.out.println(" LEYENDO: " + getTitle().toUpperCase());
            System.out.println(" Editorial: " + getEditorial());
            System.out.println(" Pagina: " + getPages().get(i).getNumber() + " de " + getPages().size());
            System.out.println("----------------------------------------------");
            System.out.println(getPages().get(i).getContent());
            System.out.println("==============================================\n");

            if (i > 0) {
                System.out.println("1. Regresar Página");
            }
            if (i < getPages().size() - 1) {
                System.out.println("2. Siguiente Página");
            }
            System.out.println("0. Cerrar Libro");
            System.out.println();

            int response = AmazonUtil.validateUserResponseMenu(0, 2);

            if (response == 2 && i < getPages().size() - 1) {
                i++;
            } else if (response == 1 && i > 0) {
                i--;
            } else if (response == 0) {
                break;
            }

        } while (i < getPages().size());

        // Terminamos la lectura
        stopToSee(dateI, new Date());
        System.out.println("\n**********************************************");
        System.out.println(" FINALIZASTE LA LECTURA DE: " + getTitle());
        System.out.println(" Tiempo total de lectura: " + getTimeReaded() + " milisegundos");
        System.out.println("**********************************************\n");
    }

    /**
     * Genera datos de prueba para los libros.
     * @return Un {@code ArrayList} de objetos {@link Book}.
     */
    public static ArrayList<Book> makeBookList() {
        BookDAO bookDAO = new BookDAO() {};
        ArrayList<Book> books = bookDAO.read();

        for (Book book : books) {
            // Aquí ya no habrá conflicto de tipos
            ArrayList<Page> pagesFromDB = bookDAO.readPages(book.getId());

            if (pagesFromDB.isEmpty()) {
                pagesFromDB.add(new Page(1, "Sin contenido en DB."));
            }
            book.setPages(pagesFromDB);
        }
        return books;
    }

    private static ArrayList<Page> makePagesList() {
        ArrayList<Page> pages = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            pages.add(new Page(i, "Contenido de la página " + i + "..."));
        }
        return pages;
    }
}
