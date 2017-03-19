package com.cmpe277.asynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by tranpham on 3/18/17.
 */

public class TemperatureSensorSimulator extends AsyncTask<Integer,Integer,Integer> {

    TextView _humidView=null;
    TextView _tempView=null;
    TextView _activityView=null;
    TextView _logView=null;
    Button _startButton =null;
    Button _cancelButton =null;
    TemperatureSensorSimulator(View humidView,View tempView, View activityView, View logView,
                                View startButton, View cancelButton)
    {
        _humidView=(TextView)humidView;
        _tempView=(TextView)tempView;
        _activityView=(TextView)activityView;
        _logView=(TextView)logView;
        _startButton =(Button)startButton;
        _cancelButton =(Button)cancelButton;
    }
    @Override
    protected Integer doInBackground(Integer... params) {
        Random rdTemp = new Random(Utils.RANDOM_SEED);
        Random rdHumidity = new Random(Utils.RANDOM_SEED+7);
        Random rdActivity = new Random(Utils.RANDOM_SEED+13);
        for (Integer loop_count:params){
            for (int i=0;i<loop_count&&!isCancelled();i++) {
                Utils.sleepForInSecs(3);
                ///randomize sensor value
                Integer humidity=rdTemp.nextInt(100);
                Integer temperature=rdHumidity.nextInt(70);
                Integer activity=rdActivity.nextInt(1000);
                publishProgress(i+1,temperature,humidity,activity);
            }
        }
        return 1;
    }

    @Override
    protected void onPreExecute() {
        toggleButton(false,true);
        _logView.setText("INITIALIZING....");
        _logView.append("-------------------------------\n");

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        updateStatus(values[0],values[1],values[2],values[3]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        toggleButton(true,false);
        _logView.append("-------------------------------\n");
        _logView.append("COMPLETE");
    }

    @Override
    protected void onCancelled() {
        _logView.append("-------------------------------\n");
        _logView.append("CANCEL");
    }

    void updateStatus(final Integer count,final Integer temperature
                        ,final Integer humidity,final Integer activity)
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _humidView.setText(humidity);
                _tempView.setText(temperature);
                _activityView.setText(activity);
                _logView.append("Output "+count+"\n");
                _logView.append("Temperature: "+temperature+"\n");
                _logView.append("Humidity: "+humidity+"\n");
                _logView.append("Activity "+activity+"\n");
                _logView.append("-------------------------------\n");
            }
        },100);
    }

    void toggleButton(boolean generateEnabled, boolean cancelEnabled) {

        _startButton.setEnabled(generateEnabled);

        _cancelButton.setEnabled(cancelEnabled);
    }
}
