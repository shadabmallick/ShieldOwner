package com.sketch.securityowner.GlobalClass;

import android.app.Application;

import com.sketch.securityowner.Font.FontsOverride;

public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Poppins-ExtraLight.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Poppins-ExtraLight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Poppins-ExtraLight.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Poppins-ExtraLight.ttf");
    }
}

