package com.example.cryptoexchangeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoexchangeapp.databinding.TopCurrencyLayoutBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency

class TopMarketAdapter(var context: Context, val list: List<CryptoCurrency>) : RecyclerView.Adapter<TopMarketAdapter.CryptoCurrencyViewHolder>() {

    inner class CryptoCurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = TopCurrencyLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoCurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return CryptoCurrencyViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CryptoCurrencyViewHolder, position: Int) {
        val item = list[position]

        holder.binding.topCurrencyNameTextView.text = item.name

        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png")
            //.thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.topCurrencyImageView)


        if (item.quotes!![0].percentChange24h > 0) {
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text = "+ ${item.quotes[0].percentChange24h}"
        } else {
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text = "- ${item.quotes[0].percentChange24h}"
        }


    }
}

//data class CryptoCurrencyModel(
//    val cryptoCurrencyName: String,
//    val cryptoCurrencyAbbreviation: String,
//    val cryptoCurrencyPrice: String,
//    val cryptoCurrencyChange: String,
//    val image: Int,
//    val graph: Int
//)
