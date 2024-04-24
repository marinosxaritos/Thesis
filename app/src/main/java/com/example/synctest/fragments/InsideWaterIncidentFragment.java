package com.example.synctest.fragments;

import static android.R.layout.simple_list_item_checked;
import static android.R.layout.simple_spinner_item;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class InsideWaterIncidentFragment extends Fragment {

    Spinner spinner;
    Spinner rescueSpinner;
    Button sendButton;
    TextView distance;
    TextView rescued;
    TextView license;
    RadioGroup radioGroup;
    String checkedRadioText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inside_water_incident, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.flagSpinner);

        String[] flag = {"Red", "Yellow", "Green"};
        ArrayAdapter<String> flagAdapter = new ArrayAdapter<>(getContext(), R.layout.text_color_spinner, flag);
        spinner.setAdapter(flagAdapter);
        flagAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        rescueSpinner = (Spinner) rootView.findViewById(R.id.rescueSpinner);

        String[] rescue = {"Can", "Board", "Boat"};
        ArrayAdapter<String> rescueAdapter = new ArrayAdapter<>(getContext(), R.layout.text_color_spinner, rescue);
        rescueSpinner.setAdapter(rescueAdapter);
        rescueAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        distance = (EditText) rootView.findViewById(R.id.distanceEdit);
        rescued = (EditText) rootView.findViewById(R.id.rescuedEdit);
        license = (EditText) rootView.findViewById(R.id.licenseTextview);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.buoysRadio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton buoysRadioButton = (RadioButton) rootView.findViewById(checkedId);
                checkedRadioText = buoysRadioButton.getText().toString();
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.getId() == checkedId) {
                        // Set the background drawable for the selected RadioButton
                        radioButton.setBackgroundResource(R.drawable.custom_popup);
                    } else {
                        // Set the background drawable for the unselected RadioButtons
                        radioButton.setBackgroundResource(R.drawable.custom_input);
                    }
                }
            }
        });


        sendButton = (Button) rootView.findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.portalbuttonpositive);
                mediaPlayer.start();

                sendButton.setBackgroundResource(R.drawable.rounded_button);
                sendButton.setTextColor(getResources().getColor(R.color.black));
                // After a delay or when certain conditions are met, revert back to the original drawable
                sendButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendButton.setBackgroundResource(R.drawable.submit_button);
                        sendButton.setTextColor(getResources().getColor(R.color.button));
                    }
                }, 100); // Change back after 1 second (adjust the delay as needed)

                // Get the selected flag from the spinner
                String selectedFlag = spinner.getSelectedItem().toString();
                String distanceMeter = distance.getText().toString();

                String selectedBuoysRadio = checkedRadioText != null ? checkedRadioText : "";

                String rescueItem = rescueSpinner.getSelectedItem().toString();
                String rescuedPeople = rescued.getText().toString();
                String licenseCode = license.getText().toString();

                // Call the method to send the flag to MySQL using Volley
                sendInsideWaterInformation(selectedFlag, distanceMeter, selectedBuoysRadio, rescueItem, rescuedPeople, licenseCode);
                distance.setText("");
                rescued.setText("");
                license.setText("");
            }
        });

        return rootView;
    }

    private void sendInsideWaterInformation(final String flag, String distance, String buoys, String rescue, String rescued, String license) {
        String url = "http://192.168.1.13/syncdemo/insideWaterInsident.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the server
                        Toast.makeText(requireContext(), "Data sent successfully", Toast.LENGTH_SHORT).show();
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
                params.put("distance", distance);
                params.put("buoys", buoys);
                params.put("rescue", rescue);
                params.put("rescued", rescued);
                params.put("license", license);
                return params;
            }
        };

        // Add the request to the RequestQueue
        MySingleton.getInstance(requireContext()).addToRequestQue(stringRequest);
    }
}