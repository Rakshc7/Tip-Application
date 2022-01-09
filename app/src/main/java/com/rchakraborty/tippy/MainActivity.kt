package com.rchakraborty.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
private const val INITIAL_NO_OF_PEOPLE = 2
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var seekBarPerson: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvNOPLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvPpTipAmount: TextView
    private lateinit var tvPpTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var tvPpTipLabel: TextView
    private lateinit var tvPpTotalLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        seekBarPerson = findViewById(R.id.seekBarPerson)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvNOPLabel = findViewById(R.id.tvNOPLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvPpTipAmount = findViewById(R.id.tvPpTipAmount)
        tvPpTotalAmount = findViewById(R.id.tvPpTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        tvPpTipLabel = findViewById(R.id.tvPpTipLabel)
        tvPpTotalLabel = findViewById(R.id.tvPpTotalLabel)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarPerson.progress = INITIAL_NO_OF_PEOPLE
        tvNOPLabel.text = "$INITIAL_NO_OF_PEOPLE"

        seekBarPerson.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvNOPLabel.text = "$progress"
                computePpTipandTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipandTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        } )
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipandTotal()
            }

        } )


    }

    private fun computePpTipandTotal() {
        if (etBaseAmount.text.isEmpty()){
            tvPpTipAmount.text = ""
            tvPpTotalAmount.text = ""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val NOP = seekBarPerson .progress
        val tipAmount =  baseAmount * tipPercent / 100
        val totalAmount = tipAmount + baseAmount
        val ppTipAmount = tipAmount / NOP
        val ppTotalAmount = totalAmount / NOP
        tvPpTipAmount.text = "%.2f".format(ppTipAmount)
        tvPpTotalAmount.text = "%.2f".format(ppTotalAmount)
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when(tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipandTotal() {
        if (etBaseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount =  baseAmount * tipPercent / 100
        val totalAmount = tipAmount + baseAmount
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}