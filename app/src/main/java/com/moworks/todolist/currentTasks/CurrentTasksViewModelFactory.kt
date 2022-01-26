package com.moworks.todolist.currentTasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moworks.todolist.database.TaskDao


class CurrentTasksViewModelFactory(private val database: TaskDao , private val application: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CurrentTasksViewModel::class.java)){
            return CurrentTasksViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}