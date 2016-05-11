package com.example.petio.onlinebelote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisterActivity extends AppCompatActivity {
    private String username;
    private String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View v) throws IOException, JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes);
                if (str.toUpperCase() == "HELLO") {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }

                try {
                    Toast
                            .makeText(getApplicationContext(), new JSONObject(new String(bytes)).getString("reason"), Toast.LENGTH_SHORT)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String url = "https://ancient-headland-44863.herokuapp.com/users/register";
        username = ((EditText) findViewById(R.id.username))
                .getText()
                .toString();
        pass = ((EditText) findViewById(R.id.password))
                .getText()
                .toString();

        JSONObject jsonParams = new JSONObject();

        jsonParams.put("username", username);
        jsonParams.put("password", pass);

        StringEntity entity = new StringEntity(jsonParams.toString());
        client.post(this, url, entity, "application/json",
                responseHandler);

    }

}
