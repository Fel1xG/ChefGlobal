package com.example.chefglobal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Ayuda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        Button btnPreguntasFrecuentes = findViewById(R.id.btnPreguntasFrecuentes);
        Button btnTerminosCondiciones = findViewById(R.id.btnTerminosCondiciones);
        Button btnContactarSoporte = findViewById(R.id.btnContactarSoporte);

        // Agregar un OnClickListener al botón "Preguntas Frecuentes"
        btnPreguntasFrecuentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad PreguntasFrecuentesActivity
                Intent intent = new Intent(Ayuda.this, PreguntasFrecuentes.class);
                startActivity(intent);
            }
        });

        // Agregar un OnClickListener al botón "Términos y Condiciones"
        btnTerminosCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad TerminosCondicionesActivity
                Intent intent = new Intent(Ayuda.this, TerminosCondiciones.class);
                startActivity(intent);
            }
        });

        // Agregar un OnClickListener al botón "Contactar Soporte"
        btnContactarSoporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la aplicación de correo electrónico
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:soporte@chefglobal.com")); // Reemplaza con la dirección de correo de soporte
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta de soporte");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Por favor, describa su consulta o problema aquí.");

                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                } else {
                    // Si no hay aplicaciones de correo electrónico disponibles, puedes mostrar un mensaje al usuario
                    Toast.makeText(Ayuda.this, "No hay aplicaciones de correo electrónico disponibles.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}