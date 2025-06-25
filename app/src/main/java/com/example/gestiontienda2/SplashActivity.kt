package com.example.gestiontienda2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
// import com.example.gestiontienda2.presentation.ui.dashboard.DashboardScreen // No longer used

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val storeWatermark: ImageView = findViewById(R.id.storeWatermark)
        val greetingText: TextView = findViewById(R.id.greetingText)

        // Cargar la imagen watermark
        storeWatermark.setImageResource(R.drawable.store_watermark)

        // Establecer el texto de bienvenida
        greetingText.text = "Welcome to Our Store!"

        // Cargar animación fade_in
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        greetingText.startAnimation(animation)

        // Navegar al MainActivity después de 3 segundos usando Handler con postDelayed
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
