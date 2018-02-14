package com.omerozer.sample.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitView;
import com.omerozer.knit.Updating;
import com.omerozer.sample.R;

/**
 * Created by omerozer on 2/6/18.
 */

@KnitView
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Knit.show(this);

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

    public void recMes(String message){
        ((TextView)findViewById(R.id.textView_rand)).setText(message);
    }


}
