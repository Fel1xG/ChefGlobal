package com.example.chefglobal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PublicacionViewHolder extends RecyclerView.ViewHolder {

    private ImageView imagenPublicacion;
    private TextView textoPublicacion;

    public PublicacionViewHolder(@NonNull View itemView) {
        super(itemView);

        imagenPublicacion = itemView.findViewById(R.id.imagenPublicacion);
        textoPublicacion = itemView.findViewById(R.id.textoPublicacion);
    }

    public void setImagenPublicacion(String imageUrl) {
        // Aquí configura la imagen de la publicación utilizando una biblioteca de carga de imágenes (Glide, Picasso, etc.)
        // Ejemplo: Picasso.get().load(imageUrl).into(imagenPublicacion);
    }

    public void setTextoPublicacion(String texto) {
        textoPublicacion.setText(texto);
    }
}

