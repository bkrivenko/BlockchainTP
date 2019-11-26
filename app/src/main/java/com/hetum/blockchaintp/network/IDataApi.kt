package com.hetum.blockchaintp.network

import com.hetum.blockchaintp.models.Account
import com.hetum.blockchaintp.models.Token
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface IDataApi {

    @POST("accounts/auth")
    fun logIn(@Body body: HashMap<String, String>): Single<Token>

    @POST("accounts/sessions/refresh")
    fun updateToken(@Header("Authorization") authorization: String): Call<Token>

    @POST("accounts/sessions/end")
    fun logOut(@Body body: HashMap<String, String?>): Single<JSONObject>

    @GET("accounts/current")
    fun getProfileInfo(): Single<Account>
}