public class DecisionTree {
    private String version;
    private String question;
    private DecisionTreeAnswer[] answers;

    // Constructor
    public DecisionTree(String version, String question, DecisionTreeAnswer[] answers) {
        this.version = version;
        this.question = question;
        this.answers = answers;
    }

    // Getters
    public String getVersion() {
        return version;
    }

    public String getQuestion() {
        return question;
    }

    public DecisionTreeAnswer[] getAnswers() {
        return answers;
    }
}
