package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class TmapAddressInfoResponse(
    @SerializedName("addressInfo")
    val addressInfo: AddressInfo
) {
    data class AddressInfo(
        @SerializedName("fullAddress")
        val fullAddress: String,
        @SerializedName("addressType")
        val addressType: String,
        @SerializedName("city_do")
        val cityDo: String,
        @SerializedName("gu_gun")
        val guGun: String,
        @SerializedName("eup_myun")
        val eupMyun: String,
        @SerializedName("adminDong")
        val adminDong: String,
        @SerializedName("adminDongCode")
        val adminDongCode: String,
        @SerializedName("legalDong")
        val legalDong: String,
        @SerializedName("legalDongCode")
        val legalDongCode: String,
        @SerializedName("ri")
        val ri: String,
        @SerializedName("bunji")
        val bunji: String,
        @SerializedName("roadName")
        val roadName: String,
        @SerializedName("buildingIndex")
        val buildingIndex: String,
        @SerializedName("buildingName")
        val buildingName: String,
        @SerializedName("mappingDistance")
        val mappingDistance: String,
        @SerializedName("roadCode")
        val roadCode: String
    )
}