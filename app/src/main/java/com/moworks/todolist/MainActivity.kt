package com.moworks.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.moworks.todolist.databinding.ActivityMainBinding

import com.moworks.todolist.notifications.createNotificationChannels


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var  topAppBar :Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ToDoList)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navigationView = binding.navigationView
        navController = findNavController(R.id.myNavHost)
        drawerLayout = binding.drawerLayout

        topAppBar = binding.topAppBar
        setSupportActionBar(topAppBar)


        createNotificationChannels(this)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == controller.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        NavigationUI.navigateUp(navController, drawerLayout)
        return super.onSupportNavigateUp()
    }




}