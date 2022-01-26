package com.moworks.todolist.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [TaskEntry::class] , version = 1 ,exportSchema = false)
abstract class TasksDatabase :RoomDatabase (){

    abstract val  taskDao: TaskDao

    companion object {
        @Volatile
        private var INSTANCE : TasksDatabase? = null

        private const val DATABASE_NAME :String = "task_database"

        fun getInstance(context: Context):TasksDatabase {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null ){
                    instance = Room.databaseBuilder(context.applicationContext,
                            TasksDatabase::class.java ,
                            DATABASE_NAME )
                            .fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}