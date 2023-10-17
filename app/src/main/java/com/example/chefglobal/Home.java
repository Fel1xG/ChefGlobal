package com.example.chefglobal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Home extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter<Publicacion, PublicacionViewHolder> adapter;

    // Este método estático se puede utilizar para crear una nueva instancia del fragmento
    public static Home newInstance() {
        return new Home();
    }

    // Constructor vacío requerido por Fragment
    public Home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        db = FirebaseFirestore.getInstance();

        Query query = db.collection("publicaciones");

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
                // Por ejemplo:
                holder.setTextoPublicacion(model.getTexto());
                holder.setImagenPublicacion(model.getImageUrl());
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





