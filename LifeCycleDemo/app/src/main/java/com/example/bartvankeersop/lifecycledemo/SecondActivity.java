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

    WriterRunnable _writerRunnable;
    int _counterThreadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        _counterThreadId = 1;
        ButterKnife.bind(this);
    }

    @BindView(R.id.txtmessageBox)
    TextView txtmessageBox;

    @OnClick(R.id.btnClose)
    public void CloseActivity() {
        this.finish();
    }

    /**
     * Starts a thread that prints a message to the UI every 5 seconds.
     */
    @OnClick(R.id.btnStartThread)
    public void StartThread() {
        if (_writerRunnable == null) {
            _writerRunnable = new WriterRunnable("Some random message \r\n");
                Thread thread = new Thread(_writerRunnable);
                thread.start();
        }
        else{
            Toast.makeText(this, "Thread is already running!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts a thread that counts to 10 and writes it's id and count to the ui every 1 second.
     */
    @OnClick(R.id.btnStartAnotherThread)
    public void StartAnotherThread() {
        Thread thread = new Thread(new CounterRunnable(_counterThreadId));
        _counterThreadId++;
        thread.start();
    }

    /**
     * Stops the messagewriter thread.
     */
    @OnClick(R.id.btnStopThread)
    public void StopThread() {
        _writerRunnable.set_stop(true);
        _writerRunnable = null;
        Toast.makeText(this, "Thread stopped.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Writes a message every 5 seconds to the UI
     */
    private class WriterRunnable implements Runnable {

        private volatile boolean _stop;
        private String message;

        public WriterRunnable(String message){
            this.message = message;
            _stop = false;
            Log.d("-LOG- VALUE OF STOP:", "TEST");
        }

        public void set_stop(boolean value){
            _stop = value;
        }

        public boolean get_stop(){
            return _stop;
        }

        @Override
        public void run() {
            while (!_stop) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtmessageBox.append(message);
                    }
                });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Counts up to 10, then terminates
     */
    private class CounterRunnable implements Runnable {

        private volatile boolean _stop;
        private int _id;
        private int _counter;
        private String _message;

        public CounterRunnable(int id){
            _stop = false;
            _id = id;
            _counter = 0;
            _message = "";
        }

        @Override
        public void run() {
            while (!_stop) {

                _message = "*Thread id: " + _id + " - Count:" + _counter + "\r\n";

                if (_counter == 10){
                    _message = _message +
                            "!--Thread("
                            + _id
                            +") has finished counting to 10.--! \r\n";
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtmessageBox.append(_message);
                    }
                });
                try {
                    _counter++;
                    if (_counter > 10){
                        return;
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
