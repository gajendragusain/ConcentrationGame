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

       // addListenerOnButton();
        //radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
       /* radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String val;
                if(i==R.id.radioButton1){
                    val = radioButton1.getText().toString();

                }
                else if(i==R.id.radioButton2){
                    val=radioButton2.getText().toString();
                }
                else if(i==R.id.radioButton3){
                    val=radioButton3.getText().toString();
                }
                else{
                    val="4";
                    Toast.makeText(getBaseContext(), "Please Check one of the radio buttons next time, value set to 4", Toast.LENGTH_LONG).show();
                }
            }
        });*/

    }


   /* public void addListenerOnButton() {

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioButton radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        final RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        final RadioButton radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        button = (Button) findViewById(R.id.button);
       button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radiobutton = (RadioButton) findViewById(selectedId);

                Toast.makeText(getBaseContext(),"id: "+radiobutton.getId(),Toast.LENGTH_LONG).show();
            }

        });

    }*/

    @Override
    public void onBackPressed() {

        finish();
    }
}