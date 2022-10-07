package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("ch_id")
        val chId: Int,
        @SerializedName("ch_mem")
        val chMem: String,
        @SerializedName("ch_counterpart")
        val chCounterpart: String,
        @SerializedName("chat")
        val chat: String,
        @SerializedName("ch_time")
        val chTime: String
    )
}