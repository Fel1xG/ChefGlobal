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

public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.RecetasViewHolder> {
    private List<Publicacion> recetas; // Cambia a la clase adecuada que contenga las recetas guardadas

    public RecetasAdapter(List<Publicacion> recetas) {
        this.recetas = recetas;
    }

    public static class RecetasViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenReceta;
        private TextView nombreReceta;
        private TextView descripcionReceta;

        public RecetasViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenReceta = itemView.findViewById(R.id.imagenPublicacion); // Ajusta el ID según tu diseño
            nombreReceta = itemView.findViewById(R.id.nombreUsuario); // Ajusta el ID según tu diseño
            descripcionReceta = itemView.findViewById(R.id.textoPublicacion); // Ajusta el ID según tu diseño
        }

        public void setImagenReceta(String imageUrl) {
            // Configura la imagen de la receta utilizando una biblioteca de carga de imágenes (Glide, Picasso, etc.)
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .into(imagenReceta);
            } else {
                // Si no hay URL de imagen, puedes ocultar la vista de la imagen o establecer una imagen predeterminada.
                imagenReceta.setVisibility(View.GONE);
            }
        }

        public void setNombreReceta(String nombre) {
            nombreReceta.setText(nombre);
        }

        public void setDescripcionReceta(String descripcion) {
            descripcionReceta.setText(descripcion);
        }
    }

    @NonNull
    @Override
    public RecetasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publicacion, parent, false); // Ajusta el diseño del elemento de receta (item_receta.xml) según tus necesidades
        return new RecetasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetasViewHolder holder, int position) {
        Publicacion receta = recetas.get(position); // Ajusta el tipo de datos según tu implementación
        holder.setNombreReceta(receta.getTexto()); // Ajusta el método para obtener el nombre de la receta
        holder.setDescripcionReceta("Descripción de la receta aquí"); // Ajusta la descripción de la receta según tu implementación
        holder.setImagenReceta(receta.getImageUrl()); // Ajusta el método para obtener la imagen de la receta
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }
}


