package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class MemResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("mem_id")
        val memId: String,
        @SerializedName("mem_pw")
        val memPw: String,
        @SerializedName("mem_address")
        val memAddress: String,
        @SerializedName("mem_license")
        val memLicense: String,
        @SerializedName("mem_name")
        val memName: String,
        @SerializedName("mem_resident")
        val memResident: String,
        @SerializedName("mem_tel")
        val memTel: String,
        @SerializedName("mem_time")
        val memTime: String,
        @SerializedName("mem_app")
        val memApp: String?,
        @SerializedName("kakao_email")
        val kakaoEmail: String?,
        @SerializedName("google_email")
        val googleEmail: String?,
        @SerializedName("naver_email")
        val naverEmail: String?,
        @SerializedName("count")
        val count: Int
    )
}