package com.hetum.blockchaintp.ui.splashscreen

import com.hetum.blockchaintp.base.BasePresenter
import com.hetum.blockchaintp.common.PrefHelper
import javax.inject.Inject

class SplashScreenPresenter(view: SplashScreenView) : BasePresenter<SplashScreenView>(view) {

    @Inject
    lateinit var prefHelper: PrefHelper

    override fun onViewCreated() {
        checkAuthorizationToken()
    }

    override fun onViewDestroyed() {

    }

    private fun checkAuthorizationToken() {
        if (prefHelper.token.isEmpty()) {
            view.startAuthorizationActivity()
        } else {
            view.startMainActivity()
        }
    }

}