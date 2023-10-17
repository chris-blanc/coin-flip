package com.example.coinflip

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    //Initialize variable and define them later
    private lateinit var coinImage: ImageView
    private lateinit var totalCount: TextView
    private lateinit var headsCount: TextView
    private lateinit var tailsCount: TextView
    private lateinit var headsPercent: TextView
    private lateinit var tailsPercent: TextView
    private lateinit var headsProgressBar: ProgressBar
    private lateinit var tailsProgressBar: ProgressBar
    private lateinit var simNumber: EditText

    private lateinit var simButton: Button

    //Counter variables to keep track of heads/tails/total flips
    private var heads = 0
    private var tails = 0
    private var total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get a reference to our switch and buttons
        val simSwitch: SwitchCompat = findViewById(R.id.main_sw_simulate)
        val flipButton: Button = findViewById(R.id.main_btn_flip)
        val resetButton: Button = findViewById(R.id.main_btn_reset)
        simButton = findViewById(R.id.main_btn_simulate)

        //Set listeners for switch and buttons
        simSwitch.setOnCheckedChangeListener { compoundButton, isChecked -> enableSim(isChecked) }
        flipButton.setOnClickListener { flip() }
        resetButton.setOnClickListener { reset() }
        simButton.setOnClickListener { sim() }

        //Set values to other views
        coinImage = findViewById(R.id.main_iv_coin)
        totalCount = findViewById(R.id.main_tv_total_flips)
        headsCount = findViewById(R.id.main_tv_total_heads)
        tailsCount = findViewById(R.id.main_tv_total_tails)
        headsPercent = findViewById(R.id.main_tv_heads_percent)
        tailsPercent = findViewById(R.id.main_tv_tails_percent)
        headsProgressBar = findViewById(R.id.main_pb_heads_percent)
        tailsProgressBar = findViewById(R.id.main_pb_tails_percent)
        simNumber = findViewById(R.id.main_et_sim_number)
    }

    //Turn on/off simulation mode
    private fun enableSim(onState: Boolean){
        //Get the state of the switch
        if (onState) {
            simNumber.visibility = View.VISIBLE
            simButton.visibility = View.VISIBLE

        } else {
            simNumber.visibility = View.INVISIBLE
            simButton.visibility = View.INVISIBLE
        }

    }

    //Simulate a single coin flip
    private fun flip(){
        val randomNumber = (0..1).random()

        //Update based on value
        if(randomNumber == 0){
            update("heads")
        }else{
            update("tails")
        }
    }

    //Update the UI based on previous coin flip
    private fun update(coinValue: String){
        //Set the correct image for coin flip and increment counters
        if (coinValue == "heads"){
            coinImage.setImageResource(R.drawable.ic_heads_icon)
            heads++
        }else{
            coinImage.setImageResource(R.drawable.ic_tails_icon)
            tails++
        }
        total++

        //Update all text views
        totalCount.text = "Flips: $total"
        headsCount.text = "Heads: $heads"
        tailsCount.text = "Tails: $tails"

        //Update the statistics
        updateStats()
    }

    //Update the statistics UI based on previous coin flips
    private fun updateStats(){
        var headsPercentValue = 0.0
        var tailsPercentValue = 0.0

        //Calculate percentages
        if (total != 0) {
            headsPercentValue = round((heads.toDouble() / total) * 10000) / 100
            tailsPercentValue = round((tails.toDouble() / total) * 10000) / 100
        }

        //Update the text views
        headsPercent.text = "Heads: $headsPercentValue%"
        tailsPercent.text = "Tails: $tailsPercentValue%"

        //Update the progress bars
        headsProgressBar.progress = headsPercentValue.toInt()
        tailsProgressBar.progress = tailsPercentValue.toInt()
    }

    //Reset all data for the simulation
    private fun reset(){
        //Change the imageView back to the default
        coinImage.setImageResource(R.drawable.ic_thumbs_up)

        //Reset all counters
        heads = 0
        tails = 0
        total = 0

        //Update all text views
        totalCount.text = "Flips: $total"
        headsCount.text = "Heads: $heads"
        tailsCount.text = "Tails: $tails"

        //Update the statistics
        updateStats()
    }

    //Run the coin simulation for a set number of flips
    private fun sim(){
        //Get number to sim and clear the text field
        var numberToSim = 1
        if (simNumber.text.toString() != ""){
            numberToSim = simNumber.text.toString().toInt()
        }
        simNumber.setText("")

        //Run the proper number of flips
        for (i in 1..numberToSim){
            flip()
        }

        //Hide the keyboard
        hideKeyboard()
    }
    //Hide Keyboard
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}