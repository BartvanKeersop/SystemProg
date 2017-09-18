package com.example.bartvankeersop.lifecycledemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    CustomApplication customApplication = CustomApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ShowMessages("Create");
        customApplication.setMessage("Create");
    }

    @BindView(R.id.txtCurrentAction)
    TextView txtCurrentAction;

    @BindView(R.id.txtPreviousAction)
    TextView txtPreviousAction;

    @Override
    @OnClick(R.id.btnStart)
    protected void onStart() {
        ShowMessages("Start");
        customApplication.setMessage("Start");
        Log.d("--------LOG--------", "onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        ShowMessages("Restart");
        customApplication.setMessage("Restart");
        Log.d("--------LOG--------", "onRestart");
        super.onRestart();
    }

    @Override
    @OnClick(R.id.btnResume)
    protected void onResume() {
        ShowMessages("Resume");
        customApplication.setMessage("Resume");
        Log.d("--------LOG--------", "onResume");
        super.onResume();
    }

    @Override
    @OnClick(R.id.btnPause)
    protected void onPause() {
        ShowMessages("Pause");
        customApplication.setMessage("Pause");
        Log.d("--------LOG--------", "onPause");
        super.onPause();
    }

    @Override
    @OnClick(R.id.btnStop)
    protected void onStop() {
        ShowMessages("Stop");
        customApplication.setMessage("Stop");
        Log.d("--------LOG--------", "onStop");
        super.onStop();
    }

    @Override
    @OnClick(R.id.btnStop)
    protected void onDestroy() {
        ShowMessages("Destroy");
        customApplication.setMessage("Destroyed");
        Log.d("--------LOG--------", "onDestroy");
        super.onDestroy();
    }

    @OnClick(R.id.btnSecondActivity)
    protected void openSecondActivity() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    private void ShowMessages(String message){
        txtCurrentAction.setText(message);
        txtPreviousAction.setText(customApplication .getMessage());
    }
}
