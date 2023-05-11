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

class TopMarketAdapter(var context: Context, var list: List<CryptoCurrency>,val listner: ItemClickListner) : RecyclerView.Adapter<TopMarketAdapter.CryptoCurrencyViewHolder>() {

    inner class CryptoCurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = TopCurrencyLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoCurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.top_currency_layout, parent, false)
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
        holder.bind(item)
    }

    private fun CryptoCurrencyViewHolder.bind(item: CryptoCurrency) {
        binding.currencyNameTextView.text = item.name
        binding.currencyCardView.setOnClickListener {
            listner.onItemClickListener(item)
        }
        bindImage(item)
        bindChangeText(item)
    }

    fun updateList(mlist: List<CryptoCurrency>) {
       list=mlist
        notifyDataSetChanged()
    }

    private fun CryptoCurrencyViewHolder.bindImage(item: CryptoCurrency) {
        val imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png"
        Glide.with(context)
            .load(imageUrl)
            //.error(R.drawable.ic_placeholder)
            //.placeholder(R.drawable.ic_loading)
            .into(binding.currencyImageView)
    }

    private fun CryptoCurrencyViewHolder.bindChangeText(item: CryptoCurrency) {
        val colorRes = if (item.quotes!![0].percentChange24h > 0) R.color.green else R.color.red_light
        try {
            val price = "$${String.format("%.2f", item.quotes[0].price)}"
            binding.currencyPriceTextView.text=price
            binding.currencyChangeTextView.setTextColor(context.resources.getColor(colorRes))
            if (item.quotes[0].percentChange24h > 0){

                binding.currencyChangeTextView.text = "+ ${item.quotes[0].percentChange24h}"
                binding.currencyChangeImageView.setImageResource(R.drawable.ic_green)
            }else{
                binding.currencyChangeTextView.text = "${item.quotes[0].percentChange24h}"
                binding.currencyChangeImageView.setImageResource(R.drawable.ic_red)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

interface ItemClickListner{
    fun onItemClickListener(item:CryptoCurrency)
}
}
