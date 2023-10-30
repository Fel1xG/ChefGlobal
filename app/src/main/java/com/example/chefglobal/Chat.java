package com.example.chefglobal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat extends Fragment {
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ListView chatListView = view.findViewById(R.id.chatListView);
        EditText messageEditText = view.findViewById(R.id.messageEditText);
        Button sendButton = view.findViewById(R.id.sendButton);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatMessages);
        chatListView.setAdapter(chatAdapter);

        // Obtener el nombre de usuario del usuario actualmente autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userName = currentUser.getDisplayName();
            // Puedes establecer el nombre de usuario predeterminado si no se ha configurado
            if (userName == null || userName.isEmpty()) {
                userName = "Usuario Anónimo";
            }

            // Inicializar la referencia a la base de datos de Firebase
            databaseReference = FirebaseDatabase.getInstance().getReference("messages");

            // Actualizar el nombre de usuario en el nuevo mensaje
            final String finalUserName = userName;  // Variable final para usar dentro del listener
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = messageEditText.getText().toString();
                    ChatMessage newMessage = new ChatMessage();
                    newMessage.setUserName(finalUserName);
                    newMessage.setMessage(message);
                    newMessage.setTime(getCurrentDateTimeFormatted());

                    // Guardar el mensaje en Firebase Realtime Database
                    databaseReference.push().setValue(newMessage);

                    messageEditText.setText("");
                }
            });

            // Escuchar cambios en la base de datos y actualizar la interfaz de usuario
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    chatMessages.add(message);
                    chatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        return view;
    }

    private String getCurrentDateTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(new Date());
    }
}

