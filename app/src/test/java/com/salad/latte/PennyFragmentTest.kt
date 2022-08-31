package com.salad.latte

import com.salad.latte.Objects.DailyWatchlistItem
import junit.framework.Assert.assertEquals
import junit.framework.TestCase
import org.junit.Test

class PennyFragmentTest {
    lateinit var penny :DailyWatchlistItem
    @Test
    fun isAdditionCorrect() {
        assertEquals(1, 1 + 0)
    }

    @Test
    fun testPennyObject(){
        penny = DailyWatchlistItem("","Test",2.0f,0.0f,4.0f,100.0f,"8/30/2022")
        assertEquals(penny.ticker,"Test")
    }



}