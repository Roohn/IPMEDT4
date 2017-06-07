package nl.exocare.ipmedt4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RekStrekActivity extends AppCompatActivity {
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekstrek);

        index = 0;
    }

    public void volgendeOefening(View v) {
        ImageView gifAfbeelding = (ImageView) findViewById(R.id.gif_afbeelding);
        TextView uitleg = (TextView) findViewById(R.id.textUitleg);
        TextView title = (TextView) findViewById(R.id.textTitel);
        int[] rekstrekoefeningenafbeeldingen = {R.drawable.animatie_side_oefening02};
        String[] rekstrekoefeningentitle = {"Pak je tenen"};
        String[] rekstrekoefeningentext = {"Herhaal deze oefening 10 keer per voet."};

        if(index < rekstrekoefeningenafbeeldingen.length) {
            gifAfbeelding.setImageResource(rekstrekoefeningenafbeeldingen[index]);
            title.setText(rekstrekoefeningentitle[index]);
            uitleg.setText(rekstrekoefeningentext[index]);

            index++;
        }
        if(index == rekstrekoefeningenafbeeldingen.length) {
            Button buttonNext = (Button) findViewById(R.id.buttonNext);
            buttonNext.setVisibility(View.INVISIBLE);
        }
    }
}
