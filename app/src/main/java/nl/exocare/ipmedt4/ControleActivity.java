package nl.exocare.ipmedt4;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class ControleActivity extends AppCompatActivity {
    private long timeCountInMilliSecondsControle = 1 * 60000;
    private long timeCountInMilliSecondsRevalidatie = 1 * 60000;
    TextView datumGipsBehandeling, datumControleBehandeling, textViewTime, textViewTime2;
    ProgressBar progressBarCircle, progressBarCircle2;
    TimelineHandler timeline = null;
    private CountDownTimer countDownTimer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behandeld);

        // method call to initialize the views
        initViews();

        //datums ophalen
        try {
            timeline = new TimelineHandler();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Behandelingspagina datums goed zetten
        fillBehandelingPagina();
    }

    /**
     * method to initialize the views
     */
    private void initViews() {
        datumGipsBehandeling = (TextView) findViewById(R.id.datum_gips);
        datumControleBehandeling = (TextView) findViewById(R.id.datum_controle);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        progressBarCircle2 = (ProgressBar) findViewById(R.id.progressBarCircle_gips);
        textViewTime = (TextView) findViewById(R.id.textViewTime_gips);
        textViewTime2 = (TextView) findViewById(R.id.textViewTime);
    }

    /**
     * het goedzetten van de datums op de behandelingspagina
     */
    public void fillBehandelingPagina() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        datumGipsBehandeling.setText(df.format(timeline.getControleDatum()));
        datumControleBehandeling.setText(df.format(timeline.getRevalidatieDatum()));

        //start timers
        startCountDownTimer();
    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues() {
        int timeControle = 0;
        int timeRevalidatie = 0;

        //uit de timelinehandler halen
        try {
            timeControle = (int) timeline.getTrajectduur(timeline.getCurrentTime(), timeline.getControleDatum());
            timeControle = timeControle * 24 * 60;

            timeRevalidatie = (int) timeline.getTrajectduur(timeline.getCurrentTime(), timeline.getRevalidatieDatum());
            timeRevalidatie = timeRevalidatie * 24 * 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // assigning values after converting to milliseconds
        timeCountInMilliSecondsControle = timeControle * 60 * 1000;
        timeCountInMilliSecondsRevalidatie = timeRevalidatie * 60 * 1000;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {
        //haal de goede tijd op
        setTimerValues();

        countDownTimer = new CountDownTimer(timeCountInMilliSecondsControle, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {

                textViewTime.setText("Klaar!");
                // call to initialize the progress bar values
                setProgressBarValues();
            }

        }.start();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSecondsControle / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSecondsControle / 1000);
        progressBarCircle2.setMax((int) timeCountInMilliSecondsRevalidatie / 1000);
        progressBarCircle2.setProgress((int) timeCountInMilliSecondsRevalidatie / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02du\n%02dm",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)));
        return hms;


    }
}
