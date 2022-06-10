package com.salad.latte.Objects

import android.os.Parcel
import android.os.Parcelable

class DailyWatchlistItem(imgr :String, tick :String, enp :Float, exp :Float,cp :Float, alloc :Float, ed :String) : Parcelable {
    public var imgUrl = imgr
    public var ticker = tick
    public var entryPrice = enp
    public var exitPrice = exp
    public var allocation = alloc
    public var currentPrice = cp;
    public var entryDate = ed;


    override fun describeContents(): Int {
        return 0
    }

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readString()!!)

    companion object CREATOR : Parcelable.Creator<DailyWatchlistItem> {
        override fun createFromParcel(parcel: Parcel): DailyWatchlistItem {
            return DailyWatchlistItem(parcel)
        }

        override fun newArray(size: Int): Array<DailyWatchlistItem?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(imgUrl)
        dest!!.writeString(ticker)
        dest!!.writeFloat(entryPrice)
        dest!!.writeFloat(exitPrice)
        dest!!.writeFloat(allocation)
        dest!!.writeFloat(currentPrice)
        dest!!.writeString(entryDate)
    }
}