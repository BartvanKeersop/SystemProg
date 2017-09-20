package com.example.bartvankeersop.observerobservable;

import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Observer {

    int _counterThreadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        _counterThreadId = 1;
    }

    @BindView(R.id.txtmessageBox)
    TextView txtMessageBox;

    @Override
    public void update(Observable observable, Object o) {
        CounterRunnable cr = (CounterRunnable) observable;
        txtMessageBox.append(cr.get_message());
    }

    @OnClick(R.id.btnStartThread)
    public void startThread(){
        CounterRunnable counterRunnable = new CounterRunnable(_counterThreadId, this);
        counterRunnable.addObserver(this);
        Thread thread = new Thread(counterRunnable);
        thread.start();
    }


    /**
     * Counts up to 10, then terminates
     */
    private class CounterRunnable extends Observable implements Runnable {

        private volatile boolean _stop;
        private int _id;
        private int _counter;
        private String _message;

        public String get_message() {return _message;}

        public CounterRunnable(int id, Observer observer){
            _stop = false;
            _id = id;
            _counter = 0;
            _message = "";
        }

        @Override
        public void run() {
            while (!_stop) {

                _message = "*Thread id: " +
                        _id + " - Count:" +
                        _counter + "\r\n";

                if (_counter == 10){
                    _message = _message +
                            "!--Thread(" +
                            _id +
                            ") has finished counting to 10.--! \r\n";
                }
                try {
                    _counter++;
                    if (_counter > 10){
                        return;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setChanged();
                            notifyObservers();
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
