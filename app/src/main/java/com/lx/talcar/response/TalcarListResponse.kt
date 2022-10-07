package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class TalcarListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("sh_id")
        val shId: Int,
        @SerializedName("mem_id")
        val memId: String,
        @SerializedName("sh_address")
        val shAddress: String,
        @SerializedName("sh_model")
        val shModel: String,
        @SerializedName("sh_car_number")
        val shCarNumber: String,
        @SerializedName("sh_title")
        val shTitle: String,
        @SerializedName("sh_post")
        val shPost: String,
        @SerializedName("sh_date")
        val shDate: String,
        @SerializedName("sh_location")
        val shLocation: ShLocation,
        @SerializedName("sh_reserve_date")
        val shReserveDate: String,
        @SerializedName("sh_return_date")
        val shReturnDate: String,
        @SerializedName("sh_reserve_time")
        val shReserveTime: String,
        @SerializedName("sh_return_time")
        val shReturnTime: String,
        @SerializedName("sh_price")
        val shPrice: Int,
        @SerializedName("sh_photo")
        val shPhoto: Any,
        @SerializedName("longitude")
        val longitude: Double,
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("distance")
        val distance: Double
    ) {
        data class ShLocation(
            @SerializedName("x")
            val x: Double,
            @SerializedName("y")
            val y: Double
        )
    }
}