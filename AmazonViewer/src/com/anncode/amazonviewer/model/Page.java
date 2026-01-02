package com.anncode.amazonviewer.model;

/**
 * <h2>Page</h2>
 * Representa una página individual de un libro.
 */
public class Page {
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