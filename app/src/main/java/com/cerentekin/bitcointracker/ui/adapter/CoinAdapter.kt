package com.cerentekin.bitcointracker.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cerentekin.bitcointracker.R
import com.cerentekin.bitcointracker.data.model.Coin
import com.cerentekin.bitcointracker.databinding.ItemCoinBinding

class CoinAdapter(
    private val coinList: List<Coin>,
    private val onCoinClick: (Coin) -> Unit
) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    inner class CoinViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin) = with(binding) {
            tvName.text = coin.name
            tvPrice.text = "$${coin.currentPrice}"
            tvSymbol.text = coin.symbol.uppercase()

            Glide.with(root.context)
                .load(coin.image.takeIf { it.isNotEmpty() })
                .placeholder(R.drawable.coin_placeholder)
                .error(R.drawable.error_image)
                .into(ivIcon)

            root.setOnClickListener {
                onCoinClick(coin)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(coinList[position])
    }

    override fun getItemCount() = coinList.size
}
