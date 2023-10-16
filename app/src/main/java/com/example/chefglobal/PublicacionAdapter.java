package com.example.chefglobal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {
    private List<Publicacion> publicaciones; // Debes definir una lista de objetos Publicacion

    public PublicacionAdapter(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    // ViewHolder: Describe una vista del elemento y sus elementos UI
    public static class PublicacionViewHolder extends RecyclerView.ViewHolder {
        // Define las vistas que quieres vincular con datos
        public View itemView; // Puedes agregar más vistas según tu diseño

        public PublicacionViewHolder(View view) {
            super(view);
            itemView = view;
            // Aquí puedes inicializar las vistas (ej. imagen, texto, etc.)
        }
    }

    @NonNull
    @Override
    public PublicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home, parent, false);
        return new PublicacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacionViewHolder holder, int position) {
        // Obtiene la Publicacion actual
        Publicacion publicacion = publicaciones.get(position);

        // Vincula los datos a las vistas (ejemplo: holder.textView.setText(publicacion.getTexto());)
        // Debes adaptar esto a tu diseño y objetos Publicacion
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }
}

