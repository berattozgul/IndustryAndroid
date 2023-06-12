package com.kut.industryandroid.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kut.industryandroid.Models.User;
import com.kut.industryandroid.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText mUsernameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Spinner mUserTypeSpinner;
    private Button mRegisterButton;
    private TextView mLoginTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Authentication and Realtime Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Get references to UI elements
        mUsernameField = findViewById(R.id.register_username);
        mEmailField = findViewById(R.id.register_email);
        mPasswordField = findViewById(R.id.register_password);
        mUserTypeSpinner = findViewById(R.id.register_usertype_spinner);

        // Populate the spinner with user types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserTypeSpinner.setAdapter(adapter);

        // Get reference to Register button and add onClickListener
        mRegisterButton = findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Get reference to Login TextView and add onClickListener
        mLoginTextView = findViewById(R.id.login_textview);
        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        final String username = mUsernameField.getText().toString().trim();
        final String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        final String userType = mUserTypeSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(username)) {
            mUsernameField.setError("Please enter a username");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Please enter an email address");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Please enter a password");
            return;
        }

        // Create a new user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User is successfully registered
                            User user = new User(mAuth.getCurrentUser().getUid(), username, userType);
                            mDatabase.child(mAuth.getCurrentUser().getUid()).setValue(user);
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // Registration failed
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}