package com.salad.latte.Objects

import android.os.Parcel
import android.os.Parcelable

class CalculateItem (tickImg :String, tick :String, ep :Float, stb :Float, totCap :Float, alloc :Float)  : Parcelable {
    var imgUrl = tickImg
    var ticker = tick
    var entryPrice = ep
    var sharesToBuy = stb
    var totalCapital =  totCap
    var allocation = alloc

    override fun describeContents(): Int {
        return 0
    }

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat())

    companion object CREATOR : Parcelable.Creator<CalculateItem> {
        override fun createFromParcel(parcel: Parcel): CalculateItem {
            return CalculateItem(parcel)
        }

        override fun newArray(size: Int): Array<CalculateItem?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(imgUrl)
        dest.writeString(ticker)
        dest.writeFloat(entryPrice)
        dest.writeFloat(sharesToBuy)
        dest.writeFloat(totalCapital)
        dest.writeFloat(allocation)
    }

}