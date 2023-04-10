package com.example.qrinternet.Activities.utility;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.InputStream;

public class UploadQRCodesToAPI extends AsyncTask<String, Void, Long> {
    private InputStream data;
    private int responseCode;
    private JSONObject responseDetails;

    public UploadQRCodesToAPI(InputStream _binaryData) {
        data = _binaryData;
    }

    @Override
    protected Long doInBackground(String... strings) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse(
                    "multipart/form-data; " +
                          "boundary=---011000010111000001101001"
            );
            RequestBody body = RequestBody.create(
                    mediaType,
                    "-----011000010111000001101001\r\n" +
                            "Content-Disposition: form-data; " +
                            "name=\"image\"\r\n\r\n" +
                            "\"YOUR_FILE_CONTENT\"\r\n" +
                            "-----011000010111000001101001--\r\n");
            Request request = new Request.Builder()
                    .url("https://qrcode3.p.rapidapi.com/images")
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .addHeader(
                            "Content-Type",
                            "multipart/form-data; " +
                                  "boundary=---011000010111000001101001"
                    )
                    .addHeader("X-RapidAPI-Key", Tags.API_KEY)
                    .build();

            Response response = client.newCall(request).execute();

            // Error Checking
            responseCode = response.code();
            if (responseCode != 200) {
                String json = response.body().string();
                try {
                    responseDetails = new JSONObject(json);
                    Log.e("JSON", responseDetails.toString());
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
                responseDetails = new JSONObject(json);
                Log.e("JSON", responseDetails.toString());
            } catch (Throwable t) {
                Log.e("JSONObject", "Could not parse JSON");
            }

            return null;
        }
    }
}
