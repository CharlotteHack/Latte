package com.salad.latte.Objects

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class DailyWatchlistHistoricalItem(imgr :String, tick :String, date: String, entry :Float, exit :Float) {
    public var imgUrl = imgr
    public var ticker = tick
    public var dt = date
    var entryPoint = entry
    var exitPoint = exit



}