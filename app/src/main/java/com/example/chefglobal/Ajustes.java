package com.example.chefglobal;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;


public class Ajustes extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private StorageReference mStorage;
    private ImageView imfoto;
    private String currentUserUid;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        mStorage = FirebaseStorage.getInstance().getReference();
        imfoto = findViewById(R.id.ivFoto);

        // Inicializa Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializa Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserUid = currentUser.getUid();
        }

        // Carga la foto de perfil del usuario actual si está disponible
        cargarFotoDePerfil();
    }

    public void tomar_foto(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void seleccionarImagenDeGaleria(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private static final int GALLERY_REQUEST_CODE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imfoto.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 500, 500, false));
            subirImagenAFirebase(imageBitmap);
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    imfoto.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 500, 500, false));
                    subirImagenAFirebase(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void subirImagenAFirebase(Bitmap imageBitmap) {
        StorageReference fotoRef = mStorage.child("images/foto.jpg");
        // Generar un arreglo de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Convierte el bitmap al formato y calidad que deseas
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data1 = baos.toByteArray();

        // Subir a Firebase Storage
        UploadTask uploadTask = fotoRef.putBytes(data1);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Ajustes.this, "Imagen subida con éxito.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Ajustes.this, "Error al subir la imagen.", Toast.LENGTH_SHORT).show();
                    Exception e = task.getException(); // Obtén información sobre el error
                    if (e != null) {
                        Log.e("Ajustes", "Error al subir imagen: " + e.getMessage());
                    }
                }
            }
        });

        // Guardar la URL de la imagen en la base de datos
        guardarURLImagenEnBaseDeDatos(fotoRef);
    }

    private void guardarURLImagenEnBaseDeDatos(StorageReference fotoRef) {
        fotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // URL de la imagen
                final String imageURL = uri.toString();

                // Guardar la URL en Firestore o Realtime Database (aquí utilizo Firestore)
                DocumentReference userRef = db.collection("usuarios").document(currentUserUid);
                userRef.update("fotoDePerfil", imageURL)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Ajustes.this, "Imagen guardada con éxito en la base de datos.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Ajustes.this, "Error al guardar la imagen en la base de datos.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
    // Método para cargar la foto de perfil del usuario actual si está disponible en la base de datos
    private void cargarFotoDePerfil() {
        if (currentUserUid != null) {
            DocumentReference userRef = db.collection("usuarios").document(currentUserUid);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String imageURL = documentSnapshot.getString("fotoDePerfil");
                        if (imageURL != null) {
                            // Cargar la imagen desde la URL en el ImageView
                            Glide.with(Ajustes.this)
                                    .load(imageURL)
                                    .into(imfoto);
                        }
                    }
                }
            });
        }
    }
}

