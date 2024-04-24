package com.example.synctest.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.synctest.MainActivity;
import com.example.synctest.R;
import com.example.synctest.ui.EquipmentAdapter;
import com.example.synctest.ui.EquipmentListener;
import com.example.synctest.utilities.MySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EquipmentFragment extends Fragment implements EquipmentListener {

    Button sendButton;
    Button backButton;
    TextView tower;
    TextView license;
    RecyclerView recyclerView;
    EquipmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_equipment, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        setRecyclerView();

        tower = (EditText) rootView.findViewById(R.id.towerTextview);
        license = (EditText) rootView.findViewById(R.id.licenseTextview);

        backButton = (Button) rootView.findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.lost_saga_select_2);
                mediaPlayer.start();

                // Create an intent to start the main activity
                Intent intent = new Intent(requireContext(), MainActivity.class);
                // Clear the back stack so that the main activity becomes the only activity in the stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // Start the main activity
                startActivity(intent);
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

                ArrayList<String> selectedEquipment = adapter.arrayList_0;
                String towerName = tower.getText().toString();
                String licenseCode = license.getText().toString();



                // Call the method to send the flag to MySQL using Volley
                sendEquipmentInformation(String.valueOf(selectedEquipment), towerName, licenseCode);
                tower.setText("");
                license.setText("");
            }
        });

        return rootView;
    }


    private ArrayList<String> getEquipmentData() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Αδιάβροχο φακό");
        arrayList.add("Κιάλια");
        arrayList.add("Ναυαγοσωτικο Σωσίβιο");
        arrayList.add("Σανίδα Διάσωσης");
        arrayList.add("Σωστικός Σωλήνας");
        arrayList.add("Φορητό Φαρμακείο");
        arrayList.add("Αυτόματο Απινιδωτή");
        arrayList.add("Πτυσσόμενο Φορείο");
        arrayList.add("Βατραχοπέδιλα");
        arrayList.add("Τηλεβόα");
        arrayList.add("Σφυρίχτρα");
        arrayList.add("Καταδυτικό Μαχαίρι");
        return arrayList;
    }
    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new EquipmentAdapter(requireContext(), getEquipmentData(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEquipmentChange(ArrayList<String> arrayList) {
        Toast.makeText(requireContext(), arrayList.toString(), Toast.LENGTH_SHORT).show();
    }

    private void sendEquipmentInformation(final String equipment, String tower, String license) {
        String url = "http://192.168.1.13/syncdemo/equipment.php";

        String equipmentString = TextUtils.join(", ", new String[]{equipment});

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
                params.put("equipment", equipmentString); // Add the flag to the request parameters
                params.put("tower", tower);
                params.put("license", license);
                return params;
            }
        };

        // Add the request to the RequestQueue
        MySingleton.getInstance(requireContext()).addToRequestQue(stringRequest);
    }
}