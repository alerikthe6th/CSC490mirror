package edu.augustana.csc490.androidbrainapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Locale;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*
 *
 * This program translates speech to text using *ONLINE* conection to Google's speech to text.
 * Basis of the progarm came from here: http://www.androidhive.info/2014/07/android-speech-to-text-tutorial/
 *
 *
 * TODO: revamp to send strings, and create a dictionary of accepted voice commands
 *
 * Created by Alerik Vi on 1/15/2017.
 */

public class VoiceActivity extends AppCompatActivity {

    //data fields and buttons
    private TextView tvSpeechInput;
    private TextView tvCommand;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100; //required to send voice strings in intents

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_activity);

        tvSpeechInput = (TextView) findViewById(R.id.tvSpeechInput);
        tvCommand = (TextView) findViewById(R.id.tvCommand);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


    }

    /**
     * Showing google speech input dialog
     **/
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "It didn't work", Toast.LENGTH_LONG);
        }
    }

    /*
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    tvSpeechInput.setText("String sent: " + result.get(0));

                    //TODO: currently testing with one direction, need to test the rest
                    /**
                     * if the voice input receives a correct movement command, the command is sent to the robot via sendMessage using the socket object
                     * the string for a successful voice command is also appropriately changed
                     *
                     * incorrect voice commands will not terminate a successful previously called action.
                     *
                     * saying "stop" should halt the robot of movement
                     */
                    if(result.get(0).equalsIgnoreCase("move")) {
                        try {
                            Log.d("inside move case", "inside move case was successful");
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnection.sendMessage("m");
                            Log.d("sent move command", "move forward command successful sent");
                        } catch(Exception e){
                            Toast.makeText(getApplicationContext() , "connection successful", Toast.LENGTH_LONG);
                        }
                    }

                    if(result.get(0).equalsIgnoreCase("back")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnection.sendMessage("b");
                        } catch(Exception e){
                            Toast.makeText(getApplicationContext() , "connection successful", Toast.LENGTH_LONG);
                        }
                    }

                    if(result.get(0).equalsIgnoreCase("left")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnection.sendMessage("l");
                        } catch(Exception e){
                            Toast.makeText(getApplicationContext() , "connection successful", Toast.LENGTH_LONG);
                        }
                    }

                    if(result.get(0).equalsIgnoreCase("right")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnection.sendMessage("r");
                        } catch(Exception e){
                            Toast.makeText(getApplicationContext() , "connection successful", Toast.LENGTH_LONG);
                        }
                    }

                    if(result.get(0).equalsIgnoreCase("stop")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnection.sendMessage("stop");
                        } catch(Exception e){
                            Toast.makeText(getApplicationContext() , "connection successful", Toast.LENGTH_LONG);
                        }
                    }

                }
                break;
            }

        }
    }


}
