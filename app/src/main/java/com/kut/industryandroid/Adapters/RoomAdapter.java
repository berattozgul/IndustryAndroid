package com.kut.industryandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kut.industryandroid.Models.Room;
import com.kut.industryandroid.R;

import java.util.ArrayList;

public class RoomAdapter extends ArrayAdapter<Room> {

    private Context mContext;
    private int mResource;

    public RoomAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        this.mResource = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        TextView username = convertView.findViewById(R.id.room_name);
        TextView messageItem = convertView.findViewById(R.id.room_last_update);
        username.setText(getItem(position).getRoomname());
        messageItem.setText(getItem(position).getLastMessage());
        return convertView;
    }
}
