package com.example.chefglobal;

public class Comida {
    private String nombre;
    private String ingredientes;

    public Comida(String nombre, String ingredientes) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }
}




