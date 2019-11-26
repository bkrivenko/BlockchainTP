package com.hetum.blockchaintp.ui.main

import com.hetum.blockchaintp.base.BaseView
import com.hetum.blockchaintp.models.BlockchainInfo
import com.hetum.blockchaintp.models.Info

interface MainView : BaseView {

    fun fillUserInfo(info: Info)

    fun openAuthorizationActivity()

    fun addDataToList(transaction : BlockchainInfo)
}