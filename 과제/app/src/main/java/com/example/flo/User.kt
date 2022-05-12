package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    // 사용자의 이메일, 패스워드, 유저 인덱스
    var email: String,
    var password: String,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
