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

public class TutorialPageTwoFragment extends Fragment {
    ImageView secondImage;
    Button tutorialFinish;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tutorial_page2,container,false);
        secondImage = v.findViewById(R.id.tutorial_pagetwo);
        tutorialFinish = v.findViewById(R.id.tutorial_finish_2);
        tutorialFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TutorialPageTwo","Click Registered");

                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        });


        return v;
    }
}
