package com.moworks.todolist.editTask
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
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
import com.moworks.todolist.database.TasksDatabase
import com.moworks.todolist.databinding.FragmentEditTaskBinding
import com.moworks.todolist.domain.TaskModel
import java.util.*


class EditTaskFragment : Fragment() , DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{

    private lateinit var binding:FragmentEditTaskBinding
    private lateinit var  database : TaskDao
    private lateinit var  viewModel :EditTaskViewModel
    private lateinit var  args :TaskModel
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_task, container, false)
        setHasOptionsMenu(true)

        database = TasksDatabase.getInstance(requireContext()).taskDao

        args  = EditTaskFragmentArgs.fromBundle(requireArguments()).taskModel
        val factory = EditTaskViewModelFactory(database  , requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory).get(EditTaskViewModel::class.java)

        viewModel.newTaskModel(args)
        viewModel.taskModel.observe(viewLifecycleOwner){  task ->
            if(task.dueDate != null ){
                viewModel.hasTimeVisibility()
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.taskModel = args
        binding.viewModel = viewModel

        dialog = createDialog(requireContext() ,keepEditingListener , cancelListener ).apply {

            setCanceledOnTouchOutside(true)

            setOnCancelListener(object : DialogInterface.OnCancelListener {
                override fun onCancel(dialog: DialogInterface?) {
                    viewModel.hasBackPressedNavigatedOrCanceled()
                }

            })
        }

        //  date picker
        binding.dateEditText.setOnClickListener {
            createDatePickerDialog(requireContext(), this).show()
        }


        // time picker
        binding.timeEditText.setOnClickListener{
            createTimePickerDialog(requireContext(), this).show()
        }

        binding.dateEditTextContainer.setEndIconOnClickListener {
            args.dueDate = null
            args.dueTime = null

            viewModel.newTaskModel(args)
            viewModel.hasTimeVisibility(isVisible = false)
        }

        binding.timeEditTextContainer.setEndIconOnClickListener {
            args.dueTime = null
            viewModel.newTaskModel(args)
        }


        binding.fab.setOnClickListener{
            args.taskDescription = binding.taskDescription.text.toString().trim()
            val hasText = viewModel.newTaskDescription(args)
            if (hasText) {
                viewModel.updateTask()
                findNavController().popBackStack()

            } else {
                Snackbar.make(requireView(), getString(R.string.add_describtion_msg), Snackbar.LENGTH_SHORT).show()  }
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if( viewModel.hasBackDispatcherPressed.value == true ){
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
        args.dueDate = calendar.timeInMillis
        viewModel.newTaskModel(args)
        viewModel.hasTimeVisibility()
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        args.dueTime = calendar.timeInMillis
        viewModel.newTaskModel(args)
    }





    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu , menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home ->{
                viewModel.hasBackPressed()
                dialog.show()
                return  true
            }
            R.id.delete_option ->{
                viewModel.deleteTask()
                findNavController().popBackStack()
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