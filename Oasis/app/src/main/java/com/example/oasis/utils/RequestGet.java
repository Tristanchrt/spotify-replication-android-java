package com.example.oasis.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.oasis.models.Track;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RequestGet extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... fullUrl) {
        try {
            URL url = new URL(fullUrl[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpURLConnection.setDoInput(true);

            int response = httpURLConnection.getResponseCode();
            if (response == 200) {
                InputStream istream = new BufferedInputStream(httpURLConnection.getInputStream());
                // readStream(in);
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
                JSONObject jsonObject = new JSONObject(reader.readLine().replace("#", ""));
                reader.close();
                return jsonObject;
            }
        } catch (
                ProtocolException e) {
            Log.e("ERROR : ", e.toString());
            e.printStackTrace();
        } catch (
                MalformedURLException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
