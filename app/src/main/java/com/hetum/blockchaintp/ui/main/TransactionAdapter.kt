package com.hetum.blockchaintp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hetum.blockchaintp.R
import com.hetum.blockchaintp.models.BlockchainInfo
import kotlinx.android.synthetic.main.item_transaction.view.*

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var transactions = mutableListOf<BlockchainInfo>()

    class TransactionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val context = v.context
        private val size = v.tvSize
        private val hash = v.tvHash
        fun bind(transaction: BlockchainInfo) {
            size.text = context.getString(R.string.size, transaction.size)
            hash.text = context.getString(R.string.hash, transaction.hash)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        )

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    fun updateData(blockchainInfo: BlockchainInfo) {
        transactions.add(blockchainInfo)
        notifyDataSetChanged()
    }

    fun clearData() {
        transactions.clear()
        notifyDataSetChanged()
    }
}