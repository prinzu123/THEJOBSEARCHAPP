package com.example.thejobsearchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class resume extends AppCompatActivity {

    private TextView enterResumeTextView;
    private EditText resumeEditText;
    private Button submitResumeButton;

    // Declare employerId and jobTitle variables
    private String employerId;
    private String jobTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        enterResumeTextView = findViewById(R.id.enterResumeTextView);
        resumeEditText = findViewById(R.id.resumeEditText);
        submitResumeButton = findViewById(R.id.submitResumeButton);

        enterResumeTextView.setText("Enter Resume");

        employerId = getIntent().getStringExtra("employerid");
        jobTitle = getIntent().getStringExtra("jobtitle");

        if (employerId != null) {
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("employerid", employerId);
            editor.apply();
        }

        if (jobTitle != null) {
            // Do something with the job title
            // For example, set it as a hint for the resumeEditText
            resumeEditText.setHint("Applying for: " + jobTitle);
        }

        resumeEditText.setTextSize(20);
        resumeEditText.setHint("Start typing your resume here...");

        submitResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitResume();
            }
        });
    }

    private void submitResume() {
        String resumeText = resumeEditText.getText().toString();
        String url = "http://192.168.254.101/basicjobapp/submit_resume.php"; // Replace with the actual URL of your PHP script

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the PHP script
                        Log.d("SubmitResume", response);
                        // Display a success message or perform other actions
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("SubmitResume", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("resume_text", resumeText);
                params.put("employer_id", employerId);
                params.put("job_title", jobTitle);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
