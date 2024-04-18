package com.example.synctest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.synctest.MainActivity;
import com.example.synctest.R;
import com.example.synctest.ui.EquipmentAdapter;
import com.example.synctest.ui.EquipmentListener;

import java.util.ArrayList;


public class EquipmentFragment extends Fragment implements EquipmentListener {

    Button backButton;
    RecyclerView recyclerView;
    EquipmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_equipment, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        setRecyclerView();


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
}