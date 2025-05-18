package com.ethiopianairlines.todoapp.ui.tododetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethiopianairlines.todoapp.data.model.Todo
import com.ethiopianairlines.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TodoDetailState())
    val state: StateFlow<TodoDetailState> = _state

    init {
        savedStateHandle.get<Int>("todoId")?.let { todoId ->
            getTodo(todoId)
        }
    }

    private fun getTodo(id: Int) {
        repository.getTodoById(id).onEach { todo ->
            _state.value = TodoDetailState(todo = todo)
        }.launchIn(viewModelScope)
    }
}

data class TodoDetailState(
    val todo: Todo? = null
)
