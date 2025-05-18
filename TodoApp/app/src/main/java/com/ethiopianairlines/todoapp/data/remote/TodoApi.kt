package com.ethiopianairlines.todoapp.data.remote

import com.ethiopianairlines.todoapp.data.model.Todo
import retrofit2.http.GET

interface TodoApi {
    @GET("todos")
    suspend fun getTodos(): List<Todo>
}
