package com.example.chefglobal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


public class PublicacionViewHolder extends RecyclerView.ViewHolder {

    private ImageView imagenPublicacion;
    private TextView textoPublicacion;

    public PublicacionViewHolder(@NonNull View itemView) {
        super(itemView);

        imagenPublicacion = itemView.findViewById(R.id.imagenPublicacion);
        textoPublicacion = itemView.findViewById(R.id.textoPublicacion);
    }

    public void setImagenPublicacion(String imageUrl) {
        // Configura la imagen de la publicación utilizando una biblioteca de carga de imágenes (Glide, Picasso, etc.)
        // Ejemplo con Glide:
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .into(imagenPublicacion);
        } else {
            // Si no hay URL de imagen, puedes ocultar la vista de la imagen o establecer una imagen predeterminada.
            imagenPublicacion.setVisibility(View.GONE);
        }
    }

    public void setTextoPublicacion(String texto) {
        textoPublicacion.setText(texto);
    }
}


