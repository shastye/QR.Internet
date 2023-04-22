package com.example.qrinternet.Activities.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.qrinternet.Activities.utility.ImageDetails;
import com.example.qrinternet.Activities.utility.Tags;

import org.json.JSONObject;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteImageFromAPI extends AsyncTask<String, Void, Long> {

    private int responseCode;
    private JSONObject errorDetails;
    private ImageDetails qrCode;

    public DeleteImageFromAPI(ImageDetails _qrCode) {
        qrCode = _qrCode;
    }

    @Override
    protected Long doInBackground(String... strings) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://qrcode3.p.rapidapi.com/images/" + qrCode.id)
                    .delete(null)
                    .addHeader("X-RapidAPI-Key", Tags.API_KEY)
                    .addHeader("X-RapidAPI-Host", "qrcode3.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();

            // Error Checking
            responseCode = response.code();
            if (responseCode != 204) {
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

            // Delete QR Code from device
            if (responseCode == 204) {
                try {
                    File file = new File(qrCode.source);
                    if (!file.delete()) {
                        responseCode = 104;

                        String json = "{\"detail\":\"" + "image not deleted" + "\"}";
                        try {
                            errorDetails = new JSONObject(json);
                            Log.e("JSON", errorDetails.toString());
                        } catch (Throwable t) {
                            Log.e("JSONObject", "Could not parse JSON");
                        }

                        return null;
                    }
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

            return 0L;
        }
        catch (Exception e) {
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
}
