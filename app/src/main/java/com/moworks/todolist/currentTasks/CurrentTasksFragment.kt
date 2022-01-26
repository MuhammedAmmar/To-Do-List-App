package com.moworks.todolist.currentTasks

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.moworks.todolist.R
import com.moworks.todolist.database.TaskEntry
import com.moworks.todolist.database.TasksDatabase
import com.moworks.todolist.databinding.FragmentCurrentTasksBinding
import com.moworks.todolist.domain.TaskModel
import com.moworks.todolist.domain.asTaskModelList


class CurrentTasksFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding :FragmentCurrentTasksBinding
    private  lateinit var viewModel: CurrentTasksViewModel
    private lateinit var  adapter: TaskAdapter
    private  lateinit var  searchableTasksList : List<TaskModel>
    private lateinit var FinishedList : List<TaskEntry>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_current_tasks, container, false)

        binding.lifecycleOwner = this

        val database = TasksDatabase.getInstance(requireContext()).taskDao
        val application = requireNotNull(this.activity).application
        val factory = CurrentTasksViewModelFactory(database, application)
        viewModel = ViewModelProvider(this, factory).get(CurrentTasksViewModel::class.java)

        binding.fab.setOnClickListener{
            findNavController().navigate(CurrentTasksFragmentDirections.actionCurrentTasksFragmentToAddTaskFragment())
        }


        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        binding.recyclerView.setHasFixedSize(true)
        setFadeInAnimation(binding.recyclerView)

        val finishedTaskListener =  TaskListener { taskModel ->
            viewModel.moveToFinishedList(taskModel) }

        val updateListener =  TaskListener { taskModel ->
            findNavController().navigate(
                CurrentTasksFragmentDirections.actionCurrentTasksFragmentToEditTaskFragment(taskModel)
            )
        }

        adapter = TaskAdapter(finishedTaskListener, updateListener)
        binding.recyclerView.adapter = adapter


        viewModel.activeTasks.observe(requireActivity(), Observer {

            if(it.isNullOrEmpty()){
                viewModel.setHasTasks(false)
            }else   viewModel.setHasTasks()

            it?.let {
                searchableTasksList = it.asTaskModelList()
                adapter.submitList(it.asTaskModelList())
            }
        })

        viewModel.label.observe(viewLifecycleOwner , Observer {
            (activity as (AppCompatActivity) ).supportActionBar?.title = it
        })

        viewModel.finishedTasks.observe(viewLifecycleOwner , Observer {
            FinishedList = it
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val searchItem : MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView :SearchView =  searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }



    override fun onQueryTextChange(queryText: String?): Boolean {
        val filteredList  = filterSearchList(searchableTasksList, queryText)
        adapter.submitList(filteredList)
        binding.recyclerView.smoothScrollToPosition(0)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }


    private fun filterSearchList(taskList: List<TaskModel>, queryText: String?): List<TaskModel>{
        val  lowerCaseQuery = queryText?.toLowerCase()
        val   filteredTaskList : MutableList<TaskModel> = mutableListOf()
        for( task in taskList ){
            val text = task.taskDescription.toLowerCase()
            if(text.contains(lowerCaseQuery!!)){
                filteredTaskList.add(task)
            }
        }
        return filteredTaskList
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_filter ->  {showFilteringPopUpMenu()
                return true
            }
            R.id.clear -> {
                viewModel.clearFinishedTasks()
                return true
            }

        }
        return  NavigationUI.onNavDestinationSelected(item , findNavController()) || return super.onOptionsItemSelected(item)
    }



    private fun showFilteringPopUpMenu() {

        val view = requireActivity().findViewById<View>(R.id.menu_filter) ?: return

        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.tasks_filter_menu , menu)
            setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {

                    when (item?.itemId) {
                        R.id.active_tasks -> {
                            viewModel.updateListLabel(getString(R.string.default_label))
                            val list = viewModel.activeTasks.value

                            if (list.isNullOrEmpty())  viewModel.setHasTasks(false)
                            binding.recyclerView.smoothScrollToPosition(0)
                            searchableTasksList = list?.asTaskModelList()!!
                            adapter.submitList(list.asTaskModelList())

                            return true
                        }

                        R.id.finished_tasks -> {

                            viewModel.updateListLabel(getString(R.string.finished_label))
                            viewModel.setHasTasks()

                            binding.recyclerView.smoothScrollToPosition(0)
                            searchableTasksList = FinishedList.asTaskModelList()
                            adapter.submitList(FinishedList.asTaskModelList())

                            return true
                        }
                    }
                    return true
                }
            })
            show()
        }
    }


    private fun setFadeInAnimation(recycler : RecyclerView ){
        val defaultItemAnimator =  DefaultItemAnimator().apply {
            removeDuration = 200
            addDuration = 200

        }
        defaultItemAnimator.supportsChangeAnimations =true

        val set =  AnimationSet(true)
        val  fadeIn : Animation = AlphaAnimation(0.0f , 1f)
        fadeIn.duration = 500
        fadeIn.fillAfter = true
        set.addAnimation(fadeIn)

        val  controller =  LayoutAnimationController(set ,.2f)
        recycler.layoutAnimation = controller
        recycler.itemAnimator = defaultItemAnimator


    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetListLabel()
    }
}


