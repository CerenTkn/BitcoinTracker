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
    private val coinList: List<Coin>
) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    inner class CoinViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin) {
            binding.tvName.text = coin.name
            binding.tvPrice.text = "$${coin.currentPrice}"
            binding.tvSymbol.text = coin.symbol.uppercase()
            if (coin.image.isNullOrEmpty()) {
                Log.e("GlideDebug", "Image URL is null or empty for coin: ${coin.name}")
            } else {
                Glide.with(binding.root.context)
                    .load(coin.image)
                    .placeholder(R.drawable.coin_placeholder)
                    .error(R.drawable.error_image)
                    .into(binding.ivIcon)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ItemCoinBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(coinList[position])
    }

    override fun getItemCount() = coinList.size
}
