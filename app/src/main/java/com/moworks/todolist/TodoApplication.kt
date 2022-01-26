package com.moworks.todolist

import android.app.Application
import timber.log.Timber

class TodoApplication :Application (){

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}