package jp.co.cyberagent.hawthorneluke.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * トップ画面
 * これがアプリ実行時に最初に表示される。
 */
public class TopActivity extends AppCompatActivity {

    private Button mButtonStart; //スタートボタン

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        setTitle(R.string.app_name);

        mButtonStart = (Button)findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //クイズを開始
                Intent intent = new Intent(TopActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish(); //アプリを終わりにする
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
