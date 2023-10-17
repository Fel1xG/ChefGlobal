package com.example.chefglobal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Ajustes extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private StorageReference mStorage;
    private ImageView imfoto;
    private String currentUserUid;
    private FirebaseFirestore db;
    private TextView tvNombreUsuario;
    private TextView tvCorreoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Inicializa Firebase Storage
        mStorage = FirebaseStorage.getInstance().getReference();
        imfoto = findViewById(R.id.ivFoto);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreoUsuario = findViewById(R.id.tvCorreoUsuario);

        // Inicializa Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Obtener el usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Obtener el correo del usuario
            String email = currentUser.getEmail();
            tvCorreoUsuario.setText(email);

            // Obtener las primeras 7 letras del correo como nombre de usuario
            String username = email.substring(0, 7); // Puedes cambiar esto si deseas un nombre diferente
            tvNombreUsuario.setText(username);

            // Define la referencia a la foto de perfil en Firebase Storage
            String currentUserUid = currentUser.getUid();
            StorageReference fotoRef = mStorage.child("images/" + "perfil_" + currentUserUid + ".jpg");

            // Carga la foto de perfil en el ImageView
            Glide.with(this)
                    .load(fotoRef)
                    .into(imfoto);
        }

        // Carga la foto de perfil del usuario actual si está disponible
        cargarFotoDePerfil();
    }

    // Resto del código para tomar una foto, seleccionar imagen de la galería, subir imagen a Firebase Storage y guardar URL en Firestore sigue igual.

    // Carga la foto de perfil del usuario al iniciar sesión
    private void cargarFotoDePerfil() {
        if (currentUserUid != null) {
            DocumentReference userRef = db.collection("usuarios").document(currentUserUid);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String imageURL = documentSnapshot.getString("fotoDePerfil");
                        if (imageURL != null) {
                            // Carga la imagen desde la URL en el ImageView
                            Glide.with(Ajustes.this)
                                    .load(imageURL)
                                    .into(imfoto);
                        }
                    }
                }
            });
        }
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

    // Sube una foto de perfil a Firebase Storage
    private void subirImagenAFirebase(Bitmap imageBitmap) {
        // Define el tamaño deseado para la imagen (por ejemplo, 500x500 píxeles)
        int targetWidth = 500;
        int targetHeight = 500;

        // Redimensiona la imagen al tamaño deseado
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, targetWidth, targetHeight, true);

        // Genera un nombre de archivo único para la foto de perfil
        String fileName = "perfil_" + currentUserUid + ".jpg";
        StorageReference fotoRef = mStorage.child("images/" + fileName);

        // Convierte la imagen redimensionada a bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data1 = baos.toByteArray();

        // Sube la imagen redimensionada con el nombre de archivo único a Firebase Storage
        UploadTask uploadTask = fotoRef.putBytes(data1);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // La imagen se ha subido con éxito.
                    // Ahora puedes guardar la URL de la imagen en la base de datos (Firestore).
                    guardarURLImagenEnBaseDeDatos(fotoRef);
                } else {
                    // Se produjo un error al subir la imagen.
                    Toast.makeText(Ajustes.this, "Error al subir la imagen. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Guarda la URL de la imagen de perfil en Firestore
    private void guardarURLImagenEnBaseDeDatos(StorageReference fotoRef) {
        fotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // URL de la imagen
                final String imageURL = uri.toString();

                // Guarda la URL en Firestore
                DocumentReference userRef = db.collection("usuarios").document(currentUserUid);
                userRef.update("fotoDePerfil", imageURL)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Ajustes.this, "Imagen guardada con éxito en la base de datos.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            public void onFailure(Exception e) {
                                Toast.makeText(Ajustes.this, "Error al guardar la imagen en la base de datos.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}