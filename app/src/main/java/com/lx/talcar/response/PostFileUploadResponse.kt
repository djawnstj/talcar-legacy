package com.lx.talcar.response

import com.google.gson.annotations.SerializedName

data class PostFileUploadResponse (
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("output")
    val output: Output
) {
    data class Output(
        @SerializedName("filename")
        val filename: String
    )
}