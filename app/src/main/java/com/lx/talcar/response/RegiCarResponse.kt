package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class RegiCarResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("regi_id")
        val regiId: Int,
        @SerializedName("regi_mem")
        val regiMem: String,
        @SerializedName("regi_car")
        val regiCar: String,
        @SerializedName("regi_car_number")
        val regiCarNumber: String,
        @SerializedName("regi_time")
        val regiTime: String,
        @SerializedName("regi_photo")
        val regiPhoto: Any
    )
}