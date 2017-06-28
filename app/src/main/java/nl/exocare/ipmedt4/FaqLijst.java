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

        Vragen[] lijstjeVragen = new Vragen [4];
        lijstjeVragen[0] = new Vragen("Hoort het te jeuken onder mijn gips?", "Ja, dat is een veelvoorkomende irritatie bij patiënten met gips.");
        lijstjeVragen[1] = new Vragen("Hoe lang duurt het voordat ik alles weer normaal kan doen?", "Volledig normaal en pijnvrij gebruik van het been kan al gauw 3-6 maanden of soms nog langer duren.");
        lijstjeVragen[2] = new Vragen("Waarom moet ik oefeningen doen met mijn been?", "Omdat spieren snel verslappen na een botbreuk en daardoor opnieuw getraind moeten worden.");
        lijstjeVragen[3] = new Vragen("Kan ik douchen met gips?", "Ja dat kan, je moet er echter wel voor zorgen dat de kous niet nat wordt, dit kan leiden tot huidverweking en blaarvorming.");
        lijstjeVragen[3] = new Vragen("Mag ik autorijden als ik in het gips zit?", "Hier is veel onduidelijkheid over, het is niet verboden, maar in sommige autoverzekeringspolisvoowaarden staat aangegeven dat er in het geval van rijden met gips bij een ongeluk niets wordt uitgekeerd.");

        ListAdapter la = new ArrayAdapter<Vragen>(this, android.R.layout.simple_list_item_1, lijstjeVragen);

        lv.setAdapter(la);
    }
}
