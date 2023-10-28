package com.example.chefglobal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<ChatMessage> chatMessages;

    public ChatAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_chat_message, null);
        }

        ChatMessage chatMessage = chatMessages.get(position);

        TextView senderTextView = view.findViewById(R.id.senderTextView);
        TextView messageTextView = view.findViewById(R.id.messageTextView);
        TextView timeTextView = view.findViewById(R.id.timeTextView);

        senderTextView.setText(chatMessage.getUserName());
        messageTextView.setText(chatMessage.getMessage());
        timeTextView.setText(chatMessage.getTime());

        return view;
    }
}

