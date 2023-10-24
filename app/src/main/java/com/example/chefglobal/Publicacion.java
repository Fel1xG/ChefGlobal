package com.example.chefglobal;

public class Publicacion {
    private String id; // Identificador único
    private String userId;
    private String userName;
    private String texto;
    private String imageUrl;

    // Constructor vacío requerido para Firestore
    public Publicacion() {
    }

    // Constructor con todos los campos excepto el ID
    public Publicacion(String userId, String userName, String texto, String imageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.texto = texto;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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




