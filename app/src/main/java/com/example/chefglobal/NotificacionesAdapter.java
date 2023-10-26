package com.example.chefglobal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {
    private List<Notifi> notificaciones;

    public NotificacionesAdapter(List<Notifi> notificaciones) {
        this.notificaciones = notificaciones;
    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificacionViewHolder holder, int position) {
        Notifi notificacion = notificaciones.get(position);
        holder.nombreUsuario.setText(notificacion.getNombreUsuario());
        holder.mensaje.setText(notificacion.getMensaje());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fechaFormateada = dateFormat.format(notificacion.getFecha());
        holder.fecha.setText(fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreUsuario;
        public TextView mensaje;
        public TextView fecha;

        public NotificacionViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.textNombreUsuario);
            mensaje = itemView.findViewById(R.id.textMensaje);
            fecha = itemView.findViewById(R.id.textFecha);
        }
    }
}
