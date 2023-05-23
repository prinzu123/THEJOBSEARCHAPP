package com.example.thejobsearchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class apply extends AppCompatActivity {

    private static final String TAG = apply.class.getSimpleName();
    private static final String URL = "http://192.168.254.101/basicjobapp/get_job_details.php";

    private TextView jobTextView;
    private TextView aboutJobTextView;
    private TextView entryLevelTextView;
    private TextView skillsTextView;
    private TextView employerDetailsTextView;
    private Button applyNowButton;

    // Declare employerId variable
    private String employerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        jobTextView = findViewById(R.id.jobTextView);
        aboutJobTextView = findViewById(R.id.aboutJobTextView);
        entryLevelTextView = findViewById(R.id.entryLevelTextView);
        skillsTextView = findViewById(R.id.skillsTextView);
        employerDetailsTextView = findViewById(R.id.employerDetailsTextView);
        applyNowButton = findViewById(R.id.applynow);

        Intent intent = getIntent();
        String selectedJob = intent.getStringExtra("selectedItem");

        if (selectedJob != null) {
            fetchJobDetails(selectedJob);
        }

        applyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(apply.this, resume.class);
                intent.putExtra("employerid", employerId);

                // Retrieve the job title from the jobTextView
                String jobTitle = jobTextView.getText().toString();
                intent.putExtra("jobtitle", jobTitle);

                startActivity(intent);
            }
        });

    }

    private void fetchJobDetails(String selectedJob) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + "?job=" + selectedJob,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String jobTitle = jsonObject.getString("job");
                            String aboutJob = jsonObject.getString("about_job");
                            String entryLevel = jsonObject.getString("entry_level");
                            String skills = jsonObject.getString("skills");
                            employerId = jsonObject.getString("employerid"); // Assign value to employerId

                            // Retrieve employer details
                            JSONObject employerObject = jsonObject.getJSONObject("employer");
                            String firstName = employerObject.getString("employers_firstname");
                            String lastName = employerObject.getString("employers_lastname");
                            String email = employerObject.getString("email");
                            String companyName = employerObject.getString("company_name");

                            jobTextView.setText(jobTitle);
                            aboutJobTextView.setText("ABOUT JOB: " + aboutJob);
                            entryLevelTextView.setText("ENTRY LEVEL: " + entryLevel);
                            skillsTextView.setText("SKILLS NEEDED: " + skills);

                            // Display employer details
                            String employerDetails = "Employer: " + firstName + " " + lastName +
                                    "\nEmail: " + email +
                                    "\nCompany: " + companyName +
                                    "\nEmployer ID: " + employerId;
                            employerDetailsTextView.setText(employerDetails);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
