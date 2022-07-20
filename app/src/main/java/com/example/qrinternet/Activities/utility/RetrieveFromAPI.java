package com.example.qrinternet.Activities.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.qrinternet.Activities.dashboard.DashboardFragment;
import com.example.qrinternet.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RetrieveFromAPI extends AsyncTask<String, Void, Long> {

    private Exception exception;
    private Bitmap bitmap;

    protected Long doInBackground(String... urls) {
        try {
            // Get QR Code from API
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String value = "\r\n{" +
            "\r\n    \"data\": {" +
            "\r\n        \"ssid\": \"My WiFi network name\"," +
            "\r\n        \"password\": \"Pass!&#^@#*@\"," +
            "\r\n        \"security\": \"WPA\"," +
            "\r\n        \"hidden\": false" +
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
                    .addHeader("X-RapidAPI-Key", "2da622fcb4mshc3c9c748526f0c4p1db57bjsna162a8b9db0b")
                    .addHeader("X-RapidAPI-Host", "qrcode3.p.rapidapi.com")
                    .build();
            Response response = client.newCall(request).execute();

            // Create and save QR Code as png
            InputStream inputStream = response.body().byteStream();
            String path = Tags.IMAGE_PATH;
            File file = new File(path, Tags.IMAGE_NAME);
            bitmap = BitmapFactory.decodeStream(inputStream);
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Logs for Debugging
            if (request != null) {
                Log.e("SUCCESS 1", "request built.");
                Log.e("request body", request.toString());
                Log.e("request header", request.headers().toString());
            } else {
                Log.e("ERROR 1", "request not built.");
            }

            if (response.isSuccessful()) {
                Log.e("SUCCESS 2", "response built.");

                try {
                    Log.e("response body", response.message().toString());
                } catch (NullPointerException e) {
                    Log.e("response body", "body is {NULL}");
                }

                try {
                    Log.e("response header", response.headers().toString());
                } catch (NullPointerException e) {
                    Log.e("response header", "header is {NULL}");
                }
            } else {
                Log.e("ERROR 2", "response not built.");
            }

            if (file.isFile()) {
                Log.e("SUCCESS 3", "image created");
                Log.e("File", file.getPath());
            } else {
                Log.e("ERROR 3", "image NOT created");
            }

            return 0L;
        } catch (Exception e) {
            this.exception = e;

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
}
