package project.wificontrolledcar;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * LoginActivity.java
 *
 * This is a class that represents the login page. It contains methods for redirecting user to
 * sign up page and the page for connecting to server. Redirection to ConnectActivity will not be
 * made if user does not exist.
 *
 * @author Jonas Eiselt
 * @since 2016-05-20
 */
public class LoginActivity extends AppCompatActivity
{
    private EditText usernameET;
    private EditText passwordET;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);

        db = new DatabaseHandler(this);
        // db.deleteAll();
    }

    /**
     * This is a public method that redirects user to page for connecting to server if all login
     * details entered were valid.
     * @param view button for logging in
     */
    public void login(View view)
    {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if(db.androidUserExists(username, password))
        {
            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, ConnectActivity.class);

            // Send username and password to next activity
            i.putExtra("username", username);
            i.putExtra("password", password);

            startActivity(i);
            finish();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This is a public method that redirects user to the sign up page.
     * @param view button for creating user
     */
    public void signup(View view)
    {
        Intent i = new Intent(this, SignupActivity.class);

        startActivity(i);
        finish();
    }
}
