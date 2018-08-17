package com.arjun.infotech.bondera.Constance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Pattern;

public class ConstantMethod {

    public static boolean isConnected(Context ctx) {
        ConnectivityManager
                cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean Checkemail(String s) {
        Pattern sPattern = Pattern.compile(Constant.EMAIL_PATTERN);

        return sPattern.matcher(s).matches();

    }

    public static String Login_Api(String s, String email) {

//        return "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
//                + locationstring.replaceAll(" ", "%20") + "&key="
//                + Constant.API_KEY_GOOGLE_PLACES;
        return "http://app.robotresponder.com/api/login?email="
                + s + "&password=" + email;

    }

    public static String Send_Sms(String mono, String text, String uid,String dob) {
//bdate format :- 0000-january-02
//http://app.robotresponder.com/ api/sendSms?sms_to_number=+12848949834&sms_text=testing&user_id=1&dob=yyyy-mm-dd

//        return "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
//                + locationstring.replaceAll(" ", "%20") + "&key="
//                + Constant.API_KEY_GOOGLE_PLACES;
        return "http://app.robotresponder.com/api/sendSms?sms_to_number="+mono+"&sms_text="+text+"&user_id="+uid+"&dob="+dob;


    }
}
