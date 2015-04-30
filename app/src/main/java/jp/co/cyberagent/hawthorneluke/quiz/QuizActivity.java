package jp.co.cyberagent.hawthorneluke.quiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

/**
 * クイズ本体
 */
public class QuizActivity extends AppCompatActivity {

    private TextView mQuestionNumberText; //何問目の問題か
    private TextView mQuestionText; //問題文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (QuizData.getQuestionCount() == 0) {
            loadQuizData();
        }

        mQuestionNumberText = (TextView)findViewById(R.id.text_question_number);
        mQuestionText = (TextView)findViewById(R.id.text_quiz_question);

        QuizData quizData = QuizData.getQuizData();
        if (quizData != null) {
            showQuestionData(QuizData.getQuizData()); //１最初の問題を取得して画面に表示する
        }
    }

    private void showQuestionData(QuizData quizData) {

        mQuestionNumberText.setText("第" + (QuizData.getAnsweredCount()+1) + "問"); //何問目かを表示
        mQuestionText.setText(quizData.getQuestion()); //問題文を表示

        //ボタンとして各答えを画面に追加
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_quiz_answer_buttons); //ここに追加
        layout.removeAllViewsInLayout(); //ボタンが既に追加されていたら全てを消す。
        for(Map.Entry<Integer, String> answer : quizData.getAnswers()) {
            Button button = new Button(this);
            button.setText(answer.getValue());
            button.setId(answer.getKey()); //こんなことしていいのかな？

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answer(v.getId());
                }
            });

            //ボタンをlayoutに追加
            layout.addView(button);
        }
    }

    private void answer(int id) {
        int correctAnswerId = QuizData.answerAndMoveToNextQuestion(id); //答えて、正解の答えのidを取得する

        //TODO 正解・不正解の表示

        QuizData quizData = QuizData.getQuizData();
        if (quizData == null) {
            //スコアを計算
            int correctCount = QuizData.getCorrectCount(); //2回無駄に計算させないため
            String message = "問題数: " + QuizData.getQuestionCount();
            message += "\n正解数: " + correctCount;
            message += "\n正解率: " + String.format("%.2f%%", ((double)correctCount / (double)QuizData.getQuestionCount() * 100));

            //結果のalertFragmentを表示
            FragmentManager fm = getSupportFragmentManager();
            ScoreDialogFragment dialog = new ScoreDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", getResources().getString(R.string.score_dialog_title));
            args.putString("message", message);
            dialog.setArguments(args);
            dialog.show(fm, "score_dialog_fragment");
        }
        else {
            showQuestionData(quizData); //次の問題を取得して画面に表示する
        }
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


    public static class ScoreDialogFragment extends DialogFragment {

        private TextView mText;

        public ScoreDialogFragment() {
            // Empty constructor required for DialogFragment
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);

            String title;
            String message;

            try {
                title = getArguments().getString("title");
                message = getArguments().getString("message");
            } catch(NullPointerException ex) {
                throw new InstantiationException("titleかmessageが渡されてない", ex);
            }

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.score_dialog_go_to_explanation),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }
                    )
                    .setNegativeButton(getResources().getString(R.string.score_dialog_return_to_top),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    getActivity().finish();
                                }
                            }
                    )
                    .create();
        }
    }
}
