package com.example.chefglobal;

import java.util.Date;

public class Notifi {
    private String nombreUsuario;
    private Date fecha;
    private String mensaje; // Agrega el atributo mensaje

    public Notifi() {
        // Constructor vacío requerido para Firebase
    }

    public Notifi(String nombreUsuario, Date fecha, String mensaje) {
        this.nombreUsuario = nombreUsuario;
        this.fecha = fecha;
        this.mensaje = mensaje; // Incluye el mensaje
    }

    // Agrega el getter para el mensaje
    public String getMensaje() {
        return mensaje;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Date getFecha() {
        return fecha;
    }
}
