package com.example.draw;

import android.os.StrictMode;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Gets {
    public String Get() throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://172.27.103.74:5000/predict")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "localhost:5000")
                .addHeader("Connection", "keep-alive")
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().string();
    }

    public ArrayList<Results> Str() throws IOException {
        Gets gets = new Gets();
        String get = gets.Get();
        JsonObject jsonObject = new JsonParser().parse(get).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("results");
        Gson gson = new Gson();
        ArrayList<Results> userBeanList = new ArrayList<>();
        for (JsonElement user : jsonArray) {
            Results userBean = gson.fromJson(user, new TypeToken<Results>() {}.getType());
            userBeanList.add(userBean);
        }
        return userBeanList;
    }
}
