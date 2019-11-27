package com.hetum.blockchaintp.base

import android.content.Context

interface BaseView {
    fun getContext(): Context

    fun isOnline(): Boolean

    fun showToast(messageRes: Int)
}