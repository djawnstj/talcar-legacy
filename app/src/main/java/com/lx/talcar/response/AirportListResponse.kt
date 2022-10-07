package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class AirportListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("air_id")
        val airId: Int,
        @SerializedName("air_mem")
        val airMem: String,
        @SerializedName("air_model")
        val airModel: String,
        @SerializedName("air_car_number")
        val airCarNumber: String,
        @SerializedName("air_reserve_date")
        val airReserveDate: String,
        @SerializedName("air_reserve_time")
        val airReserveTime: String,
        @SerializedName("air_return_date")
        val airReturnDate: String,
        @SerializedName("air_return_time")
        val airReturnTime: String,
        @SerializedName("airport")
        val airport: String,
        @SerializedName("air_date")
        val airDate: String,
        @SerializedName("air_price")
        val airPrice: Int
    )
}