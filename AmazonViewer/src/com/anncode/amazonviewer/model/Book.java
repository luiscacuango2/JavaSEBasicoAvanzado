package com.anncode.amazonviewer.model;

import java.util.ArrayList;
import java.util.Date;

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

    /**
     * Constructor para crear una instancia de la clase {@code Book}
     * @param title Título del libro.
     * @param edititionDate Fecha de edición del libro.
     * @param editorial Nombre del editorial del libro.
     * @param authors AArreglo de autores del libro.
     */
	public Book(String title, Date edititionDate, String editorial, String[] authors) {
		super(title, edititionDate, editorial);
		// TODO Auto-generated constructor stub
		setAuthors(authors);
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
     * @return "Sí" si fue leído, "No" en caso contrario.
     */
	public String isReaded() {
		String leido = "";
		if(readed == true) {
			leido = "Sí";
		}else {
			leido = "No";
		}
		
		return leido;
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

    /**
     * {@inheritDoc}
     * <p>
     * Construye una representación en cadena de texto detallada del libro.
     * Incluye el título, la editorial, la fecha de edición y recorre el arreglo
     * de autores para listarlos de forma tabular.
     * </p>
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
        return  detailBook;
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
			setTimeReaded((int)(dateF.getTime() - dateI.getTime()));
		}else {
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

        for (int i = 0; i < 100000; i++) {
            System.out.println("..........");
        }

        //Termine de verla
        stopToSee(dateI, new Date());
        System.out.println();
        System.out.println("Leíste: " + toString());
        System.out.println("Por: " + getTimeReaded() + " milisegundos");
    }

    /**
     * Genera una lista de libros ficticios para poblar la aplicación.
     * @return Un {@code ArrayList} de objetos {@link Book}.
     */
	public static ArrayList<Book> makeBookList() {
		ArrayList<Book> books = new ArrayList();
		String[] authors = new String[3];
		for (int i = 0; i < 3; i++) {
			authors[i] = "author "+i;
		}
		for (int i = 1; i <= 5; i++) {
			books.add(new Book("Book " + i, new Date(), "editorial " + i, authors));
		}
		
		return books;
	}
	
}
