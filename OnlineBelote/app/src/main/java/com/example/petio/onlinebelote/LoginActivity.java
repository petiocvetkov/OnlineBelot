package com.example.petio.onlinebelote;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText pass;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void logIn(View v) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = new URL("https://boiling-escarpment-23088.herokuapp.com/users/login");
        HttpURLConnection con = (HttpURLConnection) (url).openConnection();

        con.setRequestMethod("POST");

        con.setDoInput(true);

        con.setDoOutput(true);

        con.connect();
        JSONObject user = new JSONObject();

        username = (EditText) findViewById(R.id.et_Username);
        pass = (EditText) findViewById(R.id.et_Password);

        user.put("username", username.toString());
        user.put("password", pass.toString());
        Toast.makeText(LoginActivity.this, user.toString(), Toast.LENGTH_SHORT).show();

        con.getOutputStream().write((user).toString().getBytes());

        StringBuilder sb = new StringBuilder();

        byte[] b = new byte[1024];

        InputStream is = con.getInputStream();
        StringBuilder buffer = new StringBuilder();
        while (is.read(b) != -1)
            buffer.append(new String(b));

        con.disconnect();


    }


}
