package com.moworks.todolist.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.moworks.todolist.R
import java.util.*


const val TIME_CHANNEL_ID ="timer channel"
const val TIME_CHANNEL_NAME ="Pomodoro Timer"
const val TASK_CHANNEL_ID ="task channel"
const val TASK_CHANNEL_NAME ="To Do List"
const val  TIMER_REQUEST_CODE = 1001
const val  TASK_ALARM_REQUEST_CODE = 1001

private var PENDING_REQUEST_ID = 1100




fun createNotificationChannels(context : Context){
    val notificationManager = ContextCompat.getSystemService(context , NotificationManager::class.java) as NotificationManager

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val timerChannel = NotificationChannel(TIME_CHANNEL_ID, TIME_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            setShowBadge(false)
            enableLights(true)
            enableVibration(true)
        }

        val taskChannel = NotificationChannel(TASK_CHANNEL_ID, TASK_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            setShowBadge(false)
            enableLights(true)
            enableVibration(true)
        }

        notificationManager.createNotificationChannels(arrayListOf(timerChannel ,taskChannel))
    }
}



fun showTimerNotification(context : Context){

    val contentIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.navigation)
            .setDestination(R.id.pomodoroFragment)
            .createPendingIntent()

    val style = NotificationCompat.BigTextStyle().bigText(context.resources.getString(R.string.timer_notification_text) )
            .setBigContentTitle(context.resources.getString(R.string.timer_notification_title))



    val builder = NotificationCompat.Builder(context , TIME_CHANNEL_ID).apply {
        setContentTitle(context.resources.getString(R.string.timer_notification_title))
        setContentText(context.resources.getString(R.string.timer_notification_text) )
        setContentIntent(contentIntent)
        setSmallIcon(R.drawable.outline_timer_20)
        priority = NotificationCompat.PRIORITY_HIGH
        setStyle(style)
        color = ContextCompat.getColor(context, R.color.primaryColor)
    }

    val notificationManager = ContextCompat.getSystemService(context , NotificationManager::class.java) as NotificationManager

    notificationManager.notify(TIMER_REQUEST_CODE , builder.build())
}




fun showTaskNotification(context : Context){

    val contentIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.navigation)
            .setDestination(R.id.currentTasksFragment)
            .createPendingIntent()


    val largeIcon = BitmapFactory.decodeResource(context.resources , R.drawable.logo)




    val builder = NotificationCompat.Builder(context , TASK_CHANNEL_ID).apply {
        setContentTitle(context.resources.getString(R.string.alarm_title))
        setContentText(context.resources.getString(R.string.alarm_message))
        setSmallIcon(R.drawable.logo)
        setLargeIcon(largeIcon)
        setContentIntent(contentIntent)
        priority = NotificationCompat.PRIORITY_HIGH
        color = ContextCompat.getColor(context, R.color.primaryColor)
    }

    val notificationManager = ContextCompat.getSystemService(context , NotificationManager::class.java) as NotificationManager

    notificationManager.notify(TASK_ALARM_REQUEST_CODE , builder.build())
}



fun setAlarm(alarmManager: AlarmManager, context: Context, dueDate: Long?, dueTime: Long? ){
    PENDING_REQUEST_ID++

    Log.d("check" , "$ is $PENDING_REQUEST_ID")
    val intent = Intent(context , TaskReceiver::class.java)
    intent.putExtra(TASK_EXTRA ,"task alarm")

    val pendingIntent = PendingIntent.getBroadcast(context , PENDING_REQUEST_ID , intent , PendingIntent.FLAG_ONE_SHOT)

    if ( dueDate != null && dueTime != null ) {

        val time = Calendar.getInstance().apply { this.time = Date(dueTime) }

        val triggerTime = Calendar.getInstance().apply {
            this.time = Date(dueDate)
            this.set(Calendar.HOUR_OF_DAY , time.get(Calendar.HOUR_OF_DAY))
            this.set(Calendar.MINUTE , time.get(Calendar.MINUTE))
        }.timeInMillis

        if (triggerTime <= System.currentTimeMillis()) return

        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager , AlarmManager.RTC_WAKEUP, triggerTime , pendingIntent)
    }
    else if( dueDate != null )
    {
        if (dueDate <= System.currentTimeMillis()) return
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager , AlarmManager.RTC_WAKEUP, dueDate , pendingIntent )

    } else return
}

