import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTextView;
    private LinearLayout answerButtonLayout;
    private DecisionTree decisionTree;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        questionTextView = findViewById(R.id.questionTextView);
        answerButtonLayout = findViewById(R.id.answerButtonLayout);

        // Load decision tree from JSON file
        String json = loadDecisionTreeFromJsonFile();
        try {
            decisionTree = new DecisionTree(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Display initial question
        displayQuestion();
    }

    private String loadDecisionTreeFromJsonFile() {
        InputStream inputStream = getResources().openRawResource(R.raw.decision_tree);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
             bufferedReader.close();
        } catch (IOException e) {
             e.printStackTrace();
         }
         return stringBuilder.toString();
    }

    private void displayQuestion() {
        try {
            // Display current question
            questionTextView.setText(decisionTree.getQuestion());

            // Clear existing answer buttons
            answerButtonLayout.removeAllViews();

            // Create answer buttons dynamically
            JSONArray answers = decisionTree.getAnswers();
            for (int i = 0; i < answers.length(); i++) {
                Button answerButton = new Button(this);
                answerButton.setText(answers.getJSONObject(i).getString("answer"));
                answerButton.setTag(i);
                answerButton.setOnClickListener(this);
                answerButtonLayout.addView(answerButton);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            int answerIndex = (int) view.getTag();
            JSONObject nextNode = decisionTree.getNextNode(answerIndex);
            if (nextNode.has("question")) {
                // If next node has a question, update current question index and display next question
                currentQuestionIndex++;
                decisionTree = new DecisionTree(nextNode.toString());
                displayQuestion();
            } else if (nextNode.has("result")) {
                // If next node has a result, display the result
                String result = nextNode.getString("result");
                questionTextView.setText(result);
                answerButtonLayout.removeAllViews();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
