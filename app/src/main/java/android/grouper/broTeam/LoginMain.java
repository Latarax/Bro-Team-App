package android.grouper.broTeam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static java.security.AccessController.getContext;

public class LoginMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        //*****************************************
        // this is the code for the log in button *
        //*****************************************
        final Button logButton = findViewById(R.id.loginButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                // this is where the code for logging in goes

                // presumably bundle and send data to fire base

                // presumably wait for firebase to confirm

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
}
