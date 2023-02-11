package com.example.cryptoexchangeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.CurrencyItemBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency

class MarketAdapter(var context: Context, var list: List<CryptoCurrency>) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    inner class MarketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = CurrencyItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return MarketViewHolder(view)
    }

    override fun getItemCount(): Int = list.size


    /**
     * In this particular method, for each item in the list, it sets the currency
     * name and symbol to the corresponding text views. It then loads the currency
     * icon and chart images using the Glide library. It sets the currency price
     * and percent change to the appropriate text views and sets the text color
     * based on whether the percent change is positive or negative.
     */
    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = list[position]
        val price = "$ ${String.format("%.2f", item.quotes[0].price)}"
        val change = "${if (item.quotes[0].percentChange24h > 0) "+" else "-"} ${String.format("%.2f", item.quotes[0].percentChange24h)} %"
        val changeColor = if (item.quotes[0].percentChange24h > 0)
            context.resources.getColor(R.color.green)
        else
            context.resources.getColor(R.color.red)

        holder.binding.apply {
            currencyNameTextView.text = item.name
            currencySymbolTextView.text = item.symbol
            loadImage(currencyImageView, "https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png")
            loadImage(currencyChartImageView, "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd${item.id}.png")
            currencyPriceTextView.text = price
            currencyChangeTextView.text = change
            currencyChangeTextView.setTextColor(changeColor)
        }
    }

    private fun loadImage(imageView: ImageView, url: String) {
        Glide.with(context)
            .load(url)
            .into(imageView)
    }


    fun updateData(dataItem: List<CryptoCurrency>) {
        list = dataItem
        notifyDataSetChanged()
    }
}