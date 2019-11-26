package com.hetum.blockchaintp.injection.module

import android.content.Context
import com.hetum.blockchaintp.R
import com.hetum.blockchaintp.common.PrefHelper
import com.hetum.blockchaintp.injection.TokenAuthenticator
import com.hetum.blockchaintp.injection.TokenInterceptor
import com.hetum.blockchaintp.network.IDataApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module

@Suppress("unused")
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideApi(retrofit: Retrofit): IDataApi {
        return retrofit.create(IDataApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(context: Context, prefHelper: PrefHelper): Retrofit {
        val clientBuilder = OkHttpClient.Builder()

        val tokenInterceptor = TokenInterceptor(prefHelper)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder
            .authenticator(TokenAuthenticator(context, tokenInterceptor, prefHelper))
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}