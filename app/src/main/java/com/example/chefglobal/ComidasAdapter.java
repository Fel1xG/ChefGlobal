package com.example.chefglobal;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.util.List;

public class ComidasAdapter extends RecyclerView.Adapter<ComidasAdapter.ComidaViewHolder> {
    private List<Comida> comidas;
    private Context context;

    public ComidasAdapter(List<Comida> comidas, Context context) {
        this.comidas = comidas;
        this.context = context;
    }

    @NonNull
    @Override
    public ComidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comida, parent, false);
        return new ComidaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidaViewHolder holder, int position) {
        Comida comida = comidas.get(position);
        holder.setNombre(comida.getNombre());
        holder.setIngredientes(comida.getIngredientes());
    }

    @Override
    public int getItemCount() {
        return comidas.size();
    }

    static class ComidaViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreView;
        private TextView ingredientesView;

        ComidaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreView = itemView.findViewById(R.id.nombreComida);
            ingredientesView = itemView.findViewById(R.id.ingredientesComida);
        }

        void setNombre(String nombre) {
            nombreView.setText(nombre);
        }

        void setIngredientes(String ingredientes) {
            ingredientesView.setText(ingredientes);
        }
    }
}





