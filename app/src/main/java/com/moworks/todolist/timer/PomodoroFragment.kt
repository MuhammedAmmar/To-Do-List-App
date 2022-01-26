package com.moworks.todolist.timer

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.moworks.todolist.R
import com.moworks.todolist.databinding.FragmentPomodoroBinding
import java.text.SimpleDateFormat
import java.util.*

class PomodoroFragment : Fragment() {


    private  lateinit var  binding : FragmentPomodoroBinding
    private lateinit var  viewModel: TimerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPomodoroBinding.inflate(inflater)

        val factory = TimerFactory(requireContext().applicationContext as Application)
        viewModel = ViewModelProvider(this , factory).get(TimerViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.fragment = this

        binding.indicator.isIndeterminate = false
        binding.indicator.max = 100





        viewModel.timer.observe(viewLifecycleOwner , Observer {  timer ->
            if(timer != null) {
                binding.timerTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timer)
            }else{
                binding.timerTv.text = getString(R.string.initial_timer)
            }
        })


        viewModel.progress.observe(viewLifecycleOwner , Observer { progress ->

            if(progress != null ) {
                binding.indicator.setProgressCompat(progress, true)
            }else{
                binding.indicator.setProgressCompat(0, true)
            }

        })

        binding.interval25M.setOnLongClickListener {
            viewModel.start25M()
            true
        }

        binding.pauseTimer.setOnLongClickListener{

            viewModel.panTimer()
            true
        }

        binding.startBreak.setOnLongClickListener{
            viewModel.startBreak()
            true
        }



        return binding.root
    }



    fun  onStart25M(){
        Snackbar.make(binding.interval25M, requireContext().getString(R.string.start_25_timer) ,Snackbar.LENGTH_SHORT ).show()
    }


    fun onStartBreak(){
        Snackbar.make(binding.interval25M, requireContext().getString(R.string.start_break_timer) ,Snackbar.LENGTH_SHORT ).show()

    }


    fun onPanTimer() {
        Snackbar.make(binding.interval25M, requireContext().getString(R.string.pause_timer) ,Snackbar.LENGTH_SHORT ).show()

    }


}