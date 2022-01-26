package com.moworks.todolist.timer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TimerFactory(val application : Application) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)){
            return TimerViewModel(application) as T
        }
        throw IllegalArgumentException("invalid ViewModel ")
    }

}