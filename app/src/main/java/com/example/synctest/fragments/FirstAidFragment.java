package com.example.synctest.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.synctest.MainActivity;
import com.example.synctest.R;


public class FirstAidFragment extends Fragment {

    Button breathBtn;
    Button noBreathBtn;

    Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_first_aid, container, false);

        breathBtn = rootView.findViewById(R.id.breathButton);
        noBreathBtn = rootView.findViewById(R.id.noBreathButton);

        breathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noBreathBtn.setBackgroundResource(R.drawable.grap_button);
                noBreathBtn.setTextColor(getResources().getColor(R.color.textHover));

                breathBtn.setBackgroundResource(R.drawable.submit_button);
                breathBtn.setTextColor(getResources().getColor(R.color.button));
                // After a delay or when certain conditions are met, revert back to the original drawable
                breathBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        breathBtn.setBackgroundResource(R.drawable.rounded_button);
                        breathBtn.setTextColor(getResources().getColor(R.color.black));
                    }
                }, 40); // Change back after 1 second (adjust the delay as needed)

                loadFragment(new BreathFragment());
            }
        });

        noBreathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                breathBtn.setBackgroundResource(R.drawable.grap_button);
                breathBtn.setTextColor(getResources().getColor(R.color.textHover));

                noBreathBtn.setBackgroundResource(R.drawable.submit_button);
                noBreathBtn.setTextColor(getResources().getColor(R.color.button));
                // After a delay or when certain conditions are met, revert back to the original drawable
                noBreathBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        noBreathBtn.setBackgroundResource(R.drawable.rounded_button);
                        noBreathBtn.setTextColor(getResources().getColor(R.color.black));
                    }
                }, 40); // Change back after 1 second (adjust the delay as needed)

                loadFragment(new NoBreathFragment());
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
        fragmentTransaction.replace(R.id.frameLayout1, fragment);
        fragmentTransaction.commit();
    }


}