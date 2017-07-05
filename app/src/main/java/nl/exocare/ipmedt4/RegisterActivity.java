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

import java.sql.Time;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etMail;
    private EditText etPassword;
    private Button bRegister;
    private Button bLoginLink;
    private EditText etName;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        /*if(firebaseAuth.getCurrentUser() == null){
            //Als account al is ingelogd
            finish();
            startActivity(new Intent(getApplicationContext(), TimelineActivity.class));
        }*/

        progressDialog = new ProgressDialog(this);

        etName = (EditText) findViewById(R.id.etName);
        etMail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        bLoginLink = (Button) findViewById(R.id.bLoginLink);

        bRegister.setOnClickListener(this);
        bLoginLink.setOnClickListener(this);
    }

    private void registerUser(){
        String email = etMail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        final String name = etName.getText().toString().trim();
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
        if(password.length() <5)
        {
            Toast.makeText(this, "Een wachtwoord moet meer dan 5 karakters bevatten.", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Vergeet je naam niet!", Toast.LENGTH_SHORT).show();
        }

        //als wachtwoord en email zijn ingevuld start de progressbar

        progressDialog.setMessage("Gebruiker registreren...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //succesvol geregistreerd
                            Toast.makeText(RegisterActivity.this, "Succesvol geregistreerd " + name + "!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        }else{
                            Toast.makeText(RegisterActivity.this, "Registratie mislukt, probeer opnieuw.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if(view == bRegister){
            registerUser();
        }
        if(view == bLoginLink){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}