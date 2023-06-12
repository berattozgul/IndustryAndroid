package com.kut.industryandroid.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kut.industryandroid.Models.User;
import com.kut.industryandroid.R;

public class GetHelpActivity extends AppCompatActivity {
    private Button continueAsGuestButton;
    private Button logInButton;
    private Button registerButton;
    private User currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);
        continueAsGuestButton = findViewById(R.id.button_guest);
        logInButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, redirect to ChatActivity
            rootRef.child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    currentUser = snapshot.getValue(User.class);
                                    if (currentUser.getUserType().equals("Admin")) {
                                        startActivity(new Intent(GetHelpActivity.this, RoomSelectActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
        }
    }

    public void continueGuest(View view) {
        String guestID = rootRef.push().getKey();
        rootRef = database.getReference("Users").child(guestID);
        User guestUser = new User(guestID, "guest_" + guestID, "guest");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rootRef.setValue(guestUser);
                Intent intent = new Intent(GetHelpActivity.this, ChatActivity.class);
                intent.putExtra("guestid", guestID);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

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