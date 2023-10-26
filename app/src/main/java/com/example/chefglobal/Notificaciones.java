package com.example.chefglobal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notificaciones extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView rvNotificaciones;
    private NotificacionesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        rvNotificaciones = findViewById(R.id.rvNotificaciones);
        rvNotificaciones.setLayoutManager(new LinearLayoutManager(this));
        List<Notifi> notificacionesList = new ArrayList<>();
        adapter = new NotificacionesAdapter(notificacionesList);

        // Recuperar el nombre de usuario enviado desde Inicio1
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");

        rvNotificaciones.setAdapter(adapter);
        Button btnVolverAInicio = findViewById(R.id.btnVolverAInicio);
        btnVolverAInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Cargar notificaciones de Firebase Firestore
        db = FirebaseFirestore.getInstance();
        db.collection("notificaciones")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Manejar errores
                            return;
                        }

                        List<Notifi> notificaciones = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String nombreUsuario = document.getString("nombreUsuario");
                            String mensaje = document.getString("mensaje");
                            Date fecha = document.getDate("fecha");

                            Notifi notificacion = new Notifi(nombreUsuario, fecha, mensaje);
                            notificaciones.add(notificacion);
                        }
                        adapter.setNotificaciones(notificaciones, nombreUsuario); // Pasa el nombre de usuario
                    }
                });
    }
}
