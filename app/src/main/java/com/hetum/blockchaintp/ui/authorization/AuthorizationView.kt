package com.hetum.blockchaintp.ui.authorization

import com.hetum.blockchaintp.base.BaseView

interface AuthorizationView : BaseView {

    fun showError(error: String)

    fun hideError()

    fun makeAuthorizationAvailable(isAvailable: Boolean)

    fun startMainActivity()
}