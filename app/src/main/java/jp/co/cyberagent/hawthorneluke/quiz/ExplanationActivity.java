package jp.co.cyberagent.hawthorneluke.quiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * 解説をViewPageで表示するためのActivity
 */
public class ExplanationActivity extends AppCompatActivity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        setTitle(R.string.title_explanation);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager_explanation);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explanation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_explanation_end) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 各解説のページを表示するFragmentStatePagerAdapter
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * ページを取得
         * @param position 何ページ（問）目
         * @return そのページとなるフラグメント
         */
        @Override
        public Fragment getItem(int position) {
            QuizData quizData = QuizData.getQuizData(position); //このページのデータを取得
            if (quizData == null) {
                throw new NullPointerException("No QuizData for position :" + position);
            }

            //取得したデータで新しいページのフラグメントを作る
            return ExplanationFragment.newInstance((position + 1), quizData.getQuestion(), quizData.getChosenAnswer().getValue(), quizData.getCorrectAnswer().getValue(), quizData.getExplanation());
        }

        /**
         * ページの総ページ数を返す
         * @return 総ページ数
         */
        @Override
        public int getCount() {
            return QuizData.getQuestionCount();
        }
    }
}
