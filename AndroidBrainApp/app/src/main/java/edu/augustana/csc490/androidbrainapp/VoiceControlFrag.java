package edu.augustana.csc490.androidbrainapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hamby on 1/29/2017.
 */

public class VoiceControlFrag extends Fragment {

    private TextView tvSpeechInput;
    private TextView tvCommand;
    private ImageButton btnSpeak;
    private Button btnStop;
    private final int REQ_CODE_SPEECH_INPUT = 100; //required to send voice strings in intents

    public static VoiceControlFrag newInstance() {
        VoiceControlFrag voice_control = new VoiceControlFrag();
        return voice_control;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.voice_layout, container, false);

        tvSpeechInput = (TextView) rootView.findViewById(R.id.tvSpeechInput);
        tvCommand = (TextView) rootView.findViewById(R.id.tvCommand);
        btnSpeak = (ImageButton) rootView.findViewById(R.id.btnSpeak);
        btnStop = (Button) rootView.findViewById(R.id.btnStopCam);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(v);
            }
        });
        return rootView;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(), "It didn't work", Toast.LENGTH_LONG);
        }
    }

    public void stop(View view) {
        try {
            tvSpeechInput.setText("String sent: stop");
            tvCommand.setText("Command received: stop");
            MainActivity.mSocketConnectionRobot.sendMessage("stop");
        } catch (Exception e) {
            Toast.makeText(getContext() , "connection unsuccessful", Toast.LENGTH_LONG);
        }
    }

    /*
     * Receiving speech input
     *
     * a request code of 100 is required to print/send strings. 100 is also the value of RESULT_OK, a constant defined by Android Studios.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    tvSpeechInput.setText("String sent: " + result.get(0));

                    /**
                     * if the voice input receives a correct movement command, the command is sent to the robot via sendMessage using the socket object
                     * the string for a successful voice command is also appropriately changed
                     *
                     * incorrect voice commands will not terminate a successful previously called action.
                     *
                     * saying "stop" should halt the robot of movement
                     */
                    if (result.get(0).equalsIgnoreCase("forward")) {
                        try {
                            Log.d("inside forward case", "inside forward case was successful");
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnectionRobot.sendMessage("forward");
                            Log.d("sent move command", "move forward command successful sent");
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "connection unsuccessful, caught exception", Toast.LENGTH_LONG).show();
                        }
                    }

                    if (result.get(0).equalsIgnoreCase("back")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnectionRobot.sendMessage("back");
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "connection unsuccessful, caught exception", Toast.LENGTH_LONG).show();
                        }
                    }

                    if (result.get(0).equalsIgnoreCase("left")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnectionRobot.sendMessage("left");
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "connection unsuccessful, caught exception", Toast.LENGTH_LONG).show();
                        }
                    }

                    if (result.get(0).equalsIgnoreCase("right")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnectionRobot.sendMessage("right");
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "connection unsuccessful, caught exception", Toast.LENGTH_LONG).show();
                        }
                    }

                    if (result.get(0).equalsIgnoreCase("stop")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnectionRobot.sendMessage("stop");
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "connection unsuccessful, caught exception", Toast.LENGTH_LONG).show();
                        }
                    }

                    if (result.get(0).equalsIgnoreCase("einstein") || result.get(0).equalsIgnoreCase("i'm stein")) {
                        try {
                            tvCommand.setText("Command received: " + result.get(0));
                            MainActivity.mSocketConnectionRobot.sendMessage("einstein");
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "connection unsuccessful, caught exception", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                break;
            }

        }

    }
}
