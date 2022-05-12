package com.example.flo

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase

@Database(entities = [Song::class, User::class, Like::class, Album::class], version = 1) // 유저 클래스 추가
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database" // 다른 데이터 베이스 이름이랑 안 겹치게
                    ).allowMainThreadQueries().build()
                }
            }

            return instance
        }
    }
}