package com.example.myserviceclient.mydata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_languages")
data class Language(
        @PrimaryKey(autoGenerate = true) var uid: Int?,
        @ColumnInfo(name="name") var Name: String


) {
    override fun toString(): String {
        return Name
    }
}

