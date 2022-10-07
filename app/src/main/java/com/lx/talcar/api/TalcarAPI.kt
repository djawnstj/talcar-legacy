package com.lx.talcar.api

import com.lx.talcar.response.*
import com.lx.talcar.util.AppData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface TalcarAPI {

    @GET("getTalcarList")
    fun getTalcarList(
        @Query("point") point:String? = null,
        @Query("reserveDate1") reserveDate1:String? = null,
        @Query("reserveDate2") reserveDate2:String? = null,
        @Query("reserveDate3") reserveDate3:String? = null,
        @Query("reserveDate4") reserveDate4:String? = null,
        @Query("reserveTime1") reserveTime1:String? = null,
        @Query("reserveTime2") reserveTime2:String? = null,
        @Query("reserveTime3") reserveTime3:String? = null,
        @Query("reserveTime4") reserveTime4:String? = null,
        @Query("returnDate1") returnDate1:String? = null,
        @Query("returnDate2") returnDate2:String? = null,
        @Query("returnDate3") returnDate3:String? = null,
        @Query("returnDate4") returnDate4:String? = null,
        @Query("returnTime1") returnTime1:String? = null,
        @Query("returnTime2") returnTime2:String? = null,
        @Query("returnTime3") returnTime3:String? = null,
        @Query("returnTime4") returnTime4:String? = null
    ): Call<TalcarListResponse>

    @GET("setReservation")
    fun setReservation(
        @Query("reMem") reMem:String = AppData.memId,
        @Query("reShId") reShId:Int? = null,
        @Query("reShMem") reShMem:String? = null,
        @Query("reserveDate") reserveDate:String? = null,
        @Query("returnDate") returnDate:String? = null,
        @Query("reserveTime") reserveTime:String? = null,
        @Query("returnTime") returnTime:String? = null,
        @Query("rePrice") rePrice:Int? = null,
        @Query("reModel") reModel:String? = null,
        @Query("reCarNumber") reCarNumber:String? = null
    ): Call<CUDReponse>

    @GET("getReservation")
    fun getReservation(
        @Query("reMem") reMem:String = AppData.memId,
        @Query("reDate1") reDate1:String? = null,
        @Query("reDate2") reDate2:String? = null
    ): Call<ReservationResponse>

    @GET("insertReCheck")
    fun insertReCheck(
        @Query("reVal") reVal:String? = null,
        @Query("reId") reId:Int? = null
    ): Call<CUDReponse>

    @GET("insertReReturn")
    fun insertReReturn(
        @Query("reVal") reVal:String? = null,
        @Query("reId") reId:Int? = null,
        @Query("reReturnAddress") reReturnAddress:String? = null
    ): Call<CUDReponse>

    @GET("checkReReturn")
    fun checkReReturn(
        @Query("reId") reId:Int? = null
    ): Call<ReservationResponse>

    @GET("getReserveList")
    fun getReserveList(
        @Query("memId") memId:String? = null
    ): Call<TalcarListResponse>

    @GET("getRegiShare")
    fun getRegiShare(
        @Query("reMem") reMem:String? = null
    ): Call<ReservationResponse>

    @GET("getRegiReserve")
    fun getRegiReserve(
        @Query("reMem") reMem:String? = null
    ): Call<ReservationResponse>

    @GET("getRating")
    fun getRating(
        @Query("memId") memId:String? = null
    ): Call<RatingResponse>

    @GET("confirmReserve")
    fun confirmReserve(
        @Query("reId") reId:Int? = null,
        @Query("reVal") reVal:String? = null
    ): Call<CUDReponse>

    @GET("loginCheck")
    fun loginCheck(
        @Query("memId") memId:String? = null,
        @Query("memPw") memPw:String? = null
    ): Call<MemResponse>

    @GET("setRating")
    fun setRating(
        @Query("rtMem") rtMem:String? = null,
        @Query("rtdMem") rtdMem:String? = null,
        @Query("rating") rating:Float? = null,
        @Query("rtReId") rtReId:Int? = null,
        @Query("rtRegiId") rtRegiId:Int? = null,
        @Query("rtComment") rtComment:String? = null
    ): Call<CUDReponse>

    @GET("setSub")
    fun setSub(
        @Query("subMem") subMem:String? = null,
        @Query("reserveDate") reserveDate:String? = null,
        @Query("reserveTime") reserveTime:String? = null,
        @Query("returnDate") returnDate:String? = null,
        @Query("returnTime") returnTime:String? = null,
        @Query("location") location:String? = null,
        @Query("subAddress") subAddress:String? = null
    ): Call<CUDReponse>

    @GET("getRequestSub")
    fun getRequestSub(
       // @Query("point") point:String? = null
    ): Call<SubscriptionsResponse>

    @GET("getSub")
    fun getSub(
        @Query("subId") subId:Int? = null
    ): Call<SubscriptionsResponse>

    @GET("getRegiCar")
    fun getRegiCar(
        @Query("regiMem") regiMem:String? = null
    ): Call<RegiCarResponse>

    @GET("setReserve")
    fun setReserve(
        @Query("reMem") reMem:String? = null,
        @Query("reShMem") reShMem:String? = null,
        @Query("reReserveDate") reReserveDate:String? = null,
        @Query("reReserveTime") reReserveTime:String? = null,
        @Query("reReturnDate") reReturnDate:String? = null,
        @Query("reReturnTime") reReturnTime:String? = null,
        @Query("rePrice") rePrice:String? = null,
        @Query("reModel") reModel:String? = null,
        @Query("reCarNumber") reCarNumber:String? = null
    ): Call<CUDReponse>

    @GET("getAirportReserveList")
    fun getAirportReserveList(
        @Query("airport") airport:String? = null
    ): Call<AirportListResponse>

    @GET("getRegiCarId")
    fun getRegiCarId(
        @Query("regiCar") regiCar:String? = null,
        @Query("regiCarNumber") regiCarNumber:String? = null
    ): Call<RegiCarResponse>
}

class TalcarClient {

    companion object {

        private var instance: TalcarAPI? = null

        val api:TalcarAPI
            get() {
                return getInstance()
            }

        @Synchronized
        fun getInstance():TalcarAPI {
            if(instance == null) {
                instance = create()
            }

            return instance as TalcarAPI
        }
        // 기본 URL
        private const val BASE_URL = "http://14.55.65.168/talcar/"

        // 헤더 속성
        private const val CLIENT_ID = ""
        private const val CLIENT_SECRET = ""
        var userId:String = ""

        fun create(): TalcarAPI {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("X-Client-Id", CLIENT_ID)
                    .addHeader("X-Client-Secret", CLIENT_SECRET)
                    .addHeader("X-Client-UserId", userId)
                    .build()
                return@Interceptor it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(40, TimeUnit.SECONDS) // 타임아웃 시간 설정 40초
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TalcarAPI::class.java)
        }

    }

}