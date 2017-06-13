package nl.exocare.ipmedt4;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class RevalidatieControleActivity extends AppCompatActivity {
    private long timeCountInMilliSecondsControle = 1 * 60000;
    TextView datumGipsBehandeling, textViewTime;
    ProgressBar progressBarCircle;
    TimelineHandler timeline = null;
    private CountDownTimer countDownTimer2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revalidatiecontrole);

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
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle_revalidatie);
        textViewTime = (TextView) findViewById(R.id.textViewTime_revalidatie);
    }

    /**
     * het goedzetten van de datums op de behandelingspagina
     */
    public void fillBehandelingPagina() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        datumGipsBehandeling.setText(df.format(timeline.getRevalidatieDatum()));

        //start timers
        startCountDownTimer();
    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues() {
        int timeControle = 0;

        //uit de timelinehandler halen
        try {
            timeControle = (int) timeline.getTrajectduur(timeline.getCurrentTime(), timeline.getRevalidatieDatum());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // assigning values after converting to milliseconds
        timeCountInMilliSecondsControle = TimeUnit.DAYS.toMillis(timeControle);
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {
        //haal de goede tijd op
        setTimerValues();

        countDownTimer2 = new CountDownTimer(timeCountInMilliSecondsControle, 1000) {
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
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return DD:HH:mm time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02dd\n%02du\n%02dm",
                TimeUnit.MILLISECONDS.toDays(milliSeconds),
                TimeUnit.MILLISECONDS.toHours(milliSeconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliSeconds)),
                TimeUnit.MICROSECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MICROSECONDS.toHours(milliSeconds)));
        return hms;
    }
}
