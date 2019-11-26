package com.hetum.blockchaintp.ui.authorization

import android.util.Patterns
import com.hetum.blockchaintp.base.BasePresenter
import com.hetum.blockchaintp.common.PrefHelper
import com.hetum.blockchaintp.models.Token
import com.hetum.blockchaintp.network.IDataApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthorizationPresenter(view: AuthorizationView) : BasePresenter<AuthorizationView>(view) {

    @Inject
    lateinit var dataApi: IDataApi

    @Inject
    lateinit var prefHelper: PrefHelper

    private var subscription: Disposable? = null

    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun checkIsAuthorizationAvailable(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.makeAuthorizationAvailable(true)
        } else {
            view.makeAuthorizationAvailable(false)
        }
    }

    fun logIn(email: String, password: String) {
        val params = HashMap<String, String>()
        params["email"] = email
        params["password"] = password

        subscription = dataApi
            .logIn(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { token -> saveTokenAndStartNewActivity(token) },
                { throwable -> view.showError(throwable.message.toString()) }
            )
    }

    private fun saveTokenAndStartNewActivity(token: Token) {
        prefHelper.token = token.token
        view.startMainActivity()
    }
}