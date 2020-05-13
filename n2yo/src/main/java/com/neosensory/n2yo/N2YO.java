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

  /** List of Satellite categories based on the N2YO.com API table */
  public static final String[] CATEGORIES = {
    "ANY",
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

  /**
   * CallId are enumerations for the available API calls. These are used for determining what API
   * call provided a given response.
   */
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

  /**
   * Return the type of Satellite based on its category ID
   *
   * @param id is a Satellite category ID that should be between 0 and 53 (inclusive)
   * @return a String describing the type of satellite
   */
  public String getCategory(int id) {
    if ((id >= 0) && (id <= 53)) {
      return CATEGORIESLIST.get(id);
    } else {
      return "Error, invalid ID";
    }
  }

  /**
   * Get the ID for a corresponding satellite description based on the n2yo.com categories list
   *
   * @param type is a String that must be from the CATEGORIES array/list describing the satellite
   * @return the category ID number
   */
  public int getID(String type) {
    return CATEGORIESLIST.indexOf(type);
  }

  /**
   * set the n2yo.com API key.
   *
   * @param key - the key obtained from n2yo.com
   */
  public void SetApiKey(String key) {
    apiKey = key;
  }

  // Method for broadcasting the received response
  private void broadcastResponse(JSONObject response, CallId callType) {
    Intent intent = new Intent("gotResponse");
    Bundle bundle = new Bundle();
    bundle.putString("responseObject", response.toString());
    bundle.putSerializable("requestType", callType);
    intent.putExtras(bundle);
    context.sendBroadcast(intent);
  }

  /**
   * Make the What's Up n2yo.com API call for getting a list of satellites within an area above a
   * location of interest
   *
   * @param obsLat observer latitude
   * @param obsLng observer longitude
   * @param obsAlt observer altitude (meters)
   * @param searchRadius an angle on [0 90] degrees, where 0 is directly overhead and 90 is the
   *     horizon
   * @param categoryId the category of satellites to filter on. 0 = all kinds
   */
  public void getWhatsUp(
      float obsLat, float obsLng, float obsAlt, int searchRadius, int categoryId) {
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
    request(requestString, CallId.WHATSUP);
  }

  /**
   * Provide radio communication time/location pass information for a given Satellite ID
   *
   * @param satID satellite NORAD ID
   * @param obsLat observer latitude
   * @param obsLng observer longitude
   * @param obsAlt observer altitude (meters above sea level)
   * @param days number of days of prediction (max 10)
   * @param minElevation The minimum elevation acceptable for the highest altitude point of the pass
   *     (degrees)
   */
  public void getRadioPasses(
      int satID, float obsLat, float obsLng, float obsAlt, int days, int minElevation) {
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
    request(requestString, CallId.RADIOPASSES);
  }

  /**
   * Get time/location info for optically visible passes of a given satellite
   *
   * @param satID satellite NORAD ID
   * @param obsLat observer latitude
   * @param obsLng observer longitude
   * @param obsAlt observer altitude (meters above sea level)
   * @param days number of days of prediction (max 10)
   * @param minVisibility The minimum number of seconds the satellite should be optically visible
   *     during the pass
   */
  public void getVisualPasses(
      int satID, float obsLat, float obsLng, float obsAlt, int days, int minVisibility) {
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
    request(requestString, CallId.VISUALPASSES);
  }

  /**
   * Retrieve the future positions of any satellite as footprints (latitude, longitude) to display
   * orbits on maps. Also return the satellite's azimuth and elevation with respect to the observer
   * location. Each element in the response array is one second of calculation. First element is
   * calculated for current UTC time.
   *
   * @param satID NORAD ID
   * @param obsLat observer latitude
   * @param obsLng observer longitude
   * @param obsAlt observer altitude (meters above sea level)
   * @param seconds Number of future positions to return. Each second is a position. Maximum 300 seconds.
   */
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
    request(requestString, CallId.POSITIONS);
  }

    /**
     * Get the latest two line element set (TLE) for a given satellite
     * @param satID satellite NORAD ID
     */
  public void getTle(int satID) {
    String requestString =
        "https://www.n2yo.com/rest/v1/satellite/tle/" + String.valueOf(satID) + "&apiKey=" + apiKey;
    request(requestString, CallId.TLE);
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
              public void onErrorResponse(VolleyError error) {}
            });
    theRequest.setRetryPolicy(
        new DefaultRetryPolicy(
            6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    mRequestQueue.add(theRequest);
  }

    /**
     * Constructor for the N2YO class
     * @param tApikey your N2YO API key
     * @param context the context (typically you would pass 'this' from your Activity)
     */
  public N2YO(String tApikey, Context context) {
    apiKey = tApikey;
    this.context = context.getApplicationContext();
    mRequestQueue = Volley.newRequestQueue(context);
    CATEGORIESLIST = Arrays.asList(CATEGORIES);
  }
}
