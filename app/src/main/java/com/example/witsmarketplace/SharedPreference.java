package com.example.witsmarketplace;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

public class SharedPreference {
    Activity activity;
    public SharedPreference(Activity activity){
        this.activity = activity;
    };

    public void setSH(String key, String value){
        SharedPreferences sh = activity.getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSH(String key){
        SharedPreferences s = activity.getSharedPreferences("application", Context.MODE_PRIVATE);
        return s.getString(key, null);
    }

    public void RemoveKey(String key){
        SharedPreferences s = activity.getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.remove(key);
        editor.apply();
    }

}
