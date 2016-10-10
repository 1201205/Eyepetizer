package com.hyc.eyepetizer.event;

import android.content.Intent;

/**
 * Created by ray on 16/10/10.
 */

public class RouterWrapper {
    private Intent intent;
    private HomePageEvent event;


    public RouterWrapper(Intent intent, HomePageEvent event) {
        this.intent = intent;
        this.event = event;
    }


    public Intent getIntent() {
        return intent;
    }


    public void setIntent(Intent intent) {
        this.intent = intent;
    }


    public HomePageEvent getEvent() {
        return event;
    }


    public void setEvent(HomePageEvent event) {
        this.event = event;
    }
}
