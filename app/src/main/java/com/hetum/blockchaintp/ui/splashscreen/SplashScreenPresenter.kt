package com.hetum.blockchaintp.ui.splashscreen

import com.hetum.blockchaintp.base.BasePresenter
import com.hetum.blockchaintp.common.PrefHelper
import javax.inject.Inject

class SplashScreenPresenter(view: SplashScreenView) : BasePresenter<SplashScreenView>(view) {

    @Inject
    lateinit var prefHelper: PrefHelper
    @Inject
//    lateinit var tokenInterceptor: TokenInterceptor

    override fun onViewCreated() {
//        tokenInterceptor.token = prefHelper.token
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