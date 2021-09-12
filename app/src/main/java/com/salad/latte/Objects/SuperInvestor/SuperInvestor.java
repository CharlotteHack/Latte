package com.salad.latte.Objects.SuperInvestor;

import java.util.ArrayList;

public class SuperInvestor {
    
    String assetsUnderManagement;
    String companyName;
    String linkToHoldings;
    String numOfStocks;
    String shortName;
    ArrayList<Holding> holdings;
    ArrayList<SIActivity> activities;

    public SuperInvestor(String assetsUnderManagement, String companyName, String linkToHoldings, String numOfStocks, String shortName) {
        this.assetsUnderManagement = assetsUnderManagement;
        this.companyName = companyName;
        this.linkToHoldings = linkToHoldings;
        this.numOfStocks = numOfStocks;
        this.shortName = shortName;
        holdings = new ArrayList<>();
        activities = new ArrayList<>();
    }

    public void addActivity(SIActivity activity){
        activities.add(activity);
    }

    public void addHolding(Holding holding){
        holdings.add(holding);
    }

    public String getAssetsUnderManagement() {
        return assetsUnderManagement;
    }

    public void setAssetsUnderManagement(String assetsUnderManagement) {
        this.assetsUnderManagement = assetsUnderManagement;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLinkToHoldings() {
        return linkToHoldings;
    }

    public void setLinkToHoldings(String linkToHoldings) {
        this.linkToHoldings = linkToHoldings;
    }

    public String getNumOfStocks() {
        return numOfStocks;
    }

    public void setNumOfStocks(String numOfStocks) {
        this.numOfStocks = numOfStocks;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public ArrayList<Holding> getHoldings() {
        return holdings;
    }

    public void setHoldings(ArrayList<Holding> holdings) {
        this.holdings = holdings;
    }

    public ArrayList<SIActivity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<SIActivity> activities) {
        this.activities = activities;
    }
}
