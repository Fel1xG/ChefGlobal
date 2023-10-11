package com.example.chefglobal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText correo;
    private EditText contrasenia;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = findViewById(R.id.correo);
        contrasenia = findViewById(R.id.contrasenia);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // El usuario ya está autenticado, redirige a la actividad Inicio1
            irAInicio1();
        }
    }

    public void iniciarSesion(View view) {
        String email = correo.getText().toString();
        String password = contrasenia.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Verificar si el CheckBox "Recuérdame" está marcado
                            CheckBox cbRecuerdame = findViewById(R.id.cbRecuerdame);
                            boolean chequeado = cbRecuerdame.isChecked();

                            if (chequeado) {
                                // Guardar la preferencia de recordar la sesión
                                recordarSesion();
                            }

                            // Iniciar la actividad Inicio1
                            irAInicio1();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void recordarSesion() {
        // Nombre de la preferencia personalizada
        String preferenciaNombre = "MisPreferencias";

        // Obtener el objeto SharedPreferences personalizado
        SharedPreferences misPreferencias = getSharedPreferences(preferenciaNombre, MODE_PRIVATE);

        // Editar el objeto SharedPreferences
        SharedPreferences.Editor editor = misPreferencias.edit();
        editor.putBoolean("recordarSesion", true);

        // Aplicar los cambios
        editor.apply();
    }


    private void irAInicio1() {
        // Iniciar la actividad Inicio1
        Intent i = new Intent(getApplicationContext(), Inicio1.class);
        startActivity(i);
        // No es necesario llamar a finish() aquí
    }

    public void register(View v) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Nombre de la preferencia personalizada (debe coincidir con el que usaste en onCreate)
        String preferenciaNombre = "MisPreferencias";

        // Obtener el objeto SharedPreferences personalizado
        SharedPreferences misPreferencias = getSharedPreferences(preferenciaNombre, MODE_PRIVATE);

        // Recuperar el valor de "correo" desde las preferencias compartidas
        String correoGuardado = misPreferencias.getString("correo", "");

        if (!correoGuardado.equals("")) {
            // Si se ha guardado un correo en las preferencias, redirige a la actividad Inicio1
            irAInicio1();
        }
    }
}
