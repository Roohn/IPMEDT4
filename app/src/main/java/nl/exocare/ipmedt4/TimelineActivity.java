package nl.exocare.ipmedt4;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener {
    TimelineHandler timeline = null;
    ConstraintLayout timelineLayout;
    TextView beginDatum, eindDatum, controleDatum, revalidatieDatum;
    ProgressBar tijdlijn;
    private int currentNotificationID = 0;
    private EditText etMainNotificationText, etMainNotificationTitle;
    private Button btnMainSendNotificationActionBtn, btnMainSendMaxPriorityNotification;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private String notificationTitle;
    private String notificationText;
    private Bitmap icon;
    private int combinedNotificationCounter;
    private PendingIntent pendingIntent;

    FirebaseAuth firebaseAuth;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Intent intent2 = getIntent();
        mail = intent2.getStringExtra("mail");

        // method call to initialize the views
        initViews();

        //get controledatum van api
        getControleDatums getDatums = new getControleDatums(mail);
        getDatums.execute();

        AlarmManager alarms = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Receiver receiver = new Receiver();
        IntentFilter filter = new IntentFilter("ALARM_ACTION");
        registerReceiver(receiver, filter);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,19);
        calendar.set(Calendar.MINUTE,20);


        Intent intent = new Intent("ALARM_ACTION");
        intent.putExtra("param", "My scheduled action");
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, 0);
        Intent myIntent;
        PendingIntent pendingIntent;
        // I choose 3s after the launch of my application
        myIntent = new Intent(TimelineActivity.this,Receiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);
        alarms.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation) ;
    }


    private void bindWidgetWithAnEvent() {
        btnMainSendNotificationActionBtn.setOnClickListener((View.OnClickListener) this);
    }


    public void onClick(View v) {
        setNotificationData();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void sendNotification() {
        Intent notificationIntent = new Intent(this, TimelineActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

        currentNotificationID++;
        int notificationId = currentNotificationID;
        if (notificationId == Integer.MAX_VALUE - 1)
            notificationId = 0;

        notificationManager.notify(notificationId, notification);
    }

    private void setNotificationData() {
        notificationTitle = "RevalidatieOefeningen";
        notificationText = "Heb je je oefeningen al gedaan?";
    }


    private void setDataForNotificationWithActionButton() {


        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 11);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);

        Intent alarmIntent = new Intent(TimelineActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(TimelineActivity.this, 0, alarmIntent, 0);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText(notificationText);

        Intent answerIntent = new Intent(this, AnswerReceiveActivity.class);
        answerIntent.setAction("Yes");
        PendingIntent pendingIntentYes = PendingIntent.getActivity(this, 1, answerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.thumbs_up, "Yes", pendingIntentYes);

        answerIntent.setAction("No");
        PendingIntent pendingIntentNo = PendingIntent.getActivity(this, 1, answerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.thumbs_down, "No", pendingIntentNo);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

        sendNotification();
    }

    private void setDataForMaxPriorityNotification() {
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText(notificationText);

        sendNotification();
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

        //het goed zetten van de "in ... dagen"
        TextView beginDagen = (TextView) findViewById(R.id.beginDagen);
        TextView controleDagen = (TextView) findViewById(R.id.controleDagen);
        TextView revalidatieDagen = (TextView) findViewById(R.id.revalidatieDagen);
        TextView eindDagen = (TextView) findViewById(R.id.eindDagen);
        long totBegin = 0;
        long totControle = 0;
        long totRevalidatie = 0;
        long totEind = 0;

        try {
            totBegin = timeline.getTrajectduur(timeline.getCurrentTime(), timeline.getBeginDatum());
            totControle = timeline.getTrajectduur(timeline.getCurrentTime(), timeline.getControleDatum());
            totRevalidatie = timeline.getTrajectduur(timeline.getCurrentTime(), timeline.getRevalidatieDatum());
            totEind = timeline.getTrajectduur(timeline.getCurrentTime(), timeline.getEindDatum());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (totBegin > 0) {
            beginDagen.setText("in " + totBegin + " dagen");
        }
        if (totControle > 0) {
            controleDagen.setText("in " + totControle + " dagen");
        }
        if (totRevalidatie > 0) {
            revalidatieDagen.setText("in " + totRevalidatie + " dagen");
        }
        if (totEind > 0) {
            eindDagen.setText("in " + totEind + " dagen");
        }


        //het disablen of enabelen van de secties
        ImageView revalidatieIcon = (ImageView) findViewById(R.id.revalidatieIcon);
        ConstraintLayout revalidatielayout = (ConstraintLayout) findViewById(R.id.revalidatieLayout);
        ImageView eindIcon = (ImageView) findViewById(R.id.eindIcon);
        TextView eindText = (TextView) findViewById(R.id.eindText);

        Date nu = timeline.getCurrentTime();
        //als je voorbij de revalidatie bent
        if (nu.after(timeline.getRevalidatieDatum())) {
            eindIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundrounded_disabled));
            eindText.setTextColor(getResources().getColor(R.color.disabled));
            eindIcon.setEnabled(false);
        }
        //als je voorbij de controle bent
        else if (nu.after(timeline.getControleDatum())) {
            eindIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundrounded_disabled));
            eindText.setTextColor(getResources().getColor(R.color.disabled));
        }
        //als je voorbij gips krijgen bent
        else {
            revalidatieIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundrounded_disabled));
            eindIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundrounded_disabled));
            eindText.setTextColor(getResources().getColor(R.color.disabled));
            revalidatielayout.setOnClickListener(null);
        }

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
        //als revalidatie nog niet begonnen is dan doorsturen naar de countdown
        if(timeline.getRevalidatieDatum().getTime() > timeline.getCurrentTime().getTime()) {
            Intent intent = new Intent(this, RevalidatieControleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("timeline", timeline);

            intent.putExtras(bundle);
            startActivity(intent);
        }
        //anders doorsturen naar de revalidatie oefeningen
        else {
            Intent intent = new Intent(this, RevalidatieActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Controle redirect
     *
     * @param v
     */
    public void clickControle(View v){
        Intent intent = new Intent(this, ControleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("timeline", timeline);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Haal de juiste datums op vanuit de api
     */
    public class getControleDatums extends AsyncTask<URL, Void, ArrayList<TimelineHandler>> {
        /** URL om de modules te laden */
        private String API_URL = "http://project.ronaldtoldevelopment.nl/api/controles";
        public String LOG_TAG = LoginActivity.class.getSimpleName();

        //constructor
        public getControleDatums(String user) {
            this.API_URL = API_URL + "/" + user;
        }

        @Override
        protected ArrayList<TimelineHandler> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(API_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            Log.d(LOG_TAG, ""+jsonResponse);
            ArrayList<TimelineHandler> datums = extractDatesFromJson(jsonResponse);

            // Return the {@link Event} object as the result of the {@link ModulesAsyncTask}
            return datums;
        }

        /**
         * Update the screen with the given modules
         */
        @Override
        protected void onPostExecute(ArrayList<TimelineHandler> controle) {
            if (controle == null) {
                return;
            }

            //set datum
            timeline = controle.get(0);
            fillTimeline();
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Maak een modules arraylist aan met alle vakken erin
         */
        private ArrayList<TimelineHandler> extractDatesFromJson(String datesJSON) {
            try {
                JSONObject jsonResponseArray = new JSONObject(datesJSON);
                ArrayList<TimelineHandler> controles = new ArrayList<>();


                // If there are results in the features array
                if (jsonResponseArray.length() > 0) {
                    String tijd = jsonResponseArray.getString("tijd");
                    controles.add(new TimelineHandler(tijd));
                }
                return controles;

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the modules JSON results", e);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
