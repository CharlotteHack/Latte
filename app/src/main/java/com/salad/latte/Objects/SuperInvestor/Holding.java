package com.salad.latte.Objects.SuperInvestor;

import java.io.Serializable;

public class Holding implements Serializable {
    String numShares;
    String portfolioWeight;
    String recentActivity;
    String reportedPrice;
    String stock;
    String stockLongName;
    String value;
    String logo_url;
    String previousClose;
    Float totalReturn;

    public Holding(String numShares, String portfolioWeight, String recentActivity, String reportedPrice, String stock, String stockLongName, String value, String logo, String pc) {
        this.numShares = numShares;
        this.portfolioWeight = portfolioWeight;
        this.recentActivity = recentActivity;
        this.reportedPrice = reportedPrice;
        this.stock = stock;
        this.stockLongName = stockLongName;
        this.value = value;
        this.logo_url = logo;
        this.previousClose = pc;
    }

    public float getTotalReturn(){
        return totalReturn;
    }
    public void setTotalReturn(){
        float ret = 0.0f;
//        Log.d("SuperInvestor ", "Num stocks: " + getNumOfStocks() + " Company name "+getCompanyName());

//        if(superInvestor.getCompanyName().equals("Mohnish Pabrai - Pabrai Investments")) {
//        Log.d("SuperInvestorAdapter","Holdings size for: "+superInvestor.getCompanyName()+" | "+superInvestor.getHoldings().size());


            if(!getStock().equals("RDSB")) {
                float stepOne = Float.parseFloat(getPreviousClose()) - Float.parseFloat(getReportedPrice().replace("$", ""));
                float curRet = (stepOne / Float.parseFloat(getReportedPrice().replace("$", ""))) * Float.parseFloat(getPortfolioWeight())/10;
                ret += curRet;
            }


//            Log.d("SuperInvestor: ", "On Holding: "+i);

        String result = String.format("%.2f", ret * 10);
        totalReturn = Float.parseFloat(result);
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(String previousClose) {
        this.previousClose = previousClose;
    }

    public String getNumShares() {
        return numShares;
    }

    public void setNumShares(String numShares) {
        this.numShares = numShares;
    }

    public String getPortfolioWeight() {
        return portfolioWeight;
    }

    public void setPortfolioWeight(String portfolioWeight) {
        this.portfolioWeight = portfolioWeight;
    }

    public String getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(String recentActivity) {
        this.recentActivity = recentActivity;
    }

    public String getReportedPrice() {
        return reportedPrice;
    }

    public void setReportedPrice(String reportedPrice) {
        this.reportedPrice = reportedPrice;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStockLongName() {
        return stockLongName;
    }

    public void setStockLongName(String stockLongName) {
        this.stockLongName = stockLongName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

