package com.example.myserviceclient.mydata

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Language::class],version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao():UserDao
    abstract fun languageDao(): LanguageDao
}



