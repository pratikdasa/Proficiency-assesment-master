package com.proficiencyassesment.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.proficiencyassesment.R

class SplashActivity : AppCompatActivity() {

    private val splashTimeout = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Splassh screen will be visible for 3 seconds and then will move to the Main Activity
        Handler().postDelayed(Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTimeout)
    }
}