package com.example.collectionwidget.data

import com.google.gson.annotations.SerializedName

data class RequestCoinBody(
    @SerializedName("currency")
    private val currency: String,
    @SerializedName("sort")
    private val sort: String,
    @SerializedName("order")
    private val order: String,
    @SerializedName("offset")
    private val offset: Int,
    @SerializedName("limit")
    private val limit: Int,
    @SerializedName("meta")
    private val meta: Boolean
)