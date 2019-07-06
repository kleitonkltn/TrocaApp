package com.example.hj.trocaapp;

import android.os.StrictMode;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WebService {

    String msg;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public String enviar(JSONObject json) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String jsonS = json.toString();
        final OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://fcm.googleapis.com/fcm/send").newBuilder();
        RequestBody body = RequestBody.create(JSON,jsonS);
                String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "key=AIzaSyBFYlGETXFNLtSUN8Q3ya1tq6uR1R6ElcU")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }
}





