package nl.exocare.ipmedt4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RekStrekActivity extends AppCompatActivity{
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

        //arrays voor elke oefening
        int[] rekstrekoefeningenafbeeldingen = {R.drawable.animatie_side_oefening02, R.drawable.animatie_side_oefening03};
        String[] rekstrekoefeningentitle = {"Pak je tenen", "Sta tegen de muur en doe je been achteruit"};
        String[] rekstrekoefeningentext = {"Herhaal deze oefening 10 keer per voet.", "Herhaal 10 keer."};

        //Laat de volgende afbeelding zien
        if(index < rekstrekoefeningenafbeeldingen.length) {
            gifAfbeelding.setImageResource(rekstrekoefeningenafbeeldingen[index]);
            title.setText(rekstrekoefeningentitle[index]);
            uitleg.setText(rekstrekoefeningentext[index]);

            index++;
        }

        //als er geen oefeningen meer zijn dan de knop weghalen
        if(index == rekstrekoefeningenafbeeldingen.length) {
            Button buttonNext = (Button) findViewById(R.id.buttonNext);
            buttonNext.setVisibility(View.INVISIBLE);
        }

    }

}
