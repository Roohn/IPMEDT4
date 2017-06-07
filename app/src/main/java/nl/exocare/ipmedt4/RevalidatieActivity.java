package nl.exocare.ipmedt4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RevalidatieActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revalidatie);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonFaq:
                Intent intent1 = new Intent(this, FaqActivity.class);
                startActivity(intent1);
                break;
            case R.id.buttonStrek:
                Intent intent2 = new Intent(this, RekStrekActivity.class);
                startActivity(intent2);
                break;
            case R.id.buttonKracht:
                Intent intent3 = new Intent(this, KrachtActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
