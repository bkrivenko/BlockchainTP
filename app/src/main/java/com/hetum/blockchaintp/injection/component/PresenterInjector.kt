package com.hetum.blockchaintp.injection.component

import com.hetum.blockchaintp.base.BaseView
import com.hetum.blockchaintp.injection.module.ContextModule
import com.hetum.blockchaintp.injection.module.NetworkModule
import com.hetum.blockchaintp.injection.module.SharedPreferencesModule
import com.hetum.blockchaintp.ui.authorization.AuthorizationPresenter
import com.hetum.blockchaintp.ui.main.MainPresenter
import com.hetum.blockchaintp.ui.splashscreen.SplashScreenPresenter
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ContextModule::class), (NetworkModule::class), (SharedPreferencesModule::class)])
interface PresenterInjector {

    fun injectSplashScreen(presenter: SplashScreenPresenter)
    fun injectAuthorization(presenter: AuthorizationPresenter)
    fun injectMain(presenter: MainPresenter)

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun contextModule(contextModule: ContextModule): Builder
        fun sharedPreferencesModule(sharedPreferencesModule: SharedPreferencesModule): Builder
        fun networkModule(networkModule: NetworkModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }
}