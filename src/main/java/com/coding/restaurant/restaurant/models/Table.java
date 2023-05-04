package com.coding.restaurant.restaurant.models;

public class Table {
    public int getNumber() {
        return numero;
    }

    public void setNumber(int numero) {
        this.numero = numero;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    // UUID	Numero	Location	Size	isFull
    private int numero;
    private String location;
    private int size;
    private boolean isFull;

    public String getTableUUID() {
        return tableUUID;
    }

    private String tableUUID;

    public Table(String tableUUID, int numero, String location, int size, boolean isFull) {
        this.tableUUID = tableUUID;
        this.numero = numero;
        this.location = location;
        this.size = size;
        this.isFull = isFull;
    }
}
