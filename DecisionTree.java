import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DecisionTree {

    private String question;
    private JSONArray answers;
    private DecisionTree parent;
    private DecisionTree[] children;

    public DecisionTree(JSONObject node) throws JSONException {
        this.question = node.getString("question");
        this.answers = node.getJSONArray("answers");
        this.parent = null;
        this.children = new DecisionTree[answers.length()];
        for (int i = 0; i < answers.length(); i++) {
            JSONObject childNode = answers.getJSONObject(i).getJSONObject("child");
            children[i] = new DecisionTree(childNode);
            children[i].setParent(this);
        }
    }

    public String getQuestion() {
        return question;
    }

    public JSONArray getAnswers() {
        return answers;
    }

    public DecisionTree getParent() {
        return parent;
    }

    public void setParent(DecisionTree parent) {
        this.parent = parent;
    }

    public DecisionTree getChild(int index) {
        if (index >= 0 && index < children.length) {
            return children[index];
        } else {
            return null;
        }
    }
}
