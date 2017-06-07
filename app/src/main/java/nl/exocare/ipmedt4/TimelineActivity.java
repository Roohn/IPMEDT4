package nl.exocare.ipmedt4;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimelineActivity extends AppCompatActivity {
    TimelineHandler timeline = null;
    ConstraintLayout timelineLayout;
    TextView beginDatum, eindDatum, controleDatum, revalidatieDatum;
    ProgressBar tijdlijn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // method call to initialize the views
        initViews();

        //datums ophalen
        try {
            timeline = new TimelineHandler();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //hoogte en breedte van het scherm bepalen en tijdlijn vullen
        ViewTreeObserver observer = timelineLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                fillTimeline();
                timelineLayout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });
    }

    private void initViews() {
        timelineLayout = (ConstraintLayout) findViewById(R.id.timelineActivity);
        beginDatum = (TextView) findViewById(R.id.beginDatum);
        eindDatum = (TextView) findViewById(R.id.eindDatum);
        controleDatum = (TextView) findViewById(R.id.controleDatum);
        revalidatieDatum = (TextView) findViewById(R.id.revalidatieDatum);
        tijdlijn = (ProgressBar) findViewById(R.id.vertical_progressbar);
    }

    /**
     * Deze methode vult de tijdlijn en zorgt voor de format
     */
    private void fillTimeline() {
        //set de datums van begin en eind goed
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        beginDatum.setText(df.format(timeline.getBeginDatum()));
        eindDatum.setText(df.format(timeline.getEindDatum()));
        controleDatum.setText(df.format(timeline.getControleDatum()));
        revalidatieDatum.setText(df.format(timeline.getRevalidatieDatum()));

        int trajectDuur = 0;
        try {
            //haal de duur van het herstellen op
            trajectDuur = (int) timeline.getTrajectduur(timeline.getBeginDatum(), timeline.getEindDatum());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //de tijd die de patient al bezig is
        int verstrekenTijd = (int) timeline.getVerstrekentijd();

        //zorg dat tijdlijn progress juist is
        tijdlijn.setMax(trajectDuur);
        tijdlijn.setProgress(verstrekenTijd);

        //zet de controle en begin revalidatie afspraken op de juiste hoogte in de tijdlijn
        ConstraintLayout controleLayout = (ConstraintLayout) findViewById(R.id.controleLayout);
        ConstraintLayout revalidatieLayout = (ConstraintLayout) findViewById(R.id.revalidatieLayout);
        ConstraintLayout hoofdLayout = (ConstraintLayout) findViewById(R.id.hoofdLayout);
        //voor het toevoegen van de controleafspraak
        int procentPerPixel = timelineLayout.getHeight() / trajectDuur;
        int afstandControle = 0;
        int afstandRevalidatie = 0;
        int afstandHoofd = 0;
        try {
            afstandControle = procentPerPixel * (int) (timeline.getTrajectduur(timeline.getBeginDatum(), timeline.getControleDatum()));
            afstandRevalidatie = procentPerPixel * (int) (timeline.getTrajectduur(timeline.getBeginDatum(), timeline.getRevalidatieDatum()));
            afstandHoofd = procentPerPixel * (int) (timeline.getTrajectduur(timeline.getBeginDatum(), timeline.getCurrentTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ConstraintLayout.LayoutParams controleParams = (ConstraintLayout.LayoutParams) controleLayout.getLayoutParams();
        controleParams.topMargin = afstandControle;
        ConstraintLayout.LayoutParams revalidatieParams = (ConstraintLayout.LayoutParams) revalidatieLayout.getLayoutParams();
        revalidatieParams.topMargin = afstandRevalidatie;

        //hoofdafstand
        ConstraintLayout.LayoutParams hoofdParams = (ConstraintLayout.LayoutParams) hoofdLayout.getLayoutParams();
        hoofdParams.topMargin = afstandHoofd;
    }

    /**
     * Revalidatie redirect
     *
     * @param v
     */
    public void clickRevalidatie(View v){
        Intent intent = new Intent(this, RevalidatieActivity.class);
        startActivity(intent);
    }

    /**
     * Controle redirect
     *
     * @param v
     */
    public void clickControle(View v){
        Intent intent = new Intent(this, ControleActivity.class);
        startActivity(intent);
    }

}
