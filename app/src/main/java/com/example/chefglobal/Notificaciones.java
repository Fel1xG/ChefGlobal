package com.example.chefglobal;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class Notificaciones extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificacionesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        recyclerView = findViewById(R.id.rvNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificacionesAdapter();
        recyclerView.setAdapter(adapter);

        cargarNotificacionesDesdeFirebase();
    }

    private void cargarNotificacionesDesdeFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notificaciones")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Notifi> notificaciones = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Notifi notificacion = document.toObject(Notifi.class);
                            notificaciones.add(notificacion);
                        }
                        adapter.setNotificaciones(notificaciones);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Maneja los errores al cargar notificaciones desde Firebase
                    }
                });
    }
}
