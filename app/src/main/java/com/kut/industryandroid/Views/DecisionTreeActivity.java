package com.kut.industryandroid.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class DecisionTreeActivity extends AppCompatActivity implements View.OnClickListener {
    // Declare UI elements
    private TextView questionTextView;
    private TextView versionTextView;
    private TextView getHelpTexView;
    private LinearLayout answerButtonLayout;

    // Declare decision tree and Firebase database references
    private DecisionTree decisionTree;
    FirebaseDatabase database;
    DatabaseReference rootRef;
    String jsonCode;
    DatabaseReference jsonRef;
    // Keep track of the current question index
    private int currentQuestionIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision_tree);

        // Initialize UI elements
        questionTextView = findViewById(R.id.questionTextView);
        answerButtonLayout = findViewById(R.id.answerButtonLayout);
        versionTextView = findViewById(R.id.versionTextView);
        getHelpTexView = findViewById(R.id.getHelpText);

        // Get a reference to the Firebase database
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference("Users");
        jsonRef = database.getReference("JSONCode");

        // Load decision tree from Firebase
        loadDecisionTreeFromFirebase();
/*
        String json = loadDecisionTreeFromJsonFile();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            decisionTree = new DecisionTree(jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        // Display the version for the first time
        versionTextView.setText("Version=" + decisionTree.getVersion());
        // Display initial question
        displayQuestion();
 */
        // Set click listener for "Get Help" button
        getHelpTexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DecisionTreeActivity.this, GetHelpActivity.class);
                startActivity(intent);

            }
        });

    }

    // Load decision tree from JSON file in assets directory
    private String loadDecisionTreeFromJsonFile() {
        String json = null;
        try {
            // Read JSON file from assets directory
            InputStream inputStream = getAssets().open("example.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
    // Load decision tree from Firebase Realtime Database
    public void loadDecisionTreeFromFirebase() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading decision tree...");
        progressDialog.show();

        jsonRef.limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String json = snapshot.getValue(String.class);
                        JSONObject jsonObject = null;
                        try {
                            // Convert JSON string to JSONObject
                            jsonObject = new JSONObject(json);
                            // Create new decision tree object from JSONObject
                            decisionTree = new DecisionTree(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        // Display the version for the first time
                        versionTextView.setText("Version=" + decisionTree.getVersion());
                        // Display initial question
                        displayQuestion();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.e("hello", "Failed to load decision tree: " + databaseError.getMessage());
                Toast.makeText(DecisionTreeActivity.this, "Failed to load decision tree", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display current question and answer choices
    private void displayQuestion() {
        if (decisionTree != null) {
            try {
                if (decisionTree.isLeafNode()) {
                    // Display result if current node is a leaf node
                    String result = decisionTree.getResult();
                    questionTextView.setText(result);
                    answerButtonLayout.removeAllViews();
                    return;
                } else {
                    // Display current question and answer choices
                    questionTextView.setText(decisionTree.getQuestion());
                    answerButtonLayout.removeAllViews();
                    JSONArray answers = decisionTree.getAnswers();
                    if (answers != null) {
                        for (int i = 0; i < answers.length(); i++) {
                            // Create button for each answer choice
                            Button answerButton = new Button(this);
                            answerButton.setText(answers.getJSONObject(i).getString("answer"));
                            answerButton.setTag(i);
                            answerButton.setOnClickListener(this);
                            answerButtonLayout.addView(answerButton);
                        }
                    }

                    if (currentQuestionIndex > 0) {
                        // Create "Back" button if not on the first question
                        Button backButton = new Button(this);
                        backButton.setText("Back");
                        backButton.setTag(-1);
                        backButton.setOnClickListener(this);
                        answerButtonLayout.addView(backButton);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int answerIndex = (int) v.getTag();
        if (answerIndex == -1) {
            // "Back" button clicked
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--; // Decrement current question index
                decisionTree = decisionTree.getParent(); // Set parent node as current decision tree
                displayQuestion(); // Display previous question
            }
        } else {
            // Answer button clicked
            DecisionTree child = decisionTree.getChild(answerIndex); // Get child node based on selected answer
            if (child != null) {
                currentQuestionIndex++; // Increment current question index
                decisionTree = child; // Set child node as current decision tree
                displayQuestion(); // Display next question
            }
        }
    }

    private void updateToNewerVersion() {
        // Retrieve JSON data from Firebase
        jsonRef.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the JSON string from the snapshot
                String jsonFromFirebase = snapshot.child(snapshot.getChildren().iterator().next().getKey()).getValue(String.class);
                // Write the updated JSON data to the local file
                try {
                    OutputStream outputStream = getAssets().openFd("example.json").createOutputStream();
                    outputStream.write(jsonFromFirebase.getBytes());
                    outputStream.close();
                    Toast.makeText(DecisionTreeActivity.this, "JSON code updated from Firebase RTDB", Toast.LENGTH_SHORT).show();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                // Create alert dialog to warn about editing JSON file
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Edit JSON file")
                        .setMessage("Editing json file is dangerous! \nThis is only featuring in these versions!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(DecisionTreeActivity.this, JSONEditActivity.class));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do something when Cancel button is clicked
                            }
                        });
                // Create and show alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.action_update:
                // Update decision tree to newer version from Firebase Realtime Database
                updateToNewerVersion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}