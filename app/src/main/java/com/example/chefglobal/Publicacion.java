package com.example.chefglobal;

public class Publicacion {
    private String userId;
    private String userName; // Agrega el nombre del usuario
    private String texto;
    private String imageUrl;

    // Constructor vacío requerido para Firestore
    public Publicacion() {
    }

    public Publicacion(String userId, String userName, String texto, String imageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.texto = texto;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName; // Método para obtener el nombre del usuario
    }

    public void setUserName(String userName) {
        this.userName = userName; // Método para establecer el nombre del usuario
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

    public String getId() {
        return userId; // O el campo que uses como ID de la publicación
    }
}


