package com.projects.android.todolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class AddTaskViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(),
    BaseViewModel {
    companion object {
        const val TITLE = "TITLE"
        const val DATE = "DATE"
        const val HOUR = "HOUR"
    }

    override fun getTitle(): LiveData<String> {
        return savedStateHandle.getLiveData(TITLE)
    }

    override fun getDate(): LiveData<String> {
        return savedStateHandle.getLiveData(DATE)
    }

    override fun getHour(): LiveData<String> {
        return savedStateHandle.getLiveData(HOUR)
    }

    override fun saveTitle(title: String) {
        savedStateHandle.set(TITLE, title)
    }

    override fun saveDate(date: String) {
        savedStateHandle.set(DATE, date)
    }

    override fun saveHour(hour: String) {
        savedStateHandle.set(HOUR, hour)
    }
}