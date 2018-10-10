package project.wificontrolledcar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * SignupActivity.java
 *
 * This class is used for creating AndroidUser objects and adding them to the local database. The
 * class contains logic which, for example, prevents the user to enter a user name that already
 * exists or enter passwords that do not match each other.
 *
 * @author Jonas Eiselt
 * @since 2016-05-21
 */
public class SignupActivity extends AppCompatActivity
{
    private DatabaseHandler db;

    private EditText usernameET;
    private EditText passwordET;
    private EditText passAgainET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        passAgainET = (EditText) findViewById(R.id.passAgainET);

        db = new DatabaseHandler(this);
    }

    /**
     * This is a public method that creates an AndroidUser object if the information entered is
     * valid and that such object does not exist in the database.
     * @param view button for creating user
     */
    public void createUser(View view)
    {
        String enteredUserName = usernameET.getText().toString().trim();
        String enteredPassword = passwordET.getText().toString().trim();
        String enteredAgainPass = passAgainET.getText().toString().trim();

        if(enteredUserName.equals("") || enteredPassword.equals(""))
        {
            Toast.makeText(SignupActivity.this, "User name or password have not been entered", Toast.LENGTH_SHORT).show();
        }
        else if(enteredPassword.equals(enteredAgainPass))
        {
            if(!db.androidUserExists(enteredUserName, null))
            {
                AndroidUser androidUser = new AndroidUser();
                androidUser.setId(db.getNextID());
                androidUser.setUserName(enteredUserName);
                androidUser.setPassword(enteredPassword);
                androidUser.setClientName(null);
                androidUser.setClientPass(null);
                androidUser.setIpAddress(null);
                androidUser.setTcpPort(-99);

                db.addAndroidUser(androidUser);

                Toast.makeText(SignupActivity.this, "Yay, user created..", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, LoginActivity.class);

                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(SignupActivity.this, "User name does already exist", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(SignupActivity.this, "The entered passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This is a public method that redirects user to the login page when cancel button is pressed.
     * @param view button for canceling sign up
     */
    public void cancel(View view)
    {
        Intent i = new Intent(this, LoginActivity.class);

        startActivity(i);
        finish();
    }
}