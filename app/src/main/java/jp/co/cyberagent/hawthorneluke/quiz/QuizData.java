package jp.co.cyberagent.hawthorneluke.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * クイズの問題、答え、解説を持つもの。
 * 1つのインスタンスは１つの問題のデータを持つ。
 * staticメンバーのQuizData.listは全ての問題を持つ。
 */
public class QuizData {

    /**全ての問題を持つ*/
    private static List<QuizData> list = new ArrayList<>();
    /**答えの表示の順番をランダムにするかどうか*/
    private static boolean randomizeAnswers = false;
    /**今まで答えた問題の数*/
    private static int answeredCount = 0;

    /**問題文*/
    private String question;
    /**答えのデータ*/
    private AnswerData answers;
    /*解説文*/
    private String explanation;


    /**
     * 答えの表示の順番をランダムにするかどうか
     * @param randomizeAnswers true = 順番をランダムにする。false = 順番をランダムにしない。
     */
    public static void setRandomizeAnswers(boolean randomizeAnswers) {
        QuizData.randomizeAnswers = randomizeAnswers;
    }

    /**
     * 答えの表示の順をランダムにしてるかどうかを返す。
     * @return true = ランダムにしている。false = ランダムにしていない。
     */
    public static boolean willRandomizeAnswers() {
        return QuizData.randomizeAnswers;
    }

    /**
     * 問題の数を返す。
     * @return 問題の数
     */
    public static Integer getQuestionCount() {
        return QuizData.list.size();
    }

    /**
     * 今までに答えた問題の数を返す。
     * @return 問題の数
     */
    public static int getAnsweredCount() {
        return QuizData.answeredCount;
    }

    /**
     * 正しく答えた問題の数を返す。
     * @return 正しく答えた問題の数
     */
    public static int getCorrectCount() {
        int n = 0;
        for(QuizData data : QuizData.list) {
            if (data.answers.getChosenAnswer() != null && data.answers.getChosenAnswer() == data.answers.getCorrectAnswer()) {
                n++;
            }
        }

        return n;
    }

    /**
     * 今まで答えたデータをリセットして、またクイズを最初からやり直せるようにする。
     * 問題のリストはそのまま残る。
     */
    public static void reset() {
        QuizData.answeredCount = 0;
        for(QuizData data : QuizData.list) {
            data.answers.reset();
        }
    }

    /**
     * 次の問題のデータを返す。
     * @return 次の問題のデータ。もう次の問題がないならnullを返す。
     */
    public static QuizData getCurrentQuizData() {
        if (QuizData.answeredCount >= QuizData.list.size()) {
            return null;
        }

        return QuizData.list.get(QuizData.answeredCount);
    }

    /**
     * 指定した問題のデータを返す。
     * @param pos 0から、何番目の問題か
     * @return 問題のデータ。存在しないならnullを返す。
     */
    public static QuizData getQuizData(int pos) {
        if (pos < 0 || pos >= QuizData.list.size()) {
            return null;
        }

        return QuizData.list.get(pos);
    }

    /**
     * この問題の答えを選ぶ。
     * これでgetQuestionメソッドによって次の問題のデータを得られる。
     * @param id 答え群の中の答えのid
     * @return true = 正解。false = 不正解。
     */
    public static int answerAndMoveToNextQuestion(int id) {
        if (QuizData.answeredCount < QuizData.list.size()) {
            QuizData.answeredCount++;
        }

        QuizData data = QuizData.list.get(QuizData.answeredCount - 1);
        if (data == null) {
            return 0;
        }

        return data.answers.answer(id);
    }

    /**
     * 問題を追加する。
     * @param question 問題文
     * @param answers 答えのデータ
     * @param explanation 解説文
     */
    public static void addQuestion(String question, AnswerData answers, String explanation) {
        new QuizData(question, answers, explanation);
    }

    /**
     * 問題の順番をランダムにする。
     */
    public static void randomizeQuestions() {
        Collections.shuffle(QuizData.list);
    }


    /**
     * 指定の問題文、答えのデータ、解説文で問題を作成して問題のリストに追加する。
     * @param question 問題文
     * @param answers 答えのデータ
     * @param explanation 解説文
     */
    protected QuizData(String question, AnswerData answers, String explanation) {
        this.question = question;
        this.answers = answers;
        this.explanation = explanation;

        QuizData.list.add(this);
    }

    /**
     * この問題に対する答え群の文字列を返す。
     * @return 答えのidにその答えの文字列から出来たエントリのリスト
     */
    public List<Map.Entry<Integer, String>> getAnswers() {
        return answers.getAnswerStrings();
    }

    /**
     * この問題に対する正解の答えの文字列を返す。
     * @return　答えのidにその答えの文字列から出来たエントリ
     */
    public Map.Entry<Integer, String> getCorrectAnswer() {
        return answers.getCorrectAnswerString();
    }

    /**
     * この問題に対する選ばれた答えの文字列を返す。
     * @return　答えのidにその答えの文字列から出来たエントリ
     */
    public Map.Entry<Integer, String> getChosenAnswer() {
        return answers.getChosenAnswerString();
    }

    /**
     * この問題に対する答えの数を返す。
     * @return 答えの数
     */
    public int getAnswerCount() {
        return answers.count();
    }

    /**
     * こん問題の問題文を返す。
     * @return 問題文
     */
    public String getQuestion() {
        return question;
    }

    /**
     * この問題の解説文を返す。
     * @return 解説文
     */
    public String getExplanation() {
        return explanation;
    }
}
