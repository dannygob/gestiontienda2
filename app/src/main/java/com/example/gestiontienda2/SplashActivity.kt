package com.example.gestiontienda2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.gestiontienda2.presentation.ui.dashboard.DashboardScreen

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val storeWatermark: ImageView = findViewById(R.id.storeWatermark)
        val greetingText: TextView = findViewById(R.id.greetingText)

        // Load the store watermark image
        storeWatermark.setImageResource(R.drawable.store_watermark)

        // Set the greeting text
        greetingText.text = "Welcome to Our Store!"

        // Load animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        greetingText.startAnimation(animation)

        // Delay and navigate to DashboardScreen
        Handler().postDelayed({
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
            finish()
        }, 3000) // Delay for 3 seconds
    }
}
