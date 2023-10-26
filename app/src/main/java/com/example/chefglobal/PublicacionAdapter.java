package com.example.chefglobal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.example.chefglobal.Publicacion;

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {
    private List<Publicacion> publicaciones;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context context;

    public PublicacionAdapter(List<Publicacion> publicaciones, Context context) {
        this.publicaciones = publicaciones;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public class PublicacionViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenPublicacion;
        private TextView textoPublicacion;
        private TextView nombreUsuario;
        private Button btnGuardarPublicacion;

        public PublicacionViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenPublicacion = itemView.findViewById(R.id.imagenPublicacion);
            textoPublicacion = itemView.findViewById(R.id.textoPublicacion);
            nombreUsuario = itemView.findViewById(R.id.nombreUsuario);
            btnGuardarPublicacion = itemView.findViewById(R.id.btnGuardarPublicacion);

            btnGuardarPublicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Publicacion publicacion = publicaciones.get(position);
                        guardarPublicacion(publicacion);
                    }
                }
            });
        }

        public void setImagenPublicacion(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(context)
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

        public void guardarPublicacion(Publicacion publicacion) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            if (mAuth.getCurrentUser() != null) {
                String userId = mAuth.getCurrentUser().getUid();

                // Crea una referencia al documento de recetasGuardadas dentro de la colección de usuarios
                DocumentReference recetasGuardadasRef = db.collection("usuarios").document(userId).collection("recetasGuardadas").document();

                // Obtiene la descripción de la publicación
                String descripcion = publicacion.getTexto(); // Asegúrate de que este sea el campo correcto para la descripción

                // Establece la descripción en la receta guardada
                publicacion.setTexto(descripcion);

                // Establece la receta en el documento
                recetasGuardadasRef.set(publicacion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Receta guardada con éxito
                                // Puedes mostrar un mensaje o realizar otra acción aquí
                                showToast("Receta guardada con éxito");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                // Manejar errores
                                if (e instanceof FirebaseAuthException) {
                                    // Si es un error relacionado con Firebase Authentication, puedes mostrar un mensaje específico
                                    String mensaje = "Error de autenticación: " + e.getMessage();
                                    Log.e("Firebase", mensaje);
                                    // También puedes mostrar un mensaje al usuario
                                    showToast(mensaje);
                                } else {
                                    // Para otros tipos de errores, muestra un mensaje genérico
                                    String mensaje = "Ocurrió un error al guardar la receta. Por favor, inténtalo de nuevo más tarde.";
                                    Log.e("Firebase", mensaje);
                                    // También puedes mostrar un mensaje al usuario
                                    showToast(mensaje);
                                }
                            }
                        });
            }
        }

        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
        holder.setNombreUsuario(publicacion.getUserName());
    }


    @Override
    public int getItemCount() {
        return publicaciones.size();
    }
}