package com.example.socialcompass;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.example.socialcompass.model.SocialCompassAPI;
import com.example.socialcompass.entity.SocialCompassUser;

public class GetRemoteTest {

    @Test
    public void GetRemoteTest1() throws Exception {
        SocialCompassAPI api = new SocialCompassAPI();
        SocialCompassAPI.setServerURL("https://socialcompass.goto.ucsd.edu/");
        api = api.provide();
        SocialCompassUser currUser = api.getUser("123-456-7890");
        assertEquals(currUser.public_code,"123-456-7890");
        assertEquals(currUser.label, "Point For Testing");
        assertEquals(currUser.latitude, -48.876667, 1);
        assertEquals(currUser.longitude, -123.393333, 1);
    }

}