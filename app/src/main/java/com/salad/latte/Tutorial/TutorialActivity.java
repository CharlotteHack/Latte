package com.salad.latte.Tutorial;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.salad.latte.R;

import java.util.ArrayList;

public class TutorialActivity extends FragmentActivity {

    TutorialAdapter pagerAdapter = null;
    String PREFS_FILENAME = "com.tutorial";
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ViewPager2 viewPager2 = findViewById(R.id.tutorial_vp);
        ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>() ;
        fragmentsList.add(new TutorialPageOneFragment());
        fragmentsList.add(new TutorialPageTwoFragment());
//        fragmentsList.add(new TutorialPageThreeFragment());
        pagerAdapter = new TutorialAdapter(getSupportFragmentManager(),getLifecycle(),fragmentsList);
        viewPager2.setAdapter(pagerAdapter);
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("didViewTutorial", true);
        editor.apply();

    }


    private class TutorialAdapter extends FragmentStateAdapter {
        private ArrayList<Fragment> arrayList = new ArrayList<>();
        public TutorialAdapter(FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<Fragment> fragments) {
            super(fragmentManager,lifecycle);
            arrayList = fragments;
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return arrayList.get(position);
        }

        public void addFragment(Fragment fragment) {
            arrayList.add(fragment);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}
