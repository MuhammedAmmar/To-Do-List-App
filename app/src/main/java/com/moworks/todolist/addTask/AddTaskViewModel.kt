package com.moworks.todolist.addTask
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moworks.todolist.database.TaskDao
import com.moworks.todolist.database.TaskEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class AddTaskViewModel(private val database:TaskDao ,application: Application) :AndroidViewModel(application) {



    private  val _timeCalendar : MutableLiveData<Calendar?> = MutableLiveData()

    val timeCalendar : LiveData<Calendar?>
    get() = _timeCalendar


    private  val _dateCalendar : MutableLiveData<Calendar?> = MutableLiveData()

    val dateCalendar : LiveData<Calendar?>
    get() = _dateCalendar



    private  val _taskDescription : MutableLiveData<String> = MutableLiveData()

    val taskDescription : LiveData<String>
    get() = _taskDescription

    private  val _timeVisibility: MutableLiveData<Boolean> = MutableLiveData()

    val timeVisibility : LiveData<Boolean>
        get() = _timeVisibility




    private val _hasBackDispatcherPress :MutableLiveData<Boolean> = MutableLiveData()

    val hasBackDispatcherPress :LiveData<Boolean>
        get() = _hasBackDispatcherPress



    init {
        _hasBackDispatcherPress.value = false

    }

    //  coroutines
     fun insertTask (taskEntry:TaskEntry){
        viewModelScope.launch{
            addSingleTask(taskEntry)
        }
    }

    private suspend fun addSingleTask(taskEntry : TaskEntry){
        withContext(Dispatchers.IO) {
            database.insert(taskEntry)
        }
    }




    fun newTaskDescription(taskDescription: String){
        _taskDescription.value = taskDescription
    }


    fun newDate(calendar: Calendar?){
        _dateCalendar.value = calendar
    }


    fun newTime(calendar: Calendar?){
        _timeCalendar.value = calendar
    }


    fun clearDate(){
        _dateCalendar.value = null
    }


    fun clearTime() {
      _timeCalendar.value = null
    }

    fun hasTimeVisibility(isVisible : Boolean = true){
        _timeVisibility.value = isVisible
    }


    fun hasBackPressed(){
        _hasBackDispatcherPress.value = true
    }


    fun hasBackPressedNavigatedOrCanceled(){
        _hasBackDispatcherPress.value = false
    }




    fun reset(){
        _dateCalendar.value = null
        _taskDescription.value = null
        _timeCalendar.value = null
        _timeVisibility.value = false
    }

}