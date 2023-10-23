package com.example.chefglobal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.chefglobal.Publicacion; // Asegúrate de importar la clase Publicacion adecuada

public class Recetas extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter<Publicacion, PublicacionViewHolder> adapter;

    public Recetas() {
        // Constructor vacío requerido para Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recetas, container, false);

        recyclerView = view.findViewById(R.id.recyclerMisRecetas);
        db = FirebaseFirestore.getInstance();

        // Obtén el ID del usuario actual
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Construye una consulta para recuperar las recetas guardadas del usuario
        Query query = db.collection("usuarios").document(userId).collection("recetasGuardadas");

        FirestoreRecyclerOptions<Publicacion> options = new FirestoreRecyclerOptions.Builder<Publicacion>()
                .setQuery(query, Publicacion.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Publicacion, PublicacionViewHolder>(options) {
            @NonNull
            @Override
            public PublicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publicacion, parent, false);
                return new PublicacionViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull PublicacionViewHolder holder, int position, @NonNull Publicacion model) {
                // Configura cómo se muestra cada elemento en el RecyclerView
                holder.setTextoPublicacion(model.getTexto());
                holder.setImagenPublicacion(model.getImageUrl());
                holder.setNombreUsuario(model.getUserName());
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
