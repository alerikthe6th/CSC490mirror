package edu.augustana.csc490.androidbrainapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import edu.augustana.csc490.androidbrainapp.R;

/**
 * CURRENTLY NOT BEING USED
 *
 * Created by Alerik Vi on 2/12/2017.
 */

public class PopupColorChooserActivity extends AppCompatActivity{

    protected Button btnClose;
    protected RadioButton rbRed;
    protected RadioButton rbGreen;
    protected RadioButton rbBlue;


    private boolean red;
    private boolean green;
    private boolean blue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_chooser_layout);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // closes the window?
            }
        });

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7),(int) (height*.7));

//        if(rbRed.isChecked()) {
//            red = true;
//        } else if(rbGreen.isChecked()) {
//            green = true;
//        } else if(rbBlue.isChecked()) {
//            blue = true;
//        }
    }

}
