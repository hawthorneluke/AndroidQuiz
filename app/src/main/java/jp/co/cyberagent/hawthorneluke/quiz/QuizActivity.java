package jp.co.cyberagent.hawthorneluke.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * クイズ本体
 */
public class QuizActivity extends AppCompatActivity {

    QuizData quizData; //現在の問題

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (QuizData.getQuestionCount() == 0) {
            loadQuizData();
        }

        quizData = QuizData.getQuestion(); //１最初の問題を取得
    }

    private void loadQuizData() {
        QuizData.addQuestion("お酒に関する言葉で「おにぎす」といえば焼酎のことですが、「にがぎす」といえば何？", new AnswerData("ビール", "焼酎", "ワイン", "お屠蘇"), "きす…酒類一般。\nおにぎす…焼酎・梅酒。\nにがぎす…ビール。\n苦みがあるから。");
        QuizData.addQuestion("これ以上生きるのは珍しいということから「珍寿」と呼ばれるのは何歳以上のお祝い？", new AnswerData("95歳", "81歳", "90歳", "111歳"), "81歳…「盤寿」「半寿」\n90歳…「星寿」「卆寿」\n95歳…「珍寿」\n111歳…「皇寿」");
        QuizData.addQuestion("中高年の悩みとなる「加齢臭」という言葉を命名したメーカーは？", new AnswerData("資生堂", "コーセー", "花王", "ライオン"), "2002年12月11日に、資生堂の研究により、中高年特有の体臭の原因が発見され、この体臭を資生堂により「加齢臭」と名付けられている。");
        QuizData.addQuestion("元々は第一次世界大戦で開発されたコートの種類は？", new AnswerData("トレンチコート", "ダッフルコート", "ピーコート", "レインコート"), "寒冷な欧州での戦いに対応する防水型の軍用コートが求められたことから開発されたものである。");
        QuizData.addQuestion("日本の郵便番号で「999」で始まる番号があるのはどこの都道府県？", new AnswerData("山形県", "東京都", "徳島県", "鳥取県"), "3ケタ＋3ケタの郵便番号が施行された当時(1968年)の鉄道を主体とした郵便の輸送経路に基づいて附番されて");

        QuizData.setRandomizeAnswers(true);
        QuizData.randomizeQuestions();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_quiz_end) {
            finish(); //戻るボタンでここに戻れないように、戻るボタンを同じ動作をして、トップに戻る。
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
