package com.arjun.infotech.bondera.Webservice;

/**
 * Created by Admin on 15-11-2017.
 */

public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(Object response, WebCallid webcallid);
}
