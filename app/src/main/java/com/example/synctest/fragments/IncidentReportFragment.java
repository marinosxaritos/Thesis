package com.example.synctest.fragments;

import static android.R.layout.simple_expandable_list_item_1;
import static android.R.layout.simple_spinner_item;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.synctest.MainActivity;
import com.example.synctest.R;
import com.example.synctest.contacts.DbContact;
import com.example.synctest.utilities.MySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class IncidentReportFragment extends Fragment {



    Button backButton;
    Button nextButton;
    Button insideButton;
    Button outsideButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_incident_report, container, false);



        insideButton = (Button) rootView.findViewById(R.id.insideWaterButton);
        outsideButton = (Button) rootView.findViewById(R.id.outsideWaterButton);


        insideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new InsideWaterIncidentFragment());
            }
        });

        outsideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new OutsideWaterIncidentFragment());
            }
        });


        nextButton = (Button) rootView.findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootView.findViewById(R.id.insideWaterButton).setVisibility(View.GONE);
                rootView.findViewById(R.id.outsideWaterButton).setVisibility(View.GONE);
                rootView.findViewById(R.id.nextBtn).setVisibility(View.GONE);

                Fragment firstAidFrag = new FirstAidFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.incidentReportFragment, firstAidFrag).commit();
            }
        });


        backButton = (Button) rootView.findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the main activity
                Intent intent = new Intent(requireContext(), MainActivity.class);
                // Clear the back stack so that the main activity becomes the only activity in the stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // Start the main activity
                startActivity(intent);
            }
        });


        return rootView;
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


}
    

