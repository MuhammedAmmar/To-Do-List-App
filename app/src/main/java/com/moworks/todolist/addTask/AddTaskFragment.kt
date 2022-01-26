package com.moworks.todolist.addTask

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.moworks.todolist.R
import com.moworks.todolist.createDatePickerDialog
import com.moworks.todolist.createDialog
import com.moworks.todolist.createTimePickerDialog
import com.moworks.todolist.database.TaskDao
import com.moworks.todolist.database.TaskEntry
import com.moworks.todolist.database.TasksDatabase
import com.moworks.todolist.databinding.FragmentAddTaskBinding
import com.moworks.todolist.notifications.setAlarm
import java.util.*

class AddTaskFragment : Fragment() , DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{
    private  lateinit var binding : FragmentAddTaskBinding
    private lateinit var  database :TaskDao
    private lateinit var viewModel: AddTaskViewModel
    private lateinit var dialog : AlertDialog



    private val keepEditingListener = DialogInterface.OnClickListener { dialog, _ ->
        dialog?.dismiss()
        viewModel.hasBackPressedNavigatedOrCanceled()
        findNavController().popBackStack()
    }

    private val cancelListener = DialogInterface.OnClickListener { dialog, _ ->
        dialog?.dismiss()
        viewModel.hasBackPressedNavigatedOrCanceled()
    }



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)
        database = TasksDatabase.getInstance(requireContext()).taskDao

        val alarmManager : AlarmManager? = ContextCompat.getSystemService(requireContext() , AlarmManager::class.java)

        val factory = AddTaskViewModelFactory(database, requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory).get(AddTaskViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        dialog = createDialog(requireContext() ,keepEditingListener , cancelListener ).apply {

            setCanceledOnTouchOutside(true)

            setOnCancelListener {
                viewModel.hasBackPressedNavigatedOrCanceled()
            }
        }


        //  date picker
        binding.dateEditText.setOnClickListener {
            createDatePickerDialog(requireContext() ,this).show()
        }


        // time picker
        binding.timeEditText.setOnClickListener{
            createTimePickerDialog(requireContext() , this ).show()
        }

        binding.dateEditTextContainer.setEndIconOnClickListener {
            viewModel.clearDate()
            viewModel.clearTime()
            viewModel.hasTimeVisibility(false)
        }

        binding.timeEditTextContainer.setEndIconOnClickListener {
            viewModel.clearTime()
        }



        binding.fab.setOnClickListener {
            viewModel.newTaskDescription(binding.taskDescription.text.toString().trim())
            val taskDescription = viewModel.taskDescription.value
            val dueDate: Long? = viewModel.dateCalendar.value?.timeInMillis
            val dueTime: Long? = viewModel.timeCalendar.value?.timeInMillis

            if (taskDescription.isNullOrEmpty()) {
                Snackbar.make(requireView(), getString(R.string.add_describtion_msg), Snackbar.LENGTH_SHORT).show()
            } else{

                viewModel.insertTask(TaskEntry(taskDescription = taskDescription, dueDate = dueDate, dueTime = dueTime))

                setAlarm(alarmManager!! , requireContext() , dueDate , dueTime)
                viewModel.reset()
                findNavController().popBackStack()
            }

        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if( viewModel.hasBackDispatcherPress.value == true ){
            dialog.show()

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.hasBackPressed()
                dialog.show()

            }
        })

    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        viewModel.newDate(calendar)
        viewModel.hasTimeVisibility()
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        viewModel.newTime(calendar)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModel.hasBackPressed()
                dialog.show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        dialog.dismiss()
    }

}