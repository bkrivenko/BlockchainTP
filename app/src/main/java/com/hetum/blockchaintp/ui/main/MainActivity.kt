package com.hetum.blockchaintp.ui.main

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hetum.blockchaintp.R
import com.hetum.blockchaintp.base.BaseActivity
import com.hetum.blockchaintp.models.BlockchainInfo
import com.hetum.blockchaintp.models.Info
import com.hetum.blockchaintp.ui.authorization.AuthorizationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainPresenter>(), MainView {

    private val adapter = TransactionAdapter()

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun init() {
        setContentView(R.layout.activity_main)
        presenter.onViewCreated()

        rvTransactions.layoutManager = LinearLayoutManager(this)
        rvTransactions.adapter = adapter
        tvCnt.text = getString(R.string.cnt, "0")
    }

    override fun instantiatePresenter(): MainPresenter {
        return MainPresenter(this)
    }

    fun logOutOnClick(v: View) {
        presenter.logOut()
    }

    fun startOnClick(v: View) {
        presenter.startSocket()
    }

    fun stopOnClick(v: View) {
        presenter.closeConnection()
    }

    fun resetOnClick(v: View) {
        adapter.clearData()
        tvCnt.text = getString(R.string.cnt, "0")
    }

    override fun fillUserInfo(info: Info) {
        if (info.profiles.isNotEmpty()) {
            tvName.text = getString(R.string.user_first_name, info.profiles[0].first_name)
            tvLastName.text = getString(R.string.user_last_name, info.profiles[0].last_name)
            tvMail.text = getString(R.string.user_email, info.profiles[0].email)
            btnLogOut.visibility = View.VISIBLE
        }
    }

    override fun openAuthorizationActivity() {
        startActivity(Intent(this, AuthorizationActivity::class.java))
        finish()
    }

    override fun addDataToList(transaction: BlockchainInfo) {
        adapter.updateData(transaction)
        tvCnt.text = getString(R.string.cnt, adapter.itemCount.toString())
        rvTransactions.smoothScrollToPosition(adapter.itemCount - 1)
    }

}
