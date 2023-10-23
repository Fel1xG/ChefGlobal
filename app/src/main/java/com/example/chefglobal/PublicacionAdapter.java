package com.example.chefglobal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.bumptech.glide.Glide;

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {
    private List<Publicacion> publicaciones;
    private FirebaseFirestore db;

    public PublicacionAdapter(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
        db = FirebaseFirestore.getInstance(); // Inicializa la referencia de Firebase Firestore
    }

    public static class PublicacionViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenPublicacion;
        private TextView textoPublicacion;
        private TextView nombreUsuario; // Agregar TextView para el nombre del usuario
        private ImageView btnGuardarPublicacion; // Agregar botón de guardar publicación

        public PublicacionViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenPublicacion = itemView.findViewById(R.id.imagenPublicacion);
            textoPublicacion = itemView.findViewById(R.id.textoPublicacion);
            nombreUsuario = itemView.findViewById(R.id.nombreUsuario);
            btnGuardarPublicacion = itemView.findViewById(R.id.btnGuardarPublicacion); // Referencia al botón

            // Agregar un clic en el botón de guardar
            btnGuardarPublicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Aquí puedes implementar la lógica para guardar la publicación
                    // Por ejemplo, puedes agregarla a una colección "guardados" en Firestore o en Realtime Database
                }
            });
        }

        public void setImagenPublicacion(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .into(imagenPublicacion);
            } else {
                imagenPublicacion.setVisibility(View.GONE);
            }
        }

        public void setTextoPublicacion(String texto) {
            textoPublicacion.setText(texto);
        }

        public void setNombreUsuario(String nombre) {
            nombreUsuario.setText(nombre);
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

        obtenerNombreUsuario(publicacion.getUserId(), holder);
    }

    private void obtenerNombreUsuario(String userId, final PublicacionViewHolder holder) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String nombreUsuario = documentSnapshot.getString("nombre"); // Ajusta el campo de nombre según tu estructura de datos
                            holder.setNombreUsuario(nombreUsuario);
                        } else {
                            // Manejar el caso en el que no se encuentra el usuario
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Manejar errores
                    }
                });
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }
}



