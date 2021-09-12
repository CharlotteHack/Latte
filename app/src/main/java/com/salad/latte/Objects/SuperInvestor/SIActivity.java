package com.salad.latte.Objects.SuperInvestor;

public class SIActivity {

    String activty_;
    String changeToPortfolio;
    String logo_url;
    String previousClose;
    String qtrYear;
    String shareCountChange;
    String stock;

    public SIActivity(String activty_, String changeToPortfolio, String logo_url, String previousClose, String qtrYear, String shareCountChange, String stock) {
        this.activty_ = activty_;
        this.changeToPortfolio = changeToPortfolio;
        this.logo_url = logo_url;
        this.previousClose = previousClose;
        this.qtrYear = qtrYear;
        this.shareCountChange = shareCountChange;
        this.stock = stock;
    }


    public String getActivty_() {
        return activty_;
    }

    public void setActivty_(String activty_) {
        this.activty_ = activty_;
    }

    public String getChangeToPortfolio() {
        return changeToPortfolio;
    }

    public void setChangeToPortfolio(String changeToPortfolio) {
        this.changeToPortfolio = changeToPortfolio;
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

    public String getQtrYear() {
        return qtrYear;
    }

    public void setQtrYear(String qtrYear) {
        this.qtrYear = qtrYear;
    }

    public String getShareCountChange() {
        return shareCountChange;
    }

    public void setShareCountChange(String shareCountChange) {
        this.shareCountChange = shareCountChange;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
