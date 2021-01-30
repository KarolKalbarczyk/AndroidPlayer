package com.example.player

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.view.View.inflate
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.player.databinding.ActivityMainBinding
import com.example.player.databinding.ActivityMainBinding.inflate

class MainActivity : AppCompatActivity(){

    private lateinit var binding : ActivityMainBinding

    private lateinit var mediaPlayer: MediaPlayer
    private var index = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        binding.image.setImageResource(songs[index].imageId)

        mediaPlayer = MediaPlayer.create(this, songs[0].songId)
        mediaPlayer.start()
        val action = {
            binding.apply {
                seekBar.progress =  mediaPlayer.currentPosition / 1000
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
            changeSong()
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