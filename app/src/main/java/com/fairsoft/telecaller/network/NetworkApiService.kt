package com.fairsoft.telecaller.network

import com.fairsoft.telecaller.model.CampConNotCon
import com.fairsoft.telecaller.model.Campaign
import com.fairsoft.telecaller.model.CampaignDetailed
import com.fairsoft.telecaller.model.ContactHistory
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
    suspend fun getCampaignsList(@Query("UserId") userId: Int, @Query("IsBookXpertUser") company: Int): List<Campaign>

    @GET("Campaign/GetNotConnectedData")
    suspend fun getNotConnectedCallsList(@Query("IsBookXpertUser") company: Int, @Query("UserId") userId: Int): List<CampConNotCon>

    @GET("Campaign/GetCampaignDetailsById")
    suspend fun getCampaignById(@Query("UserId") userId: Int, @Query("CampaignId") campaignId: Int, @Query("IsBookXpertUser") company: Int): List<CampaignDetailed>

    @POST("AssigneeHistory/AssigneeHistoryByCustomerMobile")
    suspend fun getContactHistory(@Query("mobileNumber") mobileNum: String, @Query("IsBookXpertUser") company: Int): List<ContactHistory>

    @GET("Campaign/GetConnectedData")
    suspend fun getConCallsById(@Query("IsBookXpertUser") company: Int, @Query("CampaignId") campaignId: Int, @Query("UserId") userId: Int): List<CampConNotCon>

    @GET("Campaign/GetNotConnectedCampaignDataById")
    suspend fun getNotConCallsById(@Query("CampaignId") campaignId: Int, @Query("IsBookXpertUser") company: Int, @Query("UserId") userId: Int): List<CampConNotCon>
}

object NetworkApi {
    val retrofitService: NetworkApiService by lazy {
        retrofit.create(NetworkApiService::class.java)
    }
}