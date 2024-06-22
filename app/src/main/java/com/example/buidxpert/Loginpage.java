package com.example.buidxpert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loginpage extends AppCompatActivity {

    private TextInputEditText email, loginPassword;
    private Button loginButton;
    private TextView registerRedirect;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        if (mAuth.getCurrentUser() != null) {
            navigateToHomepage();
            return; // Exit the onCreate method to avoid showing the login screen
        }
        setContentView(R.layout.activity_loginpage);

        mAuth = FirebaseAuth.getInstance();
        initializeUI();

        registerRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToRegister();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void initializeUI() {
        email = findViewById(R.id.email);
        loginPassword = findViewById(R.id.passwordinput);
        registerRedirect = findViewById(R.id.sinupredirecttext);
        loginButton = findViewById(R.id.loginbutton);
        progressBar = findViewById(R.id.progressbar);
    }

    private void navigateToHomepage() {
        Intent intent = new Intent(Loginpage.this, Homepage.class);
        startActivity(intent);
        finish(); // Finish the login activity to prevent returning to it when pressing back
    }

    private void navigateToRegister() {
        Intent intent = new Intent(Loginpage.this, Registerpage.class);
        startActivity(intent);
    }

    private void loginUser() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        String emailText = email.getText().toString().trim();
        String passwordText = loginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailText)) {
            showError("Enter Email");
            return;
        }

        if (TextUtils.isEmpty(passwordText)) {
            showError("Enter Password");
            return;
        }

        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            navigateToHomepage();
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed.";
                            Toast.makeText(Loginpage.this, errorMessage, Toast.LENGTH_SHORT).show();
                            Log.e("Loginpage", "Firebase Authentication failed: " + errorMessage);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Toast.makeText(Loginpage.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Loginpage", "Exception: " + e.getMessage());
                    }
                });
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        Toast.makeText(Loginpage.this, message, Toast.LENGTH_SHORT).show();
    }
}
