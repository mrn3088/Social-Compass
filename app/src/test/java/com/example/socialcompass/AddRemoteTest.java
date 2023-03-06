package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AddRemoteTest {
    @Test
    public void addRemoteTest1() throws Exception {
        SocialCompassAPI api = new SocialCompassAPI();
        api = api.provide();
        SocialCompassUser newUser = new SocialCompassUser("333-333-33333", "333-333-3333", "Dwayne NOT Barak Johnson", 0, 0);
        try {
            api.addUser(newUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SocialCompassUser retrieved = api.getUser(newUser.public_code);
        assertEquals(retrieved.public_code, newUser.public_code);
        assertEquals(retrieved.label, newUser.label);
        assertEquals(retrieved.latitude, newUser.latitude, 1);
        assertEquals(retrieved.longitude, newUser.longitude, 1);
    }
}
