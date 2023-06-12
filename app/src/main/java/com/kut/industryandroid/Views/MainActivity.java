package com.kut.industryandroid.Views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kut.industryandroid.Models.DecisionTree;
import com.kut.industryandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    Button docButton, decisionTreeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        docButton = findViewById(R.id.docButton);
        decisionTreeButton = findViewById(R.id.decisionTreeButton);
        docButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDocActivity();
            }
        });

        decisionTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDecisionTreeActivity();
            }
        });
    }

    private void openDocActivity() {
        Intent intent = new Intent(this, DocActivity.class);
        startActivity(intent);
    }

    private void openDecisionTreeActivity() {
        Intent intent = new Intent(this, DecisionTreeActivity.class);
        startActivity(intent);
    }
}

