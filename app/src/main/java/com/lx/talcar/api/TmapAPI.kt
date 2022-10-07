package com.lx.talcar.api

import com.lx.talcar.response.TmapAddressInfoResponse
import com.lx.talcar.response.TmapSearchResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface TmapAPI {

    companion object {
        const val TMAP_KEY = "l7xxe1b752999a72440fa1e921a3f09ac1f9"
        const val MAX_PAGE_CONTENT_SIZE = 10
        const val VERSION = 1
        const val START_PAGE = 1
        const val CALLBACK_TYPE = "JSON"
    }

    @GET("tmap/pois")
    fun getSearchLocation(
        @Query("appKey") appKey: String = TMAP_KEY,
        @Query("version") version: Int = VERSION,
        @Query("callback") callback: String? = CALLBACK_TYPE,
        @Query("page") page: Int = START_PAGE,
        @Query("count") count: Int = MAX_PAGE_CONTENT_SIZE, // 한페이지에 얼마나 나타낼 지
        @Query("searchKeyword") keyword: String, // 검색어
        @Query("centerLon") currentLon: String?,
        @Query("centerLat") currentLat: String?,
        @Query("searchtypCd") searchtypCd: String = "A"
    ): Call<TmapSearchResponse>

    @GET("tmap/geo/reversegeocoding")
    fun getReverseGeoCode(
        @Header("appKey") appKey: String = TMAP_KEY,
        @Query("version") version: Int = VERSION,
        @Query("callback") callback: String? = CALLBACK_TYPE,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Call<TmapAddressInfoResponse>
}

class TmapClient {

    companion object {

        private var instance: TmapAPI? = null

        val api:TmapAPI
            get() {
                return getInstance()
            }

        @Synchronized
        fun getInstance():TmapAPI {
            if(instance == null) {
                instance = create()
            }

            return instance as TmapAPI
        }
        // 기본 URL
        private const val BASE_URL = "https://apis.openapi.sk.com"

        // 헤더 속성
        private const val CLIENT_ID = ""
        private const val CLIENT_SECRET = ""
        var userId:String = ""

        fun create(): TmapAPI {
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
                .create(TmapAPI::class.java)
        }

    }

}

//object RetrofitUtil {
//
//    val apiService: TmapAPI by lazy { getRetrofit().create(TmapAPI::class.java) }
//
//    private fun getRetrofit(): Retrofit {
//
//        return Retrofit.Builder()
//            .baseUrl(TMAP_URL)
//            .addConverterFactory(GsonConverterFactory.create()) // gson으로 파싱
//            .client(buildOkHttpClient()) // OkHttp 사용
//            .build()
//    }
//
//    private fun buildOkHttpClient(): OkHttpClient {
//        val interceptor = HttpLoggingInterceptor() // 매번 api 호출 시 마다 로그 확인 할것
//        if (BuildConfig.DEBUG) {
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//        } else {
//            interceptor.level = HttpLoggingInterceptor.Level.NONE
//        }
//        return OkHttpClient.Builder()
//            .connectTimeout(5, TimeUnit.SECONDS) // 5초 동안 응답 없으면 에러
//            .addInterceptor(interceptor)
//            .build()
//    }
//}
//
//object Url {
//    const val TMAP_URL = "https://apis.openapi.sk.com"
//
//    const val GET_TMAP_LOCATION = "tmap/pois"
//
//    const val GET_TMAP_REVERSE_GEO_CODE = "tmap/geo/reversegeocoding"
//}
