package com.salad.latte.Objects;

public class Watchlist implements Comparable<Watchlist> {
    public String icon = "";
    public String ticker = "";
    public String targetEntry = "";
    public String currentPrice = "";
    public String allocation =  "";
    public boolean inOurPortfolio = false;

    @Override
    public int compareTo(Watchlist o) {
        if(getReturn() > o.getReturn()){
//            Log.d("Pie","Comparing "+this.ticker+" to "+o.ticker+" "+this.ticker+" is larger then "+o.ticker);
            return -1;
        }else{
//            Log.d("Pie","Comparing "+this.ticker+" to "+o.ticker+" "+o.ticker+" is larger then "+this.ticker);
            return 1;
        }
    }

    public String entryDate = "";

    public float getReturn(){
        return  ((Float.parseFloat(currentPrice)/Float.parseFloat(targetEntry))-1)*100;
    }

    public Watchlist(){
        //

    }

    public Watchlist(String i, String t, String te, String cp, String a,  String e, boolean iow){
     icon = i;
     ticker = t;
     targetEntry = te;
     currentPrice = cp;
     allocation = a;
     entryDate = e;
     inOurPortfolio = iow;
    }
}
