package com.sypcio.smarthouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.pass);
        loginButton = findViewById(R.id.loginbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarLogin);

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email = loginEmail.getText().toString();
            final String password = loginPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(email)) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            //authenticate user
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),Toast.LENGTH_SHORT).show(); // there was an error
                            if (password.length() < 6) {
                                progressBar.setVisibility(View.INVISIBLE);
                                loginPassword.setError(getString(R.string.minimum_password));
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
