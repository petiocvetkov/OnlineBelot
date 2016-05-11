package com.example.petio.onlinebelote;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class AccountActivity extends AppCompatActivity {
    private ArrayList<String> rooms = new ArrayList<>();
    ArrayAdapter<String> roomsAdapter;

    private HashMap<String,JSONObject> roomsMap = new HashMap<>();
    private ListView roomsList;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             username = extras.getString("account_username");
        }

        roomsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,rooms);
        roomsList = (ListView) findViewById(R.id.listRooms);


        roomsList.setAdapter(roomsAdapter);
        roomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    for (int ctr=0;ctr<=rooms.size();ctr++){
                        if(i==ctr){
                            roomsList.getChildAt(ctr).setBackgroundColor(Color.CYAN);
                        }else{
                            roomsList.getChildAt(ctr).setBackgroundColor(Color.WHITE);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    public void refreshRooms(View view){
        String url = "https://boiling-escarpment-23088.herokuapp.com/rooms/get";
        AsyncHttpClient client = new AsyncHttpClient();
        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                formatJson(new String(bytes));
            }


            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast
                        .makeText(getApplicationContext(), "No found rooms", Toast.LENGTH_SHORT)
                        .show();
            }
        };



        client.get(this, url, responseHandler);
    }
    public void formatJson(String json){
        JSONArray jsonArray = null;
        rooms.clear();
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                String name = jsonobject.getString("name");
                String id = jsonobject.getString("_id");
                rooms.add(name);
                roomsAdapter.notifyDataSetChanged();
                roomsMap.put(name, jsonobject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public void displayRooms(ArrayList<String> visited,ArrayAdapter<String> adapter) {
        adapter.clear();
        adapter.addAll(visited);
        adapter.notifyDataSetChanged();
    }

}
