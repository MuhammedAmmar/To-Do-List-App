package com.moworks.todolist


import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import com.moworks.todolist.currentTasks.TaskFilter
import com.moworks.todolist.currentTasks.TaskListener
import com.moworks.todolist.domain.TaskModel
import java.text.SimpleDateFormat
import java.util.*


/// populate Item List
@BindingAdapter("date")
fun bindDateStatement(view: TextView, item: TaskModel?) {
    if (item?.dueDate == null) {
        view.visibility = View.GONE
    } else {
        var time =""
        if (item.dueTime != null ){
            time = SimpleDateFormat( ", h:mm a" ,Locale.getDefault()).format(item.dueTime)
        }
        val formatter: String = SimpleDateFormat("E, MMM dd, yyyy", Locale.getDefault()).format(Date(item.dueDate!!)) + time
        view.text = formatter
    }
}


@BindingAdapter(value = ["task_item" , "listener"] , requireAll = true )
fun bindCheckboxState(checkBox: CheckBox , item : TaskModel , listener : TaskListener){
    when(item.listType){

        TaskFilter.ALL_TASKS.listTypes ->{
            checkBox.apply {
                isEnabled = true
                isChecked = false
                setOnClickListener { listener.onClick(item) }
            }
        }

        TaskFilter.FINISHED_TASKS.listTypes -> {
            checkBox.apply {
                isChecked = true
                isEnabled = false
            }

        }

    }
}




// add task population
@BindingAdapter("dateLabel")
fun bindDateLabel( view :EditText , calendar: Calendar?){
    if (calendar != null ) {
        val myFormat = "E, MMM dd, yyyy"
        val formatter = SimpleDateFormat(myFormat , Locale.getDefault())
        view.setText(formatter.format(calendar.time))
    }else{
        view.text = null
    }
}

@BindingAdapter("timeLabel")
fun bindTimeLabel( view :EditText , calendar: Calendar?){
    if(calendar != null) {
        val myFormat = "h:mm a"
        val formatter = SimpleDateFormat(myFormat , Locale.getDefault())
        view.setText(formatter.format(calendar.time))
    }else{
        view.text = null
    }
}


@BindingAdapter("isVisible")
fun setTimeVisibility(view :LinearLayout, visibility :Boolean){
    if (visibility){
        view.visibility = View.VISIBLE
    }else{
        view.visibility = View.INVISIBLE
    }

}


/// edit activity

@BindingAdapter("dateUpdate")
fun bindDateUpdate( view :EditText , taskModel: TaskModel){
    if (taskModel.dueDate != null ) {
        val myFormat = "E, MMM dd, yyyy"
        val formatter = SimpleDateFormat(myFormat ,Locale.getDefault())
        view.setText(formatter.format(taskModel.dueDate))
    }else{
        view.text = null
    }
}

@BindingAdapter("timeUpdate")
fun bindTimeUpdate( view :EditText , taskModel: TaskModel){
    if (taskModel.dueTime != null ) {
        val myFormat = "h:mm a"
        val formatter = SimpleDateFormat(myFormat ,Locale.getDefault())
        view.setText(formatter.format(taskModel.dueTime))
    }else{
        view.text = null
    }
}


// timer play pause
@BindingAdapter("timer_state")
fun bindTimerState(view :ImageButton , isPlaying: Boolean){
    if (isPlaying){
        view.setImageResource(R.drawable.baseline_pause_white_48)
    }else{
        view.setImageResource(R.drawable.baseline_play_arrow_white_48)
    }

}

@BindingAdapter("contentVisibility")
fun bindContentVisibility(view :View , hasTasks : Boolean){
   if (hasTasks){
       view.visibility = View.GONE
    }else{
       view.visibility = View.VISIBLE
    }
}

