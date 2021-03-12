package com.salad.latte.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.salad.latte.Objects.Pie;
import com.salad.latte.R;

import java.util.ArrayList;

public class PieAdapter extends ArrayAdapter<Pie> {
    ArrayList<Pie> pies;
    Context context;
    int res;
    public PieAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Pie> objects) {
        super(context, resource, objects);
        pies = objects;
        this.context = context;
        this.res = resource;
    }

    @Override
    public int getCount() {
        return pies.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_closedpos,parent,false);
        return v;

    }
}
