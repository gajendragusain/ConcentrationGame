package com.example.gajendra.memorygameforkids;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class End extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Button button1=(Button)findViewById(R.id.button1);
        Button button2=(Button)findViewById(R.id.button2);
        Button button3=(Button)findViewById(R.id.button3);
        int attempts=getIntent().getIntExtra("attempts",0);
        long time_elapsed=getIntent().getLongExtra("timeElapsed",0);
        TextView textView2=(TextView)findViewById(R.id.textView2);
        textView2.setText("No. of Attempts: " + attempts);
        TextView textView3=(TextView)findViewById(R.id.textView3);
        textView3.setText("Time Taken: "+ (time_elapsed/1000) + " Seconds");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent board=new Intent(End.this,Board.class);
                startActivity(board);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main=new Intent(End.this,MainActivity.class);
                startActivity(main);
                finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
