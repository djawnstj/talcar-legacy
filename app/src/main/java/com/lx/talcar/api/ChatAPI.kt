package com.lx.talcar.api

import com.lx.talcar.response.CUDReponse
import com.lx.talcar.response.ChatResponse
import com.lx.talcar.response.MemResponse
import com.lx.talcar.response.TalcarListResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ChatAPI {

    @GET("setAppId")
    fun setAppId(
        @Query("memId") memId:String? = null,
        @Query("memApp") memApp:String? = null
    ): Call<CUDReponse>

    @GET("getRecMem")
    fun getRecMem(
        @Query("shMem") shMem:String? = null,
    ): Call<MemResponse>

    @GET("setChat")
    fun setChat(
        @Query("chMem") chMem:String? = null,
        @Query("chCounterpart") chCounterpart:String? = null,
        @Query("chat") chat:String? = null
    ): Call<CUDReponse>

    @GET("getChat")
    fun getChat(
        @Query("shMem") shMem:String? = null,
        @Query("memId") memId:String? = null,
        @Query("shMem2") shMem2:String? = null,
        @Query("memId2") memId2:String? = null
    ): Call<ChatResponse>

    @GET("getChatList")
    fun getChatList(
        @Query("memId") memId:String? = null
    ): Call<ChatResponse>

}

class ChatClient {

    companion object {

        private var instance: ChatAPI? = null

        val api:ChatAPI
            get() {
                return getInstance()
            }

        @Synchronized
        fun getInstance():ChatAPI {
            if(instance == null) {
                instance = create()
            }

            return instance as ChatAPI
        }
        // 기본 URL
        private const val BASE_URL = "http://14.55.65.168/talcar/"

        // 헤더 속성
        private const val CLIENT_ID = ""
        private const val CLIENT_SECRET = ""
        var userId:String = ""

        fun create(): ChatAPI {
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
                .create(ChatAPI::class.java)
        }

    }

}