/**
 * This class is a view model
 */

package com.example.socialcompass.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialcompass.model.SocialCompassDao;
import com.example.socialcompass.model.SocialCompassDatabase;
import com.example.socialcompass.entity.SocialCompassUser;
import com.example.socialcompass.model.SocialCompassRepository;

import java.util.List;

public class SocialCompassViewModel extends AndroidViewModel {
    private final SocialCompassRepository repo;
    private final SocialCompassDao dao;
    public SocialCompassViewModel(@NonNull Application application) {
        super(application);
        var db = SocialCompassDatabase.provide(application.getApplicationContext());
        var dao = db.getDao();
        var repo = new SocialCompassRepository(dao);
        this.repo = repo;
        this.dao = dao;
    }

    /**
     * get synced data from local database and remote server
     * @param public_code
     * @return
     * @throws Exception
     */
    public LiveData<SocialCompassUser> getUserSynced(String public_code) throws Exception {
        return repo.getSynced(public_code);
    }

    /**
     * get users from local database
     * @return
     */
    public LiveData<List<SocialCompassUser>> getAllUserLocal(){
        return repo.getAllLocal();
    }

    /**
     * get users from remote server.
     * @param public_code
     * @return
     * @throws Exception
     */
    public LiveData<SocialCompassUser> getUserRemote(String public_code) throws Exception {
        return repo.getRemote(public_code);
    }

    /**
     * insert user to local database
     * @param user
     */
    public void upsert(SocialCompassUser user){
        dao.upsert(user);
    }

    /**
     * upsert user to remote server.
     */

    public void upsertRemote(SocialCompassUser user) throws Exception {
        repo.upsertRemote(user);
    }
}
