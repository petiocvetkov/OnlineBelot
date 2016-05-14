package com.example.petio.onlinebelote;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.emitter.Emitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Scanner;
import com.github.nkzawa.socketio.client.Socket;


public class GameActivity extends AppCompatActivity {
    String host = "ws://ancient-headland-44863.herokuapp.com";
    int port = 3000;
    JSONObject joinedPlayer = new JSONObject();

    String[] teamOne;

    private Emitter.Listener onNewMessage = new Emitter.Listener() {


        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    formatJsonFromServer(data);

                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("room_id");
        String username = extras.getString("player_name");

        try {
            joinedPlayer.put("playerName",username);
            joinedPlayer.put("roomId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Socket socket;

        try {
            socket = IO.socket(host);
            socket.emit(joinedPlayer.toString());

            while(socket.on("",onNewMessage) != null){
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        //startGame();

    }



    private void formatJsonFromServer(JSONObject cardsInHand){
        try {
            if(cardsInHand.length() == 8){
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

        }
    }

    public Activity getActivity() {
        return this;
    }
}
