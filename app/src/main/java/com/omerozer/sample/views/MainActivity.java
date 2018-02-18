package com.omerozer.sample.views;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitEvents;
import com.omerozer.knit.KnitView;
import com.omerozer.sample.R;

@KnitView
public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.button = (Button)findViewById(R.id.btn_next) ;
        KnitEvents.onClick("button",this,button);
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
        ((TextView)findViewById(R.id.textView_t)).setText(message);
    }

    public void showToast(String message){
        Toast.makeText(this,"YASSTOASTHEYA",Toast.LENGTH_LONG).show();
    }


}
