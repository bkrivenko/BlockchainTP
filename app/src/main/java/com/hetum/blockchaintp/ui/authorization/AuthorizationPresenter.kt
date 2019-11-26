package com.hetum.blockchaintp.ui.authorization

import android.annotation.SuppressLint
import android.util.Patterns
import com.hetum.blockchaintp.base.BasePresenter
import com.hetum.blockchaintp.common.PrefHelper
import com.hetum.blockchaintp.models.Token
import com.hetum.blockchaintp.network.IDataApi
import com.hetum.blockchaintp.network.repositories.AccountRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthorizationPresenter(view: AuthorizationView) : BasePresenter<AuthorizationView>(view) {

    @Inject
    lateinit var dataApi: IDataApi

    @Inject
    lateinit var prefHelper: PrefHelper

    lateinit var accountRepository: AccountRepository

    override fun onViewCreated() {
        accountRepository = AccountRepository(dataApi)
    }

    override fun onViewDestroyed() {
    }

    fun checkIsAuthorizationAvailable(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.makeAuthorizationAvailable(true)
        } else {
            view.makeAuthorizationAvailable(false)
        }
    }

    @SuppressLint("CheckResult")
    fun logIn(email: String, password: String) {
        val params = HashMap<String, String>()
        params["email"] = email
        params["password"] = password

        accountRepository.logIn(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe(
                { token -> saveTokenAndStartNewActivity(token) },
                { throwable -> view.showError(throwable.message.toString()) }
            )
    }

    private fun saveTokenAndStartNewActivity(token: Token) {
        prefHelper.token = token.token
        view.startMainActivity()
    }
}