package com.example.chefglobal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {
    private List<Publicacion> publicaciones;

    public PublicacionAdapter(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public static class PublicacionViewHolder extends RecyclerView.ViewHolder {
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

    @NonNull
    @Override
    public PublicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publicacion, parent, false);
        return new PublicacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacionViewHolder holder, int position) {
        Publicacion publicacion = publicaciones.get(position);
        holder.setTextoPublicacion(publicacion.getTexto());
        holder.setImagenPublicacion(publicacion.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }
}

