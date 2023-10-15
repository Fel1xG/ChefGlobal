package com.example.chefglobal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private EditText correo2;
    private EditText contrasenia2;
    private EditText contrasenia3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        correo2 = findViewById(R.id.correo2);
        contrasenia2 = findViewById(R.id.contrasenia2);
        contrasenia3 = findViewById(R.id.contrasenia3);
    }

    public void onStart() {
        super.onStart();
    }

    public void registrarUsuario(View view) {
        final String email = correo2.getText().toString();
        final String password = contrasenia2.getText().toString();
        final String idPersonalizado = obtenerIdPersonalizado(email);

        if (password.equals(contrasenia3.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                // Guardar el correo y el ID personalizado en la base de datos Realtime Database
                                guardarRegistroEnRealtimeDatabase(user.getUid(), email, idPersonalizado);

                                Toast.makeText(getApplicationContext(), "Usuario Creado", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Confirmacion1.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarRegistroEnRealtimeDatabase(String userId, String correo, String idPersonalizado) {
        DatabaseReference userRef = databaseReference.child("Registros").child(userId);
        userRef.child("correo").setValue(correo);
        userRef.child("id_personalizado").setValue(idPersonalizado);
    }

    private String obtenerIdPersonalizado(String correo) {
        // Aquí puedes implementar la lógica para obtener un ID personalizado, por ejemplo, tomar las primeras cinco letras del correo.
        if (correo.length() >= 7) {
            return correo.substring(0, 7);
        } else {
            return correo;
        }
    }



    public void register2(View v){
        EditText campo1 = this.findViewById(R.id.correo2);
        String correo2 = campo1.getText().toString();
        EditText campo2 = this.findViewById(R.id.contrasenia2);
        String contrasenia2 = campo2.getText().toString();
        EditText campo3 = this.findViewById(R.id.contrasenia3);
        String contrasenia3 = campo3.getText().toString();

        if (isValidRegistration(correo2, contrasenia2, contrasenia3)) {
            Intent i = new Intent(this,Confirmacion1.class);
            startActivity(i);
        }
    }

    private boolean isValidRegistration(String correo2, String contrasenia2, String contrasenia3) {
        if (correo2.equals("felix@gmail.com") && contrasenia2.equals("123") && contrasenia3.equals("123")) {
            return true;
        }
        return false;

    }
}