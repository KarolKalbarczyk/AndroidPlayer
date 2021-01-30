package com.example.player

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import com.example.player.databinding.ActivityMainBinding
import com.example.player.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer : MediaPlayer
    private var index = 0
    private lateinit var binding: ActivityPlayerBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaPlayer = MediaPlayer.create(this, songs[0].songId)
        mediaPlayer.start()

        binding.image.setImageResource(songs[0].imageId)
        binding.seekBar.max = mediaPlayer.duration / 1000
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