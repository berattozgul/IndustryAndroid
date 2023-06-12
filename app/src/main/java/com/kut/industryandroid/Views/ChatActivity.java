package com.kut.industryandroid.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kut.industryandroid.Adapters.MessageAdapter;
import com.kut.industryandroid.Models.Message;
import com.kut.industryandroid.Models.User;
import com.kut.industryandroid.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MessageAdapter adapter;
    private ArrayList<Message> listOfMessages;
    private FirebaseDatabase firebaseDatabase;
    private ImageButton sendButton;
    private ListView messagesList;
    private EditText sendInput;
    private FirebaseUser firebaseUser;
    private DatabaseReference usersRef;
    private DatabaseReference messageRef;
    private String guestID;
    private User currentUser;
    private User toUser;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        listOfMessages = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        usersRef = firebaseDatabase.getReference("Users");
        messageRef = firebaseDatabase.getReference("Rooms");
        sendButton = findViewById(R.id.send_button);
        messagesList = findViewById(R.id.message_list);
        sendInput = findViewById(R.id.message_input);
        listOfMessages = new ArrayList<Message>();
        adapter = new MessageAdapter(ChatActivity.this, R.layout.message_items, listOfMessages);
        roomName = getIntent().getStringExtra("roomName");

        if (firebaseUser != null) {
            getUserData();
            if (roomName == null) {
                roomName="TdZ7sOWH62YE3sjOz57Ktg9hK4G2 <-> " + firebaseUser.getUid();
            }
        } else {
            guestID = getIntent().getStringExtra("guestid");
            if (roomName == null) {
                roomName="TdZ7sOWH62YE3sjOz57Ktg9hK4G2 <-> " + guestID;
            }
        }
        getMessages();
    }

    private void getMessages() {
        if (firebaseUser != null) {
            messageRef.child(roomName)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                listOfMessages.clear();
                                for (DataSnapshot sn : snapshot.getChildren()) {
                                    String fromWho = sn.child("fromWho").getValue(String.class);
                                    String messageText = sn.child("messageText").getValue(String.class);
                                    String messageID = sn.child("messageID").getValue(String.class);
                                    String toWho = sn.child("toWho").getValue(String.class);
                                    String date = sn.child("date").getValue(String.class);
                                    listOfMessages.add(new Message(messageID, fromWho, toWho, messageText, date));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        } else {
            messageRef.child(roomName)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                listOfMessages.clear();
                                for (DataSnapshot sn : snapshot.getChildren()) {
                                    String fromWho = sn.child("fromWho").getValue(String.class);
                                    String messageText = sn.child("messageText").getValue(String.class);
                                    String messageID = sn.child("messageID").getValue(String.class);
                                    String toWho = sn.child("toWho").getValue(String.class);
                                    String date = sn.child("date").getValue(String.class);
                                    listOfMessages.add(new Message(messageID, fromWho, toWho, messageText, date));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        messagesList.setAdapter(adapter);
    }

    private void getUserData() {
        usersRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    currentUser = snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendMessage(View view) {
        String toSent = sendInput.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String messageID = messageRef.push().getKey();
        if (firebaseUser != null) {
            Message message = new Message(messageID, currentUser.getUsername(), "Admin", toSent, dateFormat.format(date));
            messageRef.child(roomName).
                    child(messageID).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChatActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                                sendInput.getText().clear();
                            } else {
                                Toast.makeText(ChatActivity.this, "Message cannot sent! Error+" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Message message = new Message(messageID, "guest_" + guestID, "Admin", toSent, dateFormat.format(date));
            messageRef.child(roomName).
                    child(messageID).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChatActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                                sendInput.getText().clear();
                            } else {
                                Toast.makeText(ChatActivity.this, "Message cannot sent! Error+" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mAuth.getCurrentUser() != null) {
            getMenuInflater().inflate(R.menu.chat_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, redirect to ChatActivity
            switch (item.getItemId()) {
                case R.id.action_add:
                    return true;
                case R.id.action_sign_out:
                    mAuth.signOut();
                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.action_settings:
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return false;
    }
}
