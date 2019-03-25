package com.example.timer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.hw1.R
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private var timeInMilSeconds: Long = 2000

    val timerKey: String = "TIMER"

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        timer = object : CountDownTimer(timeInMilSeconds, 1000) {
            override fun onTick(milSeconds: Long) {
                timeInMilSeconds = milSeconds
            }

            override fun onFinish() {
                val intent = Intent(this@MainActivity, TimeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }.start()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        timeInMilSeconds = savedInstanceState.getLong(timerKey)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(timerKey, timeInMilSeconds)
    }
}
