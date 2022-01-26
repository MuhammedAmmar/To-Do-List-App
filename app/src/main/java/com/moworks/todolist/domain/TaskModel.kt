package com.moworks.todolist.domain

import android.os.Parcelable
import com.moworks.todolist.database.TaskEntry
import kotlinx.android.parcel.Parcelize

@Parcelize
 data class TaskModel(
         var taskId:Long,
         var taskDescription: String,
         var startTime: Long,
         var dueDate:Long?,
         var dueTime:Long?,
         var listType : String
):Parcelable


fun TaskEntry.asTaskModel():TaskModel{
     return TaskModel(taskId ,taskDescription ,startTime , dueDate , dueTime, listType)
 }


fun TaskModel.asTaskEntry():TaskEntry{
    return TaskEntry(taskId ,taskDescription ,startTime ,dueDate ,dueTime , listType)
}

fun List<TaskEntry>.asTaskModelList ():List<TaskModel>{
    return  map {
        TaskModel(it.taskId ,it.taskDescription , it.startTime , it.dueDate , it.dueTime , it.listType)
    }
}

fun List<TaskModel>.asTaskEntryList ():List<TaskEntry>{
    return  map {
        TaskEntry(it.taskId ,it.taskDescription , it.startTime , it.dueDate , it.dueTime , it.listType)
    }
}