package com.salad.latte.Objects;

import android.util.Log;

public class Pie implements Comparable<Pie> {
    public String icon = "";
    public String ticker = "";
    public String entryDate = "";
    public String entryPrice = "";
    public String currentPrice = "";
    public String allocation = "";

    @Override
    public int compareTo(Pie o) {
        if(getReturn() > o.getReturn()){
//            Log.d("Pie","Comparing "+this.ticker+" to "+o.ticker+" "+this.ticker+" is larger then "+o.ticker);
            return -1;
        }else{
//            Log.d("Pie","Comparing "+this.ticker+" to "+o.ticker+" "+o.ticker+" is larger then "+this.ticker);
            return 1;
        }
    }

    public float getReturn(){
        return  ((Float.parseFloat(currentPrice)/Float.parseFloat(entryPrice))-1)*100;
    }

    public Pie(String icon, String ticker, String entryDate, String entryPrice, String currentPrice, String allocation) {
        this.icon = icon;
        this.ticker = ticker;
        this.entryDate = entryDate;
        this.entryPrice = entryPrice;
        this.currentPrice = currentPrice;
        this.allocation = allocation;
    }
}
