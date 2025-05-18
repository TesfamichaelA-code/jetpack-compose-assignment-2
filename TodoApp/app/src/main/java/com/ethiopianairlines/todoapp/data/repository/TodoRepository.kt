package com.ethiopianairlines.todoapp.data.repository

import com.ethiopianairlines.todoapp.data.local.TodoDao
import com.ethiopianairlines.todoapp.data.model.Todo
import com.ethiopianairlines.todoapp.data.remote.TodoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject

/**
 * Repository that handles Todo operations with both local and remote data sources.
 * Follows the single source of truth principle by using the local database as the main source.
 */
class TodoRepository @Inject constructor(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {
    /**
     * Gets all todos from the local database. If the database is empty,
     * it populates it with predefined English todos.
     * 
     * @return Flow emitting Results containing Todo lists.
     */
    fun getTodos(): Flow<Result<List<Todo>>> = flow {
        try {
            // Emit loading state with any cached data we have
            val cachedData = try {
                todoDao.getAllTodos().first()
            } catch (e: Exception) {
                emptyList()
            }
            
            emit(Result.Loading(data = cachedData))

            // Check if we have any data stored locally
            if (cachedData.isEmpty()) {
                // Populate with English tasks
                val englishTodos = createEnglishTodos()
                todoDao.insertTodos(englishTodos)
                emit(Result.Success(englishTodos))
            } else {
                // We already have data, just return it
                emit(Result.Success(cachedData))
            }
        } catch (e: IOException) {
            // Handle network errors
            val cachedData = try {
                todoDao.getAllTodos().first()
            } catch (ex: Exception) {
                emptyList()
            }
            emit(Result.Error("Network error. Please check your connection.", cachedData))
        } catch (e: Exception) {
            // Handle general errors
            val cachedData = try {
                todoDao.getAllTodos().first()
            } catch (ex: Exception) {
                emptyList()
            }
            emit(Result.Error("An unexpected error occurred: ${e.message}", cachedData))
        }
    }

    /**
     * Gets a specific todo by ID from the local database.
     * 
     * @param id The ID of the todo to fetch.
     * @return Flow emitting the requested Todo.
     */
    fun getTodoById(id: Int): Flow<Todo> = todoDao.getTodoById(id)
    
    /**
     * Creates a predefined list of English todos.
     */
    private fun createEnglishTodos(): List<Todo> {
        return listOf(
            Todo(id = 1, title = "Complete weekly report", completed = false, userId = 1),
            Todo(id = 2, title = "Buy groceries for dinner", completed = false, userId = 1),
            Todo(id = 3, title = "Schedule dentist appointment", completed = false, userId = 1),
            Todo(id = 4, title = "Clean the kitchen", completed = true, userId = 1),
            Todo(id = 5, title = "Finish reading book chapter", completed = false, userId = 1),
            Todo(id = 6, title = "Respond to important emails", completed = false, userId = 1),
            Todo(id = 7, title = "Pay monthly bills", completed = false, userId = 1),
            Todo(id = 8, title = "Go for a morning run", completed = true, userId = 1),
            Todo(id = 9, title = "Call mom for her birthday", completed = false, userId = 1),
            Todo(id = 10, title = "Plan weekend activities", completed = false, userId = 1)
        )
    }
}

/**
 * Represents the result of an operation that can be in one of three states:
 * Success, Error, or Loading.
 */
sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
    class Loading<T>(data: T? = null) : Result<T>(data)
}
