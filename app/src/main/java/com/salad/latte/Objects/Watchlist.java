package com.salad.latte.Objects;

public class Watchlist {
    public String icon = "";
    public String ticker = "";
    public String targetEntry = "";
    public String currentPrice = "";
    public String allocation =  "";
    public String entryDate = "";
    public Watchlist(){
        //

    }

    public Watchlist(String i, String t, String te, String cp, String a,  String e){
     icon = i;
     ticker = t;
     targetEntry = te;
     currentPrice = cp;
     allocation = a;
     entryDate = e;
    }
}
