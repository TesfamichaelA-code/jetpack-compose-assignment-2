package com.ethiopianairlines.todoapp.ui.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.todoapp.data.model.Todo
import com.ethiopianairlines.todoapp.data.repository.Result
import com.ethiopianairlines.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TodoListState())
    val state: StateFlow<TodoListState> = _state

    init {
        getTodos()
    }

    private fun getTodos() {
        repository.getTodos().onEach { result ->
            when (result) {
                is Result.Success -> {
                    _state.value = TodoListState(
                        todos = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _state.value = TodoListState(
                        todos = result.data ?: emptyList(),
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Result.Loading -> {
                    _state.value = TodoListState(
                        todos = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}

data class TodoListState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
