package com.projects.android.todolist.viewmodel

import androidx.lifecycle.*
import com.projects.android.todolist.room.Task
import com.projects.android.todolist.room.TaskRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: TaskRepository) : ViewModel() {
    val allTasks: LiveData<List<Task>> = repository.allTasks.asLiveData()

    fun countTasks(): Int {
        return repository.countTasks()
    }

    fun findById(taskId: Int): LiveData<Task> {
        return repository.findById(taskId)
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun updateTask(oldTitle: String, title: String, date: String, hour: String) =
        viewModelScope.launch {
            repository.updateTask(oldTitle, title, date, hour)
        }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }
}

class MainViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}