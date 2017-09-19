package com.example.veikko.weathergetter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class WeatherEngine /* implements HTTPGetThread.OnRequestDoneInterface */
{
    // This interface is used to report data back to UI
    public interface WeatherDataAvailableInterface
    {
        // This method is called back in background thread.
        void weatherDataAvailable();
    }

    double KELVIN_CONVERT = 273.15;

    protected String temperature;
    protected String iconId;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    WeatherDataAvailableInterface uiCallback;

    public WeatherEngine(WeatherDataAvailableInterface callbackInterface)
    {
        this.uiCallback = callbackInterface;
    }

    public void getWeatherData(String city)
    {
        try {
            new HttpFetcher().execute(
                    new URL("http://api.openweathermap.org/data/2.5/weather?q="
                            + city +
                            "&APPID=65dbec3aae5e5bf9000c7a956c8b76f6"));
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches HTML from the passed URL.
     */
    private class HttpFetcher extends AsyncTask<URL, Integer, String> {

        @Override
        protected String doInBackground(URL... URLS) {
            try {

                //use HttpURLConnection so we can check the response code
                HttpURLConnection urlConnection = (HttpURLConnection) URLS[0].openConnection();

                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    StreamReader reader = new StreamReader();
                    return reader.readStream(urlConnection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //Update a progressbar or whatever.
        }

        protected void onPostExecute(String httpResult) {
            //Done
            Map<String, Object> parsed = null;
            try {
                parsed = JsonUtils.jsonToMap(new JSONObject(httpResult));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map<String, Object> mainElement = (Map) parsed.get("main");
            double temp = (double)mainElement.get("temp");
            double tempInC = temp - KELVIN_CONVERT;
            temperature = String.format("%.1f", tempInC);

            ArrayList<Map<String, Object>> array = (ArrayList<Map<String, Object>>)parsed.get("weather");
            Map<String, Object> weatherElement = array.get(0);
            iconId = (String)weatherElement.get("icon");

            uiCallback.weatherDataAvailable();
        }
    }
}
