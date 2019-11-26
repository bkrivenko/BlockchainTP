package com.hetum.blockchaintp.base

import com.hetum.blockchaintp.injection.component.DaggerPresenterInjector
import com.hetum.blockchaintp.injection.component.PresenterInjector
import com.hetum.blockchaintp.injection.module.ContextModule
import com.hetum.blockchaintp.injection.module.NetworkModule
import com.hetum.blockchaintp.injection.module.SharedPreferencesModule
import com.hetum.blockchaintp.ui.authorization.AuthorizationPresenter
import com.hetum.blockchaintp.ui.main.MainPresenter
import com.hetum.blockchaintp.ui.splashscreen.SplashScreenPresenter

abstract class BasePresenter<out V : BaseView>(protected val view: V) {

    private val injector: PresenterInjector = DaggerPresenterInjector
        .builder()
        .baseView(view)
        .contextModule(ContextModule)
        .networkModule(NetworkModule)
        .sharedPreferencesModule(SharedPreferencesModule)
        .build()

    init {
        inject()
    }

    abstract fun onViewCreated()

    abstract fun onViewDestroyed()

    private fun inject() {
        when (this) {
            is SplashScreenPresenter -> injector.injectSplashScreen(this)
            is AuthorizationPresenter -> injector.injectAuthorization(this)
            is MainPresenter -> injector.injectMain(this)
        }
    }
}