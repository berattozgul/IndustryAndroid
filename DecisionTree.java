import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DecisionTree {
    private JSONObject decisionTree;

    public DecisionTree(String json) throws JSONException {
        decisionTree = new JSONObject(json);
    }

    public String getQuestion() throws JSONException {
        return decisionTree.getString("question");
    }

    public JSONArray getAnswers() throws JSONException {
        return decisionTree.getJSONArray("answers");
    }

    public JSONObject getNextNode(int answerIndex) throws JSONException {
        JSONArray answers = getAnswers();
        return answers.getJSONObject(answerIndex).getJSONObject("nextNode");
    }

    public String getResult() throws JSONException {
        return decisionTree.getJSONObject("nextNode").getString("result");
    }
}
