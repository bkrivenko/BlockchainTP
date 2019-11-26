package com.hetum.blockchaintp.ui.main

import android.annotation.SuppressLint
import android.util.Log
import com.auth0.android.jwt.JWT
import com.hetum.blockchaintp.base.BasePresenter
import com.hetum.blockchaintp.common.PrefHelper
import com.hetum.blockchaintp.models.Info
import com.hetum.blockchaintp.network.IDataApi
import com.hetum.blockchaintp.network.repositories.AccountRepository
import com.hetum.blockchaintp.network.repositories.BlockchainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainPresenter(view: MainView) : BasePresenter<MainView>(view) {

    @Inject
    lateinit var dataApi: IDataApi

    @Inject
    lateinit var prefHelper: PrefHelper

    lateinit var accountRepository: AccountRepository
    lateinit var blockchainRepository: BlockchainRepository
    override fun onViewCreated() {
        accountRepository = AccountRepository(dataApi)
        blockchainRepository = BlockchainRepository()
        getAccountInfo()
    }

    override fun onViewDestroyed() {
        if (::blockchainRepository.isInitialized)
            blockchainRepository.webSocket.cancel()
    }

    @SuppressLint("CheckResult")
    private fun getAccountInfo() {
        accountRepository.getProfileInfo()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { account -> fillForm(account.info) },
                { throwable -> logError("Profile", throwable.message.toString()) }
            )
    }

    @SuppressLint("CheckResult")
    private fun startWebSocket() {
        blockchainRepository.getTransaction().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { blockchainInfo ->
                    view.addDataToList(blockchainInfo)
                },
                { error -> logError("LogOut", error.message!!) })
    }

    fun startSocket() {
        startWebSocket()
    }

    fun closeConnection() {
        blockchainRepository.webSocket.cancel()
    }

    @SuppressLint("CheckResult")
    fun logOut() {
        val params = HashMap<String, String?>()
        params["session_id"] = JWT(prefHelper.token).getClaim("session_id").asString()
        accountRepository.logOut(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { clearData() },
                { throwable -> logError("LogOut", throwable.message.toString()) }
            )
        prefHelper.clearToken()
        view.openAuthorizationActivity()
    }

    private fun fillForm(info: Info) {
        view.fillUserInfo(info)
        startWebSocket()
    }

    private fun clearData() {
        prefHelper.clearToken()
        view.openAuthorizationActivity()
    }

    private fun logError(tag: String, error: String) {
        Log.e(tag, error)
    }
}