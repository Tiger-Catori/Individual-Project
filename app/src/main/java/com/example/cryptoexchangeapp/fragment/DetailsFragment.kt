package com.example.cryptoexchangeapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
// import androidx.navigation.fragment.navArgs
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentDetailsBinding
import com.example.cryptoexchangeapp.models.CryptoCurrency
import androidx.navigation.fragment.navArgs
import com.example.cryptoexchangeapp.apis.ApiInterface
import com.example.cryptoexchangeapp.apis.ApiUtilities
import com.example.cryptoexchangeapp.models.Coin
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        binding.progressBar.visibility=View.VISIBLE
        binding.lineChart.setNoDataText("Loading Graph Data..")
        val data : CryptoCurrency = args.cryptoCurrency!!
        setUpLineChart()
        setUpDetails(data)
        /*loadChart(data)*/
        setButtonOnClick(data)
        binding.backStackButton.setOnClickListener {
            onBackPressed()
        }
        loadChartData(binding.btnDaily, TimePeriod.DAILY, data)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                        onBackPressed()
                    }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
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


    /**
     *  Responsible for setting up the UI of the DetailsActivity with the given CryptoCurrency data.
     */
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

    private fun setUpLineChart() {
        binding.lineChart.apply {
            setDrawBorders(false)
            description.isEnabled = false
            isDragEnabled = false
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.setDrawAxisLine(false)
            axisRight.textColor = ContextCompat.getColor(requireContext(), R.color.secondary_color)
            legend.isEnabled = false
            setTouchEnabled(false)
            setScaleEnabled(false)
            setDrawBorders(false)
        }
    }

    private  val LABEL = "Price"
    private  val RIGHT_PARENTHESES = ")"
    private  val LEFT_PARENTHESES = " ("
    private  val ANALYTICS_SEPARATOR = " - "
    private  val LINE_WIDTH = 2F

    /**
     * Sets up line chart
     * */
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

    /**
     * Returns the appropriate chart background based on the coin's change value.
     * @param coin The Coin object containing the change value.
     * @return The Drawable resource for the chart background.
     */
    private fun getChartBackground(coin: Coin): Drawable? {
        // Check if the coin's change value is positive or negative.
        val backgroundResource = if (coin.change.toDoubleOrNull()?.isPositive() == true) {
            R.drawable.background_chart_up
        } else {
            R.drawable.background_chart_down
        }
        // Get and return the Drawable from the resource.
        return ContextCompat.getDrawable(requireContext(), backgroundResource)
    }


    /**
     * Returns the appropriate color based on the coin's change value.
     * @param coin The Coin object containing the change value.
     * @return The color resource for the chart.
     */
    private fun getColor(coin: Coin): Int {
        // Check if the coin's change value is positive or negative.
        val colorResource = if (coin.change.toDoubleOrNull()?.isPositive() == true) {
            R.color.green
        } else {
            R.color.red
        }
        // Get and return the color from the resource.
        return ContextCompat.getColor(requireContext(), colorResource)
    }


    // Extension function to check if a Double is positive
    private fun Double.isPositive(): Boolean = this > 0

    /**
     * Returns the appropriate change icon based on the coin's change value.
     * @param coin The Coin object containing the change value.
     * @return The Drawable resource for the change icon.
     */
    private fun getChangeIcon(coin: Coin): Drawable? {
        // Check if the coin's change value is positive or negative.
        val iconResource = if (coin.change.toDoubleOrNull()?.isPositive() == true) {
            R.drawable.ic_green
        } else {
            R.drawable.ic_red
        }

        // Get and return the Drawable from the resource.
        return ContextCompat.getDrawable(requireContext(), iconResource)
    }

    // Enum class to represent time periods
    enum class TimePeriod(val param: String) {
        DAILY("24h"),
        WEEKLY("7d"),
        MONTHLY("30d")
    }

    /**
     * Handles the back button press by navigating to the previous screen.
     */
    private fun onBackPressed() {
        findNavController().popBackStack()
    }

}
