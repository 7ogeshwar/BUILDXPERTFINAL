package com.example.buidxpert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registerpage extends AppCompatActivity {

    private static final String TAG = "Registerpage";

    private TextInputEditText email, password;
    private TextView loginRedirectText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            navigateToHomepage();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        if (mAuth.getCurrentUser() != null) {
            navigateToHomepage();
            return; // Exit the onCreate method to avoid showing the login screen
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registerpage);

        mAuth = FirebaseAuth.getInstance();
        initializeUI();

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void initializeUI() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerbutton);
        loginRedirectText = findViewById(R.id.loginredirecttext);
        progressBar = findViewById(R.id.progressbar);
    }

    private void navigateToHomepage() {
        Intent intent = new Intent(getApplicationContext(), Homepage.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(Registerpage.this, Loginpage.class);
        startActivity(intent);
        finish();
    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailText)) {
            showError("Enter Email");
            return;
        }

        if (TextUtils.isEmpty(passwordText)) {
            showError("Enter Password");
            return;
        }

        if (passwordText.length() < 6) {
            showError("Password must be at least 6 characters long");
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        registerButton.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(Registerpage.this, "Account created", Toast.LENGTH_SHORT).show();
                            try {
                              navigateToLogin();
                            } catch (Exception e) {
                                Log.e(TAG, "Error starting Loginpage activity", e);
                                showError("Failed to navigate to login page");
                            }
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed.";
                            Toast.makeText(Registerpage.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        registerButton.setEnabled(true);
        Toast.makeText(Registerpage.this, message, Toast.LENGTH_SHORT).show();
    }
}
