package com.example.chefglobal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Home extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter<Publicacion, PublicacionViewHolder> adapter;

    public static Home newInstance() {
        return new Home();
    }

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
                return new PublicacionViewHolder(itemView, getContext());
            }

            @Override
            protected void onBindViewHolder(@NonNull PublicacionViewHolder holder, int position, @NonNull Publicacion model) {
                // Configura cómo se muestra cada elemento en el RecyclerView
                holder.setTextoPublicacion(model.getTexto());
                holder.setImagenPublicacion(model.getImageUrl());

                // Obtén el publicacionId de la publicación actual
                String publicacionId = this.getSnapshots().getSnapshot(position).getId();

                // Configura el OnClickListener para el botón de "Guardar"
                holder.btnGuardarPublicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Ahora puedes usar publicacionId para guardar la publicación
                        guardarPublicacion(publicacionId, getContext());
                    }
                });
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

    // Código de Home
    // Código de Home
    private void guardarPublicacion(String publicacionId, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtiene la referencia de la publicación
        DocumentReference publicacionDocRef = db.collection("publicaciones").document(publicacionId);

        publicacionDocRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                // La publicación existe, obtenemos sus datos
                                String imageUrl = documentSnapshot.getString("imageUrl");
                                String texto = documentSnapshot.getString("texto");

                                // Accede a la instancia de Firebase Firestore para la colección de recetas del usuario
                                CollectionReference recetasRef = db.collection("usuarios").document(userId).collection("recetasGuardadas");

                                // Crea una nueva publicación en la colección de recetas
                                Publicacion nuevaReceta = new Publicacion(userId, "", texto, imageUrl);

                                recetasRef.add(nuevaReceta)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(context, "Publicación guardada como receta", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                Toast.makeText(context, "Error al guardar la receta", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(context, "La publicación no existe", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Error al obtener la publicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


