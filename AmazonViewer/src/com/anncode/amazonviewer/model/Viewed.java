package com.anncode.amazonviewer.model;

import java.util.Date;

public class Viewed {
    private int id;
    private int idItem;     // ID de la película, capítulo o libro
    private int idUser;     // ID del usuario que realizó la acción
    private int idType;     // 1 para Movie, 2 para Serie, 3 para Chapter, 4 para Book
    private Date date;      // La fecha exacta de la acción

    public Viewed(int idItem, int idUser, int idType, Date date) {
        this.idItem = idItem;
        this.idUser = idUser;
        this.idType = idType;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
