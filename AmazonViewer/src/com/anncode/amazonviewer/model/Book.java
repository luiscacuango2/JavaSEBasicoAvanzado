package com.anncode.amazonviewer.model;

import com.anncode.util.AmazonUtil;

import java.util.ArrayList;
import java.util.Date;

import com.anncode.util.AmazonUtil;

/**
 * Book es una clase que representa los libros en la aplicación.
 * <p>
 *  Hereda de {@link Publication} e implementa {@link IVisualizable} para
 *  gestionar el tiempo de lectura y el estado de leído.
 * </p>
 * @author Luigi
 * @version 1.2
 * @since 2025-12-31
 */
public class Book extends Publication implements IVisualizable {
    private int id;
    private String isbn;
    private boolean readed;
    private int timeReaded;
    private ArrayList<Page> pages;

    /**
     * Constructor para crear una instancia de la clase {@code Book}
     *
     * @param title         Título del libro.
     * @param edititionDate Fecha de edición del libro.
     * @param editorial     Nombre del editorial del libro.
     * @param authors       AArreglo de autores del libro.
     */
    public Book(String title, Date edititionDate, String editorial, String[] authors, ArrayList<Page> pages) {
        super(title, edititionDate, editorial);
        // TODO Auto-generated constructor stub
        setAuthors(authors);
        this.pages = pages;
    }


    public int getId() {
        return id;
    }


    public String getIsbn() {
        return isbn;
    }


    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Devuelve una cadena de texto indicando si el libro fue leído.
     *
     * @return "Sí" si fue leído, "No" en caso contrario.
     */
    public String isReaded() {
        return readed ? "Sí" : "No";
    }


    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public boolean getIsReaded() {
        return readed;
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
        // TODO Auto-generated method stub
        String detailBook = "\n :: BOOK ::" +
                "\n Title: " + getTitle() +
                "\n Editorial: " + getEditorial() +
                "\n Edition Date: " + getEdititionDate() +
                "\n Authors: ";
        for (int i = 0; i < getAuthors().length; i++) {
            detailBook += "\t" + getAuthors()[i] + " ";
        }
        return detailBook;
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
        ArrayList<Book> books = new ArrayList();
        String[] authors = new String[3];
        for (int i = 0; i < 3; i++) {
            authors[i] = "author " + i;
        }

        ArrayList<Page> pages = new ArrayList();
        int pagina = 0;
        for (int i = 0; i < 3; i++) {
            pagina = i + 1;
            pages.add(new Book.Page(pagina, "El contenido de la página " + pagina));
        }

        for (int i = 1; i <= 5; i++) {
            books.add(new Book("Book " + i, new Date(), "editorial " + i, authors, pages));
        }

        return books;

    }

    public static class Page {
        private int id;
        private int number;
        private String content;

        public Page(int number, String content) {
            this.number = number;
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
