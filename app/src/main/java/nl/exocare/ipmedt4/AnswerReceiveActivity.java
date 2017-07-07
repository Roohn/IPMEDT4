package nl.exocare.ipmedt4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Daan on 28-6-2017.
 */

public class AnswerReceiveActivity extends AppCompatActivity {
    private TextView tvAnswerReceiveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_receive);

        tvAnswerReceiveText = (TextView) findViewById(R.id.tvAnswerReceiveText);
        Log.d("Main", getIntent().getAction());

        tvAnswerReceiveText.setText(getIntent().getAction());
    }
}
