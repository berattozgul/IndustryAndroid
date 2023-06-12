package com.kut.industryandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kut.industryandroid.Models.Message;
import com.kut.industryandroid.R;

import java.util.ArrayList;


public class MessageAdapter extends ArrayAdapter<Message> {
    private Context mContext;
    private int mResource;
    public MessageAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        this.mResource=resource;
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(mResource,parent,false);
        TextView username=convertView.findViewById(R.id.username_item);
        TextView messageItem=convertView.findViewById(R.id.message_item);
        username.setText(getItem(position).getFromWho()+":");
        messageItem.setText(getItem(position).getMessageText());

        return convertView;
    }
}
