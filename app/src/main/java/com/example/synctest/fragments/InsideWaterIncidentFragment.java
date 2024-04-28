package com.example.synctest.fragments;

import static android.R.layout.simple_list_item_checked;
import static android.R.layout.simple_spinner_item;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.synctest.MainActivity;
import com.example.synctest.R;
import com.example.synctest.utilities.MySingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class InsideWaterIncidentFragment extends Fragment {

    Spinner spinner;
    Spinner rescueSpinner;
    Button sendButton;
    TextView distance;
    TextView rescued;
    TextView license;
    TextView Time;
    int timeHour, timeMinute;
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

        Time = (TextView) rootView.findViewById(R.id.time);

        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Time.setBackgroundResource(R.drawable.submit_button);
                Time.setTextColor(getResources().getColor(R.color.button));
                // After a delay or when certain conditions are met, revert back to the original drawable
                Time.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Time.setBackgroundResource(R.drawable.time_button);
                        Time.setTextColor(getResources().getColor(R.color.text));
                    }
                }, 40); // Change back after 1 second (adjust the delay as needed)

                //Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfDay) {
                                //initialize hour and minute
                                timeHour = hourOfDay;
                                timeMinute = minuteOfDay;
                                //store hour and minute in string
                                String time = timeHour + ":" + timeMinute;
                                //Initialize 24 hours time format
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    java.util.Date date_t = f24Hours.parse(time);
                                    //Initialize 12 hours time format
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    //Set selected time on text view
                                    Time.setText(f12Hours.format(date_t));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, 12, 0, false
                );
                //Set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //Displayed previous selected time
                timePickerDialog.updateTime(timeHour, timeMinute);
                //Show dialog
                timePickerDialog.show();
            }
        });

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
                String selectedTime = Time.getText().toString();

                // Call the method to send the flag to MySQL using Volley
                sendInsideWaterInformation(selectedFlag, selectedTime, distanceMeter, selectedBuoysRadio, rescueItem, rescuedPeople, licenseCode);
                distance.setText("");
                rescued.setText("");
                license.setText("");
                Time.setText(" Select Time");
            }
        });

        return rootView;
    }

    private void sendInsideWaterInformation(final String flag, String time, String distance, String buoys, String rescue, String rescued, String license) {
        //String url = "http://192.168.1.13/syncdemo/insideWaterInsident.php";
        String url = "https://xaritos.com/insideWaterInsident.php";
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
                params.put("time", time);
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