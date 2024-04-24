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


public class BreathFragment extends Fragment {

    Button sendButton;
    RadioGroup cardioRadio, aedRadio, recoveryRadio;
    String checkedRadioText, checkedRadioText1, checkedRadioText2;
    EditText license;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_breath, container, false);

        license = (EditText) rootView.findViewById(R.id.licenseTextview);
        cardioRadio = (RadioGroup) rootView.findViewById(R.id.cardiopulmonaryRadio);
        aedRadio = (RadioGroup) rootView.findViewById(R.id.defibrillatorRadio);
        recoveryRadio = (RadioGroup) rootView.findViewById(R.id.recoveryRadio);



        cardioRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton cardioRadioButton = (RadioButton) rootView.findViewById(checkedId);
                checkedRadioText = cardioRadioButton.getText().toString();
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

        aedRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton aedRadioButton = (RadioButton) rootView.findViewById(checkedId);
                checkedRadioText1 = aedRadioButton.getText().toString();
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

        recoveryRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton recoveryRadioButton = (RadioButton) rootView.findViewById(checkedId);
                checkedRadioText2 = recoveryRadioButton.getText().toString();
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

                String selectedCardioRadio = checkedRadioText != null ? checkedRadioText : "";
                String selectedAedRadio = checkedRadioText1 != null ? checkedRadioText1 : "";
                String selectedRecoveryRadio = checkedRadioText2 != null ? checkedRadioText2 : "";


                // Call the method to send the flag to MySQL using Volley
                sendBreathInformation(selectedCardioRadio, selectedAedRadio, selectedRecoveryRadio, licenseCode);
                license.setText("");
            }
        });

        return rootView;
    }


    private void sendBreathInformation(final String cardiopulmonary, String aed, String recovery, String license) {
        String url = "http://192.168.1.13/syncdemo/noBreathInsident.php";

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
                params.put("cardiopulmonary", cardiopulmonary); // Add the flag to the request parameters
                params.put("aed", aed);
                params.put("recovery", recovery);
                params.put("license", license);
                return params;
            }
        };

        // Add the request to the RequestQueue
        MySingleton.getInstance(requireContext()).addToRequestQue(stringRequest);
    }
}