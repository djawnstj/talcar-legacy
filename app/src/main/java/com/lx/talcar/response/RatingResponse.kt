package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("rt_id")
        val rtId: Int,
        @SerializedName("rt_mem")
        val rtMem: String,
        @SerializedName("rtd_mem")
        val rtdMem: String,
        @SerializedName("rating")
        val rating: Double,
        @SerializedName("rt_re_id")
        val rtReId: Int,
        @SerializedName("re_regi_id")
        val reRegiId: Int,
        @SerializedName("re_comment")
        val reComment: String
    )
}