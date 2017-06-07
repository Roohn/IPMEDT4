package nl.exocare.ipmedt4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by avdbe on 7-6-2017.
 */

public class FaqLijst extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ListView lv = (ListView) findViewById(R.id.faqLijst);

        Vragen[] lijstjeVragen = new Vragen [3];
        lijstjeVragen[0] = new Vragen("Hoort het te jeuken onder mijn gips?", "Ja.");
        lijstjeVragen[1] = new Vragen("Hoort het te jeuken onder mijn gips?", "Ja.");
        lijstjeVragen[2] = new Vragen("Hoort het te jeuken onder mijn gips?", "Ja.");

        ListAdapter la = new ArrayAdapter<Vragen>(this, android.R.layout.simple_list_item_1, lijstjeVragen);

        lv.setAdapter(la);
    }
}
