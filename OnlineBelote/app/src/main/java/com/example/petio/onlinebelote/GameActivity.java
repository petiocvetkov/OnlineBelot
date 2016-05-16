package com.example.petio.onlinebelote;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class GameActivity extends AppCompatActivity {
    String host = "ws://ancient-headland-44863.herokuapp.com";
    int port = 3000;
    JSONObject joinedPlayer = new JSONObject();
    JSONArray cardsInHands;
    String cardsString[] = new String[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("room_id");
        String username = extras.getString("player_name");

        try {
            joinedPlayer.put("playerName",username);
            joinedPlayer.put("roomId", id.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        URI uri;
        try {
            uri = new URI(host);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        WebSocketClient mWebSocketClient;
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                this.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(final String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("json seks",s.toString());
                        formatJsonFromServer(s);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
        mWebSocketClient.send(joinedPlayer.toString());




    }




    private void formatJsonFromServer(String cardsInHand){
        try {
            JSONArray cards = new JSONArray(cardsInHand);
            if(cards.length() == 8){
                cardsInHands = new JSONArray(cards);
                cardsInHand(new JSONArray(cardsInHand));
                return ;
            }else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cardsInHand(JSONArray cards){
        for(int i = 0 ; i < cards.length() ; i++){
            try {
                JSONObject card = new JSONObject((String) cards.get(i));
                cardsString[i] = card.getString("card") + card.get("paint");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Activity getActivity() {
        return this;
    }
}
