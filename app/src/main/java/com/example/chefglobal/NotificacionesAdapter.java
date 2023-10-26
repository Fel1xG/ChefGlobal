package com.example.chefglobal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {

    private List<Notifi> notificaciones;
    private String nombreUsuario;

    public NotificacionesAdapter(List<Notifi> notificaciones) {
        this.notificaciones = notificaciones;
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        Notifi notificacion = notificaciones.get(position);
        holder.nombreUsuario.setText(nombreUsuario); // Usar el nombre de usuario
        holder.mensaje.setText("Ha publicado una receta nueva");
        holder.fecha.setText(formatDate(notificacion.getFecha()));
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public void setNotificaciones(List<Notifi> notificaciones, String nombreUsuario) {
        this.notificaciones = notificaciones;
        this.nombreUsuario = nombreUsuario;
        notifyDataSetChanged();
    }

    public class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView nombreUsuario;
        TextView mensaje;
        TextView fecha;

        public NotificacionViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            mensaje = itemView.findViewById(R.id.textMensaje);
            fecha = itemView.findViewById(R.id.textFecha);
        }
    }

    private String formatDate(Date date) {
        // Implementa el formato de fecha deseado
        return date.toString();
    }
}