package com.ethiopianairlines.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey
    val id: Int,
    val title: String,
    val completed: Boolean,
    @Json(name = "userId")
    val userId: Int
)
