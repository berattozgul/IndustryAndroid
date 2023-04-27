package com.kut.industryandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextView;
    private TextView versionTextView;
    private TextView getHelpTexView;
    private LinearLayout answerButtonLayout;
    private DecisionTree decisionTree;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = database.getReference("Users");
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        questionTextView = findViewById(R.id.questionTextView);
        answerButtonLayout = findViewById(R.id.answerButtonLayout);
        versionTextView = findViewById(R.id.versionTextView);
        getHelpTexView = findViewById(R.id.getHelpText);

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
        getHelpTexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetHelpActivity.class);
                startActivity(intent);

            }
        });
    }

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

    private void displayQuestion() {
        if (decisionTree != null) {
            try {
                if (decisionTree.isLeafNode()) {
                    // Display result
                    String result = decisionTree.getResult();
                    questionTextView.setText(result);
                    answerButtonLayout.removeAllViews();
                    return;
                } else {
                    questionTextView.setText(decisionTree.getQuestion());
                    answerButtonLayout.removeAllViews();
                    JSONArray answers = decisionTree.getAnswers();
                    if (answers != null) {
                        for (int i = 0; i < answers.length(); i++) {
                            Button answerButton = new Button(this);
                            answerButton.setText(answers.getJSONObject(i).getString("answer"));
                            answerButton.setTag(i);
                            answerButton.setOnClickListener(this);
                            answerButtonLayout.addView(answerButton);
                        }
                    }

                    if (currentQuestionIndex > 0) {
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
}

