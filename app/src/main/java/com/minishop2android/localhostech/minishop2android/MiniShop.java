package com.minishop2android.localhostech.minishop2android;

import android.content.Context;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MiniShop {

    public static String api_server = "hackside.ru";
    public static  String api_protocol = "http";

    /**
     * Returns JSONArray of products. It's just the wrapper for api method
     * @param ctx Current context for requests
     * @param callback callback functions that will be executed after success request
     */
    public static void getProducts(Context ctx, final ProductsCallback callback) {
        api(ctx, "getProducts", new ApiCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                callback.onSuccess(result);
            }
        });
    }


    /**
     * Universal api functions
     * @param ctx
     * @param method API method name
     * @param callback
     */
    private static void api(Context ctx, String method, final ApiCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String method_name = method;
        String url =api_protocol+"://"+api_server+"/api/"+method_name+".json";
        ArrayList<String> productListArr = new ArrayList<String>();
        final JSONArray[] dataProducts = {null};
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MY_TAG", response);

                        try {
                            dataProducts[0] = new JSONArray(response);
                            callback.onSuccess(dataProducts[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MY_TAG", error.getMessage());
            }
        });

        queue.add(stringRequest);
    }
}

/**
 *
 * Callback interfaces
 *
 */
interface ProductsCallback{
    void onSuccess(JSONArray result);
}

interface ApiCallback{
    void onSuccess(JSONArray result);
}
