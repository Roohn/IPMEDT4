package nl.exocare.ipmedt4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etMail;
    private EditText etPassword;
    private Button bLogin;
    private Button bRegisterLink;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();
          /*  if(firebaseAuth.getCurrentUser() != null){
                //Als account al is ingelogd
                finish();
                startActivity(new Intent(getApplicationContext(), Home.class));
            }*/

        etMail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bRegisterLink = (Button) findViewById(R.id.bRegisterLink);
        progressDialog = new ProgressDialog(this);

        bLogin.setOnClickListener(this);
        bRegisterLink.setOnClickListener(this);
    }
    public void init() {
                Intent intent1 = new Intent(LoginActivity.this, TimelineActivity.class);
                startActivity(intent1);
    }

    private void userLogin(){
        String email = etMail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        //benodigdheden voor inloggen
        if(TextUtils.isEmpty(email)){
            //als email leeg is
            Toast.makeText(this, "Voer een geldig email adres in", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //als password leeg is
            Toast.makeText(this, "Voer een geldig wachtwoord in.", Toast.LENGTH_SHORT).show();
            return;
        }
        //als wachtwoord en email zijn ingevuld start de progressbar

        progressDialog.setMessage("Gebruiker inloggen...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.hide();
                    if(task.isSuccessful()){
                        //Scherm tonen na inloggen
                        finish();
                        init();
                    }


            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == bRegisterLink){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if(view == bLogin){
            userLogin();
        }
    }
}
