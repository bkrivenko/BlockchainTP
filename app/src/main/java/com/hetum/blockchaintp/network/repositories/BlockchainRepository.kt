package com.hetum.blockchaintp.network.repositories

import android.util.Log
import com.hetum.blockchaintp.models.BlockchainInfo
import io.reactivex.Observable
import okhttp3.*
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class BlockchainRepository {

    lateinit var webSocket: WebSocket

    fun getTransaction(): Observable<BlockchainInfo> {

        return Observable.create { emitter ->
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
                    emitter.onNext(parseJson(text))
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


    }

    private fun parseJson(text: String): BlockchainInfo {
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
                    return BlockchainInfo(x.optString("size"), x.optString("hash"))

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return BlockchainInfo("", "")
    }

    private fun logError(tag: String, error: String) {
        Log.e(tag, error)
    }
}