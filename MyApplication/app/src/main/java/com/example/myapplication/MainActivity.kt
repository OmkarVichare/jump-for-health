package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    private lateinit var jumpCountTextView: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private var isJumping = false
    private var jumpCount = 0
    private lateinit var mediaPlayer: MediaPlayer
    private var isFirstStart = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jumpCountTextView = findViewById(R.id.jumpCountTextView)
        startButton = findViewById(R.id.startButton)
        resetButton = findViewById(R.id.resetButton)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

// Create the MediaPlayer object and set the audio resource
        mediaPlayer = MediaPlayer.create(this, R.raw.all_set_audio)


        startButton.setOnClickListener {

            if (isFirstStart) {
                mediaPlayer.start()
                isFirstStart = false
            } else if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }

            // Start listening to the accelerometer sensor
            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

            showToast("You're all set to jump!")
        }

        resetButton.setOnClickListener {
            resetJumpCounter()
        }

        val bannerImages = listOf(
            R.drawable.img_8,
            R.drawable.img_3,
            R.drawable.img_7,
            R.drawable.img_4,
            R.drawable.img_6,
            R.drawable.img_9,
            R.drawable.img_5,
            R.drawable.img_2
        )
        val bannerRecyclerView: RecyclerView = findViewById(R.id.bannerRecyclerView)
        bannerRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val bannerAdapter = BannerAdapter(bannerImages)
        bannerRecyclerView.adapter = bannerAdapter
        bannerRecyclerView.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.banner_item_spacing)))

    }

    private fun resetJumpCounter() {
        // Stop listening to the accelerometer sensor
        sensorManager.unregisterListener(sensorListener)
        jumpCount = 0
        updateJumpCountText()
    }

    private fun updateJumpCountText() {
        val jumpCountText = "Jump Count: $jumpCount"
        jumpCountTextView.text = jumpCountText
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        // Stop listening to the accelerometer sensor when the app is paused or closed
        sensorManager.unregisterListener(sensorListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer resources
        mediaPlayer.release()
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            val zAcceleration = event.values[2]
            val threshold = 3.0f

            if (zAcceleration > threshold && !isJumping) {
                isJumping = true
                jumpCount++
                updateJumpCountText()
            } else if (zAcceleration < -threshold) {
                isJumping = false
            }
        }
    }
}
