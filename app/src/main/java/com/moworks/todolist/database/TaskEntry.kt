package com.moworks.todolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="tasks")
data class TaskEntry (
        @PrimaryKey(autoGenerate = true)
        val taskId :Long = 0L,
        @ColumnInfo(name ="task_description")
        val taskDescription :String,
        @ColumnInfo(name ="start_time")
        val startTime : Long = System.currentTimeMillis(),
        @ColumnInfo(name ="due_date")
        val dueDate :Long? ,
        @ColumnInfo(name = "due_time")
        val dueTime :Long?,
        @ColumnInfo(name = "list_type")
        val listType : String = "default"
)