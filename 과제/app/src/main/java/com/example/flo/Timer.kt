package com.example.flo

import android.util.Log

class Timer(private val playTime: Int, private var isPlaying: Boolean,
            private var playMills: Float) : Thread() {

    fun getPlayTime() : Int {
        return playTime
    }
    fun getPlayMills() : Float {
        return playMills
    }
    fun getIsPlaying() : Boolean {
        return isPlaying
    }

    fun setPlayMills(playMills: Float) {
        this.playMills = playMills
    }

    fun setIsPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }

    fun millSecPass(millsec: Float) {
        if ((playMills/1000).toInt() >= playTime){
            playMills = 0f
            isPlaying = false
        }
        playMills += millsec
        Log.d("timer play", (playMills*100/playTime).toString())
    }
}