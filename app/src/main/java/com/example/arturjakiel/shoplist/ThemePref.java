package com.example.kizuhane.shoplist;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemePref {
    SharedPreferences mySharedPref ;
    public ThemePref(Context context) {
        mySharedPref = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }
    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }
    public Boolean loadNightModeState (){
        Boolean state = mySharedPref.getBoolean("NightMode",false);
        return  state;
    }
}
