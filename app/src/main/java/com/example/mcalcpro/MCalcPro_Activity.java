//This project was created by Kaanhan Okyay(217584731 Lab2) and Emre Okyay(217643651 Lab3).
//https://youtu.be/Ik4ZUqlZhj4
package com.example.mcalcpro;


import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ca.roumani.i2c.MPro;


public class MCalcPro_Activity extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener {


    @Override
    public void onSensorChanged(SensorEvent event) {


        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];
        double b = Math.sqrt(ax*ax + ay*ay + az*az);
        if(b > 20)
        {
            ((EditText) findViewById(R.id.pBox)).setText("");
            ((EditText) findViewById(R.id.aBox)).setText("");
            ((EditText) findViewById(R.id.iBox)).setText("");
            ((TextView) findViewById(R.id.output)).setText("");
            tts.stop();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcalcpro_layout);
        this.tts = new TextToSpeech(this,this);
        SensorManager sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor event=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, event, SensorManager.SENSOR_DELAY_NORMAL);

    }

        public void buttonClicked(View v)
        {
                MPro mp = new MPro();
            try {

                try
                {
                    mp.setPrinciple(String.valueOf(((EditText) findViewById(R.id.pBox)).getText().toString()));
                    mp.setAmortization(String.valueOf(((EditText) findViewById(R.id.aBox)).getText().toString()));
                    mp.setInterest(String.valueOf(((EditText) findViewById(R.id.iBox)).getText().toString()));
                } catch (Exception e)
                {
                   Toast label1 = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
                    label1.show();
                    tts.stop();
                    ((TextView) findViewById(R.id.output)).setText(" ");

                }
                String z = String.valueOf(((EditText) findViewById(R.id.aBox)).getText().toString());
                double a = Integer.parseInt(z);
                double b = Integer.parseInt(String.valueOf(((EditText) findViewById(R.id.iBox)).getText().toString()));
                double c = Integer.parseInt(String.valueOf(((EditText) findViewById(R.id.pBox)).getText().toString()));
                if(a >= MPro.AMORT_MIN && a <= MPro.AMORT_MAX && b >= 0 && b<= 50 && c > 0)
                {
                    String s = "Monthly Payment =" + mp.computePayment("%,.2f") + "\n\n" +
                            "By making this payment monthly for " + String.valueOf(((EditText) findViewById(R.id.aBox)).getText().toString()) +
                            " years, the mortgage will be paid in full. But if you terminate the mortgage on its nth anniversary, the balance still owing depends on n as shown below:" +
                            "\n\n" + "       n         Balance " + "\n\n";

                    for (int i = 0; i <= a; i++)
                    {
                        s += String.format("%8d", i) + mp.outstandingAfter(i, "%,16.0f");
                        s += "\n\n";
                    }
                    ((TextView) findViewById(R.id.output)).setText(s);
                    tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                }


            } catch(Exception e)
            {
                Toast label4 = Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG);
                label4.show();
                tts.stop();
            }
        }


    @Override
    public void onInit(int status) {
        this.tts.setLanguage(Locale.US);
    }


}







