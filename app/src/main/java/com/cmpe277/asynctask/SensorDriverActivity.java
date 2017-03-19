package com.cmpe277.asynctask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SensorDriverActivity extends Activity {

    HumiditySensorSimulator sensor=null;

    Integer loop_count=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_driver);
        toggleButton(true,false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void onGenerate(View v)
    {
        final TextView loop=(TextView)findViewById(R.id.txt_sensors);
        final TextView _logView= (TextView)findViewById(R.id.txt_log);
        loop.setText("20");
        _logView.setText("INITIALIZING....\n");
        _logView.append("-------------------------------\n");
        final Handler handler = new Handler();

        sensor = new HumiditySensorSimulator(new HumiditySensorSimulator.AsyncResponse() {
            @Override
            public void progressUpdate(Integer... values) {
                updateStatus(values[0],values[1],values[2],values[3]);
            }

            @Override
            public void postExecute() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggleButton(true,false);
                        _logView.append("COMPLETE");
                    }
                },150);
            }

            @Override
            public void cancel() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggleButton(true,false);
                        _logView.append("-------------------------------\n");
                        _logView.append("CANCEL");
                    }
                },100);
            }
        });

        loop_count= Integer.parseInt(loop.getText().toString());
        sensor.execute(loop_count);

        toggleButton(false,true);
    }

//    public void onGenerate(View v)
//    {
//        tempSensor = new TemperatureSensorSimulator(findViewById(R.id.txt_humidity),
//                findViewById(R.id.txt_temperature),
//                findViewById(R.id.txt_activities),
//                findViewById(R.id.txt_log),
//                findViewById(R.id.btn_generate),
//                findViewById(R.id.btn_cancel));
//        TextView loop=(TextView)findViewById(R.id.txt_sensors);
//        Integer loop_count= Integer.parseInt(loop.getText().toString());
//        tempSensor.execute(loop_count);
//
//        toggleButton(false,true);
//    }

    public void toggleButton(boolean generateEnabled, boolean cancelEnabled) {
        Button btnGenerate = (Button) findViewById(R.id.btn_generate);
        btnGenerate.setEnabled(generateEnabled);

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setEnabled(cancelEnabled);
    }

    public void onCancel(View v){
        toggleButton(true, false);
        if(sensor!=null){
            sensor.cancel(true);
        }
    }

    public void updateStatus(final Integer count, final Integer temperature
                            ,final Integer humidity, final Integer activity)
    {
        final TextView _humidView=(TextView)findViewById(R.id.txt_humidity);
        final TextView _tempView=(TextView)findViewById(R.id.txt_temperature);
        final TextView _activityView=(TextView)findViewById(R.id.txt_activities);
        final TextView _loopView=(TextView)findViewById(R.id.txt_sensors);
        final TextView _logView= (TextView)findViewById(R.id.txt_log);

        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                _humidView.setText(humidity.toString());
                _tempView.setText(temperature.toString());
                _activityView.setText(activity.toString());
                Integer remain_loop=loop_count-count;
                _loopView.setText(remain_loop.toString());
                _logView.append("Output "+count+"\n");
                _logView.append("Temperature: "+temperature+" F"+"\n");
                _logView.append("Humidity: "+humidity+"%"+"\n");
                _logView.append("Activity "+activity+"\n");
                _logView.append("-------------------------------\n");
                _logView.setMovementMethod(new ScrollingMovementMethod());
            }
        },100);

    }
}
