package project.wificontrolledcar;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * MainActivity.java
 *
 * This is the class for controlling the Arduino-car by sending data to server.
 *
 * @author Jonas Eiselt
 * @since 2016-05-19
 */
public class MainActivity extends AppCompatActivity
{
    private String username = "driver";
    private String password = "pass";

    private boolean readyToPlay = false;
    private boolean passwordSent = false;

    private TextView velocityTV;
    private WifiControlledCar wifiCar;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiCar = new WifiControlledCar();
        new Thread(new MyThread()).start();

        velocityTV = (TextView) findViewById(R.id.velocityTV);
    }

    /**
     * This is a public method that gets called when button forward is clicked. Speed-values
     * will not be sent if previous speed is the same as the current one -> less network traffic.
     * @param view button for accelerating
     */
    public void forward(View view)
    {
        if(readyToPlay)
        {
            int oldF = wifiCar.getForwardSpeed();
            int oldB = wifiCar.getBackwardsSpeed();

            wifiCar.accelerate();
            if((oldF != wifiCar.getForwardSpeed()) || (oldB != wifiCar.getBackwardsSpeed()))
            {

                velocityTV.setText(wifiCar.getPercentage() + "%");
                new MyTask().execute(wifiCar.getSpeedValues());
            }
        }
        else
        {
            showMessage("Waiting for Arduino..");
        }
    }

    /**
     * This is a public method that gets called when button back is clicked. Speed-values
     * will not be sent if previous speed is the same as the current one -> less network traffic.
     * @param view button for retarding
     */
    public void backwards(View view)
    {
        if(readyToPlay == true)
        {
            int oldF = wifiCar.getForwardSpeed();
            int oldB = wifiCar.getBackwardsSpeed();

            wifiCar.retard();
            if((oldF != wifiCar.getForwardSpeed()) || (oldB != wifiCar.getBackwardsSpeed()))
            {

                velocityTV.setText(wifiCar.getPercentage() + "%");
                new MyTask().execute(wifiCar.getSpeedValues());
            }
        }
        else
        {
            showMessage("Waiting for Arduino..");
        }
    }

    /**
     * This is a public method that gets called when button left is clicked. Speed-values
     * will not be sent if previous click was button left -> less network traffic.
     * @param view button for turning left
     */
    public void left(View view)
    {
        if(readyToPlay == true)
        {
            int oldL = wifiCar.getLeft();
            int oldR = wifiCar.getRight();

            wifiCar.turnLeft();
            if((oldL != wifiCar.getLeft()) || (oldR != wifiCar.getRight()))
            {

                velocityTV.setText(wifiCar.getPercentage() + "%");
                new MyTask().execute(wifiCar.getSpeedValues());
            }
        }
        else
        {
            showMessage("Waiting for Arduino..");
        }
    }

    /**
     * This is a public method that gets called when button right is clicked. Speed-values
     * will not be sent if previous click was button right -> less network traffic.
     * @param view button for turning right
     */
    public void right(View view)
    {
        if(readyToPlay == true)
        {
            int oldL = wifiCar.getLeft();
            int oldR = wifiCar.getRight();

            wifiCar.turnRight();
            if((oldL != wifiCar.getLeft()) || (oldR != wifiCar.getRight()))
            {

                velocityTV.setText(wifiCar.getPercentage() + "%");
                new MyTask().execute(wifiCar.getSpeedValues());
            }
        }
        else
        {
            showMessage("Waiting for Arduino..");
        }
    }

    /**
     * This is a public method that gets called when button brake is clicked. Speed-values
     * will only be sent if speed forward or speed backwards is greater than 0 -> less network
     * traffic.
     * @param view button for braking
     */
    public void stop(View view)
    {
        if(readyToPlay)
        {
            int oldF = wifiCar.getForwardSpeed();
            int oldB = wifiCar.getBackwardsSpeed();

            wifiCar.brake();
            if((oldF != wifiCar.getForwardSpeed()) || (oldB != wifiCar.getBackwardsSpeed()))
            {
                velocityTV.setText(wifiCar.getPercentage() + "%");
                new MyTask().execute(wifiCar.getSpeedValues());
            }
        }
        else
        {
            showMessage("Waiting for Arduino..");
        }
    }

    /**
     * This is a private class used for sending out data to server.
     */
    private class MyTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            if(readyToPlay)
            {
                Log.d("Output", params[0]);

                out.println(params[0]);
                out.flush();
            }
            return null;
        }
    }

    /**
     * This is a private class that listens for incoming data from server.
     */
    private class MyThread implements Runnable
    {
        @Override
        public synchronized void run()
        {
            String input;
            try
            {
                socket = SocketHandler.getSocket();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while(true)
            {
                try
                {
                    input = in.readLine();
                    if (input != null)
                    {
                        Log.d("Input", input);
                        if (input.equals("username:password"))
                        {
                            out.println("name:" + username + ":" + password);
                            out.flush();

                            passwordSent = true;
                            Log.d("Sent", "Login details");
                        }
                        else if(input.equals("true") && passwordSent)
                        {
                            Log.d("Debug", "Driver connected");
                            showMessage("Ready to drive..");
                            readyToPlay = true;
                        }
                        else if (input.equals("exit"))
                        {
                            System.exit(0);
                        }
                    }
                } catch (IOException e) {
                    showMessage("No connection to server..");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This is a private method that is for displaying a Toast (notification).
     * @param msg to be shown
     */
    private void showMessage(final String msg)
    {
        new Thread()
        {
            public void run()
            {
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }
}