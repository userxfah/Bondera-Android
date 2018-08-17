package com.arjun.infotech.bondera.Webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.arjun.infotech.bondera.Model.DataPart;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Admin on 15-11-2017.
 */

public class VolleyUtils {

    public static void GET_METHOD(Context context, String url, final VolleyResponseListener listener, final WebCallid webcallid, boolean isShowProgress) {

        if (context != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context, android.support.v4.app.DialogFragment.STYLE_NO_TITLE);
            progressDialog.setCancelable(false);
//        final ProgressDialog process = new ProgressDialog(context);
//        process.setCancelable(false);
            if (isShowProgress) {
                progressDialog.show();
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                progressDialog.setContentView(R.layout.custom_progress_dialog);
            }

            // Initialize a new StringRequest
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            listener.onResponse(response, webcallid);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            listener.onError(error.toString());

                        }
                    });

            // Access the RequestQueue through singleton class.
            MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        }
    }

    public static void POST_METHOD(final Activity context, String url, JSONObject requestJson, final Map<String, String> hashMap,
                                   final VolleyResponseListener listener, final WebCallid webcallid, final boolean isShowProgress) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
//        final ProgressDialog process = new ProgressDialog(context);
//        process.setCancelable(false);
        if (isShowProgress) {
            progressDialog.show();
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            progressDialog.setContentView(R.layout.custom_progress_dialog);
        }


        if (hashMap != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    listener.onResponse(response, webcallid);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    listener.onError(error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return hashMap;
                }
            };

            MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    requestJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    listener.onResponse(response, webcallid);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    listener.onError(error.toString());
                }
            });

            // Access the RequestQueue through singleton class.
            MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }


    public static void POST_MULTIPART(Context context, String url, final Map<String, String> params,
                                      final Map<String, DataPart> multipartParams,
                                      final VolleyResponseListener listener, final WebCallid webcallid, boolean isShowProgress) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setCancelable(false);
        if (isShowProgress && progressDialog != null) {
            progressDialog.show();
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            progressDialog.setContentView(R.layout.custom_progress_dialog);
        }


        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        listener.onResponse(new String(response.data), webcallid);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        listener.onError(error.toString());
                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
        };

        MySingleton.getInstance(context).addToRequestQueue(volleyMultipartRequest);

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
