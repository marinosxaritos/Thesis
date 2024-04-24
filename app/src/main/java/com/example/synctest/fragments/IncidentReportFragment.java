package com.example.synctest.fragments;

import static android.R.layout.simple_expandable_list_item_1;
import static android.R.layout.simple_spinner_item;

import android.content.Intent;
import android.media.MediaPlayer;
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

                outsideButton.setBackgroundResource(R.drawable.grap_button);
                outsideButton.setTextColor(getResources().getColor(R.color.textHover));

                insideButton.setBackgroundResource(R.drawable.submit_button);
                insideButton.setTextColor(getResources().getColor(R.color.white));

                insideButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        insideButton.setBackgroundResource(R.drawable.rounded_button);
                        insideButton.setTextColor(getResources().getColor(R.color.black));
                    }
                }, 40); // Change back after 1 second (adjust the delay as needed)
                // After a delay or when certain conditions are met, revert back to the original drawable


                loadFragment(new InsideWaterIncidentFragment());
            }
        });

        outsideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insideButton.setBackgroundResource(R.drawable.grap_button);
                insideButton.setTextColor(getResources().getColor(R.color.textHover));

                outsideButton.setBackgroundResource(R.drawable.submit_button);
                outsideButton.setTextColor(getResources().getColor(R.color.white));

                outsideButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        outsideButton.setBackgroundResource(R.drawable.rounded_button);
                        outsideButton.setTextColor(getResources().getColor(R.color.black));
                    }
                }, 40); // Change back after 1 second (adjust the delay as needed)
                // After a delay or when certain conditions are met, revert back to the original drawable



                loadFragment(new OutsideWaterIncidentFragment());
            }
        });





        nextButton = (Button) rootView.findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.lost_saga_select_2);
                mediaPlayer.start();

                /*rootView.findViewById(R.id.insideWaterButton).setVisibility(View.GONE);
                rootView.findViewById(R.id.outsideWaterButton).setVisibility(View.GONE);
                rootView.findViewById(R.id.nextBtn).setVisibility(View.GONE);*/

                hideAllViews(rootView);

                Fragment firstAidFrag = new FirstAidFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.incidentReportFragment, firstAidFrag).commit();
            }
        });


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


        return rootView;
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void hideAllViews(View rootView) {
        if (rootView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) rootView;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                child.setVisibility(View.GONE);
            }
        }
    }

}
    

