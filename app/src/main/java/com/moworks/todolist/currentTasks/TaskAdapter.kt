package com.moworks.todolist.currentTasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moworks.todolist.databinding.TaskItemListBinding
import com.moworks.todolist.domain.TaskModel

class TaskAdapter(private val finishedTaskListener: TaskListener, private val updateTaskListener: TaskListener ) : ListAdapter<TaskModel,TaskAdapter.TasksViewHolder>(DiffUtilCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val  item : TaskModel = getItem(position)
        holder.bind(item , finishedTaskListener , updateTaskListener )
    }



    class TasksViewHolder(private val binding:TaskItemListBinding) :RecyclerView.ViewHolder(binding.root){
        companion object {
            fun from(parent: ViewGroup): TasksViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = TaskItemListBinding.inflate(inflater, parent , false)
                return TasksViewHolder(binding)
            }
        }

        fun bind(item: TaskModel, finishedTaskListener: TaskListener , updateTask :TaskListener) {
            binding.taskModel = item
            binding.finishedTaskListener = finishedTaskListener
            binding.updateListener = updateTask
            binding.executePendingBindings()

        }
    }


}

class DiffUtilCallback : DiffUtil.ItemCallback<TaskModel>(){
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return  oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem.taskId == newItem.taskId
    }
}

 class TaskListener(private val clickListener : (taskModel: TaskModel)->Unit){

    fun onClick(taskModel: TaskModel) = clickListener(taskModel )

}
