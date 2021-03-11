package com.salad.latte.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.salad.latte.Objects.ClosedPosition;
import com.salad.latte.R;

import java.util.ArrayList;
import java.util.List;

public class ClosedPostionsAdapter extends ArrayAdapter<ClosedPosition> {
    ArrayList<ClosedPosition> closedPositions;
    Context context;
    int res;
    public ClosedPostionsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ClosedPosition> objects) {
        super(context, resource, objects);
        closedPositions = objects;
        this.context = context;
        this.res = resource;
    }

    @Override
    public int getCount() {
        return closedPositions.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_closedpos,parent,false);
        return v;

    }
}