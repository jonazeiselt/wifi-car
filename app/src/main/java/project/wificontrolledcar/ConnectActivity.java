package project.wificontrolledcar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * ConnectActivity.java
 *
 * This is a class that represents the page for connecting to a server.
 *
 * @author Jonas Eiselt
 * @since 2016-05-20
 */
public class ConnectActivity extends AppCompatActivity
{
    private String ip_address;
    private int tcp_port;

    private String username;
    private String password;

    private TextView userTV;
    private EditText ipaddressET;
    private EditText portET;

    private Socket socket;

    private DatabaseHandler db;
    private CheckBox rememberMeCB;
    private AndroidUser thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        // Gets username from previous activity
        Intent thisIntent = getIntent();
        username = thisIntent.getStringExtra("username");
        password = thisIntent.getStringExtra("password");

        Log.d("username", username);
        Log.d("password", password);

        db = new DatabaseHandler(this);
        thisUser = db.getAndroidUser(username);

        userTV = (TextView) findViewById(R.id.userTV);
        userTV.setText("Hi " + thisUser.getUserName() + "!");

        rememberMeCB = (CheckBox) findViewById(R.id.rememberMeCB);
        ipaddressET = (EditText) findViewById(R.id.ipaddressET);
        portET = (EditText) findViewById(R.id.portET);

        // If user has saved IP-address at previous session
        if(thisUser.getIpAddress() != null)
        {
            ipaddressET.setText(thisUser.getIpAddress());
            portET.setText(thisUser.getTcpPort() + "");

            rememberMeCB.setChecked(true);
        }
    }

    /**
     * This is a public method that redirects user to the main page for controlling Arduino-car if
     * entered IP-address and port number was entered correctly, and if the server is running.
     * @param view button for connecting
     */
    public void connect(View view)
    {
        if (ipaddressET.getText().toString().equals("") || portET.getText().toString().equals(""))
        {
            Toast.makeText(ConnectActivity.this, "Oops, you forgot to enter IP-address or port number", Toast.LENGTH_SHORT).show();
        }
        else if (rememberMeCB.isChecked())
        {
            ip_address = ipaddressET.getText().toString().trim();
            tcp_port = Integer.valueOf(portET.getText().toString().trim());

            if (!(ip_address.equals(thisUser.getIpAddress())) || !(tcp_port == thisUser.getTcpPort()))
            {
                thisUser.setIpAddress(ip_address);
                thisUser.setTcpPort(tcp_port);

                db.updateAndroidUser(thisUser);
            }
            new MyTask().execute();
        }
        else
        {
            ip_address = ipaddressET.getText().toString().trim();
            tcp_port = Integer.valueOf(portET.getText().toString().trim());

            new MyTask().execute();
        }
    }

    /**
     * This is a private class used for connecting to a server.
     */
    private class MyTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                InetAddress addr = InetAddress.getByName(ip_address);
                SocketAddress sockaddr = new InetSocketAddress(addr, tcp_port);

                socket = new Socket();

                // Exception is thrown if timeout occurs..
                socket.connect(sockaddr, 4000);

                return "success";
            }
            catch (UnknownHostException e) {
                return "reach error";
            }
            catch (IOException e) {
                return "reach error";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result.equals("reach error"))
            {
                Toast.makeText(ConnectActivity.this, "Oops, make sure server is running and your Wifi is on", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SocketHandler.setSocket(socket);

                Toast.makeText(ConnectActivity.this, "Yay, connection was successful", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ConnectActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }
    }
}