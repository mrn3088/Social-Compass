package com.example.socialcompass;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

public class GetRemoteTest {

    @Test
    public void GetRemoteTest1() throws Exception {
        assertEquals(1,1);
        SocialCompassAPI api = new SocialCompassAPI();
        api = api.provide();
        SocialCompassUser currUser = api.getUser("123-456-7890");
        assertEquals(currUser.public_code,"123-456-7890");
        assertEquals(currUser.label, "Point Nemo");
        assertEquals(currUser.latitude, -48.876667, 1);
        assertEquals(currUser.longitude, -123.393333, 1);
    }
}