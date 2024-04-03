package com.example.synctest.fragments;

import static android.R.layout.simple_spinner_item;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.synctest.R;
import com.example.synctest.utilities.MySingleton;

import java.util.HashMap;
import java.util.Map;


public class OutsideWaterIncidentFragment extends Fragment {

    Spinner spinner;
    Spinner causeSpinner;
    EditText distance;
    TextView rescued;
    Button sendButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outside_water_incident, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.flagSpinner);

        String[] flag = {"Red", "Yellow", "Green"};
        ArrayAdapter<String> flagAdapter = new ArrayAdapter<>(getContext(), simple_spinner_item, flag);
        spinner.setAdapter(flagAdapter);

        causeSpinner = (Spinner) rootView.findViewById(R.id.causeSpinner);

        String[] cause = {"Pathological", "Bleeding", "Burns", "Injury", "Sunstroke", "Heat stroke", "Marine life"};
        ArrayAdapter<String> causeAdapter = new ArrayAdapter<>(getContext(), simple_spinner_item, cause);
        causeSpinner.setAdapter(causeAdapter);

        distance = (EditText) rootView.findViewById(R.id.distanceEdit);

        rescued = (EditText) rootView.findViewById(R.id.rescuedEdit);

        sendButton = (Button) rootView.findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected flag from the spinner
                String selectedFlag = spinner.getSelectedItem().toString();
                String causeOfIncident = causeSpinner.getSelectedItem().toString();
                String distanceMeter = distance.getText().toString();
                String rescuedPeople = rescued.getText().toString();
                // Call the method to send the flag to MySQL using Volley
                sendOutsideWaterInformation(selectedFlag, causeOfIncident, distanceMeter, rescuedPeople);
                distance.setText("");
                rescued.setText("");
            }
        });

        return rootView;
    }

    private void sendOutsideWaterInformation(final String flag, String cause, String distance, String rescued) {
        String url = "http://192.168.1.13/syncdemo/outsideWaterInsident.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the server
                        Toast.makeText(requireContext(), "Flag sent successfully", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to another fragment or perform other actions
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("flag", flag); // Add the flag to the request parameters
                params.put("cause", cause);
                params.put("distance", distance);
                params.put("rescued", rescued);
                return params;
            }
        };

        // Add the request to the RequestQueue
        MySingleton.getInstance(requireContext()).addToRequestQue(stringRequest);
    }

}