package com.lx.talcar.response


import com.google.gson.annotations.SerializedName

data class TmapSearchResponse(
    @SerializedName("searchPoiInfo")
    val searchPoiInfo: SearchPoiInfo
) {
    data class SearchPoiInfo(
        @SerializedName("totalCount")
        val totalCount: String,
        @SerializedName("count")
        val count: String,
        @SerializedName("page")
        val page: String,
        @SerializedName("pois")
        val pois: Pois
    ) {
        data class Pois(
            @SerializedName("poi")
            val poi: List<Poi>
        ) {
            data class Poi(
                @SerializedName("id")
                val id: String,
                @SerializedName("pkey")
                val pkey: String,
                @SerializedName("navSeq")
                val navSeq: String,
                @SerializedName("collectionType")
                val collectionType: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("telNo")
                val telNo: String,
                @SerializedName("frontLat")
                val frontLat: String,
                @SerializedName("frontLon")
                val frontLon: String,
                @SerializedName("noorLat")
                val noorLat: String,
                @SerializedName("noorLon")
                val noorLon: String,
                @SerializedName("upperAddrName")
                val upperAddrName: String,
                @SerializedName("middleAddrName")
                val middleAddrName: String,
                @SerializedName("lowerAddrName")
                val lowerAddrName: String,
                @SerializedName("detailAddrName")
                val detailAddrName: String,
                @SerializedName("mlClass")
                val mlClass: String,
                @SerializedName("firstNo")
                val firstNo: String,
                @SerializedName("secondNo")
                val secondNo: String,
                @SerializedName("roadName")
                val roadName: String,
                @SerializedName("firstBuildNo")
                val firstBuildNo: String,
                @SerializedName("secondBuildNo")
                val secondBuildNo: String,
                @SerializedName("radius")
                val radius: String,
                @SerializedName("bizName")
                val bizName: String,
                @SerializedName("upperBizName")
                val upperBizName: String,
                @SerializedName("middleBizName")
                val middleBizName: String,
                @SerializedName("lowerBizName")
                val lowerBizName: String,
                @SerializedName("detailBizName")
                val detailBizName: String,
                @SerializedName("rpFlag")
                val rpFlag: String,
                @SerializedName("parkFlag")
                val parkFlag: String,
                @SerializedName("detailInfoFlag")
                val detailInfoFlag: String,
                @SerializedName("desc")
                val desc: String,
                @SerializedName("dataKind")
                val dataKind: String,
                @SerializedName("newAddressList")
                val newAddressList: NewAddressList,
                @SerializedName("evChargers")
                val evChargers: EvChargers
            ) {
                data class NewAddressList(
                    @SerializedName("newAddress")
                    val newAddress: List<NewAddres>
                ) {
                    data class NewAddres(
                        @SerializedName("centerLat")
                        val centerLat: String,
                        @SerializedName("centerLon")
                        val centerLon: String,
                        @SerializedName("frontLat")
                        val frontLat: String,
                        @SerializedName("frontLon")
                        val frontLon: String,
                        @SerializedName("roadName")
                        val roadName: String,
                        @SerializedName("bldNo1")
                        val bldNo1: String,
                        @SerializedName("bldNo2")
                        val bldNo2: String,
                        @SerializedName("roadId")
                        val roadId: String,
                        @SerializedName("fullAddressRoad")
                        val fullAddressRoad: String
                    )
                }

                data class EvChargers(
                    @SerializedName("evCharger")
                    val evCharger: List<Any>
                )
            }
        }
    }
}