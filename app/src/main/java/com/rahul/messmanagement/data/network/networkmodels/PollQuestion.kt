package com.rahul.messmanagement.data.network.networkmodels

import android.os.Parcel
import android.os.Parcelable

class PollQuestion() : Parcelable {
    var id: Int = 0
    var question: String = ""
    var mess : String = ""
    var isLive: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        question = parcel.readString()
        mess = parcel.readString()
        isLive = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(question)
        parcel.writeString(mess)
        parcel.writeInt(isLive)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PollQuestion> {
        override fun createFromParcel(parcel: Parcel): PollQuestion {
            return PollQuestion(parcel)
        }

        override fun newArray(size: Int): Array<PollQuestion?> {
            return arrayOfNulls(size)
        }
    }

}