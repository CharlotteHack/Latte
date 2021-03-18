package com.salad.latte.Tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.salad.latte.R;

public class TutorialPageTwoFragment extends Fragment {
    ImageView secondImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tutorial_page2,container,false);
        secondImage = v.findViewById(R.id.tutorial_pagetwo);


        return v;
    }
}
