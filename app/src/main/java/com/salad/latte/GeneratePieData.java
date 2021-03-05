package com.salad.latte;

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
    protected static PieData generatePieData() {

        int count = 4;
        ArrayList <PieEntry> entries1 = new ArrayList <PieEntry> ();
        ArrayList < String > xVals = new ArrayList < String > ();

        xVals.add("Quarter 1");
        xVals.add("Quarter 2");
        xVals.add("Quarter 3");
        xVals.add("Quarter 4");

        for (int i = 0; i < count; i++) {
            xVals.add("entry" + (i + 1));

            entries1.add(new PieEntry(13F,"label"));
        }

//        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
//        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        ds1.setSliceSpace(2f);
//        ds1.setValueTextColor(Color.WHITE);
//        ds1.setValueTextSize(12f);

        PieData d = new PieData(new PieDataSet(entries1,"label"));
        //PieData da = new PieData();

        return d;
    }
}
