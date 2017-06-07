package nl.exocare.ipmedt4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static nl.exocare.ipmedt4.R.layout.activity_faq;

/**
 * Created by avdbe on 6-6-2017.
 */

public class FaqActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_faq);
    }

    protected void mijnFunctie(View v){
        Intent i = new Intent(this, FaqLijst.class);

        i.putExtra("firstKeyName", "FirstKeyValue");
        i.putExtra("secondKeyName", "SecondKeyValue");

        startActivity(i);
    }
}
