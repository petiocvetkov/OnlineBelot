package com.example.petio.onlinebelote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Bundle extras = getIntent().getExtras();
        String value;
        if (extras != null) {
             value = extras.getString("account_username");
        }
        refreshRooms();


    }
    public void refreshRooms(){
        String url = "https://boiling-escarpment-23088.herokuapp.com/rooms/get";
        AsyncHttpClient client = new AsyncHttpClient();
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                formatJson(new String(bytes));
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                String str = new String(bytes);
                Toast
                        .makeText(getApplicationContext(), "No found rooms", Toast.LENGTH_SHORT)
                        .show();
            }
        };



        client.get(this,url,responseHandler);
    }
    public void formatJson(String json){
        JSONObject jsonFormated  = null;
        String usernam = null;
        String[] arr = json.split(",");
        try {
            jsonFormated = new JSONObject(json);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    int i = 100;
        i++;
    }
}
