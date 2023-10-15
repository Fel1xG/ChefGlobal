package com.example.chefglobal;

public class Publicacion {
    private String userId; // ID del usuario que hizo la publicación
    private String texto;   // Texto de la publicación
    private String imageUrl; // URL de la imagen de la publicación

    public Publicacion() {
        // Constructor vacío requerido para Firestore
    }

    public Publicacion(String userId, String texto, String imageUrl) {
        this.userId = userId;
        this.texto = texto;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


