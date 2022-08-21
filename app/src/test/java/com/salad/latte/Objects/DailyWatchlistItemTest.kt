package com.salad.latte.Objects

import junit.framework.TestCase

class DailyWatchlistItemTest : TestCase() {

    var testCase = DailyWatchlistItem("Test","AAPL",0.0f,0.0f,0.0f,0.0f,"")
    //Given/When/Then
    fun testGetImgUrl() {}
    fun testGetEntryPrice() {
        assertEquals(testCase.ticker, "AAPL")
    }


}