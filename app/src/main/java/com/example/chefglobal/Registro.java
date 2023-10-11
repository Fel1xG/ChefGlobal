package com.example.chefglobal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText correo2;
    private EditText contrasenia2;
    private EditText contrasenia3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();

        correo2 = findViewById(R.id.correo2);
        contrasenia2 = findViewById(R.id.contrasenia2);
        contrasenia3 = findViewById(R.id.contrasenia3);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser)
    }

    public void registrarUsuario(View view){

        if (contrasenia2.getText().toString().equals(contrasenia3.getText().toString())){

            mAuth.createUserWithEmailAndPassword(correo2.getText().toString(), contrasenia2.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(), "Usuario Creado", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(getApplicationContext(), Confirmacion1.class);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
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