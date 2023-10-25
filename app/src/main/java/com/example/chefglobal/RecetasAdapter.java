package com.example.chefglobal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RecetasAdapter extends FirestoreRecyclerAdapter<Publicacion, RecetasAdapter.RecetasViewHolder> {
    private boolean isRecetasFragment;
    private Context context; // Agregar una variable de contexto

    public RecetasAdapter(@NonNull FirestoreRecyclerOptions<Publicacion> options, boolean isRecetasFragment, Context context) {
        super(options);
        this.isRecetasFragment = isRecetasFragment;
        this.context = context; // Asignar el contexto
    }

    public static class RecetasViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenReceta;
        private TextView nombreReceta;
        private TextView descripcionReceta;
        private Button botonGuardar;
        private Button botonEliminar;

        public RecetasViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenReceta = itemView.findViewById(R.id.imagenPublicacion);
            nombreReceta = itemView.findViewById(R.id.nombreUsuario);
            descripcionReceta = itemView.findViewById(R.id.textoPublicacion);
            botonGuardar = itemView.findViewById(R.id.btnGuardarPublicacion);
            botonEliminar = itemView.findViewById(R.id.btnEliminarPublicacion);
        }

        public void setImagenReceta(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .into(imagenReceta);
            } else {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publicacion, parent, false);
        return new RecetasViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecetasViewHolder holder, int position, @NonNull Publicacion receta) {
        holder.setNombreReceta(receta.getTexto());

        // Establece el estilo de texto normal para el TextView
        holder.descripcionReceta.setTypeface(null, Typeface.NORMAL);

        holder.setImagenReceta(receta.getImageUrl());

        if (isRecetasFragment) {
            // Si estás en el fragmento Recetas, muestra el botón de eliminar
            holder.botonEliminar.setVisibility(View.VISIBLE);
            holder.botonGuardar.setVisibility(View.GONE);
            holder.botonEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarPublicacion(receta.getId());
                }
            });
        } else {
            holder.botonEliminar.setVisibility(View.GONE);
            holder.botonGuardar.setVisibility(View.VISIBLE);
        }
    }

    private void eliminarPublicacion(String publicacionId) {
        new AlertDialog.Builder(context)
                .setTitle("Eliminar Receta")
                .setMessage("¿Estás seguro de que quieres eliminar esta receta?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Llama a la función para eliminar la publicación
                        Log.d("PublicacionId", "ID de publicación: " + publicacionId);
                        eliminarPublicacionDeMisRecetas(publicacionId);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // Agregar un método para eliminar la publicación de "Mis Recetas"
    private void eliminarPublicacionDeMisRecetas(String publicacionId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Verifica que el usuario esté autenticado
            if (publicacionId != null && !publicacionId.isEmpty()) {
                db.collection("usuarios").document(userId).collection("recetasGuardadas")
                        .document(publicacionId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Publicación eliminada con éxito
                                Toast.makeText(context, "Receta eliminada con éxito", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                // Error al eliminar la receta de "Mis Recetas"
                                Toast.makeText(context, "Error al eliminar la receta de Mis Recetas", Toast.LENGTH_SHORT).show();
                                e.printStackTrace(); // Registra la excepción
                            }
                        });
            } else {
                // Maneja el caso en el que publicacionId es nulo o vacío
                Toast.makeText(context, "ID de publicación no válido", Toast.LENGTH_SHORT).show();
            }
        } else {
            // El usuario no está autenticado, muestra un mensaje de error o redirige a la autenticación.
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

}



