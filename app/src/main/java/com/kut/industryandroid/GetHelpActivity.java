package com.kut.industryandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetHelpActivity extends AppCompatActivity {
    Button continueAsGuestButton;
    Button logInButton;
    Button registerButton;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = database.getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);
        continueAsGuestButton = findViewById(R.id.button_guest);
        logInButton = findViewById(R.id.button_login);
        registerButton=findViewById(R.id.button_register);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, redirect to ChatActivity
            startActivity(new Intent(GetHelpActivity.this, ChatActivity.class));
            finish();
        }
    }

    public void continueGuest(View view) {
        String guestID = rootRef.push().getKey();
        rootRef = database.getReference("Users").child("Guest").child("Guest_" + guestID);
        User guestUser = new User(guestID, "guest_" + guestID, "guest");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rootRef.setValue(guestUser);
                Intent intent = new Intent(GetHelpActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logIn(View view) {
        Intent intent = new Intent(GetHelpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(GetHelpActivity.this, RegisterActivity.class);
        startActivity(intent);

    }

}