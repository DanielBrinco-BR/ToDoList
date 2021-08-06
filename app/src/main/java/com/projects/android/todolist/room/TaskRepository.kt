package com.projects.android.todolist.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAlphabetizedTasks()

    fun countTasks(): Int {
        return taskDao.countTasks()
    }

    fun findById(taskId: Int): LiveData<Task> {
        return taskDao.findById(taskId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateTask(oldTitle: String, title: String, date: String, hour: String) {
        taskDao.updateTask(oldTitle, title, date, hour)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}
