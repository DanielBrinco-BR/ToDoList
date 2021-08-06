package com.projects.android.todolist.viewmodel

import androidx.lifecycle.LiveData

interface BaseViewModel {
    fun getTitle(): LiveData<String>
    fun getDate(): LiveData<String>
    fun getHour(): LiveData<String>
    fun saveTitle(username: String)
    fun saveDate(date: String)
    fun saveHour(hour: String)
}