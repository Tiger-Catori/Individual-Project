package com.example.cryptoexchangeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
// import androidx.navigation.fragment.navArgs
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentDetailsBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency
import androidx.navigation.fragment.navArgs
// import com.example.cryptoexchangeapp.DetailsFragmentArgs


class DetailsFragment : Fragment() {
    lateinit var binding : FragmentDetailsBinding

    private val args : DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        val data : CryptoCurrency = args.cryptoCurrency!!

        setUpDetails(data)
        loadChart(data)
        setButtonOnClick(data)


        return binding.root
    }

    /**
    Set click listeners for buttons to load chart data with different time intervals
    When a button is clicked, the chart data will be loaded according to the selected time interval.
    */
    private fun setButtonOnClick(item: CryptoCurrency) {

        val oneMonthBtn = binding.button
        val oneWeekBtn = binding.button1
        val oneDayBtn = binding.button2
        val fourHourBtn = binding.button3
        val oneHourBtn = binding.button4
        val fifteenMinuteBtn = binding.button5

        // Click listener for buttons
        val clickListener = View.OnClickListener { clickedView ->
            when (clickedView.id) {
                fifteenMinuteBtn.id -> loadChartData(clickedView, "15", item, oneDayBtn, oneMonthBtn, oneWeekBtn, fourHourBtn, oneHourBtn)
                oneHourBtn.id -> loadChartData(clickedView, "1H", item, oneDayBtn, oneMonthBtn, oneWeekBtn, fourHourBtn, fifteenMinuteBtn)
                fourHourBtn.id -> loadChartData(clickedView, "4H", item, oneDayBtn, oneMonthBtn, oneWeekBtn, fifteenMinuteBtn, oneHourBtn)
                oneDayBtn.id -> loadChartData(clickedView, "1D", item, fifteenMinuteBtn, oneMonthBtn, oneWeekBtn, fourHourBtn, oneHourBtn)
                oneWeekBtn.id -> loadChartData(clickedView, "1W", item, oneDayBtn, oneMonthBtn, fifteenMinuteBtn, fourHourBtn, oneHourBtn)
                oneMonthBtn.id -> loadChartData(clickedView, "1M", item, oneDayBtn, fifteenMinuteBtn, oneWeekBtn, fourHourBtn, oneHourBtn)
            }
        }

        // Set the click listener for all buttons
        fifteenMinuteBtn.setOnClickListener(clickListener)
        oneHourBtn.setOnClickListener(clickListener)
        fourHourBtn.setOnClickListener(clickListener)
        oneDayBtn.setOnClickListener(clickListener)
        oneWeekBtn.setOnClickListener(clickListener)
        oneMonthBtn.setOnClickListener(clickListener)
    }


    /**
     * Load chart data for the given CryptoCurrency item and selected time interval
     * */
    private fun loadChartData(
        selectedView: View?,
        timeInterval: String,
        item: CryptoCurrency,
        oneDayBtn: AppCompatButton,
        oneMonthBtn: AppCompatButton,
        oneWeekBtn: AppCompatButton,
        fourHourBtn: AppCompatButton,
        oneHourBtn: AppCompatButton
    ) {
        // Disable all buttons and set the selected view's background
        disableButtons(oneDayBtn, oneMonthBtn, oneWeekBtn, fourHourBtn, oneHourBtn)
        selectedView?.setBackgroundResource(R.drawable.active_button)

        // Enable JavaScript and set layer type for the WebView
        binding.detailChartWebView.settings.javaScriptEnabled = true
        binding.detailChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        // Construct the URL for the TradingView chart with the given time interval
        val chartUrl = "https://s.tradingview.com/widgetembed?" +
                "symbol=${item}USD&" +
                "interval=${timeInterval}&" +
                "hidesidetoolbar=1&" +
                "hidetoptoolbar=1&" +
                "symboledit=1&" +
                "saveimage=1&" +
                "toolbarbg=F1F3F6&" +
                "studies=[]&" +
                "hideideas=1&" +
                "theme=Dark&" +
                "style=1&" +
                "timezone=Etc%2FUTC&" +
                "studies_overrides={}&" +
                "overrides={}&" +
                "enabled_features=[]&" +
                "disabled_features=[]&" +
                "locale=en&" +
                "utm_source=coinmarketcap.com&" +
                "utm_medium=widget&" +
                "utm_campaign=chart&" +
                "utm_term=BTCUSDT"

        // Load the TradingView chart in the WebView
        binding.detailChartWebView.loadUrl(chartUrl)
    }

    // Disable all buttons by setting their backgrounds to null
    private fun disableButtons(
        oneDayBtn: AppCompatButton,
        oneMonthBtn: AppCompatButton,
        oneWeekBtn: AppCompatButton,
        fourHourBtn: AppCompatButton,
        oneHourBtn: AppCompatButton
    ) {
        oneDayBtn.background = null
        oneWeekBtn.background = null
        oneMonthBtn.background = null
        fourHourBtn.background = null
        oneHourBtn.background = null
    }


    // Load the chart for the given CryptoCurrency item
    private fun loadChart(item: CryptoCurrency) {
        // Enable JavaScript and set layer type for the WebView
        binding.detailChartWebView.settings.javaScriptEnabled = true
        binding.detailChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        // Construct the URL for the TradingView chart
        //val itemSymbol = "BTC"
        //val timeInterval = "1D"

        val chartUrl = "https://s.tradingview.com/widgetembed?" +
                "symbol=${item}USD&" +
                "interval=1D&" +
                "hidesidetoolbar=1&" +
                "hidetoptoolbar=1&" +
                "symboledit=1&" +
                "saveimage=1&" +
                "toolbarbg=F1F3F6&" +
                "studies=[]&" +
                "hideideas=1&" +
                "theme=Dark&" +
                "style=1&" +
                "timezone=Etc%2FUTC&" +
                "studies_overrides={}&" +
                "overrides={}&" +
                "enabled_features=[]&" +
                "disabled_features=[]&" +
                "locale=en&" +
                "utm_source=coinmarketcap.com&" +
                "utm_medium=widget&" +
                "utm_campaign=chart&" +
                "utm_term=BTCUSDT"

        // Load the TradingView chart in the WebView
        binding.detailChartWebView.loadUrl(chartUrl)
    }


    // Set up the details of the CryptoCurrency data object
    private fun setUpDetails(data: CryptoCurrency) {    val url = "https://s2.coinmarketcap.com/static/img/coins/64x64/${data.id}.png"

        // Set the symbol text
        binding.detailSymbolTextView.text = data.symbol

        // Load the image using Glide
        Glide.with(requireContext())
            .load(url)
            .into(binding.detailImageView)

        // Format and set the price text
        binding.detailPriceTextView.text = String.format("$%.4f", data.quotes[0].price)

        // Get the 24h percent change and set color based on its value
        val percentChange24h = data.quotes[0].percentChange24h
        val changeColor = if (percentChange24h > 0) {
            ContextCompat.getColor(requireContext(), R.color.green)
        } else {
            ContextCompat.getColor(requireContext(), R.color.red)
        }


        // Set the text color and text for the 24h percent change
        if (changeColor != null) {
            binding.detailChangeTextView.setTextColor(changeColor)
        }
        binding.detailChangeTextView.text = String.format("%.02f", percentChange24h)
    }

}
