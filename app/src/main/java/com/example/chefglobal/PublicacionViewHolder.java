package com.example.chefglobal;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PublicacionViewHolder extends RecyclerView.ViewHolder {

    private ImageView imagenPublicacion;
    private TextView textoPublicacion;
    private TextView nombreUsuario;
    Button btnGuardarPublicacion;
    private Context context;

    public PublicacionViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;

        imagenPublicacion = itemView.findViewById(R.id.imagenPublicacion);
        textoPublicacion = itemView.findViewById(R.id.textoPublicacion);
        nombreUsuario = itemView.findViewById(R.id.nombreUsuario);
        btnGuardarPublicacion = itemView.findViewById(R.id.btnGuardarPublicacion);

        btnGuardarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el publicacionId de la publicación desde un método, por ejemplo, obtenerPublicacionId()
                String publicacionId = obtenerPublicacionId();

                if (publicacionId != null && !publicacionId.isEmpty()) {
                    guardarPublicacion(publicacionId);
                }
            }
        });
    }

    public Button getBtnGuardarPublicacion() {
        return btnGuardarPublicacion;
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

    private String obtenerPublicacionId() {
        // Aquí debes implementar la lógica para obtener el publicacionId
        // Puedes recuperarlo de donde lo estés almacenando o acceder a la base de datos
        // Dependiendo de tu implementación, esta función debe retornar el ID de la publicación actual
        return "ID_de_la_publicacion";  // Reemplaza esto con la lógica real
    }

    private void guardarPublicacion(String publicacionId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference publicacionesRef = db.collection("publicaciones");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtiene la referencia de la publicación
        DocumentReference publicacionDocRef = publicacionesRef.document(publicacionId);

        publicacionDocRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            // La publicación existe, obtenemos sus datos
                            String imageUrl = documentSnapshot.getString("imageUrl");
                            String texto = documentSnapshot.getString("texto");

                            // Creamos la nueva publicación
                            Publicacion nuevaPublicacion = new Publicacion(userId, "", texto, imageUrl);

                            // Guardamos la nueva publicación
                            db.collection("publicaciones_guardadas")
                                    .add(nuevaPublicacion)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(context, "Publicación guardada", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error al guardar la publicación", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(context, "La publicación no existe", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error al obtener la publicación", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


