package com.example.flo

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insert(user : User)

    @Query("SELECT * FROM UserTable")
    fun getUsers() : List<User>

    @Query("SELECT * FROM UserTable WHERE id = :id")
    fun getUserById(id:Int) : User

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String) : User? // 정보가 있을 수도 있고 없을 수도 있으니 null 처리
}