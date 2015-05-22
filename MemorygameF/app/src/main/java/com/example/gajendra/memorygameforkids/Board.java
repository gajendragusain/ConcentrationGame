package com.example.gajendra.memorygameforkids;

import android.app.Application;
import android.content.Intent;
import android.media.ExifInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class Board extends ActionBarActivity {
    private RadioGroup radioGroup;
    private  Button button;
    private RadioButton radiobutton,radioButton1,radioButton2,radioButton3;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        button = (Button) findViewById(R.id.button);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                String val = null;
                int radiobuttonId = radioGroup.getCheckedRadioButtonId();
               radiobutton=(RadioButton)findViewById(radiobuttonId);
                switch (radiobuttonId) {
                    case R.id.radioButton1:
                        if (radiobutton.isChecked()) {

                            val = radiobutton.getText().toString();
                        }

                        break;
                    case R.id.radioButton2:
                        if (radiobutton.isChecked()) {
                            val = radiobutton.getText().toString();
                        }
                        break;
                    case R.id.radioButton3:
                        if (radiobutton.isChecked()) {
                            val = radiobutton.getText().toString();
                        }
                        break;
                    default:
                        Toast.makeText(getBaseContext(), "Please Check one of the radio buttons next time, value set to 4", Toast.LENGTH_LONG).show();
                        val = "4";
                        break;
                }

                int vl = Integer.parseInt(val);
                Intent i = new Intent(Board.this, MainActivity.class);
                i.putExtra("size", vl);
                startActivity(i);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {

        finish();
    }
}