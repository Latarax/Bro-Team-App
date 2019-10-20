package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

/*
 ############################################################
 # This is the first activity to load, it is a login screen #
 ############################################################
 */

public class LoginMain extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText usernameField, passwordField;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        mAuth = FirebaseAuth.getInstance();

        usernameField = findViewById(R.id.usernameInput);
        passwordField = findViewById(R.id.passwordInput);
        progressBar = findViewById(R.id.progressBar);

        //*****************************************
        // this is the code for the log in button *
        //*****************************************
        final Button logButton = findViewById(R.id.loginButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                userLogin();

                // If valid account, create intent to go to user main page

                // if fail change dialog box to read log in error
            }
        });

        //*************************************************
        // This is the code for the create account button *
        //*************************************************
        final Button createAccButton = findViewById(R.id.createAccountButton);
        createAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                // this is the code for going to create account page
                Intent gotoNewAccount = new Intent(view2.getContext(), CreateNewAccount.class);
                view2.getContext().startActivity(gotoNewAccount);
            }
        });
    }

    private void userLogin() {
        String email = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty()) {
            usernameField.setError("Must enter an email address");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordField.setError("Must enter a password");
            passwordField.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent goToHome = new Intent(LoginMain.this, HomeGroupList.class);
                    goToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goToHome);
                }

                else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "This email is already in use", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            }
        );}
}
