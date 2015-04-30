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
     * 正しく答えた質問の数を返す。
     * @return 正しく答えた質問の数
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
     * 次の質問のデータを返す。
     * @return 次の質問のデータ
     */
    public static QuizData getQuestion() {
        return QuizData.list.get(QuizData.answeredCount);
    }

    /**
     * この質問の答えを選ぶ。
     * これでgetQuestionメソッドによって次の質問のデータを得られる。
     * @param id 答え群の中の答えのid
     * @return true = 正解。false = 不正解。
     */
    public static boolean answerAndMoveToNextQuestion(int id) {
        QuizData data = QuizData.list.get(QuizData.answeredCount);
        QuizData.answeredCount++;
        return data.answers.answer(id);
    }

    /**
     * 質問を追加する。
     * @param question 問題文
     * @param answers 答えのデータ
     * @param explanation 解説文
     */
    public static void addQuestion(String question, AnswerData answers, String explanation) {
        new QuizData(question, answers, explanation);
    }

    /**
     * 質問の順番をランダムにする。
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
     * この質問に対する答え群の文字列を返す。
     * @return 答えのidにその答えの文字列から出来たエントリのリスト
     */
    public List<Map.Entry<Integer, String>> getAnswerStrings() {
        return answers.getAnswerStrings();
    }

    /**
     * この質問に対する答えの数を返す。
     * @return 答えの数
     */
    public int getAnswerCount() {
        return answers.count();
    }
}
