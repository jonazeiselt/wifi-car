package project.wificontrolledcar;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

/**
 * WelcomeActivity.java
 *
 * This is a class which purpose is to display a welcome page and then redirect user to the
 * login page (LoginActivity.java).
 *
 * @author Jonas Eiselt
 * @since 2016-05-19
 */
public class WelcomeActivity extends Activity
{
    private final int TIMER = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // When timer is over this will be executed..
                Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(i);

                finish();
            }
        }, TIMER);
    }
}