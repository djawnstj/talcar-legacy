package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class CUDReponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: Data
) {
    data class Data(
        @SerializedName("fieldCount")
        val fieldCount: Int,
        @SerializedName("affectedRows")
        val affectedRows: Int,
        @SerializedName("insertId")
        val insertId: Int,
        @SerializedName("serverStatus")
        val serverStatus: Int,
        @SerializedName("warningCount")
        val warningCount: Int,
        @SerializedName("message")
        val message: String,
        @SerializedName("protocol41")
        val protocol41: Boolean,
        @SerializedName("changedRows")
        val changedRows: Int
    )
}