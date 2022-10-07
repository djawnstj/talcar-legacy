package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class ReservationResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("re_id")
        val reId: Int,
        @SerializedName("re_mem")
        val reMem: String,
        @SerializedName("re_sh_id")
        val reShId: Int,
        @SerializedName("re_sh_mem")
        val reShMem: String,
        @SerializedName("re_reserve_date")
        val reReserveDate: String,
        @SerializedName("re_return_date")
        val reReturnDate: String,
        @SerializedName("re_reserve_time")
        val reReserveTime: String,
        @SerializedName("re_return_time")
        val reReturnTime: String,
        @SerializedName("re_price")
        val rePrice: String,
        @SerializedName("re_model")
        val reModel: String,
        @SerializedName("re_car_number")
        val reCarNumber: String,
        @SerializedName("re_return")
        val reReturn: String?,
        @SerializedName("re_check")
        val reCheck: String?,
        @SerializedName("re_confirm")
        val reConfirm: String?
    )
}