package com.moworks.todolist.editTask
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moworks.todolist.database.TaskDao
import com.moworks.todolist.database.TaskEntry
import com.moworks.todolist.domain.TaskModel
import com.moworks.todolist.domain.asTaskEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditTaskViewModel(private val database: TaskDao , application: Application) : AndroidViewModel(application){


    private  val _timeVisibility: MutableLiveData<Boolean> = MutableLiveData()

    val timeVisibility : LiveData<Boolean>
        get() = _timeVisibility

    private  val _taskModel : MutableLiveData<TaskModel> = MutableLiveData()

    val taskModel : LiveData<TaskModel>
        get() = _taskModel



    private val _hasBackDispatcherPressed :MutableLiveData<Boolean> = MutableLiveData()

    val hasBackDispatcherPressed :LiveData<Boolean>
        get() = _hasBackDispatcherPressed



    init {
        _hasBackDispatcherPressed.value = false
    }






    fun updateTask(){
        viewModelScope.launch{
            updateSingleTask(_taskModel.value?.asTaskEntry()!!)
        }
    }


    private suspend fun updateSingleTask(taskEntry: TaskEntry){
        withContext(Dispatchers.IO){
            database.updateTask(taskEntry)
        }
    }



    fun deleteTask(){
        viewModelScope.launch{
            deleteSingleTask(_taskModel.value?.asTaskEntry()!!)
        }
    }


    private suspend fun deleteSingleTask(taskEntry: TaskEntry){
        withContext(Dispatchers.IO){
            database.deleteTask(taskEntry)
        }
    }



    // utilities

    fun newTaskModel(taskModel: TaskModel?){
        _taskModel.value = taskModel
    }

    fun newTaskDescription(taskModel: TaskModel?) : Boolean{
        return if (! taskModel?.taskDescription.isNullOrEmpty()) {
            _taskModel.value  = taskModel
            true

        }else false
    }


    fun hasTimeVisibility(isVisible : Boolean = true){
        _timeVisibility.value = isVisible
    }



    fun hasBackPressed(){
        _hasBackDispatcherPressed.value = true
    }


    fun hasBackPressedNavigatedOrCanceled(){
        _hasBackDispatcherPressed.value = false
    }

    override fun onCleared() {
        super.onCleared()
        _taskModel.value = null
    }
}
