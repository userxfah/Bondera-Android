package com.arjun.infotech.bondera.MyPreference;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by Admin on 08-09-2017.
 */

public class MyPref {


    public static final String Units = "Units";
    public static final String when = "when";
    public static final String Current = "Current";
    public static final String Currencys = "Currencys";
    public static final String ConsumptionRate = "ConsumptionRate";
    public static final String TypeOfCar = "TypeOfCar";
    public static final String PricePerLiter = "PricePerLiter";
    public static final String journy_date = "alreadyset";
    public static SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context mContext;


    private static final String PREF_NAME = "Roboresponer";


    public MyPref(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

    }

    public void setPref(String key, Set<String> stringList) {
        editor.putStringSet(key, stringList);
        editor.commit();
    }


    public Set<String> getPref1(String key, Set<String> stringSet) {
        return pref.getStringSet(key, stringSet);
    }


    public void setPref(String key, int fid) {
        editor.putInt(key, fid);
        editor.commit();
    }

    public Integer getPref(String key, int fid) {
        return pref.getInt(key, fid);
    }

    public void setPref(String key, String val) {
        editor.putString(key, val);
        editor.commit();

    }

    public String getPref(String key, String val) {
        return pref.getString(key, val);
    }

    public void setPref(String pref, boolean val) {
        editor.putBoolean(pref, val);
        editor.commit();

    }


    public boolean getPref(String key, boolean val) {
        return pref.getBoolean(key, val);
    }

    public void clearPref() {
        editor.clear();
        editor.commit();
    }




}
