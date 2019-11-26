package com.hetum.blockchaintp.injection.module

import android.content.Context
import com.hetum.blockchaintp.common.PrefHelper
import dagger.Module
import dagger.Provides
import dagger.Reusable


@Module

@Suppress("unused")
object SharedPreferencesModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideSharedPreferences(context: Context) : PrefHelper{
        return PrefHelper(context)
    }
}