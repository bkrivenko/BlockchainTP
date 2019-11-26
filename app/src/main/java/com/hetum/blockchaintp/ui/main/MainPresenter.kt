package com.hetum.blockchaintp.ui.main

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.auth0.android.jwt.JWT
import com.hetum.blockchaintp.base.BasePresenter
import com.hetum.blockchaintp.common.PrefHelper
import com.hetum.blockchaintp.models.BlockchainInfo
import com.hetum.blockchaintp.models.Info
import com.hetum.blockchaintp.network.IDataApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainPresenter(view: MainView) : BasePresenter<MainView>(view) {

    @Inject
    lateinit var dataApi: IDataApi

    @Inject
    lateinit var prefHelper: PrefHelper

    private var subscription: Disposable? = null

    lateinit var webSocket: WebSocket

    override fun onViewCreated() {
        getAccountInfo()
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
        if (::webSocket.isInitialized)
            webSocket.cancel()
    }

    private fun getAccountInfo() {
        subscription = dataApi
            .getProfileInfo()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { account -> fillForm(account.info) },
                { throwable -> logError("Profile", throwable.message.toString()) }
            )
    }

    private fun startWebSocket() {
        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val requestBlockchain = Request.Builder().url("wss://ws.blockchain.info/inv").build()
        val webSocketListenerBlockchain = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response?) {
                webSocket.send("{\"op\":\"unconfirmed_sub\"}")
            }

            override fun onMessage(webSocket: WebSocket?, text: String) {
                parseJson(text)
            }

            override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
            }

            override fun onClosing(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
                logError("WebSocketBlockchain", "CLOSE: $code $reason")
            }

            override fun onClosed(
                webSocket: WebSocket?,
                code: Int,
                reason: String?
            ) {
                logError("WebSocketBlockchain", "CLOSE: $code $reason")
            }

            override fun onFailure(
                webSocket: WebSocket?,
                t: Throwable?,
                response: Response?
            ) {
                logError("WebSocketBlockchain", t!!.message!!)
            }
        }
        webSocket = client.newWebSocket(requestBlockchain, webSocketListenerBlockchain)
        client.dispatcher().executorService().shutdown()

    }

    private fun parseJson(text: String) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(text)
        } catch (ex: JSONException) {
            ex.printStackTrace()
        }
        if (jsonObject != null) {
            try {
                val x = jsonObject.optJSONObject("x")
                if (x != null)
                    Handler(Looper.getMainLooper()).post {
                        view.addDataToList(
                            BlockchainInfo(
                                x.optString("size"),
                                x.optString("hash")
                            )
                        )
                    }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    fun startSocket() {
        startWebSocket()
    }

    fun closeConnection() {
        webSocket.cancel()
    }

    fun logOut() {
        val params = HashMap<String, String?>()
        params["session_id"] = JWT(prefHelper.token).getClaim("session_id").asString()
        subscription = dataApi
            .logOut(params)
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