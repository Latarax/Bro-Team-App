package android.grouper.broTeam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



/*
 ########################################################################
 # This is the create account screen, it branches from the login screen #
 ########################################################################
 */


public class CreateNewAccount extends AppCompatActivity {

    EditText emailField, passwordField, usernameField;
    Button submitAccountBtn, haveAccountBtn;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account);

        emailField = findViewById(R.id.newEmailEntry);
        passwordField = findViewById(R.id.newPasswordEntry);
        progressBar = findViewById(R.id.progressBar);
        usernameField = findViewById(R.id.newUsernameEntry);

        mAuth = FirebaseAuth.getInstance();

        Button submitAccountBtn = findViewById(R.id.submitAccountButton);
        submitAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        Button haveAccountBtn = findViewById(R.id.haveAccountButton);
        haveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                // this is the code for going to create account page
                Intent returnToLogin = new Intent(view2.getContext(), LoginMain.class);
                view2.getContext().startActivity(returnToLogin);
            }
        });
    }


    private void createAccount() {
        final String email = emailField.getText().toString().trim();
        final String password = passwordField.getText().toString().trim();
        final String username = usernameField.getText().toString().trim();

        if (email.isEmpty()) {
            emailField.setError("Must enter an email address");
            emailField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordField.setError("Must enter a password");
            passwordField.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            passwordField.setError("Must enter a password");
            passwordField.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("Email", email);
                    userData.put("Username", username);
                    userData.put("groupList", Arrays.asList());
                    userData.put("groupInvites", Arrays.asList());
                    db.collection("usersList").document(user.getUid())
                            .set(userData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Status", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Status", "Error writing document", e);
                                }
                            });
                    Toast.makeText(getApplicationContext(), "User Registered Successful", Toast.LENGTH_SHORT).show();
                    Intent goToHome = new Intent(CreateNewAccount.this, HomeGroupList.class);
                    goToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goToHome);
                    if (user != null){
                        /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();
                        user.updateProfile(profileUpdates);
                        */
                        //setUsername();

                        ;


                    }
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
        });
    }
    /*
    private void setUsername() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(usernameField.getText().toString().trim())
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TESTING", "User profile updated.");
                            }
                        }
                    });
        }
    };
    */

}
