package com.omerozer.sample.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitView;
import com.omerozer.knit.Leech;
import com.omerozer.knit.Updating;
import com.omerozer.sample.R;

/**
 * Created by omerozer on 2/6/18.
 */

@KnitView
public class SecondActivity extends AppCompatActivity {

    @Leech("age")
    int age;

    @Leech("rand")
    String rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Knit.show(this);

        ((TextView) findViewById(R.id.textView_two)).setText(Integer.toString(age));
        ((TextView) findViewById(R.id.textView_rand)).setText(rand);
    }

    @Updating("age")
    public void incAge(int ageUpdate){
        ((TextView) findViewById(R.id.textView_two)).setText(Integer.toString(ageUpdate));
    }

    @Updating("rand")
    public void incRand(String rand){
        ((TextView) findViewById(R.id.textView_rand)).setText(rand);
    }


}
