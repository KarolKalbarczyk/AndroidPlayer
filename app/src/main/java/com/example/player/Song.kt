package com.example.player

data class Song(val songId: Int, val imageId: Int)

val songs = listOf(
        Song(R.raw.kalimba, R.drawable.nie_w4rto),
        Song(R.raw.maidwiththeflaxenhair, R.drawable.the_tragedy),
        Song(R.raw.sanic, R.drawable.the_senate),
        Song(R.raw.sleepaway, R.drawable.spooktober),
        Song(R.raw.titanic, R.drawable.shrek)
)
