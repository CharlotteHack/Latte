package com.salad.latte.Objects;

public class Pie {
    public String icon = "";
    public String ticker = "";
    public String entryDate = "";
    public String entryPrice = "";
    public String currentPrice = "";
    public String allocation = "";

    public Pie(String icon, String ticker, String entryDate, String entryPrice, String currentPrice, String allocation) {
        this.icon = icon;
        this.ticker = ticker;
        this.entryDate = entryDate;
        this.entryPrice = entryPrice;
        this.currentPrice = currentPrice;
        this.allocation = allocation;
    }
}