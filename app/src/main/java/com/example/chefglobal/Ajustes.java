package com.example.chefglobal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Ajustes extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewPerfil;
    private TextView nombreUsuarioTextView;
    private TextView correoUsuarioTextView;
    private Button btnSeleccionarGaleria;
    private Spinner spinnerPais;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private String currentUserUid;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        imageViewPerfil = findViewById(R.id.ivFoto);
        nombreUsuarioTextView = findViewById(R.id.tvNombreUsuario);
        correoUsuarioTextView = findViewById(R.id.tvCorreoUsuario);
        btnSeleccionarGaleria = findViewById(R.id.btnSeleccionarGaleria);
        spinnerPais = findViewById(R.id.pais);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            currentUserUid = currentUser.getUid();
            String nombreUsuario = obtenerNombreDeUsuario(currentUser.getEmail());
            String correoUsuario = currentUser.getEmail();

            nombreUsuarioTextView.setText(nombreUsuario);
            correoUsuarioTextView.setText(correoUsuario);

            btnSeleccionarGaleria.setOnClickListener(v -> seleccionarImagenDeGaleria());

            storageReference = FirebaseStorage.getInstance().getReference()
                    .child("FotoPerfil")
                    .child(currentUserUid)
                    .child("perfil.jpg");

            cargarFotoDePerfil();
        }

        // Inicializar el Spinner con opciones de países
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lista_paises, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapter);

        // Agregar un listener para guardar el país seleccionado
        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String paisSeleccionado = parentView.getItemAtPosition(position).toString();

                // Aquí puedes guardar el país seleccionado en Firebase Firestore
                if (!paisSeleccionado.isEmpty()) {  // Asegúrate de que el país no esté vacío
                    guardarPaisSeleccionadoEnFirestore(paisSeleccionado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Maneja la falta de selección si es necesario.
                Toast.makeText(Ajustes.this, "Por favor, selecciona un país", Toast.LENGTH_SHORT).show();
            }

            // Método para guardar el país seleccionado en Firebase Firestore
            private void guardarPaisSeleccionadoEnFirestore(String pais) {
                if (currentUserUid != null) {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    DocumentReference userRef = firestore.collection("usuarios").document(currentUserUid);

                    // Crear un objeto que represente los datos a guardar
                    Map<String, Object> datosUsuario = new HashMap<>();
                    datosUsuario.put("pais", pais);

                    // Actualiza el documento del usuario con el país seleccionado
                    userRef.update(datosUsuario)
                            .addOnSuccessListener(aVoid -> {
                                // Éxito al guardar el país seleccionado
                                Toast.makeText(Ajustes.this, "País seleccionado: " + pais, Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Manejar errores en la actualización
                                Toast.makeText(Ajustes.this, "Error al guardar el país: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        // Agregar el código para abrir RecetasGuardadas y Notificaciones
        TextView recetasGuardadasTextView = findViewById(R.id.recetasGuardadas);
        recetasGuardadasTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre el fragmento de Recetas
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Recetas()) // Reemplaza RecetasFragment con el nombre correcto de tu fragmento de Recetas
                        .commit();
            }
        });

        TextView notificacionesTextView = findViewById(R.id.notificaciones2);
        notificacionesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la actividad de Notificaciones
                Intent intent = new Intent(Ajustes.this, Notificaciones.class);
                startActivity(intent);
            }
        });
    }


    private String obtenerNombreDeUsuario(String correoElectronico) {
        int indexArroba = correoElectronico.indexOf("@");
        if (indexArroba > 0) {
            return correoElectronico.substring(0, indexArroba);
        } else {
            return correoElectronico;
        }
    }

    public void seleccionarImagenDeGaleria() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                filePath = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                    imageViewPerfil.setImageBitmap(imageBitmap);

                    // Sube la imagen a Firebase Storage
                    subirImagenAFirebase(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void subirImagenAFirebase(Bitmap imageBitmap) {
        if (filePath != null) {
            // Construye la ruta de almacenamiento única para la imagen de perfil del usuario en Firebase Storage
            StorageReference fotoRef = FirebaseStorage.getInstance().getReference()
                    .child("FotoPerfil")
                    .child(currentUserUid)
                    .child("perfil.jpg");

            // Convierte el bitmap a un arreglo de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            // Sube la imagen a Firebase Storage
            UploadTask uploadTask = fotoRef.putBytes(data);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // La imagen se subió con éxito
                    // Obtén la URL de la imagen subida y guárdala en el Realtime Database
                    fotoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageURL = uri.toString();
                        guardarURLImagenEnBaseDeDatos(imageURL);
                        Toast.makeText(Ajustes.this, "Imagen subida y URL guardada con éxito", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Error al subir la imagen
                    Toast.makeText(Ajustes.this, "Error al subir la imagen. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void guardarURLImagenEnBaseDeDatos(String imageURL) {
        // Reemplaza "fotoDePerfil" con el nombre de tu nodo en Realtime Database
        DatabaseReference realtimeDatabaseReference = FirebaseDatabase.getInstance().getReference("fotoDePerfil");
        DatabaseReference userNode = realtimeDatabaseReference.child(currentUserUid);
        userNode.setValue(imageURL).addOnSuccessListener(aVoid -> {
            // La URL de la imagen de perfil se guardó con éxito en Realtime Database
            cargarFotoDePerfil(); // Actualiza la imagen en la interfaz
            Toast.makeText(Ajustes.this, "URL de imagen guardada en Realtime Database", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Error al guardar la URL de la imagen de perfil en Realtime Database
            Toast.makeText(Ajustes.this, "Error al guardar la URL de imagen en Realtime Database.", Toast.LENGTH_SHORT).show();
        });
    }

    private void cargarFotoDePerfil() {
        if (currentUserUid != null) {
            DocumentReference userRef = db.collection("usuarios").document(currentUserUid);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String imageURL = documentSnapshot.getString("fotoDePerfil");
                    if (imageURL != null) {
                        Glide.with(Ajustes.this)
                                .load(imageURL)
                                .into(imageViewPerfil);
                    }
                }
            });
        }
    }
}