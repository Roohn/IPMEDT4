package nl.exocare.ipmedt4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AfsprakenActivity extends AppCompatActivity {
    String URL_POST = "http://project.ronaldtoldevelopment.nl/api/insertControle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afspraken);
    }

    public void saveAfspraak(View v) {
        EditText inputDate = (EditText) findViewById(R.id.input_date);
        EditText inputTime = (EditText) findViewById(R.id.input_time);
        EditText inputDokter = (EditText) findViewById(R.id.input_dokter);
        EditText inputLocatie = (EditText) findViewById(R.id.input_locatie);
        Intent fromIntent = getIntent();
        String userName = fromIntent.getStringExtra("user");
        String email = fromIntent.getStringExtra("email");

        String regEx ="^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";
        Matcher matcherObj = Pattern.compile(regEx).matcher(inputDate.getText().toString());
        Log.d("datumstring: ", ""+inputDate.getText().toString());
        if (matcherObj.matches()) {
            opslaan(inputDate.getText().toString(), inputTime.getText().toString(), inputDokter.getText().toString(), inputLocatie.getText().toString(), userName, email);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else { //verkeerde datum
            Toast.makeText(getApplication(), "Dit is geen datum!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * registeren van een controle afspraak bij juiste user (id)
     * @param date
     * @param time
     * @param dokter
     * @param locatie
     */
    public void opslaan(final String date, final String time, final String dokter, final String locatie, final String username, final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplication(), "Controle opgeslagen!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AfsprakenActivity.this, error+"", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", username);
                params.put("email", email);
                params.put("dokter", dokter);
                params.put("tijd", date);
                params.put("locatie", locatie);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
