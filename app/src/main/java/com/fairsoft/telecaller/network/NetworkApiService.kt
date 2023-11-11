package com.fairsoft.telecaller.network

import com.fairsoft.telecaller.model.CampNotConnected
import com.fairsoft.telecaller.model.Campaign
import com.fairsoft.telecaller.model.CampaignsList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "https://fssservices.bookxpert.co/api/"

private val interceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface NetworkApiService {

    @POST("Login/IsValidUser")
    suspend fun checkLogin(@Body body: Map<String, String>): Any

    @GET("Campaign/GetCampaignMaster")
    suspend fun getCampaignsList(@Query("UserId") userId: Int, @Query("IsBookXpertUser") company: Int): List<CampaignsList>

    @GET("Campaign/GetNotConnectedData")
    suspend fun getNotConnectedCallsList(@Query("IsBookXpertUser") company: Int, @Query("UserId") userId: Int): List<CampNotConnected>

    @GET("Campaign/GetCampaignDetailsById")
    suspend fun getCampaignById(@Query("UserId") userId: Int, @Query("CampaignId") campaignId: Int, @Query("IsBookXpertUser") company: Int): List<Campaign>
}

object NetworkApi {
    val retrofitService: NetworkApiService by lazy {
        retrofit.create(NetworkApiService::class.java)
    }
}