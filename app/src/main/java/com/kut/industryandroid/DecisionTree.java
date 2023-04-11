package com.kut.industryandroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DecisionTree {
    private String question;
    private String version;
    private JSONArray answers;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private DecisionTree parent;
    private DecisionTree[] children;
    private String result; // added field for leaf node result

    public DecisionTree(JSONObject node) throws JSONException {
        if (node.has("question")) {
            this.question = node.getString("question");
        }
        if (node.has("version")) {
            this.version = node.getString("version");
        }
        if (node.has("answers")) {
            this.answers = node.getJSONArray("answers");
            this.children = new DecisionTree[answers.length()];
            for (int i = 0; i < answers.length(); i++) {
                JSONObject answer = answers.getJSONObject(i);
                if (answer.has("nextNode")) {
                    children[i] = new DecisionTree(answer.getJSONObject("nextNode"));
                    children[i].setParent(this);
                }
            }
        }
        if (node.has("result")) {
            this.result = node.getString("result");
        }
        this.parent = null;
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

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(JSONArray answers) {
        this.answers = answers;
    }

    public DecisionTree[] getChildren() {
        return children;
    }

    public void setChildren(DecisionTree[] children) {
        this.children = children;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getResult() {
        return result;
    }

    public boolean isLeafNode() {
        return children == null || children.length == 0;
    }
}
