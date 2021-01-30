package com.example.player

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.inflate
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.player.databinding.ActivityMainBinding
import com.example.player.databinding.ActivityMainBinding.inflate

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        val pendingIntent: PendingIntent =
            Intent(this, ExampleActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.drawable.icon)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.ticker_text))
            .build()

// Notification ID cannot be 0.
        startForeground(ONGOING_NOTIFICATION_ID, notification)
        val action = {
            binding.apply {
                seekBar.progress = mediaPlayer.currentPosition / 1000
            }
        }

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                action()
                mainHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun changeTime(seconds: Int){
        val newTime = mediaPlayer.currentPosition + seconds * 1000
        val newPosition = if (newTime < 0) 0 else if (newTime > mediaPlayer.duration) mediaPlayer.duration - 1 else newTime
        mediaPlayer.seekTo(newPosition)
    }

    fun rewind(view: View){
        changeTime(-10)
    }

    fun forward(view: View){
        changeTime(10)
    }

    fun stop(view: View){
        if (mediaPlayer.isPlaying)
            mediaPlayer.stop()
        else
            mediaPlayer.start()
    }

    fun pause(view: View){
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
        else
            mediaPlayer.start()
    }

    private fun changeSong(){
        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this, songs[index].songId)
        binding.image.setImageResource(songs[index].imageId)
        mediaPlayer.start()
        binding.seekBar.max = mediaPlayer.duration / 1000
    }

    fun next(view: View){
        index = (index + 1) % songs.size
        changeSong()
    }

    fun previous(view: View){
        index = (if (index == 0) songs.size else index) - 1
        changeSong()
    }



}