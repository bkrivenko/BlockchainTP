package com.hetum.blockchaintp.ui.authorization

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.hetum.blockchaintp.R
import com.hetum.blockchaintp.base.BaseActivity
import com.hetum.blockchaintp.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_authorization.*

class AuthorizationActivity : BaseActivity<AuthorizationPresenter>(), AuthorizationView {

    override fun init() {
        setContentView(R.layout.activity_authorization)
        presenter.onViewCreated()
        presenter.checkIsAuthorizationAvailable(etEmail.text.toString(), etPassword.text.toString())
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                presenter.checkIsAuthorizationAvailable(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                presenter.checkIsAuthorizationAvailable(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
        })
    }

    override fun instantiatePresenter(): AuthorizationPresenter {
        return AuthorizationPresenter(this)
    }

    override fun showError(error: String) {
        tvError.text = error
        tvError.visibility = View.VISIBLE
    }

    override fun hideError() {
        tvError.text = ""
        tvError.visibility = View.GONE
    }

    override fun makeAuthorizationAvailable(isAvailable: Boolean) {
        btnLogIn.isEnabled = isAvailable
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun logInOnClick(v: View) {
        presenter.logIn(
            etEmail.text.toString(),
            etPassword.text.toString()
        )
    }
}