package com.projects.android.todolist.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table ORDER BY title ASC")
    fun getAlphabetizedTasks(): Flow<List<Task>>

    @Query("SELECT count(*) FROM task_table")
    fun countTasks(): Int

    @Query("SELECT * FROM task_table WHERE id LIKE :taskId LIMIT 1")
    fun findById(taskId: Int): LiveData<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("UPDATE task_table SET title = :title, date = :date, hour = :hour WHERE title = :oldTitle")
    suspend fun updateTask(oldTitle: String, title: String, date: String, hour: String)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

}
