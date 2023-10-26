package com.example.chefglobal;

import java.util.Date;

public class Notifi {
    private String nombreUsuario;
    private Date fecha;

    public Notifi() {
        // Constructor vacío requerido para Firebase
    }

    public Notifi(String nombreUsuario, Date fecha) {
        this.nombreUsuario = nombreUsuario;
        this.fecha = fecha;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Date getFecha() {
        return fecha;
    }
}
