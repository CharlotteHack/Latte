package com.salad.latte.Tutorial;

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
                Log.d("TutorialPageThree","Click Registered");
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        });
//        secondImage = v.findViewById(R.id.tutorial_pagetwo);


        return v;
    }
}
