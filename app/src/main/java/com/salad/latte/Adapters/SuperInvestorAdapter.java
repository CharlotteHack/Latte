package com.salad.latte.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.salad.latte.MainActivity;
import com.salad.latte.Objects.SuperInvestor.Holding;
import com.salad.latte.Objects.SuperInvestor.SuperInvestor;
import com.salad.latte.R;
import com.salad.latte.SuperInvestorRecentMovesActivity;

import java.util.ArrayList;
import java.util.List;

public class SuperInvestorAdapter extends ArrayAdapter<SuperInvestor> {

    ArrayList<SuperInvestor> superInvestors;
    Context ctx;
    int res;
    public SuperInvestorAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SuperInvestor> objects) {
        super(context, resource, objects);
        superInvestors = objects;
        ctx = context;
        res = resource;
    }

    @Override
    public int getCount() {
        return superInvestors.size();
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = ((LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_superinvestor,parent,false);
        SuperInvestor superInvestor = superInvestors.get(position);
//        superInvestor.printInfo();
        ((TextView) view.findViewById(R.id.superinvestor_rank_tv)).setText("#"+(position+1));
        ((TextView) view.findViewById(R.id.superinvestor_generalInfo)).setText("Portfolio. Val "+superInvestor.getAssetsUnderManagement()+ " | "+superInvestor.getNumOfStocks()+" positions");
        ((TextView) view.findViewById(R.id.superinvestor_shortname)).setText(superInvestor.getCompanyName());
//        if(superInvestor.getTotalReturn() == 0) {
            String ret = superInvestor.getTotalReturn() + "";
            superInvestor.setTotalReturn(Float.parseFloat(ret));
            ((TextView) view.findViewById(R.id.superinvestor_return)).setText(ret + "%");
//        }
        Button recentMoves = (Button) view.findViewById(R.id.superinvestor_recentmoves_btn);
        Button positions = (Button) view.findViewById(R.id.superinvestor_postions_btn);
        recentMoves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx,"Clicked Holdings for: "+superInvestor.getCompanyName(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ctx, SuperInvestorRecentMovesActivity.class);
                i.putExtra("superInvestor",superInvestor.getCompanyName());
                i.putExtra("ytd",superInvestor.getTotalReturn()+"");
                ((MainActivity) ctx).startActivity(i);

            }
        });

        positions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ctx,"Clicked Positions for: "+superInvestor.getCompanyName(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
