package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class SubscriptionsResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("sub_id")
        val subId: Int,
        @SerializedName("sub_mem")
        val subMem: String,
        @SerializedName("sub_reserve_date")
        val subReserveDate: String,
        @SerializedName("sub_reserve_time")
        val subReserveTime: String,
        @SerializedName("sub_return_date")
        val subReturnDate: String,
        @SerializedName("sub_return_time")
        val subReturnTime: String,
        @SerializedName("sub_time")
        val subTime: String,
        @SerializedName("sub_location")
        val subLocation: SubLocation,
        @SerializedName("sub_address")
        val subAddress: String,
        @SerializedName("sub_confirm")
        val subConfirm: Any,
        @SerializedName("longitude")
        val longitude: Double,
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("distance")
        val distance: Double
    ) {
        data class SubLocation(
            @SerializedName("x")
            val x: Double,
            @SerializedName("y")
            val y: Double
        )
    }
}