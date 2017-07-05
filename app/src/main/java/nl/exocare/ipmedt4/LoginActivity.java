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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etMail;
    private EditText etPassword;
    private Button bLogin;
    private Button bRegisterLink;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private TextView tvForgottenPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();

           /*if(firebaseAuth.getCurrentUser() != null){
                //Als account al is ingelogd
                finish();
                startActivity(new Intent(getApplicationContext(), TimelineActivity.class));
            }*/

        tvForgottenPass = (TextView) findViewById(R.id.tvForgottenPass);

        etMail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bRegisterLink = (Button) findViewById(R.id.bRegisterLink);
        progressDialog = new ProgressDialog(this);

        bLogin.setOnClickListener(this);
        bRegisterLink.setOnClickListener(this);
        tvForgottenPass.setOnClickListener(this);
    }
    public void init() {
                Intent intent1 = new Intent(LoginActivity.this, TimelineActivity.class);
                startActivity(intent1);
    }
    public void init2() {
        Toast.makeText(this, "Verkeerde combinatie email en wachtwoord", Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                    if(task.isSuccessful()){
                        //Scherm tonen na inloggen
                        finish();
                        init();
                    }else{
                        init2();
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
        if(view ==tvForgottenPass){
            //wachtwoord vergeten scherm hier
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }
    }
}
