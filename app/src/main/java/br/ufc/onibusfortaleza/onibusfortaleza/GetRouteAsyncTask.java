package br.ufc.onibusfortaleza.onibusfortaleza;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by eduardo on 16-06-22.
 */
public class GetRouteAsyncTask extends AsyncTask<String, String, String> {
    public GetRouteAsyncTask() {
    }

    protected String doInBackground(String... params) {
        try {
            String origin = params[0];
            String dest = params[1];

            String directionsUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="+origin+"&destination="+dest+"&mode=transit&key=AIzaSyAEXVH95CyyTHZE9dle3My_2J_yyo0xcxo";
            //connection
            URL url = new URL(directionsUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            //read content
            InputStream is = conn.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); String data = null;
            String content = "";
            while ((data = reader.readLine()) != null) {
                content += data + "\n";
            }
            return content;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }
    protected void onProgressUpdate(String result) {

    }

    protected void onPostExecute(String result) {

    }

}