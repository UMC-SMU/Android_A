package com.example.flo

import androidx.room.*

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    @Insert
    fun likeAlbum(like: Like)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikedAlbum(userId:Int, albumId:Int): Int? //null 처리

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikedAlbum(userId:Int, albumId:Int) //null 처리

    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")
    fun getLikedAlbums(userId: Int) : List<Album>
    // LikeTable 기준으로 join, LikeTable에 있는 id와 Albumid의 id가 같은 것끼리
}