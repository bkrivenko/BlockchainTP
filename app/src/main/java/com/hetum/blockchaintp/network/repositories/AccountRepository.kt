package com.hetum.blockchaintp.network.repositories

import com.hetum.blockchaintp.models.Account
import com.hetum.blockchaintp.models.Token
import com.hetum.blockchaintp.network.IDataApi
import io.reactivex.Observable
import org.json.JSONObject

class AccountRepository(private val dataApi: IDataApi) {

    fun logIn(body: HashMap<String, String>): Observable<Token> {
        return dataApi.logIn(body)
    }

    fun logOut(body: HashMap<String, String?>): Observable<JSONObject> {
        return dataApi.logOut(body)
    }

    fun getProfileInfo(): Observable<Account> {
        return dataApi.getProfileInfo()
    }
}