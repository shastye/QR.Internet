package com.example.qrinternet.Activities.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.qrinternet.Activities.utility.Image;
import com.example.qrinternet.Activities.utility.Tags;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListAllQRCodesFromAPI extends AsyncTask<String, Void, Long> {

    private int responseCode;
    private JSONObject errorDetails;
    private JSONArray responseArray;
    private Vector<Image> imagesFromAPI;

    @Override
    protected Long doInBackground(String... strings) {
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://qrcode3.p.rapidapi.com/images")
                    .get()
                    .addHeader("X-RapidAPI-Key", Tags.API_KEY)
                    .addHeader("X-RapidAPI-Host", "qrcode3.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();

            // Error Checking
            responseCode = response.code();
            if (responseCode != 200) {
                String json = response.body().string();
                try {
                    errorDetails = new JSONObject(json);
                    Log.e("JSON", errorDetails.toString());
                } catch (Throwable t) {
                    Log.e("JSONObject", "Could not parse JSON");
                }

                Log.e("Response Code", String.valueOf(responseCode));
                Log.e("Response Body string", json);
            }



            // Create List of Image Data
            if (responseCode == 200) {
                String json = response.body().string();
                try {
                    responseArray = new JSONArray(json);
                } catch (Exception e) {
                    Log.e("OOPS", "List is probably empty");
                }

                imagesFromAPI = new Vector<Image>(responseArray.length());
                for (int i =0; i < responseArray.length(); i++) {
                    Gson gson = new Gson();
                    JSONObject temp = responseArray.getJSONObject(i);

                    Image image = gson.fromJson(temp.toString(), Image.class);
                    imagesFromAPI.add(image);
                }
            }

            return 0L;
        } catch (Exception e) {
            responseCode = 0;

            String json = "{\"detail\":\"" + e.getMessage().toString() + "\"}";
            try {
                errorDetails = new JSONObject(json);
                Log.e("JSON", errorDetails.toString());
            } catch (Throwable t) {
                Log.e("JSONObject", "Could not parse JSON");
            }

            return null;
        }
    }

    protected void onPostExecute(Long feed) {
        super.onPostExecute(feed);

        // TODO: check this.exception
        // TODO: do something with the feed
    }

    public int getResponseCode() {
        return responseCode;
    }
    public JSONObject getErrorDetails() {
        return errorDetails;
    }
    public Vector<Image> getImagesFromAPI() {
        return imagesFromAPI;
    }
}
