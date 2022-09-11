package com.salad.latte.ClientManagement;

public class Transaction {
    String action = "";
    String dateRequested = "";
    String dateFullfilled = "";
    String amount = "";
    String status = "";

    public Transaction(String a, String dr, String df, String am, String s){
        action = a;
        dateRequested = dr;
        dateFullfilled = df;
        amount = am;
        status = s;
    }
}
