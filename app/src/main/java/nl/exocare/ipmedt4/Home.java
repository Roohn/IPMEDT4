package nl.exocare.ipmedt4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Home extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private TextView tvName;
    private Button bLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
 /*if(firebaseAuth.getCurrentUser() != null){
            //Als account nog niet is ingelogd
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }*/
        FirebaseUser user = firebaseAuth.getCurrentUser();

        tvName = (TextView) findViewById(R.id.tvName);

        tvName.setText("Welkom " + user.getEmail());
        bLogOut = (Button) findViewById(R.id.bLogOut);

        bLogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}
