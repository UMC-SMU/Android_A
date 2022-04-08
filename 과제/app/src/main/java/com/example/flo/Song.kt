package com.example.flo

data class Song(
    val title : String = "",
    val singer : String = "",
    var second: Int = 0, // 재생 시간
    var playTime: Int = 0, // 총 재생 시간
    var isPlaying: Boolean = false
)