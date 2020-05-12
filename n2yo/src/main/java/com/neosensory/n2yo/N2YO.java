package com.neosensory.n2yo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class N2YO {

    public static List<String> CATEGORIESLIST;

    public static final String[] CATEGORIES = {"ANY",
    "Brightest",
    "ISS",
    "Weather",
    "NOAA",
    "GOES",
    "Earth Resources",
    "Search and Rescue",
    "Disaster Monitoring",
    "Tracking Data Relay",
    "Geostationary",
    "Intelsat",
    "Gorizont",
    "Raduga",
    "Molniya",
    "Iridium",
    "Orbcomm",
    "Globalstar",
    "Amateur Radio",
    "Experimental",
    "GPS Ops",
    "Glosnass Ops",
    "Galileo",
    "Augmentation System",
    "Navy Nav",
    "Russian LEO Nav",
    "Space Earth Sci",
    "Geodetic",
    "Engineering",
    "Education",
    "Military",
    "Radar Calibration",
    "CubeSats",
    "SiriusXM",
    "TV",
    "Beidou Nav Sys",
    "Yaogan",
    "Westford Needles",
    "Parus",
    "Strela",
    "Gonets",
    "Tsiklon",
    "Tsikada",
    "O3B Networks",
    "Tselina",
    "Celestis",
    "IRNSS",
    "QZSS",
    "Flock",
    "Lemur",
    "GPS Constellation",
    "Glosnass Constellation",
    "Starlink",
    "OneWeb"
  };

  public enum CallId {
      TLE,
      POSITIONS,
      VISUALPASSES,
      RADIOPASSES,
      WHATSUP
  }

  private String apiKey = "";
  private RequestQueue mRequestQueue;
  protected Context context;

  public String getCategory(int id){
      if((id>=0)&&(id<=53)){
          return CATEGORIESLIST.get(id);
    } else {
      return "Error, invalid ID";
          }

  }

  public int getID(String type){
      return CATEGORIESLIST.indexOf(type);
  }

  public void getSetApiKey(String key){
      apiKey = key;
  }

  private void broadcastResponse(JSONObject response, CallId callType){
      Intent intent = new Intent("gotResponse");
      Bundle bundle = new Bundle();
      bundle.putString("responseObject", response.toString());
      bundle.putSerializable("requestType", callType);
      intent.putExtras(bundle);
      context.sendBroadcast(intent);
  }

    public void getWhatsUp(float obsLat, float obsLng, float obsAlt, int searchRadius, int categoryId){
        String requestString =
                "https://www.n2yo.com/rest/v1/satellite/above/"
                        + String.valueOf(obsLat)
                        + "/"
                        + String.valueOf(obsLng)
                        + "/"
                        + String.valueOf(obsAlt)
                        + "/"
                        + String.valueOf(searchRadius)
                        + "/"
                        + String.valueOf(categoryId)
                        + "/&apiKey="
                        + apiKey;
        request(requestString,CallId.WHATSUP);
    }

    public void getRadioPasses(int satID, float obsLat, float obsLng, float obsAlt, int days, int minElevation){
        String requestString =
                "https://www.n2yo.com/rest/v1/satellite/radiopasses/"
                        + String.valueOf(satID)
                        + "/"
                        + String.valueOf(obsLat)
                        + "/"
                        + String.valueOf(obsLng)
                        + "/"
                        + String.valueOf(obsAlt)
                        + "/"
                        + String.valueOf(days)
                        + "/"
                        + String.valueOf(minElevation)
                        + "/&apiKey="
                        + apiKey;
        request(requestString,CallId.RADIOPASSES);
    }

  public void getVisualPasses(int satID, float obsLat, float obsLng, float obsAlt, int days, int minVisibility){
      String requestString =
              "https://www.n2yo.com/rest/v1/satellite/visualpasses/"
                      + String.valueOf(satID)
                      + "/"
                      + String.valueOf(obsLat)
                      + "/"
                      + String.valueOf(obsLng)
                      + "/"
                      + String.valueOf(obsAlt)
                      + "/"
                      + String.valueOf(days)
                      + "/"
                      + String.valueOf(minVisibility)
                      + "/&apiKey="
                      + apiKey;
      request(requestString,CallId.VISUALPASSES);
  }

  public void getSatPositions(int satID, float obsLat, float obsLng, float obsAlt, int seconds) {
    String requestString =
        "https://www.n2yo.com/rest/v1/satellite/positions/"
            + String.valueOf(satID)
            + "/"
            + String.valueOf(obsLat)
            + "/"
            + String.valueOf(obsLng)
            + "/"
            + String.valueOf(obsAlt)
            + "/"
            + String.valueOf(seconds)
            + "/&apiKey="
            + apiKey;
      request(requestString,CallId.POSITIONS);
  }

  public void getTle(int satID) {
    String requestString =
        "https://www.n2yo.com/rest/v1/satellite/tle/" + String.valueOf(satID) + "&apiKey=" + apiKey;
    request(requestString,CallId.TLE);
  }

  private void request(String requestString, final CallId callName) {

    JsonObjectRequest theRequest =
        new JsonObjectRequest(
            Request.Method.GET,
            requestString,
            null,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                broadcastResponse(response, callName);
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
              }
            });
    theRequest.setRetryPolicy(
        new DefaultRetryPolicy(
            6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    mRequestQueue.add(theRequest);
  }

  public N2YO(String tApikey, Context context) {
    apiKey = tApikey;
    this.context = context.getApplicationContext();
    mRequestQueue = Volley.newRequestQueue(context);
    CATEGORIESLIST = Arrays.asList(CATEGORIES);
  }
}
