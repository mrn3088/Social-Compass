package com.example.socialcompass;

import static com.example.socialcompass.SocialCompassRepository.JSON;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.NoSuchElementException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class SocialCompassAPI {
    private volatile static SocialCompassAPI instance = null;


    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client;
    public SocialCompassAPI() {
        this.client = new OkHttpClient();
    }

    public static SocialCompassAPI provide() {
        if (instance == null) {
            instance = new SocialCompassAPI();
        }
        return instance;
    }

    public SocialCompassUser getUser(String public_code) throws IOException, InterruptedException {
        final String[] fullBody = new String[1];
        String noSpaceID = public_code.replace(" ", "%20");
        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + noSpaceID)
                .build();
        //try isn't running, operating on background thread, need completion block? or stop main thread and run on separate thread.
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    fullBody[0] = response.body().string(); // WHAT DOES THIS LINE DO??
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start(); // spawn thread
        t.join();  // wait for thread to finish

        if (fullBody[0] == null) {
            throw new NoSuchElementException("No user with that id found");
        }
        var user = SocialCompassUser.fromJSON(fullBody[0]);
        return user;
    }

    public void addUser(SocialCompassUser user) throws InterruptedException, JSONException {

        Gson gson = new Gson();
        AdaptedUser newUser = new AdaptedUser(user);
        RequestBody body = RequestBody.create(gson.toJson(newUser).toString(), JSON);
        String noSpaceID = user.public_code.replace(" ", "%20");
        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + noSpaceID)
                .put(body)
                .build();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }
            }
        });

        t.start();
        t.join();
    }
}
