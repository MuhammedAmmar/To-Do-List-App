package com.moworks.todolist.currentTasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moworks.todolist.R
import com.moworks.todolist.database.TaskDao
import com.moworks.todolist.database.TaskEntry
import com.moworks.todolist.domain.TaskModel
import com.moworks.todolist.domain.asTaskEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentTasksViewModel(private val database: TaskDao, application: Application): AndroidViewModel(application) {



  val activeTasks :LiveData<List<TaskEntry>> = database.getActiveTasks()

  val finishedTasks : LiveData<List<TaskEntry>> = database.getFinishedTasks()


  private val _label : MutableLiveData<String> = MutableLiveData()

  val label :LiveData<String>
    get() = _label


  private val _hasTasks : MutableLiveData<Boolean> = MutableLiveData()

  val hasTasks :LiveData<Boolean>
    get() = _hasTasks


  init {
    resetListLabel()
  }




  fun clearFinishedTasks(){
    viewModelScope.launch{
      withContext(Dispatchers.IO){
        database.clear()
      }
    }
  }



  fun moveToFinishedList( taskModel: TaskModel){
    taskModel.apply {
      listType = TaskFilter.FINISHED_TASKS.listTypes
    }
    viewModelScope.launch {
      finishedTask(taskModel.asTaskEntry())
    }

  }

  private suspend fun finishedTask(taskEntry: TaskEntry) {
    withContext(Dispatchers.IO){
      database.updateTask(taskEntry)
    }
  }

  fun updateListLabel(label :String){
    _label.value = label
  }

  fun resetListLabel(){
    _label.value = getApplication<Application>().getString(R.string.default_label)
  }

  fun setHasTasks(hasTasks :Boolean = true) {
    _hasTasks.value = hasTasks
  }
}

