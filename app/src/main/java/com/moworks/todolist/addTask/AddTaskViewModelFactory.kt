package com.moworks.todolist.addTask

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moworks.todolist.database.TaskDao

class AddTaskViewModelFactory(private val database :TaskDao , private val application : Application) :ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTaskViewModel::class.java)){
            return AddTaskViewModel(database, application) as T
        }
        throw IllegalArgumentException("class not exist ")
    }
}