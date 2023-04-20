package com.example.qrinternet.Activities.utility;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MultipartBody;

public class UploadQRCodesToAPI extends AsyncTask<String, Void, Long> {
    private String filename;

    private int responseCode;
    private JSONObject errorDetails;

    public UploadQRCodesToAPI(String _filename) {
        filename = _filename;
    }

    @Override
    protected Long doInBackground(String... strings) {
        try {
            File fileInput = new File(Tags.SAVE_PATH, filename);
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", Tags.SAVE_PATH + filename,
                            RequestBody.create(fileInput, MediaType.parse("image/png")))
                    .build();

            Request request = new Request.Builder()
                    .url("https://qrcode3.p.rapidapi.com/images")
                    .post(body)
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
}
