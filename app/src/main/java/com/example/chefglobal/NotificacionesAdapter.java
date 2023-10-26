package com.example.chefglobal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chefglobal.Notifi;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;


public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {
    private List<Notifi> notificaciones;

    public NotificacionesAdapter(List<Notifi> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public class NotificacionViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreUsuario;
        private TextView mensaje;
        private TextView fecha;

        public NotificacionViewHolder(View view) {
            super(view);
            nombreUsuario = view.findViewById(R.id.tvNombreUsuario);
            mensaje = view.findViewById(R.id.textMensaje);
            fecha = view.findViewById(R.id.textFecha);
        }
    }

    public void setNotificaciones(List<Notifi> notificaciones) {
        this.notificaciones = notificaciones;
        notifyDataSetChanged();
    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View notificacionView = inflater.inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(notificacionView);
    }

    @Override
    public void onBindViewHolder(NotificacionViewHolder holder, int position) {
        Notifi notificacion = notificaciones.get(position);
        holder.nombreUsuario.setText(notificacion.getNombreUsuario());
        holder.mensaje.setText(notificacion.getMensaje());
        holder.fecha.setText(formatDate(notificacion.getFecha()));
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    private String formatDate(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(fecha);
    }
}
