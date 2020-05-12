package com.neosensory.n2yo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class N2YO {
    static final String[] CATEGORIES = {"Brightest", "ISS", "Weather",
            "NOAA", "GOES", "Earth Resources", "Search and Rescue", "Disaster Monitoring",
            "Tracking Data Relay", "Geostationary", "Intelsat", "Gorizont", "Raduga", "Molniya",
            "Iridium", "Orbcomm", "Globalstar", "Amateur Radio", "Experimental", "GPS Ops",
            "Glosnass Ops", "Galileo", "Augmentation System", "Navy Nav", "Russian LEO Nav",
            "Space Earth Sci", "Geodetic", "Engineering", "Education", "Military", "Radar Calibration",
            "CubeSats", "SiriusXM", "TV", "Beidou Nav Sys", "Yaogan", "Westford Needles", "Parus",
            "Strela", "Gonets", "Tsiklon", "Tsikada", "O3B Networks", "Tselina", "Celestis", "IRNSS",
            "QZSS", "Flock", "Lemur", "GPS Constellation", "Glosnass Constellation", "Starlink",
            "OneWeb"};

    private String apiKey="";
    private RequestQueue mRequestQueue;
    protected Context context;

    // create an Intent to broadcast Neosensory CLI Output.
    private void broadcastWhatsUp(JSONObject whatsUp) {
        Intent intent = new Intent("gotWhatsUp");
        intent.putExtra("whatsUp", whatsUp);
        context.sendBroadcast(intent);
    }



    public N2YO(String tApikey, Context context){
        apiKey = tApikey;
        this.context = context.getApplicationContext();

        mRequestQueue = Volley.newRequestQueue(context);

        String testN2YO = "https://www.n2yo.com/rest/v1/satellite/tle/25544&apiKey=HNJTUZ-NG73US-PMNGZ7-4DOC";
        JsonObjectRequest TestRequest =
                new JsonObjectRequest(
                        Request.Method.GET,
                        ISS_FeedUrl,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    x = (float) response.getDouble("latitude");
                                    y = (float) response.getDouble("longitude");
                                    Log.i("N2YO", String.valueOf(x));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });
        mRequestQueue.add(TestRequest);

    }


}
