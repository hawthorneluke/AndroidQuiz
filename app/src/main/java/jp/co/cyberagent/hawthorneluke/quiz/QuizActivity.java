package jp.co.cyberagent.hawthorneluke.quiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * クイズ本体
 */
public class QuizActivity extends AppCompatActivity {

    private static final int WAIT_TIME = 2000; //答えた後、次の問題に移るまでの時間(ms)
    private static final int BUTTON_WIDTH = 350; //ボタンの幅
    private static final int BUTTON_HEIGHT = 80; //ボタンの高さ（まる・ばつの図の幅と高さもこの値になる）

    private Map<Integer, View> mButtonList = new HashMap<>(); //ボタンを持つLinearLayouはどのボタン・答えのidに対応してるか格納する

    private TextView mQuestionNumberText; //何問目の問題か
    private TextView mQuestionText; //問題文
    private LinearLayout mAnswersLayout; //答えが存在するlayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionNumberText = (TextView)findViewById(R.id.text_question_number);
        mQuestionText = (TextView)findViewById(R.id.text_quiz_question);

        mAnswersLayout = (LinearLayout) findViewById(R.id.layout_quiz_answer_buttons);


        //最初からなら
        if (savedInstanceState == null) {
            QuizData.reset(); //前に答えた時のデータとかを全て消す

            //まだ問題のデータがロードされてないなら
            if (QuizData.getQuestionCount() == 0) {
                loadQuizData();
            }

            QuizData.setRandomizeAnswers(true); //答えの出現順番をランダムに
            QuizData.randomizeQuestions(); //問題の出現順番をランダムに
        }

