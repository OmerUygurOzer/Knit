package com.omerozer.sample.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitEvents;
import com.omerozer.knit.KnitView;
import com.omerozer.knit.Updating;
import com.omerozer.sample.R;

@KnitView
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Knit.show(this);

        Button b = ((Button) findViewById(R.id.btn_next));
        KnitEvents.onClick("clickButton",this,b);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Knit.show(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Knit.dismiss(this);
    }

    @Updating("firstName")
    public void updateTestString(String testInteger) {
        Log.d("KNIT_TEST" , testInteger);
        ((TextView) findViewById(R.id.textView_t)).setText(testInteger);
    }

    @Updating("fullName")
    public void updateFullname(String testInteger) {
        Log.d("KNIT_TEST" , "Receiving fullName:" + testInteger);
        ((TextView) findViewById(R.id.textView_t)).setText(testInteger);
    }
}
