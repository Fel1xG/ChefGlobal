package com.example.chefglobal;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


public class Notificaciones extends AppCompatActivity {
    private static final String TAG = "NotificacionesActivity";
    private RecyclerView recyclerView;
    private NotificacionesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        recyclerView = findViewById(R.id.rvNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificacionesAdapter(new ArrayList<>()); // Inicializa el adaptador con una lista vacía
        recyclerView.setAdapter(adapter);

        cargarNotificacionesDesdeFirebase();

        // Configura el botón para volver a la actividad Inicio1 (Home)
        Button btnVolverAInicio = findViewById(R.id.btnVolverAInicio);
        btnVolverAInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notificaciones.this, Inicio1.class);
                startActivity(intent);
            }
        });
    }

    private void cargarNotificacionesDesdeFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notificaciones")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Manejar errores, por ejemplo, mostrando un mensaje de error
                            // o registrando el error en la consola
                            Log.w(TAG, "Error al obtener las notificaciones.", e);
                            return;
                        }

                        List<Notifi> notificaciones = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Notifi notificacion = document.toObject(Notifi.class);
                            notificaciones.add(notificacion);
                        }
                        adapter.setNotificaciones(notificaciones);
                    }
                });
    }
}