        //最初の問題を取得して画面に表示する
        QuizData quizData = QuizData.getCurrentQuizData();
        if (quizData != null) {
            showQuestionData(quizData);
        }
    }

    private void showQuestionData(QuizData quizData) {

        mQuestionNumberText.setText("第" + (QuizData.getAnsweredCount()+1) + "問"); //何問目かを表示
        mQuestionText.setText(quizData.getQuestion()); //問題文を表示

        //ボタンとして各答えを画面に追加
        mAnswersLayout.removeAllViewsInLayout(); //ボタンが既に追加されていたら全てを消す。
        mButtonList.clear();
        for(Map.Entry<Integer, String> answer : quizData.getAnswers()) {
            //ボタンとまる・ばつを持つLinearLayoutを作る
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_HORIZONTAL);
            mButtonList.put(answer.getKey(), row); //このLinearLayoutはどのidに対応してるかを記録

            //ボタンを作る
            Button button = new Button(this);
            button.setTextColor(getResources().getColor(R.color.text));
            button.setTextSize(12);
            button.setText(answer.getValue());

            //ボタンのレイアウト
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            button.setLayoutParams(params);
            button.setWidth(BUTTON_WIDTH);
            button.setHeight(BUTTON_HEIGHT);

            //ボタンを押した時、そのボタンが連携してる答えのiがわかるように
            button.setId(answer.getKey()); //こんなことしていいのかな？

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answer(v.getId());
                }
            });

            //ボタンをlayoutに追加
            row.addView(button);
            mAnswersLayout.addView(row);
        }
    }

    /**
     * ボタンをタッチして答える
     * @param id タッチしたボタンのid(答えのidと同じ)
     */
    private void answer(int id) {
        //全てのボタンを洗濯不可にする
        for(int i = 0; i < mAnswersLayout.getChildCount(); i++) {
            LinearLayout childLayout = (LinearLayout)mAnswersLayout.getChildAt(i);
            for(int j = 0; j < childLayout.getChildCount(); j++) {
                View child = childLayout.getChildAt(j);
                child.setEnabled(false);
            }
        }

        int correctAnswerId = QuizData.answerAndMoveToNextQuestion(id); //答えて、正解の答えのidを取得する

        //ユーザが選んだボタンと正解のボタンを持つLinearLayoutを取得
        LinearLayout rowCorrect = (LinearLayout)mButtonList.get(correctAnswerId);
        LinearLayout rowAnswered = (LinearLayout)mButtonList.get(id);


        //不正解の表示
        if (correctAnswerId != id) {
            ImageView image = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.height = BUTTON_HEIGHT;
            //noinspection SuspiciousNameCombination
            params.width = params.height; //正方形だから
            params.gravity = Gravity.CENTER;
            params.setMargins(-BUTTON_HEIGHT, 0, 0, 0); //左に画像が入ってくるから
            image.setLayoutParams(params);
            image.setImageResource(R.mipmap.batu);
            rowAnswered.addView(image, 0);
        }

        //正解の表示
        ImageView image = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = BUTTON_HEIGHT;
        //noinspection SuspiciousNameCombination
        params.width = params.height; //正方形だから
        params.gravity = Gravity.CENTER;
        params.setMargins(-BUTTON_HEIGHT, 0, 0, 0); //左に画像が入ってくるから
        image.setLayoutParams(params);
        image.setImageResource(R.mipmap.maru);
        rowCorrect.addView(image, 0);

        //時間を待つ
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                QuizData quizData = QuizData.getCurrentQuizData();
                if (quizData == null) {
                    showScore();
                }
                else {
                    showQuestionData(quizData); //次の問題を取得して画面に表示する
                }
            }
        }, WAIT_TIME);
    }

    /**
     * 最後にスコアをダイアログで表示
     */
    private void showScore() {
        //スコアを計算
        //TODO res/stringへ
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

    /**
     * デフォルトのクイズデータをロードする
     */
    private void loadQuizData() {
        QuizData.addQuestion("お酒に関する言葉で「おにぎす」といえば焼酎のことですが、「にがぎす」といえば何？", new AnswerData("ビール", "焼酎", "ワイン", "お屠蘇"), "きす…酒類一般。\nおにぎす…焼酎・梅酒。\nにがぎす…ビール。\n苦みがあるから。");
        QuizData.addQuestion("これ以上生きるのは珍しいということから「珍寿」と呼ばれるのは何歳以上のお祝い？", new AnswerData("95歳", "81歳", "90歳", "111歳"), "81歳…「盤寿」「半寿」\n90歳…「星寿」「卆寿」\n95歳…「珍寿」\n111歳…「皇寿」");
        QuizData.addQuestion("中高年の悩みとなる「加齢臭」という言葉を命名したメーカーは？", new AnswerData("資生堂", "コーセー", "花王", "ライオン"), "2002年12月11日に、資生堂の研究により、中高年特有の体臭の原因が発見され、この体臭を資生堂により「加齢臭」と名付けられている。");
        QuizData.addQuestion("元々は第一次世界大戦で開発されたコートの種類は？", new AnswerData("トレンチコート", "ダッフルコート", "ピーコート", "レインコート"), "寒冷な欧州での戦いに対応する防水型の軍用コートが求められたことから開発されたものである。");
        QuizData.addQuestion("日本の郵便番号で「999」で始まる番号があるのはどこの都道府県？", new AnswerData("山形県", "東京都", "徳島県", "鳥取県"), "3ケタ＋3ケタの郵便番号が施行された当時(1968年)の鉄道を主体とした郵便の輸送経路に基づいて附番されて");
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


    /**
     * スコアを表示するためのダイアログフラグメント
     */
    public static class ScoreDialogFragment extends DialogFragment {

        private TextView mText;

        public ScoreDialogFragment() {
            // Empty constructor required for DialogFragment
        }

        @Override
        @NonNull
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
                                    Intent intent = new Intent(getActivity(), ExplanationActivity.class);
                                    startActivity(intent);
                                    getActivity().finish(); //解説から戻る時はトップになるように
                                }
                            }
                    )
                    .setNegativeButton(getResources().getString(R.string.score_dialog_return_to_top),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    getActivity().finish(); //終わりにしてトップに戻る
                                }
                            }
                    )
                    .create();
        }
    }
}
