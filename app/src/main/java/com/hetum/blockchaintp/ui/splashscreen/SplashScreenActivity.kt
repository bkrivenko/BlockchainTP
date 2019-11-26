package com.hetum.blockchaintp.ui.splashscreen

import android.content.Intent
import com.hetum.blockchaintp.base.BaseActivity
import com.hetum.blockchaintp.ui.authorization.AuthorizationActivity
import com.hetum.blockchaintp.ui.main.MainActivity

class SplashScreenActivity : BaseActivity<SplashScreenPresenter>(), SplashScreenView {


    override fun init() {
        presenter.onViewCreated()
    }

    override fun instantiatePresenter(): SplashScreenPresenter {
        return SplashScreenPresenter(this)
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun startAuthorizationActivity() {
        startActivity(Intent(this, AuthorizationActivity::class.java))
        finish()
    }
}