package jp.co.cyberagent.hawthorneluke.quiz;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExplanationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplanationFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_QUESTION_NUMBER = "questionNumber";
    private static final String ARG_QUESTION = "question";
    private static final String ARG_YOUR_ANSWER = "yourAnswer";
    private static final String ARG_CORRECT_ANSWER = "correctAnswer";
    private static final String ARG_EXPLANATION = "explanation";

    private int mQuestionNumber;
    private String mQuestion;
    private String mYourAnswer;
    private String mCorrectAnswer;
    private String mExplanation;

    private TextView mTextQuestion;
    private TextView mTextYourAnswer;
    private TextView mTextCorrectAnswer;
    private TextView mTextExplanation;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param questionNumber これは何番目の問題だったか
     * @param question 問題文
     * @param yourAnswer プレイヤーが選んだ答えの文章
     * @param correctAnswer 正解の答えの文章
     * @param explanation 解説文
     * @return A new instance of fragment ExplanationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExplanationFragment newInstance(int questionNumber, String question, String yourAnswer, String correctAnswer, String explanation) {
        ExplanationFragment fragment = new ExplanationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_NUMBER, questionNumber);
        args.putString(ARG_QUESTION, question);
        args.putString(ARG_YOUR_ANSWER, yourAnswer);
        args.putString(ARG_CORRECT_ANSWER, correctAnswer);
        args.putString(ARG_EXPLANATION, explanation);
        fragment.setArguments(args);
        return fragment;
    }

    public ExplanationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestionNumber = getArguments().getInt(ARG_QUESTION_NUMBER);
            mQuestion = getArguments().getString(ARG_QUESTION);
            mYourAnswer = getArguments().getString(ARG_YOUR_ANSWER);
            mCorrectAnswer = getArguments().getString(ARG_CORRECT_ANSWER);
            mExplanation = getArguments().getString(ARG_EXPLANATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_explanation_slide_page, container, false);

        mTextQuestion = (TextView)rootView.findViewById(R.id.text_explanation_question);
        mTextYourAnswer = (TextView)rootView.findViewById(R.id.text_explanation_your_answer);
        mTextCorrectAnswer = (TextView)rootView.findViewById(R.id.text_explanation_correct_answer);
        mTextExplanation = (TextView)rootView.findViewById(R.id.text_explanation_explanation);

        mTextQuestion.setText(mQuestion);
        mTextYourAnswer.setText(mYourAnswer);
        mTextCorrectAnswer.setText(mCorrectAnswer);
        mTextExplanation.setText(mExplanation);

        //Toast.makeText(getActivity(), "第" + mQuestionNumber + "問", Toast.LENGTH_SHORT).show();

        return rootView;
    }

}
