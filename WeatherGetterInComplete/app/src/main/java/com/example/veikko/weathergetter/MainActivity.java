package com.example.veikko.weathergetter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WeatherEngine.WeatherDataAvailableInterface {

    WeatherEngine engine = new WeatherEngine(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        getWeatherData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        getWeatherData();
    }

    public void getWeatherData(){
        try {
            EditText editor = (EditText) findViewById(R.id.editText);
            String location = editor.getText().toString();

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

            if (location == null || location.equals("")) {
                location = sharedPref.getString("last_used_location", "Oulu");
                editor.append(location);
            } else {
                writeToSharedPreferences(location);
            }
            engine.getWeatherData(location);
        }
        catch(Exception e){
            Log.d("---ERROR---", e.toString());
        }
    }

    public void writeToSharedPreferences(String location){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putString("last_used_location", location);
        editor.commit();
    }

    protected void updateUI()
    {
        TextView temperatureTextView = (TextView) findViewById(R.id.textView);
        String formatted = String.format(getString(R.string.temp), engine.getTemperature());

        temperatureTextView.setText(formatted);
        ImageView img = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("http://openweathermap.org/img/w/" + engine.getIconId() + ".png").into(img);
    }

    @Override
    public void weatherDataAvailable() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }
}
