package com.example.petio.onlinebelote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity {
    String host = "ws://ancient-headland-44863.herokuapp.com";
    int port = 3000;
    JSONObject joinedPlayer = new JSONObject();

    String[] teamOne;

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

        Thread thread = new Thread() {
            @Override
            public void run() {
                try{
                    Socket server = new Socket(host,port);
                    Scanner serverScanner = new Scanner(server.getInputStream());
                    PrintWriter outWriter =
                            new PrintWriter(server.getOutputStream());


                    String input;
                    outWriter.write(joinedPlayer.toString());
                    //while ((input = serverScanner.nextLine()) != null){

                    //      formatJsonFromServer(input);
                    //}


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
        //startGame();

    }

    public void startGame(){
        try{
            Socket server = new Socket(host,port);
            Scanner serverScanner = new Scanner(server.getInputStream());
            PrintWriter outWriter =
                    new PrintWriter(server.getOutputStream());


            String input;
            outWriter.write(joinedPlayer.toString());
            //while ((input = serverScanner.nextLine()) != null){

              //      formatJsonFromServer(input);
            //}


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void formatJsonFromServer(String input){
        JSONArray cardsInHand ;
        try {
            cardsInHand = new JSONArray(input);
            if(cardsInHand.length() == 8){
                cardsInHand(cardsInHand);
                return ;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cardsInHand(JSONArray cards){
        for(int i = 0 ; i < cards.length() ; i++){

        }
    }
}
