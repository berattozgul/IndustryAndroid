package com.kut.industryandroid.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kut.industryandroid.Adapters.RoomAdapter;
import com.kut.industryandroid.Models.Room;
import com.kut.industryandroid.Models.User;
import com.kut.industryandroid.R;

import java.util.ArrayList;

public class RoomSelectActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    ListView listView;
    RoomAdapter roomAdapter;
    ArrayList<Room> rooms;
    User currentUser;

    DatabaseReference roomReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_select);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");
        roomReference = firebaseDatabase.getReference("Rooms");
        firebaseUser = firebaseAuth.getCurrentUser();
        rooms = new ArrayList<>();
        roomAdapter = new RoomAdapter(RoomSelectActivity.this, R.layout.room_items, rooms);
        listView = findViewById(R.id.room_items_lv);
        System.out.println(firebaseUser.getUid());
        checkUser();
        // Set up the item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected room name
                String selectedRoomName = ((Room) parent.getItemAtPosition(position)).getRoomname();

                // Start a new activity and pass the selected room name as an extra
                Intent intent = new Intent(RoomSelectActivity.this, ChatActivity.class);
                intent.putExtra("roomName", selectedRoomName);
                startActivity(intent);
            }
        });
        listView.setAdapter(roomAdapter);
    }

    private void checkUser() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading user data...");
        progressDialog.show();

        if (firebaseUser != null) {
            userReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUser = snapshot.getValue(User.class);
                    progressDialog.dismiss();
                    if (currentUser != null && currentUser.getUserType() != null) {
                        if (currentUser.getUserType().equals("Admin")) {
                            getRooms();
                        } else {
                            // User is not an admin, do something else
                        }
                    } else {
                        // currentUser or currentUser.getUserType() is null, handle error
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    // Handle error
                }
            });
        } else {
            progressDialog.dismiss();
            // Handle error: firebaseUser is null
        }
    }

    private void getRooms() {
        roomReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rooms.clear();
                if (snapshot.exists()) {
                    // Iterate over the children of the snapshot to extract the room names
                    for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                        String roomName = roomSnapshot.getKey();
                        System.out.println(roomName);
                        rooms.add(new Room(roomName, "Today"));
                        roomAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
        listView.setAdapter(roomAdapter);
    }
}