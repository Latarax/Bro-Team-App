package android.grouper.prot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login_main.*

class LoginMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)

        // define the username string

        // define the password string

        // onclick event handler for sign in
        // will bundle? username and password strings to check against database
        // if account login is valid, will generate intent to go to mainActivity
        // if account sign in is not valid, will use loginErrorText bar to indicate failure
        loginButton.setOnClickListener() {

        }

        // onclick event handler for create account
        // will generate intent to go strait to createAccountActivity
        loginButton.setOnClickListener() {

        }
    }
}
