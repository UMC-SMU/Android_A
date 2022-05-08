package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

// 제목, 가수, 사진, 재생시간, 현재 재생시산, isPlaying(재생 되고 있는지)

@Entity(tableName = "SongTable")
data class Song(
    val title : String = "",
    val singer : String = "",
    var second: Int = 0, // 재생 시간
    var playTime: Int = 0, // 총 재생 시간
    var isPlaying: Boolean = false,
    var music: String ="",
    var coverImg: Int? = null,
    var isLike: Boolean = false
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}