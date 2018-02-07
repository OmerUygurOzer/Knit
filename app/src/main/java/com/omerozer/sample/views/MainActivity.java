package com.omerozer.sample.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitView;
import com.omerozer.knit.Leech;
import com.omerozer.knit.Updating;
import com.omerozer.sample.R;

@KnitView
public class MainActivity extends AppCompatActivity {

    @Leech("firstName")
    String testString;

    @Leech("surname")
    String lastName;

    @Leech("fullName")
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Knit.show(this);
        ((TextView) findViewById(R.id.textView_t)).setText(fullName);
        ((Button) findViewById(R.id.btn_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(
                        MainActivity.this, SecondActivity.class);
                MainActivity.this.startActivity(next);
            }
        });
    }

    @Updating("firstName")
    public void updateTestString(String testInteger) {

    }

    @Updating("fullName")
    public void updateFullname(String testInteger) {

    }
}
