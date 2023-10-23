package com.example.chefglobal;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PublicacionViewHolder extends RecyclerView.ViewHolder {

    private ImageView imagenPublicacion;
    private TextView textoPublicacion;
    private TextView nombreUsuario;
    private Button btnGuardarPublicacion; // Agrega un botón para guardar la publicación

    public PublicacionViewHolder(@NonNull View itemView) {
        super(itemView);

        imagenPublicacion = itemView.findViewById(R.id.imagenPublicacion);
        textoPublicacion = itemView.findViewById(R.id.textoPublicacion);
        nombreUsuario = itemView.findViewById(R.id.nombreUsuario);
        btnGuardarPublicacion = itemView.findViewById(R.id.btnGuardarPublicacion); // Asigna el botón en el layout

        // Configura el clic del botón Guardar
        btnGuardarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para guardar la publicación
                guardarPublicacion();
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

    // Método para guardar la publicación
    private void guardarPublicacion() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference publicacionesRef = db.collection("publicaciones");

        // Asegúrate de tener el resto de los datos requeridos
        String userId = "ID del usuario";
        String userName = "Nombre del usuario";
        String texto = "Texto de la publicación";
        String imageUrl = "URL de la imagen";

        // Crear un objeto Publicacion con los datos
        Publicacion nuevaPublicacion = new Publicacion(userId, userName, texto, imageUrl);

        // Guardar la publicación en Firestore
        publicacionesRef.add(nuevaPublicacion)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(itemView.getContext(), "Publicación guardada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(itemView.getContext(), "Error al guardar la publicación", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
