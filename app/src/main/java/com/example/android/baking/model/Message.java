package com.example.android.baking.model;

import android.os.Bundle;

/**
 * Created by ryanrogers on 4/12/2017.
 */

public class Message {

    private final int message;
    private final Bundle bundle;


    public Message(int message, Bundle bundle){
        this.message = message;
        this.bundle = bundle;
    }

    public int getMessage(){
        return message;
    }

    public Bundle getBundle(){
        return bundle;
    }




}
