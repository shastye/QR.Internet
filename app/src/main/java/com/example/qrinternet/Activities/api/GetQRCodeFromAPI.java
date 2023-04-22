package com.example.qrinternet.Activities.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.qrinternet.Activities.utility.Methods;
import com.example.qrinternet.Activities.utility.Tags;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

public class GetQRCodeFromAPI extends AsyncTask<String, Void, Long> {
    private byte[] binaryData;
    private Bitmap bitmap;
    private int responseCode;
    private JSONObject errorDetails;
    private String ssid, password, security, hidden;

    public GetQRCodeFromAPI() {
        ssid = "\"My WiFi network name\"";
        password = "\"Pass!&#^@#*@\"";
        security = "\"WPA\"";
        hidden = "false";
    }
    public GetQRCodeFromAPI(String _ssid, String _password, String _security, String _hidden) {
        ssid = "\"" + _ssid + "\"";
        password = "\"" + _password + "\"";
        security = "\"" + _security + "\"";
        hidden = _hidden;
    }

    protected Long doInBackground(String... urls) {
        try {
            // Get QR Code from API
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String value = "\r\n{" +
            "\r\n    \"data\": {" +
            "\r\n        \"ssid\": " + ssid + "," +
            "\r\n        \"password\": " + password + "," +
            "\r\n        \"security\": " + security + "," +
            "\r\n        \"hidden\": " + hidden +
            "\r\n    }," +
            "\r\n    \"image\": {" +
            "\r\n        \"uri\": \"icon://appstore\"," +
            "\r\n        \"modules\": true" +
            "\r\n    }," +
            "\r\n    \"style\": {" +
            "\r\n        \"module\": {" +
            "\r\n            \"color\": \"black\"," +
            "\r\n            \"shape\": \"default\"" +
            "\r\n        }," +
            "\r\n        \"inner_eye\": {" +
            "\r\n            \"shape\": \"default\"" +
            "\r\n        }," +
            "\r\n        \"outer_eye\": {" +
            "\r\n            \"shape\": \"default\"" +
            "\r\n        }," +
            "\r\n        \"background\": {}" +
            "\r\n    }," +
            "\r\n    \"size\": {" +
            "\r\n        \"width\": 400," +
            "\r\n        \"quiet_zone\": 4," +
            "\r\n        \"error_correction\": \"M\"" +
            "\r\n    }," +
            "\r\n    \"output\": {" +
            "\r\n        \"filename\": \"qrcode\"," +
            "\r\n        \"format\": \"png\"" +
            "\r\n    }" +
            "\r\n}";
            RequestBody body = RequestBody.create(mediaType, value);
            Request request = new Request.Builder()
                    .url("https://qrcode3.p.rapidapi.com/qrcode/wifi")
                    .post(body)
                    .addHeader("content-type", "application/json")
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


            // Create QR Code as bitmap
            if (responseCode == 200) {
                binaryData = response.body().bytes();
                bitmap = Methods.convertToBitmap(binaryData);

                if (bitmap == null) {
                    responseCode = 102;

                    String json = "{\"detail\":\"Bitmap could not be decoded.\"}";
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

    public Bitmap getBitmap() {
        return bitmap;
    }
    public int getResponseCode() {
        return responseCode;
    }
    public JSONObject getErrorDetails() {
        return errorDetails;
    }
    public byte[] getBinaryData() {
        return binaryData;
    }
}