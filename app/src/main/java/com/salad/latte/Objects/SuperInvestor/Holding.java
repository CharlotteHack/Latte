package com.salad.latte.Objects.SuperInvestor;

public class Holding {
    String numShares;
    String portfolioWeight;
    String recentActivity;
    String reportedPrice;
    String stock;
    String stockLongName;
    String value;

    public Holding(String numShares, String portfolioWeight, String recentActivity, String reportedPrice, String stock, String stockLongName, String value) {
        this.numShares = numShares;
        this.portfolioWeight = portfolioWeight;
        this.recentActivity = recentActivity;
        this.reportedPrice = reportedPrice;
        this.stock = stock;
        this.stockLongName = stockLongName;
        this.value = value;
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

