import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private Button yesButton;
    private Button noButton;

    private DecisionTree currentDecisionTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        questionTextView = findViewById(R.id.questionTextView);
        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);

        // Load JSON data from assets folder
        String json = loadJSONFromAsset("decision_tree.json");

        // Parse JSON data into DecisionTree object
        try {
            JSONObject jsonObject = new JSONObject(json);
            currentDecisionTree = parseDecisionTree(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set initial question
        setQuestion(currentDecisionTree.getQuestion());

        // Set click listeners for answer buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswerButtonClick(true);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswerButtonClick(false);
            }
        });
    }

    private DecisionTree parseDecisionTree(JSONObject jsonObject) throws JSONException {
        String version = jsonObject.getString("version");
        String question = jsonObject.getString("question");

        DecisionTree decisionTree = new DecisionTree(version, question);

        if (jsonObject.has("result")) {
            String result = jsonObject.getString("result");
            decisionTree.setResult(result);
        } else if (jsonObject.has("nextNode")) {
            JSONObject nextNodeObject = jsonObject.getJSONObject("nextNode");
            DecisionTree yesNode = parseDecisionTree(nextNodeObject.getJSONObject("yesNode"));
            DecisionTree noNode = parseDecisionTree(nextNodeObject.getJSONObject("noNode"));
            decisionTree.setNextNode(new DecisionNode(yesNode, noNode));
        }

        return decisionTree;
    }

    private void setQuestion(String question) {
        questionTextView.setText(question);
    }

    private void handleAnswerButtonClick(boolean isYes) {
        if (currentDecisionTree.getNextNode() != null) {
            // Set next question or display result
            if (isYes) {
                currentDecisionTree = currentDecisionTree.getNextNode().getYesNode();
            } else {
                currentDecisionTree = currentDecisionTree.getNextNode().getNoNode();
            }

            if (currentDecisionTree.getResult() != null) {
                // Display result
                String result = currentDecisionTree.getResult();
                setQuestion(result);
                // Perform any other actions based on the result
            } else {
                // Set next question
                setQuestion(currentDecisionTree.getQuestion());
            }
        }
    }

    private String loadJSONFromAsset(String fileName) {
        String json;
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
