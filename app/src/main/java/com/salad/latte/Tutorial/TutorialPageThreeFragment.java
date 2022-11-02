package com.salad.latte.Tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.salad.latte.ClientManagement.ActivityClient;
import com.salad.latte.R;

public class TutorialPageThreeFragment extends Fragment {
    ImageView thirdImage;
    Button finishTutorial;
    TutorialPageThreeFragment frag;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tutorial_page3,container,false);
        finishTutorial = v.findViewById(R.id.tutorial_finish);
        finishTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreference =  getActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putBoolean("isTutorialComplete",true);
                editor.apply();
                Log.d("TutorialPageThree","Click Registered");
                Intent i = new Intent(getActivity(), ActivityClient.class);
                startActivity(i);
            }
        });
//        secondImage = v.findViewById(R.id.tutorial_pagetwo);


        return v;
    }
}
