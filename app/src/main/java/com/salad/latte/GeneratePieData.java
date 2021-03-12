package com.salad.latte;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class GeneratePieData {
    GeneratePieData(){

    }
    protected static PieData generatePieData(Context c,int allocatedPos) {

        int count = 4;
        ArrayList <PieEntry> entries1 = new ArrayList <PieEntry> ();
        ArrayList < String > xVals = new ArrayList < String > ();



            xVals.add("Allocated");
            entries1.add(new PieEntry(Float.parseFloat(allocatedPos*2+""),"Allocated"));
            entries1.add(new PieEntry(100-(allocatedPos*2),"Unallocated"));



//        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
//        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        ds1.setSliceSpace(2f);
//        ds1.setValueTextColor(Color.WHITE);
//        ds1.setValueTextSize(12f);


        PieDataSet pieDS = new PieDataSet(entries1,"Allocations");
//        pieDS.setColors(ColorTemplate.LIBERTY_COLORS);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(c.getResources().getColor(R.color.purple_200));
        colors.add(c.getResources().getColor(R.color.purple_500));
        pieDS.setColors(colors);
        pieDS.setSliceSpace(2f);
        pieDS.setValueTextColor(Color.BLACK);
        pieDS.setValueTextSize(12f);
        PieData d = new PieData(pieDS);
        //PieData da = new PieData();

        return d;
    }
}
