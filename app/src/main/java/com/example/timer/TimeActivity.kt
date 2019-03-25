package com.example.timer


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.hw1.R
import android.util.SparseArray
import android.widget.Button
import android.widget.TextView
import java.lang.Math.pow
import java.lang.StringBuilder
import kotlin.math.roundToInt


class TimeActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer

    enum class TimerState{
        Start, Stop
    }

    private var timerState = TimerState.Start

    var seconds : Int = 0

    val time : Long = 1001
    val timeInterval : Long = 1000

    lateinit var textView : TextView
    lateinit var button : Button

    private val secondKey : String = "SECONDS"
    private val buttonKey : String = "BUTTON"
    private val textKey : String = "TEXT"

    private val numbers: SparseArray<String> = SparseArray()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        fillArray(numbers)
        button = findViewById(R.id.button) as Button
        textView = findViewById(R.id.text) as TextView
        button.setText("START")
        timerState = TimerState.Start

        button.setOnClickListener({

            if (timerState == TimerState.Start) {
                button.setText("STOP")
                timerState = TimerState.Stop
                timer.start()

            } else if (timerState == TimerState.Stop) {
                button.setText("START")
                timerState = TimerState.Start
                timer.cancel()
            }
        })
    }

    fun fillArray(numbers : SparseArray<String>) {

        val numberStr = getResources().getStringArray(R.array.number_strings)

        var num  = getResources().getIntArray(R.array.numbers)

        var i : Int = 0
        for (str in numberStr) {
            numbers.append(num[i], str)
            i += 1
        }
    }

    override fun onResume() {
        super.onResume()

        val allTime : Long = (time - seconds) * 1000
        timer = object : CountDownTimer(allTime, timeInterval) {

            var temp : StringBuilder = StringBuilder()

            override fun onTick(milSeconds: Long) {
                seconds += 1
                if (seconds > time) {
                    this.onFinish()
                    this.cancel()
                }

                temp.append(numbers.get(seconds, ""))

                if (temp.length == 0) {
                    parseNumber(temp)
                }

                textView.setText(temp.toString())
                temp.delete(0, temp.length)
            }

            override fun onFinish() {
                button.setText("START")
                timerState = TimerState.Start
                temp.delete(0, temp.length)
                seconds = 0
            }

        }

        if (timerState === TimerState.Stop) {
            timer.start()
        }
    }

    fun parseNumber (buff : StringBuilder) {

        var curNum = seconds
        var i : Int = 0

        while (curNum > 0) {

            var n : Int = curNum % 10
            curNum = curNum / 10

            if (n != 0) {
                if (i == 0 && curNum % 10 == 1) {
                    buff.insert(0, numbers.get(n + 10) + " ")
                    curNum = curNum / 10
                    i += 1
                } else {
                    var p: Int = pow(10.0, i.toDouble()).roundToInt()
                    buff.insert(0, numbers.get(n * p) + " ")
                }
            }

            i += 1
        }
    }


    override fun onPause() {
        super.onPause()
        timer.cancel()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(secondKey, seconds)
        outState.putString(buttonKey, button.getText().toString())
        outState.putString(textKey, textView.getText().toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        seconds = savedInstanceState.getInt(secondKey)
        val textStr : String = savedInstanceState.getString(textKey, "")
        val buttonStr : String = savedInstanceState.getString(buttonKey)

        if (buttonStr == "START") {
            timerState = TimerState.Start
        }
        else {
            timerState = TimerState.Stop
        }

        textView.setText(textStr)
        button.setText(buttonStr)
    }
}
