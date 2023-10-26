package com.example.chefglobal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class Agregar extends Fragment {

    private static final int GALLERY_REQUEST = 1889;

    private Button seleccionarFotoButton;
    private ImageView imagenPreview;
    private EditText textoPublicacion;
    private Button publicarButton;

    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar, container, false);

        seleccionarFotoButton = view.findViewById(R.id.seleccionar_foto_button);
        imagenPreview = view.findViewById(R.id.imagen_preview);
        textoPublicacion = view.findViewById(R.id.texto_publicacion);
        publicarButton = view.findViewById(R.id.publicar_button);

        seleccionarFotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la galería para seleccionar una foto
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        publicarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtén el texto de la publicación y la imagen (si se seleccionó una)
                String texto = textoPublicacion.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String nombreUsuario = user.getDisplayName();

                    // Sube la imagen al Firebase Storage
                    if (imageUri != null) {
                        subirImagenAFirebaseStorage(texto);
                        guardarNotificacion(texto); // Agrega la notificación
                    } else {
                        // No hay imagen seleccionada, muestra un mensaje de error
                        Toast.makeText(getActivity(), "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // El usuario no ha iniciado sesión; debes manejar este caso según tus requerimientos
                    Toast.makeText(getActivity(), "Debes iniciar sesión para publicar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // Recibe la imagen seleccionada desde la galería
                imageUri = data.getData();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(),
                            imageUri
                    );
                    imagenPreview.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void subirImagenAFirebaseStorage(final String texto) {
        final String nombreImagen = UUID.randomUUID().toString(); // Genera un nombre único para la imagen

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("publicaciones/" + nombreImagen);

        storageRef.putFile(imageUri) // Utiliza putFile en lugar de putBytes para manejar cualquier formato de imagen
                .addOnSuccessListener(taskSnapshot -> {
                    // Imagen subida exitosamente
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // URL de la imagen
                        String imageUrl = uri.toString();
                        guardarPublicacion(texto, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Maneja errores si la subida de la imagen falla
                    Log.e("MiApp", "Error al subir la imagen: " + e.getMessage());
                    Toast.makeText(getActivity(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                });
    }

    private void guardarPublicacion(String texto, String imageUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String userName = user.getDisplayName(); // Obtén el nombre del usuario

            // Crear una instancia de Firebase Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Crear un documento para la nueva publicación
            DocumentReference nuevaPublicacion = db.collection("publicaciones").document();

            // Crear un objeto Publicacion con los datos
            Publicacion publicacion = new Publicacion(userId, userName, texto, imageUrl);

            // Guardar la publicación en Firestore
            nuevaPublicacion.set(publicacion);

            Toast.makeText(getActivity(), "Publicación guardada exitosamente", Toast.LENGTH_SHORT).show();

            // Limpia los campos después de la publicación
            textoPublicacion.setText("");
            imagenPreview.setImageResource(0);
        }
    }

    private void guardarNotificacion(String mensaje) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String nombreUsuario = user.getDisplayName();
            Date fechaActual = new Date(); // Obtén la fecha actual

            // Crear una instancia de Firebase Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Crear una instancia de Notifi con el nombre de usuario y la fecha actual
            Notifi notificacion = new Notifi(nombreUsuario, fechaActual);

            // Agregar la notificación a la colección "
            // notificaciones" en Firebase Firestore
            db.collection("notificaciones")
                    .add(notificacion)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // La notificación se ha guardado exitosamente en Firestore
                            Log.d("MiApp", "Notificación guardada en Firestore con ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Maneja los errores si la operación de guardado falla
                            Log.e("MiApp", "Error al guardar notificación en Firestore: " + e.getMessage());
                        }
                    });
        }
    }
}