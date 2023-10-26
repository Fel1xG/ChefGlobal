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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.chefglobal.Publicacion;

public class RecetasAdapter extends FirestoreRecyclerAdapter<Publicacion, com.example.chefglobal.RecetasAdapter.RecetasViewHolder> {
    private final boolean isRecetasFragment;
    private final Context context;

    public RecetasAdapter(FirestoreRecyclerOptions<Publicacion> options, boolean isRecetasFragment, Context context) {
        super(options);
        this.isRecetasFragment = isRecetasFragment;
        this.context = context;
    }


    static class RecetasViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagenReceta;
        private final TextView nombreReceta;
        private final TextView descripcionReceta;
        private final Button botonGuardar;
        private final Button botonEliminar;

        public RecetasViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenReceta = itemView.findViewById(R.id.imagenPublicacion);
            nombreReceta = itemView.findViewById(R.id.nombreUsuario);
            descripcionReceta = itemView.findViewById(R.id.textoPublicacion);
            botonGuardar = itemView.findViewById(R.id.btnGuardarPublicacion);
            botonEliminar = itemView.findViewById(R.id.btnEliminarPublicacion);
        }

        public void bind(Publicacion receta) {
            nombreReceta.setText(receta.getUserName());
            descripcionReceta.setTypeface(null, Typeface.NORMAL);
            if (receta.getImageUrl() != null && !receta.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(receta.getImageUrl())
                        .into(imagenReceta);
            } else {
                imagenReceta.setVisibility(View.GONE);
            }
        }

        public void setBotonesVisibilidad(boolean mostrarGuardar, boolean mostrarEliminar) {
            botonGuardar.setVisibility(mostrarGuardar ? View.VISIBLE : View.GONE);
            botonEliminar.setVisibility(mostrarEliminar ? View.VISIBLE : View.GONE);
        }
    }

    @NonNull
    @Override
    public com.example.chefglobal.RecetasAdapter.RecetasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publicacion, parent, false);
        return new com.example.chefglobal.RecetasAdapter.RecetasViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecetasViewHolder holder, int position, @NonNull Publicacion receta) {
        holder.bind(receta); // Esto establece la imagen y el nombre
        holder.descripcionReceta.setText(receta.getTexto()); // Esto establece la descripción
        holder.setBotonesVisibilidad(!isRecetasFragment, isRecetasFragment);

        // Obtén el ID de la receta
        String recetaId = getSnapshots().getSnapshot(position).getId();

        if (isRecetasFragment) {
            holder.botonEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarReceta(recetaId);
                }
            });
        }
    }


    private void eliminarReceta(String recetaId) {
        Log.d("MiApp", "ID de receta a eliminar (en eliminarReceta): " + recetaId); // Agrega esta línea para verificar el ID
        new AlertDialog.Builder(context)
                .setTitle("Eliminar Receta")
                .setMessage("¿Estás seguro de que quieres eliminar esta receta?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarRecetaDeMisRecetas(recetaId);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void eliminarRecetaDeMisRecetas(String recetaId) {
        Log.d("MiApp", "Eliminar receta con ID: " + recetaId);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            if (recetaId != null && !recetaId.isEmpty()) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference recetaDocRef = db.collection("usuarios").document(userId)
                        .collection("recetasGuardadas").document(recetaId);
                recetaDocRef.delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Receta eliminada con éxito", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Error al eliminar la receta de Mis Recetas", Toast.LENGTH_SHORT).show();
                        });
            }
        } else {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
