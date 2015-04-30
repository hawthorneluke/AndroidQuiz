package jp.co.cyberagent.hawthorneluke.quiz;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ある質問に対しての答え群を持つ
 */
public class AnswerData {

    /**答えのidに答えの文字列を持つ*/
    private Map<Integer, String> answerStrings = new HashMap<>();
    /**answerStringsの中のどのidが正解の答えか*/
    private Integer correctAnswer = null;
    /**ユーザはどの答えを選んだか。nullならまだ答えていない。*/
    private Integer chosenAnswer = null;

    /**
     * 引数で指定した文字列で答え群を作る。
     * @param answerStrings 書く答えの文字列。先頭のものは正解となり、その後のものは全て不正解となる。
     */
    public AnswerData(String ... answerStrings) {
        correctAnswer = 0;
        for (String answerString : answerStrings) {
            int id = this.answerStrings.size();
            this.answerStrings.put(id, answerString);
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
            if (answer.getValue() == true) correctAnswer = id;
        }
    }

    /**
     * ユーザはどの答えを選んだか。nullならまだ答えていない。
     * @return 答えのid
     */
    public Integer getChosenAnswer() {
        return this.chosenAnswer;
    }

    /**
     * * この答え群の各答えとそのidを返す。
     * @return 答えのidにその答えの文字列のエントリのリスト
     */
    public List<Map.Entry<Integer, String>>  getAnswerStrings() {
        List<Map.Entry<Integer, String>> list = new ArrayList<>(answerStrings.entrySet());
        if (QuizData.willRandomizeAnswers()) {
            Collections.shuffle(list);
        }

        return list;
    }

    /**
     * この答え群の中の正解の答えとそのidを返す。
     * @return 答えのidにその答えの文字列のエントリ
     */
    public Map.Entry<Integer, String> getCorrectAnswerString() {
        return new AbstractMap.SimpleEntry<Integer, String>(correctAnswer, answerStrings.get(correctAnswer));
    }

    /**
     * この答え群の中の選ばれた答えとそのidを返す。
     * @return 答えのidにその答えの文字列のエントリ
     */
    public Map.Entry<Integer, String> getChosenAnswerString() {
        return new AbstractMap.SimpleEntry<Integer, String>(chosenAnswer, answerStrings.get(chosenAnswer));
    }

    /**
     * この答え群にある答えの数を返す。
     * @return 答えの数
     */
    public int count() {
        return answerStrings.size();
    }

    /**
     * どの答えが正解かを返す。
     * @return answersマップに対する正解の答えのid
     */
    public Integer getCorrectAnswer() {
        return this.correctAnswer;
    }

    /**
     * まだユーザが答えてない状態に戻す。
     */
    public void reset() {
        chosenAnswer = null;
    }

    /**
     * 答え群から答えを選ぶ。
     * @param id  答え群の中の答えのid
     * @return true = 正解。false = 不正解。
     */
    public int answer(int id){
        chosenAnswer = id;
        return correctAnswer;
    }

}
