package com.example.chefglobal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {
        EditText campo1 = this.findViewById(R.id.correo);
        String correo = campo1.getText().toString();
        EditText campo2 = this.findViewById(R.id.contrasenia);
        String contrasenia = campo2.getText().toString();

        if (correo.equals("felix@gmail.com") && contrasenia.equals("123")) {
            Intent i = new Intent(this, Inicio1.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Error con las Credenciales", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View v) {
        Intent i = new Intent(this, Registro.class);
        startActivity(i);
    }
}