package com.example.socialcompass;

import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SocialCompassRepository {
    private final SocialCompassDao dao;
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.get("applicaion/json; charset=utf-8");

    public SocialCompassRepository(SocialCompassDao dao) {
        this.dao = dao;
    }

    // Synced Methods
    // =============
    /*
    @requires non null title
     */
    public LiveData<SocialCompassUser> getSynced(String title) throws Exception {
        var user = new MediatorLiveData<SocialCompassUser>();

        Observer<SocialCompassUser> updateFromRemote = theirUser -> {
            var ourUser = user.getValue();
            if(theirUser == null) return;
            if(ourUser == null) {
                upsertLocal(theirUser);
            }
        };

        user.addSource(getLocal(title), user::postValue);
        user.addSource(getRemote(title), updateFromRemote);

        return user;
    }

    public void upsertSynced(SocialCompassUser user) throws Exception {
        upsertLocal(user);
        upsertRemote(user);
    }

    // Local Methods
    // =============

    public LiveData<SocialCompassUser> getLocal(String title) {
        return dao.get(title);
    }

    public LiveData<List<SocialCompassUser>> getAllLocal() {
        return dao.getAll();
    }

    public void upsertLocal(SocialCompassUser user) {
        dao.upsert(user);
    }

    public boolean existsLocal(String title) {
        return dao.exists(title);
    }

    // Remote Methods
    // ==============
    /*
    @requires user id corresponds to a user in the server
    @ensures local has most updated user instance from server
     */
    public LiveData<SocialCompassUser> getRemote(String userID) throws Exception {
        SocialCompassAPI api = new SocialCompassAPI();
        api = api.provide();
        SocialCompassUser user = api.getUser(userID);
        MutableLiveData<SocialCompassUser> currUser = new MutableLiveData<>();
        currUser.setValue(user);
        return currUser;
    }

    /*
    @requires non null user
    @ensures server has most updated version of user
     */
    public void upsertRemote(SocialCompassUser user) throws Exception {
        SocialCompassAPI api = new SocialCompassAPI();
        api = api.provide();
        api.addUser(user);
    }
}
