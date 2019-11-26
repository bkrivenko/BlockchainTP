package com.hetum.blockchaintp.injection

import android.content.Context
import com.hetum.blockchaintp.R
import com.hetum.blockchaintp.common.PrefHelper
import com.hetum.blockchaintp.models.Token
import com.hetum.blockchaintp.network.IDataApi
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
//    private val tokenHolder: TokenHolder,
    private val context: Context,
    private val tokenInterceptor: TokenInterceptor,
    private val prefHelper: PrefHelper
) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {


//        if (response.request().header("Authorization") != null) {
//            return null
//        }

        val header = response.request().header("Authorization")


        val tokenService = Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IDataApi::class.java)


        var body: Token? = null
        try {
            body = tokenService.updateToken(header!!).execute()?.body()
        } catch (e: Exception) {

        }

        val newToken = body?.token
        tokenInterceptor.token = newToken!!
        prefHelper.token = newToken
        return response.request().newBuilder().header("Authorization", newToken).build()
    }


}