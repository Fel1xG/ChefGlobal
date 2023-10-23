package com.example.chefglobal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PreguntasFrecuentes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_frecuentes);

        // Configurar un botón para retroceder a la actividad Ayuda
        Button backButton = findViewById(R.id.btnBackToAyuda);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra esta actividad para volver a la anterior (Ayuda)
            }
        });

        // Puedes personalizar el diseño y la lógica de esta actividad según tus necesidades.
    }
}
