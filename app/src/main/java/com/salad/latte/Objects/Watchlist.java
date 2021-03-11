package com.salad.latte.Objects;

public class Watchlist {
    public String icon = "";
    public String ticker = "";
    public String targetEntry = "";
    public String allocation =  "";
    public String buySellOrHold = "";
    public String entryDate = "";
    public Watchlist(){
        //

    }

    public Watchlist(String i, String t, String te, String a, String b, String e){
     icon = i;
     ticker = t;
     targetEntry = te;
     allocation = a;
     buySellOrHold = b;
     entryDate = e;
    }
}
