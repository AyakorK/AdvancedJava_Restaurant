package com.coding.restaurant.restaurant.Model;

public class Table {
    // UUID	Numero	Location	Size	isFull
    private String numero;
    private String location;
    private int size;
    private boolean isFull;

    public Table(String numero, String location, int size, boolean isFull) {
        this.numero = numero;
        this.location = location;
        this.size = size;
        this.isFull = isFull;
    }
}
