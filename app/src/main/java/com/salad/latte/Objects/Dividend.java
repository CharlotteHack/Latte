package com.salad.latte.Objects;

public class Dividend {

    String date;
    String timestamp;
    String dividendAmount;
    String ticker;

    public Dividend(String date, String timestamp, String dividendAmount, String ticker) {
        this.date = date;
        this.timestamp = timestamp;
        this.dividendAmount = dividendAmount;
        this.ticker = ticker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(String dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
