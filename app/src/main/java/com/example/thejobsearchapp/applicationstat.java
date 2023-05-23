package com.example.thejobsearchapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class applicationstat extends AppCompatActivity {
    private static final String URL = "http://192.168.254.101/basicjobapp/fetch_status.php";
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        statusTextView = findViewById(R.id.statusTextView);

        // Fetch status from the MySQL database
        fetchStatus();
    }

    private void fetchStatus() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Assuming the response is a JSON array with a single object containing the status field
                            JSONObject jsonObject = response.getJSONObject(0);
                            String status = jsonObject.getString("status");
                            statusTextView.setText(status);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            statusTextView.setText("Failed to parse response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        statusTextView.setText("Failed to fetch status");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}