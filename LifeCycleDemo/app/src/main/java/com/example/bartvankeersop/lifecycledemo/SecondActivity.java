package com.example.bartvankeersop.lifecycledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondActivity extends AppCompatActivity {

    WriterRunnable writerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
    }

    @BindView(R.id.txtmessageBox)
    TextView txtmessageBox;

    @OnClick(R.id.btnClose)
    public void CloseActivity() {
        this.finish();
    }

    @OnClick(R.id.btnStartThread)
    public void StartThread() {
        if (writerRunnable == null) {
            writerRunnable = new WriterRunnable("Some random message \r\n", false);
                Log.d("--------LOG--------", "Attempting to start thread");
                Thread thread = new Thread(writerRunnable);
                thread.start();
        }
        else{
            Toast.makeText(this, "Thread is already running!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnStopThread)
    public void StopThread() {
        writerRunnable.setStop(true);
        writerRunnable = null;
        Toast.makeText(this, "Thread stopped.", Toast.LENGTH_SHORT).show();
    }

    private class WriterRunnable implements Runnable {

        private volatile boolean stop;
        private String message;

        public WriterRunnable(String message, boolean stop){

            this.stop = stop;
            this.message = message;
            Log.d("-LOG- VALUE OF STOP:", "TEST");
        }

        public void setStop(boolean value){
            stop = value;
        }

        public boolean getStop(){
            return stop;
        }

        @Override
        public void run() {
            while (!stop) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("--------LOG--------", message);
                        txtmessageBox.append(message);
                    }
                });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("--------LOG--------", "THREAD INTERRUPTED");
                }
            }
        }
    }
}
