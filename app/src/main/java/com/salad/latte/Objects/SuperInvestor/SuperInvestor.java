package com.salad.latte.Objects.SuperInvestor;

import android.util.Log;

import java.util.ArrayList;

public class SuperInvestor implements Comparable{
    
    String assetsUnderManagement;
    String companyName;
    String linkToHoldings;
    String numOfStocks;
    String shortName;
    ArrayList<Holding> holdings;
    ArrayList<SIActivity> activities;
    float totalReturn = 0;

    public float getTotalReturn() {
        return totalReturn;
    }

    public void calculateReturn(){
        float ret = 0.0f;
//        Log.d("SuperInvestor ", "Num stocks: " + getNumOfStocks() + " Company name "+getCompanyName());

//        if(superInvestor.getCompanyName().equals("Mohnish Pabrai - Pabrai Investments")) {
//        Log.d("SuperInvestorAdapter","Holdings size for: "+superInvestor.getCompanyName()+" | "+superInvestor.getHoldings().size());
        for (int i = 0; i < getHoldings().size(); i++)
        {
            Holding holding = getHoldings().get(i);
            if(!holding.getStock().equals("RDSB")) {
                float stepOne = Float.parseFloat(holding.getPreviousClose()) - Float.parseFloat(holding.getReportedPrice().replace("$", ""));
                float curRet = (stepOne / Float.parseFloat(holding.getReportedPrice().replace("$", ""))) * Float.parseFloat(holding.getPortfolioWeight())/10;
//                Log.d("SuperInvestor", "-----------------");
//                Log.d("SuperInvestor: ", "Reported price: " + holding.getReportedPrice());
//                Log.d("SuperInvestor: ", "Portfolio Weight: " + Float.parseFloat(holding.getPortfolioWeight())/10);
//                Log.d("SuperInvestor: ", "Previous close: " + holding.getPreviousClose());
//                Log.d("SuperInvestor: ", "Cur Ret: " + curRet);
                ret += curRet;
            }


//            Log.d("SuperInvestor: ", "On Holding: "+i);
        }
        String result = String.format("%.2f", ret * 10);
//        Log.d("SuperInvestor: ", "Total Return " + result);

        totalReturn = Float.parseFloat(result);
//            }
//    return 0;
    }

    public void setTotalReturn(float totalReturn) {
        this.totalReturn = totalReturn;
    }

    @Override
    public int compareTo(Object o) {
        if(this.totalReturn < ((SuperInvestor) o).totalReturn){
            return 1;
        }
        else if (this.totalReturn > ((SuperInvestor) o).totalReturn){
            return -1;
        }
        return 0;
    }

    public SuperInvestor(String assetsUnderManagement, String companyName, String linkToHoldings, String numOfStocks, String shortName) {
        this.assetsUnderManagement = assetsUnderManagement;
        this.companyName = companyName;
        this.linkToHoldings = linkToHoldings;
        this.numOfStocks = numOfStocks;
        this.shortName = shortName;
        holdings = new ArrayList<>();
        activities = new ArrayList<>();
    }

//    public void printInfo(){
//        Log.d("SuperInvestor", "Company Name: "+this.companyName);
//    }

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
