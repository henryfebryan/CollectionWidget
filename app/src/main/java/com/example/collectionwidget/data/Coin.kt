package com.example.collectionwidget.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Coin(
    @SerializedName("name")
    var name: String
) : Parcelable