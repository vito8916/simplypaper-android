package com.dev.victor.spaper.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.victor.spaper.MainActivity;
import com.dev.victor.spaper.R;
import com.dev.victor.spaper.util.AppController;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vic_a on 22/5/2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegiIntentService";
    private static final String pathToServer = "Path to registeruser.php";
    // private RequestParams params;
    String token;
    String activarGCM;
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // Initially this call goes out to the network to retrieve the token, subsequent calls are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);
            // save token
            // sharedPreferences.edit().putString(GCM_TOKEN, token).apply();


            sendRegistrationToServer(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {

        activarGCM = activarGCM();
        String tag_json_obj = "json_obj_req";

        String url = GcmConfig.BASE_URL+GcmConfig.REGISTRATION_URL;
        Log.d("URLVOLLEY", url);


        /*JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(
                        Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //...
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //...
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", "vito8916@gmail.com");
                        params.put("app_type", GcmConfig.APP_TYPE);
                        params.put("gcm_id", token);
                        return params;
                    }


                };*/


        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ///// mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mPostCommentResponse.requestEndedWithError(error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", MainActivity.Correo);
                params.put("app_type", GcmConfig.APP_TYPE);
                params.put("gcm_id", token);
                params.put("is_active",activarGCM);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };




// Añadimos la petición a la cola de peticiones de Volley
        ////RequestQueue queue = Volley.newRequestQueue(this);
        //queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, tag_json_obj);



        // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();

    }

    private String activarGCM(){
        String isActive;
        PreferenceManager.setDefaultValues(this, R.xml.ajustes, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean syncConnPref = sharedPref.getBoolean(getString(R.string.key_switch_activarGcm),true);

        if(syncConnPref){
            isActive = "1";
        }else {
            isActive = "0";
        }


        return isActive;
    }

    public interface PostCommentResponseListener {
        public void requestStarted();
        public void requestCompleted();
        public void requestEndedWithError(VolleyError error);
    }

}