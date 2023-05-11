package com.example.cryptoexchangeapp

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cryptoexchangeapp.apis.ApiInterface
import com.example.cryptoexchangeapp.apis.ApiUtilities
import com.example.cryptoexchangeapp.databinding.ActivityDetailsBinding
import com.example.cryptoexchangeapp.models.Coin
import com.example.cryptoexchangeapp.models.CryptoCurrency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    companion object{
        var dataItem:CryptoCurrency?=null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility= View.VISIBLE
        binding.lineChart.setNoDataText("Loading Graph Data..")
        dataItem?.let {data->
            setUpLineChart()
            setUpDetails(data)
            /*loadChart(data)*/
            setButtonOnClick(data)
            binding.backStackButton.setOnClickListener {
                finish()
            }
            loadChartData(binding.btnDaily,TimePeriod.DAILY, data)
        }

    }
    private fun fetchData(item: CryptoCurrency, timePeriod: TimePeriod){
        lifecycleScope.launch {
            val res = withContext(Dispatchers.IO) {
                ApiUtilities.getInstanceCoinranking().create(ApiInterface::class.java).getCoinDetail(item.coinUUID,timePeriod.param)
            }

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    if(res.isSuccessful && res.body()!=null){
                        res.body()?.let {data->
                            setUpLineChartData(data.data.coin)
                        }
                        binding.progressBar.visibility=View.GONE
                    }

                }
            }else{
                binding.progressBar.visibility=View.GONE
            }
        }
    }
    /**
    Set click listeners for buttons to load chart data with different time intervals
    When a button is clicked, the chart data will be loaded according to the selected time interval.
     */
    private fun setButtonOnClick(item: CryptoCurrency) {
        binding.btnDaily.setOnClickListener {clickedView->
            loadChartData(clickedView, TimePeriod.DAILY, item)
        }
        binding.btnWeekly.setOnClickListener {clickedView->
            loadChartData(clickedView, TimePeriod.WEEKLY, item)
        }
        binding.btnMonthly.setOnClickListener {clickedView->
            loadChartData(clickedView, TimePeriod.MONTHLY, item)
        }


    }


    /**
     * Load chart data for the given CryptoCurrency item and selected time interval
     * */
    private fun loadChartData(
        selectedView: View?,
        timeInterval: TimePeriod,
        item: CryptoCurrency
    ) {
        binding.progressBar.visibility=View.VISIBLE
        // Disable all buttons and set the selected view's background

        disableButtons()
        selectedView?.setBackgroundResource(R.drawable.active_button)
        fetchData(item,timeInterval)
    }

    // Disable all buttons by setting their backgrounds to null
    private fun disableButtons(
    ) {
        binding.btnDaily.setBackgroundResource(R.drawable.bg_disable_button)
        binding.btnWeekly.setBackgroundResource(R.drawable.bg_disable_button)
        binding.btnMonthly.setBackgroundResource(R.drawable.bg_disable_button)
    }


    // Set up the details of the CryptoCurrency data object
    private fun setUpDetails(data: CryptoCurrency) {    val url = "https://s2.coinmarketcap.com/static/img/coins/64x64/${data.id}.png"

        // Set the symbol text
        binding.detailSymbolTextView.text = data.symbol

        // Load the image using Glide
        Glide.with(this@DetailsActivity)
            .load(url)
            .into(binding.detailImageView)

        // Format and set the price text
        binding.detailPriceTextView.text = String.format("$%.4f", data.quotes[0].price)

        // Get the 24h percent change and set color based on its value
        val percentChange24h = data.quotes[0].percentChange24h
        val changeColor = if (percentChange24h > 0) {
            ContextCompat.getColor(this@DetailsActivity, R.color.green)
        } else {
            ContextCompat.getColor(this@DetailsActivity, R.color.red)
        }


        // Set the text color and text for the 24h percent change
        if (changeColor != null) {
            binding.detailChangeTextView.setTextColor(changeColor)
        }
        binding.detailChangeTextView.text = String.format("%.02f", percentChange24h)
    }
    private fun setUpLineChart() {
        binding.lineChart.apply {
            setDrawBorders(false)
            description.isEnabled = false
            isDragEnabled = false
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.setDrawAxisLine(false)
            axisRight.textColor = ContextCompat.getColor(this@DetailsActivity, R.color.secondary_color)
            legend.isEnabled = false
            setTouchEnabled(false)
            setScaleEnabled(false)
            setDrawBorders(false)
        }
    }

    private val LABEL = "Price"
    private val RIGHT_PARENTHESES = ")"
    private val LEFT_PARENTHESES = " ("
    private val ANALYTICS_SEPARATOR = " - "
    private val LINE_WIDTH = 2F
    private fun setUpLineChartData(coin: Coin) {
        val entries = coin.sparkline.mapIndexed { index, sparkLine ->
            try {
                Entry(index.toFloat(), sparkLine.toFloat())
            } catch (e: Exception) {
                Entry(index.toFloat(), 0f)
            }

        }

        return LineDataSet(entries, LABEL).apply {
            color = getColor(coin)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillDrawable = getChartBackground(coin)
            setDrawCircles(false)
            lineWidth = LINE_WIDTH
        }.let { lineDataSet ->

            binding.lineChart.data = LineData(lineDataSet).apply {
                setDrawValues(false)
            }
            binding.lineChart.invalidate()
        }
    }
    private fun getChartBackground(coin: Coin): Drawable? {
        return if (coin.change.toDouble().isPositive()) {
            ContextCompat.getDrawable(this@DetailsActivity, R.drawable.background_chart_up)
        } else {
            ContextCompat.getDrawable(this@DetailsActivity, R.drawable.background_chart_down)
        }
    }

    private fun getColor(coin: Coin): Int {
        return if (coin.change.toDouble().isPositive()) {
            ContextCompat.getColor(this@DetailsActivity, R.color.green)
        } else {
            ContextCompat.getColor(this@DetailsActivity, R.color.red)
        }
    }
    private fun Double.isPositive(): Boolean = this > 0
    fun getChangeIcon(coin: Coin): Drawable? {
        return if (coin.change.toDouble().isPositive()) {
            ContextCompat.getDrawable(this@DetailsActivity, R.drawable.ic_green)
        } else {
            ContextCompat.getDrawable(this@DetailsActivity, R.drawable.ic_red)
        }
    }
    enum class TimePeriod(val param: String) {
        HOUR("1h"),
        HOURS3("3h"),
        HOURS12("12h"),
        DAILY("24h"),
        WEEKLY("7d"),
        MONTHLY("30d") ,
        MONTHS3("3m"),
        YEAR1("1y"),
        YEARS3("3y")
    }
    override fun onBackPressed() {
        finish()
    }
}