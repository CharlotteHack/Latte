package com.salad.latte.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.Hold;
import com.salad.latte.MainActivity;
import com.salad.latte.Objects.SuperInvestor.Holding;
import com.salad.latte.Objects.SuperInvestor.SuperInvestor;
import com.salad.latte.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SuperInvestorPositionsAdapter extends ArrayAdapter<Holding> {
    ArrayList<Holding> holdings;
    Context ctx;
    int res;
    public SuperInvestorPositionsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Holding> objects) {
        super(context, resource, objects);
        holdings = objects;
        this.ctx =  context;
        res = resource;


    }

    @Override
    public int getCount() {
        return holdings.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_superinvestor_positions, parent, false);
        FloatingActionButton icon = view.findViewById(R.id.fab_positions_icon);
        Holding holding = holdings.get(position);
//        superInvestor.printInfo();
        ((TextView) view.findViewById(R.id.tv_positions_ticker)).setText(holding.getStock());
        if(holding.getRecentActivity().length() > 0){
            ((TextView) view.findViewById(R.id.tv_positions_recenta)).setText(holding.getRecentActivity());
        }
        else{
            ((TextView) view.findViewById(R.id.tv_positions_recenta)).setText("-");
        }

        ((TextView) view.findViewById(R.id.tv_positions_ytd)).setText(holding.getTotalReturn()+"%");
        try {
            if(holding.getLogo_url().length() > 0) {
                Picasso.get().load(holding.getLogo_url()).into(icon);
            }
            else
            {
                icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.dollar));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
}
