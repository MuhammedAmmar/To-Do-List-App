package com.moworks.todolist.notifications
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val TASK_EXTRA ="task alarm"
class TaskReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.extras!!.containsKey(TASK_EXTRA)) {
            showTaskNotification(context)

        }
    }
}