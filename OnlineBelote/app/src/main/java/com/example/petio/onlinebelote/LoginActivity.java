package com.example.petio.onlinebelote;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class LoginActivity extends AppCompatActivity {
    private String pass;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void register(View v){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);

    }

    public void logIn(View v) throws IOException, JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                textFrom(new String(bytes));
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                intent.putExtra("account_username", username.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Toast
                        .makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        String url = "https://ancient-headland-44863.herokuapp.com/users/login";
        username = ((EditText) findViewById(R.id.username))
                    .getText()
                    .toString();
        pass = ((EditText) findViewById(R.id.password))
                .getText()
                .toString();

        JSONObject jsonParams = new JSONObject();

        jsonParams.put("username", username);
        jsonParams.put("password",pass);

        StringEntity entity = new StringEntity(jsonParams.toString());
       client.post(this, url, entity, "application/json",
                responseHandler);

    }

    public boolean textFrom(String strr) {
        String str = new String(strr);
        JSONObject json  = null;
        String usernam = null;

        try {
            json = new JSONObject(str);
            usernam = json
                    .get("username")
                    .toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(usernam.equals(username.toString())) {
            return true;
        }else {
            return false;
        }
    }


}
