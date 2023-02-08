package com.example.cryptoexchangeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.CurrencyItemBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency

class MarketAdapter(var context: Context, var list: ArrayList<CryptoCurrency>) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    inner class MarketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = CurrencyItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return MarketViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = list[position]
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol

        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png")
            //.thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyImageView)

        Glide.with(context).load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd" + item.id + ".png")
            //.thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyChartImageView)

        holder.binding.currencyPriceTextView.text =
        "${String.format("$%.02f", item.quotes[0].price)} "

        if (item.quotes!![0].percentChange24h > 0) {
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "+ ${String.format("%.02f", item.quotes[0].percentChange24h)} %"

            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = "- ${String.format("%.02f", item.quotes[0].percentChange24h)} %"
        }

    }
}