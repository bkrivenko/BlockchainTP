package com.hetum.blockchaintp.injection

import com.hetum.blockchaintp.common.PrefHelper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(prefHelper: PrefHelper) : Interceptor {
    var token: String = prefHelper.token
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        if (original.url().encodedPath().contains("/auth") && original.method() == "post") {
            return chain.proceed(original)
        }

        val originalHttpUrl = original.url()
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", token)
            .url(originalHttpUrl)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}