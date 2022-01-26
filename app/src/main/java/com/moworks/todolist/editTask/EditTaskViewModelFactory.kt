package com.moworks.todolist.editTask

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moworks.todolist.database.TaskDao

class EditTaskViewModelFactory (private val database : TaskDao , private val application : Application):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditTaskViewModel::class.java)){
            return EditTaskViewModel(database , application) as T
        }
        throw IllegalArgumentException("class not exist ")
    }

}