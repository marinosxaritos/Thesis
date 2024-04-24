package com.example.synctest.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.synctest.R;
import com.example.synctest.utilities.MySingleton;

import java.util.HashMap;
import java.util.Map;


public class NoBreathFragment extends Fragment {

    Button sendButton;
    RadioGroup positionRadio, transportRadio, sensesRadio;
    String checkedRadioText, checkedRadioText1, checkedRadioText2;
    EditText license;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_no_breath, container, false);

        license = (EditText) rootView.findViewById(R.id.licenseTextview);
        positionRadio = (RadioGroup) rootView.findViewById(R.id.recoveryPositionRadio);
        transportRadio = (RadioGroup) rootView.findViewById(R.id.transportedRadio);
        sensesRadio = (RadioGroup) rootView.findViewById(R.id.sensationsRadio);

        positionRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton positionRadioButton = (RadioButton) rootView.findViewById(checkedId);
                checkedRadioText = positionRadioButton.getText().toString();
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

        transportRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton transportRadioButton = (RadioButton) rootView.findViewById(checkedId);
                checkedRadioText1 = transportRadioButton.getText().toString();
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

        sensesRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton sensesRadioButton = (RadioButton) rootView.findViewById(checkedId);
                checkedRadioText2 = sensesRadioButton.getText().toString();
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

                String licenseCode = license.getText().toString();

                String selectedPositionRadio = checkedRadioText != null ? checkedRadioText : "";
                String selectedTransportRadio = checkedRadioText1 != null ? checkedRadioText1 : "";
                String selectedSensesRadio = checkedRadioText2 != null ? checkedRadioText2 : "";


                // Call the method to send the flag to MySQL using Volley
                sendNoBreathInformation(selectedPositionRadio, selectedTransportRadio, selectedSensesRadio, licenseCode);
                license.setText("");
            }
        });


        return rootView;
    }

    private void sendNoBreathInformation(final String recoveryPosition, String transported, String sensations, String license) {
        String url = "http://192.168.1.13/syncdemo/breathInsident.php";

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
                params.put("recoveryPosition", recoveryPosition); // Add the flag to the request parameters
                params.put("transported", transported);
                params.put("sensations", sensations);
                params.put("license", license);
                return params;
            }
        };

        // Add the request to the RequestQueue
        MySingleton.getInstance(requireContext()).addToRequestQue(stringRequest);
    }
}