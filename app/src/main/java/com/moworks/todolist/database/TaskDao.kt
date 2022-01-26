package com.moworks.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
 interface TaskDao {
    @Insert
    fun insert (taskEntry: TaskEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(taskEntry: TaskEntry)

    @Delete
    fun deleteTask(taskEntry: TaskEntry)


    @Query("SELECT * FROM tasks WHERE list_type = :listType")
    fun getActiveTasks (listType : String ="default"):LiveData<List<TaskEntry>>


    @Query("SELECT * FROM tasks WHERE list_type =:listType")
    fun getFinishedTasks (listType : String ="finished"):LiveData<List<TaskEntry>>

    @Query("DELETE FROM tasks WHERE list_type =:listType ")
    fun clear (listType : String ="finished")

}