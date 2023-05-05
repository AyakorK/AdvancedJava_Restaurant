package com.coding.restaurant.restaurant.models;

/**
 * Model of the table
 * @param numero : number of the table (int)
 * @param location : location of the table (String)
 * @param size : size of the table (int)
 * @param isFull : is the table full (boolean)
 */
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

    private final int numero;
    private final String location;
    private final int size;
    private final boolean isFull;

    public Table(int numero, String location, int size, boolean isFull) {
        this.numero = numero;
        this.location = location;
        this.size = size;
        this.isFull = isFull;
    }
}
