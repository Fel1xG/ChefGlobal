package com.example.chefglobal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private static final int STORAGE_PERMISSION_CODE = 1;

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
                // Verifica los permisos de almacenamiento (nuevo)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        return;
                    }
                }

                // Obtén el texto de la publicación y la imagen (si se seleccionó una)
                String texto = textoPublicacion.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Sube la imagen al Firebase Storage
                    if (imageUri != null) {
                        subirImagenAFirebaseStorage(texto);
                    } else {
                        // No hay imagen seleccionada, muestra un mensaje de error
                        Toast.makeText(requireActivity(), "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // El usuario no ha iniciado sesión; debes manejar este caso según tus requerimientos
                    Toast.makeText(requireActivity(), "Debes iniciar sesión para publicar", Toast.LENGTH_SHORT).show();
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
                            requireActivity().getContentResolver(),
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
                    Toast.makeText(requireActivity(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                });
    }

    private void guardarPublicacion(String texto, String imageUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String userName = user.getDisplayName();


            FirebaseFirestore db = FirebaseFirestore.getInstance();


            DocumentReference nuevaPublicacion = db.collection("publicaciones").document();



            Publicacion publicacion = new Publicacion(userId, userName, texto, imageUrl);


            nuevaPublicacion.set(publicacion)
                    .addOnSuccessListener(aVoid -> {

                        crearNotificacion(userName, texto);

                        Toast.makeText(requireActivity(), "Publicación guardada exitosamente", Toast.LENGTH_SHORT).show();

                        // Limpia los campos después de la publicación
                        textoPublicacion.setText("");
                        imagenPreview.setImageResource(0);
                    })
                    .addOnFailureListener(e -> {
                        // Maneja errores si la subida de la imagen falla
                        Log.e("MiApp", "Error al subir la imagen: " + e.getMessage());
                        Toast.makeText(requireActivity(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Método para crear una notificación
    private void crearNotificacion(String userName, String mensaje) {
        // Obtiene la hora actual
        Date fechaActual = new Date();

        // Crea una instancia de Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crea un documento para la nueva notificación en la colección "notificaciones"
        Notifi notificacion = new Notifi(userName, fechaActual, mensaje);
        db.collection("notificaciones")
                .add(notificacion) // Agrega la notificación a Firestore
                .addOnSuccessListener(documentReference -> {
                    // Notificación creada exitosamente
                    Log.d("MiApp", "Notificación creada exitosamente");
                })
                .addOnFailureListener(e -> {
                    // Maneja errores si la creación de la notificación falla
                    Log.e("MiApp", "Error al crear la notificación: " + e.getMessage());
                });
    }
}