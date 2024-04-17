package com.example.synctest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.synctest.MainActivity;
import com.example.synctest.R;


public class EquipmentFragment extends Fragment {

    Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_equipment, container, false);

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
}