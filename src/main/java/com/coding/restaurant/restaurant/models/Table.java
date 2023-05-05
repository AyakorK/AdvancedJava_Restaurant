package com.coding.restaurant.restaurant.models;

public class Table {
    public int getNumber() {
        return numero;
    }

    public String getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public boolean isFull() {
        return isFull;
    }

    public String getTableUUID() {
        return tableUUID;
    }

    private final int numero;
    private final String location;
    private final int size;
    private final boolean isFull;

    private String tableUUID;

    /**
     * 
     * @param tableUUID
     * @param numero
     * @param location
     * @param size
     * @param isFull
     */
    public Table(String tableUUID, int numero, String location, int size, boolean isFull) {
        this.tableUUID = tableUUID;
        this.numero = numero;
        this.location = location;
        this.size = size;
        this.isFull = isFull;
    }
}
