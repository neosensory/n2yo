package com.neosensory.n2yoexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.neosensory.n2yo.N2YO;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // obtain API key from https://n2yo.com -- be advised of rate limits on calls
    static final String APIKEY = " ";

    N2YO n2yo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        n2yo = new N2YO(APIKEY, this);
        registerReceiver(responseReceiver, new IntentFilter("gotResponse"));

        n2yo.getTle(25544);
        n2yo.getSatPositions(25544, 41.702f,-76.014f,0,2);
        n2yo.getVisualPasses(25544, 41.702f,-76.014f,0,2,5);
        n2yo.getRadioPasses(25544, 41.702f,-76.014f,0,2,0);
        n2yo.getWhatsUp(41.70f,-76.014f,0,2,0);

        Log.i("N2YO",String.valueOf(n2yo.getID("SiriusXM")));
        Log.i("N2YO",n2yo.getCategory(53));
    }
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(responseReceiver);
    }

    private final BroadcastReceiver responseReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    JSONObject receivedJSON = null;
                    Bundle bundle = intent.getExtras();
                    N2YO.CallId requestType = (N2YO.CallId) bundle.getSerializable("requestType");
                    try {
                        receivedJSON = new JSONObject((String) bundle.getString("responseObject"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String receivedJSONString = receivedJSON.toString();
                    Log.i("N2YO","API Call Type: " + requestType.name() + " Response: " + receivedJSONString);
                }
            };

}
