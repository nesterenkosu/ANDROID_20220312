package com.example.myserviceclient.mydata

import androidx.room.*

@Dao
interface UserDao {
    //CRUD - Create Read Update Delete
    //---------Create---------
    @Insert
    fun insertAll(vararg user: User)

    //---------Read---------

    //все пользователи
    @Query("SELECT * FROM table_users")
    fun getAll():List<User>

    //пользователь с заданным id
    @Query("SELECT * FROM table_users WHERE uid=:Id")
    fun getUserById(Id:Int):User

    //---------Update---------
    @Update
    fun update(user:User)

    //---------Delete---------
    @Delete
    fun delete(user:User)

    @Query("DELETE FROM table_users")
    fun deleteAll()

}

