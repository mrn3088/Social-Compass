package com.example.socialcompass.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.socialcompass.entity.SocialCompassUser;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class SocialCompassRepository {
    private final SocialCompassDao dao;
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.get("applicaion/json; charset=utf-8");
    private SocialCompassAPI api;
    public SocialCompassRepository(SocialCompassDao dao) {
        this.dao = dao;
    }

    // Synced Methods
    // =============
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
        user.private_code = user.public_code;
        dao.upsert(user);
    }

    // Remote Methods
    // ==============

    public LiveData<SocialCompassUser> getRemote(String userID) throws Exception {
        api = SocialCompassAPI.provide();
        SocialCompassUser user = api.getUser(userID);
        MutableLiveData<SocialCompassUser> currUser = new MutableLiveData<>();
        currUser.setValue(user);
        return currUser;
    }

    public void upsertRemote(SocialCompassUser user) throws Exception {
        api = SocialCompassAPI.provide();
        api.addUser(user);
    }

    public SocialCompassUser getRemoteWithoutLiveData(String userID) throws Exception{
        api = SocialCompassAPI.provide();
        SocialCompassUser user = api.getUser(userID);
        return user;
    }
}
