package com.example.cryptoexchangeapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoexchangeapp.R
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


    /**
     * This is part of a RecyclerView adapter and it is called for each item in the list to bind
     * the data to the respective view holder. It takes in
     * @param holder
     * @param position
     * The method first retrieves the item from the list at the given position
     * and sets the text for the topCurrencyNameTextView to the name of the item.
     * Then, it loads an image from a URL using the Glide library and sets it topCurrencyImageView
     *  Finally, the method sets the color and text of the topCurrencyChangeTextView
     *  based on the percentChange24h of the item's quote. If percentChange24h is
     *  greater than 0, the text color is set to green, otherwise it is set to red.
     *  The text of the view is set to either a positive or negative sign
     *  followed by the percentChange24h value.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CryptoCurrencyViewHolder, position: Int) {
        val item = list[position]

        holder.binding.topCurrencyNameTextView.text = item.name

        val imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png"
        Glide.with(context)
            .load(imageUrl)
            //.error(R.drawable.ic_placeholder) // Display a placeholder image in case of error
            //.placeholder(R.drawable.ic_loading) // Display a loading indicator while the image is being fetched
            .into(holder.binding.topCurrencyImageView)

        val colorRes = if (item.quotes!![0].percentChange24h > 0) R.color.green else R.color.red
        holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(colorRes))
        holder.binding.topCurrencyChangeTextView.text = "${if (item.quotes[0].percentChange24h > 0) "+" else "-"} ${item.quotes[0].percentChange24h}"
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
