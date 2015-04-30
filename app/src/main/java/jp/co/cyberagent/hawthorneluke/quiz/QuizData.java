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
    private static Integer answeredNumber = 0;

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
    public static void randomizeAnswers(boolean randomizeAnswers) {
        QuizData.randomizeAnswers = randomizeAnswers;
    }

    /**
     * 問題の数を返す。
     * @return 問題の数
     */
    public static Integer size() {
        return list.size();
    }

    /**
     * 今までに答えた問題の数を返す。
     * @return 問題の数
     */
    public static Integer getAnsweredNumber() {
        return QuizData.answeredNumber;
    }

    /**
     * 今まで答えたデータをリセットして、またクイズを最初からやり直せるようにする。
     * 問題のリストはそのまま残る。
     */
    public static void reset() {
        QuizData.answeredNumber = 0;
        for(QuizData data : list) {
            data.answers.reset();
        }
    }

    /**
     * 指定の問題文、答えのデータ、解説文で問題を作成して問題のリストに追加する。
     * @param question 問題文
     * @param answers 答えのデータ
     * @param explanation 解説文
     */
    public QuizData(String question, AnswerData answers, String explanation) {
        this.question = question;
        this.answers = answers;
        this.explanation = explanation;

        list.add(this);
    }


    /**
     * ある質問に対しての答え群を持つ
     */
    class AnswerData {

        /**答えのidに答えの文字列を持つ*/
        private Map<Integer, String> answerStrings = new HashMap<>();
        /**答えのidに答えが正解か不正解かを持つ*/
        private Map<Integer, Boolean> answerBools = new HashMap<>();
        /**ユーザはどの答えを選んだか。nullならまだ答えていない。*/
        private Integer chosenAnswer = null;

        /**
         * 引数で指定した文字列で答え群を作る。
         * @param answerStrings 書く答えの文字列。先頭のものは正解となり、その後のものは全て不正解となる。
         */
        public AnswerData(String ... answerStrings) {
            boolean first = true;
            for (String answerString : answerStrings) {
                int id = this.answerStrings.size();
                this.answerStrings.put(id, answerString);
                if (first) {
                    first = false;
                    this.answerBools.put(id, true);
                }
                else {
                    this.answerBools.put(id, false);
                }
            }
        }

        /**
         * 指定したentrySetのデータで答え群を作る。
         * @param answers 答えの文字列に正解か不正解かのセットで出来たエントリ
         */
        public AnswerData(Set<Map.Entry<String, Boolean>> answers) {
            for(Map.Entry<String, Boolean> answer : answers) {
                int id = this.answerStrings.size();
                this.answerStrings.put(id, answer.getKey());
                this.answerBools.put(id, answer.getValue());
            }
        }

        /**
         * 質問を全てランダムな順番にする
         */
        public void shuffle() {
            Collections.shuffle(list);
        }

        /**
         * ユーザはどの答えを選んだか。nullならまだ答えていない。
         * @return 答えのid
         */
        public Integer getChosenAnswer() {
            return this.chosenAnswer;
        }

        /**
         * この答え群の各答えに対しての答えの文字列を返す。
         * @return 答えのidに問題文のマップ
         */
        public Map<Integer, String> getAnswerStrings() {
            return this.answerStrings;
        }

        /**
         * この答え群の各答えに対しての正解・不正解のデータを返す。
         * @return 答えのidにその答えが正解か不正解か。
         */
        public Map<Integer, Boolean> getAnswerBools() {
            return this.answerBools;
        }

        /**
         * まだユーザが答えてない状態に戻す。
         */
        public void reset() {
            chosenAnswer = null;
        }

    }
}
